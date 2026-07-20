import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private static final int ROWS = 5;
    private static final int COLS = 8;
    private static final int SPACING_X = 70;
    private static final int SPACING_Y = 55;

    private Cell[][] cells = new Cell[ROWS][COLS];

    private double gridX;
    private int gridY;
    private int direction = 1;
    private double horizontalSpeed;
    private int verticalStep;
    private int panelWidth;

    private List<Egg> eggs = new ArrayList<>();
    private long eggIntervalMs;
    private long lastEggTime = 0;

    private List<PowerUp> powerUps = new ArrayList<>();

    public Grid(int startX, int startY, int panelWidth, int stageNumber) {
        this.gridX = startX;
        this.gridY = startY;
        this.panelWidth = panelWidth;

        applyStageConfig(stageNumber);
        buildGrid(stageNumber);
    }

    private void applyStageConfig(int stage) {
        switch (stage) {
            case 1: horizontalSpeed = 1.0; verticalStep = 20; eggIntervalMs = 3000; break;
            case 2: horizontalSpeed = 1.5; verticalStep = 20; eggIntervalMs = 2000; break;
            case 3: horizontalSpeed = 2.0; verticalStep = 25; eggIntervalMs = 1500; break;
            case 5: horizontalSpeed = 2.5; verticalStep = 25; eggIntervalMs = 1000; break;
            case 6: horizontalSpeed = 3.0; verticalStep = 30; eggIntervalMs = 800; break;
            case 7: horizontalSpeed = 3.5; verticalStep = 30; eggIntervalMs = 700; break;
            default: horizontalSpeed = 1.0; verticalStep = 20; eggIntervalMs = 3000;
        }
    }

    private int cellSpawnCounterFor(int stage) {
        switch (stage) {
            case 1: case 2: return 2;
            case 3: case 5: return 3;
            case 6: case 7: return 4;
            default: return 2;
        }
    }

    private int hitsFor(Cell.EnemyType type, int stage) {
        boolean earlyStages = stage <= 3;
        switch (type) {
            case NORMAL:  return earlyStages ? 2 : 3;
            case FAST:    return earlyStages ? 1 : 2;
            case ZIGZAG:  return earlyStages ? 2 : 3;
            case SHOOTER: return earlyStages ? 2 : 3;
            default: return 2;
        }
    }

    private Cell.EnemyType pickTypeForCell(int row, int col, int stage) {
        switch (stage) {
            case 1:
                return Cell.EnemyType.NORMAL;
            case 2:
                return (row % 2 == 0) ? Cell.EnemyType.NORMAL : Cell.EnemyType.FAST;
            case 3:
                return (row % 2 == 0) ? Cell.EnemyType.NORMAL : Cell.EnemyType.ZIGZAG;
            case 5:
                return (row % 2 == 0) ? Cell.EnemyType.SHOOTER : Cell.EnemyType.FAST;
            case 6:
                return (row % 2 == 0) ? Cell.EnemyType.ZIGZAG : Cell.EnemyType.SHOOTER;
            case 7:
                Cell.EnemyType[] all = Cell.EnemyType.values();
                return all[row % all.length];
            default:
                return Cell.EnemyType.NORMAL;
        }
    }

    private void buildGrid(int stage) {
        int spawnCounter = cellSpawnCounterFor(stage);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell.EnemyType type = pickTypeForCell(r, c, stage);
                int hits = hitsFor(type, stage);

                Cell cell = new Cell(r, c, type, spawnCounter, hits);
                cells[r][c] = cell;
                cell.spawnAt((int) (gridX + c * SPACING_X), gridY + r * SPACING_Y);
            }
        }
    }

    public void update(int panelHeight) {
        gridX += direction * horizontalSpeed;
        if (gridX <= 0 || gridX + (COLS - 1) * SPACING_X + 40 >= panelWidth) {
            direction *= -1;
            gridY += verticalStep;
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = cells[r][c];
                Enemy en = cell.getCurrentEnemy();

                if (en != null && !en.isAlive()) {
                    if (Math.random() < 0.2) {
                        powerUps.add(new PowerUp(en.getX(), en.getY(), randomPowerUpType()));
                    }
                    cell.notifyEnemyDied();
                }

                if (cell.needsRespawn()) {
                    cell.spawnAt((int) (gridX + c * SPACING_X), gridY + r * SPACING_Y);
                } else if (en != null) {
                    en.setX((int) (gridX + c * SPACING_X));
                    en.setY(gridY + r * SPACING_Y);
                }
            }
        }

        layEggIfDue();
        eggs.forEach(Egg::update);
        eggs.removeIf(egg -> !egg.isActive() || egg.isOffScreen(panelWidth, panelHeight));

        powerUps.forEach(PowerUp::update);
        powerUps.removeIf(p -> !p.isActive() || p.isOffScreen(panelHeight));
    }

    private String randomPowerUpType() {
        return PowerUp.randomType();
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    private void layEggIfDue() {
        long now = System.currentTimeMillis();
        if (now - lastEggTime < eggIntervalMs) return;

        List<Enemy> alive = getAllEnemies();
        if (alive.isEmpty()) return;

        lastEggTime = now;
        Enemy shooter = alive.get((int) (Math.random() * alive.size()));
        eggs.add(new Egg(shooter.getX() + 15, shooter.getY() + 30));
    }

    public List<Enemy> getAllEnemies() {
        List<Enemy> result = new ArrayList<>();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                Enemy en = cell.getCurrentEnemy();
                if (en != null && en.isAlive()) result.add(en);
            }
        }
        return result;
    }

    public List<Egg> getEggs() {
        return eggs;
    }

    public boolean isStageComplete() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isFinished()) return false;
            }
        }
        return true;
    }

    public void draw(Graphics g) {
        for (Enemy en : getAllEnemies()) {
            en.draw(g);
        }
        for (Egg egg : eggs) {
            egg.draw(g);
        }
        for (PowerUp p : powerUps) {
            p.draw(g);
        }
    }
}
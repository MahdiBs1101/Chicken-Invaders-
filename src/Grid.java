import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private static final int ROWS = 5;
    private static final int COLS = 8;
    private static final int SPACING_X = 70;
    private static final int SPACING_Y = 55;

    private Cell[][] cells = new Cell[ROWS][COLS];

    private int gridX, gridY;
    private int direction = 1;
    private double horizontalSpeed;
    private int verticalStep;
    private int panelWidth;

    private List<Egg> eggs = new ArrayList<>();
    private long eggIntervalMs;
    private long lastEggTime = 0;

    public Grid(int startX, int startY, double horizontalSpeed, int verticalStep,
                int initialHitsPerCell, int panelWidth, long eggIntervalMs) {
        this.gridX = startX;
        this.gridY = startY;
        this.horizontalSpeed = horizontalSpeed;
        this.verticalStep = verticalStep;
        this.panelWidth = panelWidth;
        this.eggIntervalMs = eggIntervalMs;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = new Cell(r, c, Cell.EnemyType.NORMAL, initialHitsPerCell, 2);
                cells[r][c] = cell;
                cell.spawnAt(gridX + c * SPACING_X, gridY + r * SPACING_Y);
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
                    cell.notifyEnemyDied();
                }

                if (cell.needsRespawn()) {
                    cell.spawnAt(gridX + c * SPACING_X, gridY + r * SPACING_Y);
                } else if (en != null) {
                    en.setX(gridX + c * SPACING_X);
                    en.setY(gridY + r * SPACING_Y);
                }
            }
        }

        layEggIfDue();
        eggs.forEach(Egg::update);
        eggs.removeIf(egg -> !egg.isActive() || egg.isOffScreen(panelHeight));
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
    }
}
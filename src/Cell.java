public class Cell {
    public enum EnemyType { NORMAL, FAST, ZIGZAG, SHOOTER }

    private int row, col;
    private EnemyType type;
    private int spawnsRemaining;
    private int hitsPerEnemy;
    private Enemy currentEnemy;

    public Cell(int row, int col, EnemyType type, int spawnsRemaining, int hitsPerEnemy) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.spawnsRemaining = spawnsRemaining;
        this.hitsPerEnemy = hitsPerEnemy;
    }

    public void spawnAt(int x, int y) {
        currentEnemy = createEnemy(x, y);
    }

    public void respawnFromCorner(int targetX, int targetY, int panelWidth) {
        boolean fromLeft = Math.random() < 0.5;
        int startX = fromLeft ? -40 : panelWidth;
        int startY = -40;

        Enemy enemy = createEnemy(startX, startY);
        enemy.startSpawn(startX, startY, targetX, targetY);
        currentEnemy = enemy;
    }

    private Enemy createEnemy(int x, int y) {
        switch (type) {
            case NORMAL:  return new NormalEnemy(x, y, hitsPerEnemy);
            case FAST:    return new FastEnemy(x, y, hitsPerEnemy);
            case ZIGZAG:  return new ZigzagEnemy(x, y, hitsPerEnemy);
            case SHOOTER: return new ShooterEnemy(x, y, hitsPerEnemy);
            default:      return new NormalEnemy(x, y, hitsPerEnemy);
        }
    }

    public void notifyEnemyDied() {
        spawnsRemaining--;
        currentEnemy = null;
    }

    public boolean needsRespawn() {
        return spawnsRemaining > 0 && currentEnemy == null;
    }

    public boolean isFinished() {
        return spawnsRemaining <= 0 && currentEnemy == null;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }
}
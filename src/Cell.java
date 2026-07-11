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
        switch (type) {
            case NORMAL:  currentEnemy = new NormalEnemy(x, y, hitsPerEnemy); break;
            case FAST:    currentEnemy = new FastEnemy(x, y, hitsPerEnemy); break;
            case ZIGZAG:  currentEnemy = new ZigzagEnemy(x, y, hitsPerEnemy); break;
            case SHOOTER: currentEnemy = new ShooterEnemy(x, y, hitsPerEnemy); break;
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

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
}
import java.awt.*;

public abstract class Enemy {
    protected int x, y;
    protected int width = 40, height = 40;
    protected int speed;
    protected Image image;

    protected int hitsRequired;
    protected int scoreValue;
    protected boolean alive = true;

    protected boolean spawning = false;
    protected int targetX, targetY;
    protected double spawnSpeed = 4.0;

    public Enemy(int x, int y, int speed, int hitsRequired, int scoreValue, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hitsRequired = hitsRequired;
        this.scoreValue = scoreValue;
        this.image = image;
    }

    public abstract void move();
    public void maybeShoot(java.util.List<Egg> eggs, int planeX, int planeY) {
    }

    public void takeHit() {
        hitsRequired--;
        if (hitsRequired <= 0) {
            alive = false;
        }
    }

    public void startSpawn(int startX, int startY, int targetX, int targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.spawning = true;
    }

    public void updateSpawnMovement() {
        if (!spawning) return;
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < spawnSpeed) {
            x = targetX;
            y = targetY;
            spawning = false;
        } else {
            x += (int) Math.round(spawnSpeed * dx / dist);
            y += (int) Math.round(spawnSpeed * dy / dist);
        }
    }

    public boolean isSpawning() {
        return spawning;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getScoreValue() {
        return scoreValue;
    }
}
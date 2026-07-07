import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Plane {
    protected int x, y;
    protected int width = 50, height = 50;

    protected int speed;
    protected int fireRateMs;
    protected int lives;
    protected static final int MAX_LIVES = 5;

    private long lastShotTime = 0;
    private int bulletCount = 1;

    private Image image;
    private List<Bullet> bullets = new ArrayList<>();

    public Plane(int startX, int startY, int speed, int fireRateMs, int initialLives, String imagePath) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.fireRateMs = fireRateMs;
        this.lives = initialLives;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Error loading plane image: " + e.getMessage());
        }
    }

    public double getDamageMultiplier() {
        return 1.0;
    }

    public void moveLeft(int panelWidth)  { if (x > 0) x -= speed; }
    public void moveRight(int panelWidth) { if (x < panelWidth - width) x += speed; }
    public void moveUp(int panelHeight)   { if (y > 0) y -= speed; }
    public void moveDown(int panelHeight) { if (y < panelHeight - height) y += speed; }

    public void shoot() {
        long now = System.currentTimeMillis();
        if (now - lastShotTime < fireRateMs) return;
        lastShotTime = now;

        int centerX = x + width / 2;
        int spacing = 15;
        int startOffset = -(bulletCount - 1) * spacing / 2;

        for (int i = 0; i < bulletCount; i++) {
            int bulletX = centerX + startOffset + i * spacing;
            bullets.add(new Bullet(bulletX, y, getDamageMultiplier()));
        }
    }

    public void update(int panelHeight) {
        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> b.getY() < 0 || !b.isActive());
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
        for (Bullet b : bullets) {
            b.draw(g);
        }
    }

    public void loseLife() {
        lives--;
    }

    public void addLife() {
        if (lives < MAX_LIVES) lives++;
    }

    public void addBulletSlot() {
        bulletCount++;
    }

    public void setFireRateMs(int fireRateMs) {
        this.fireRateMs = fireRateMs;
    }

    public boolean isDead() {
        return lives <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getLives() { return lives; }
    public int getBulletCount() { return bulletCount; }
    public List<Bullet> getBullets() { return bullets; }
    public int getX() { return x; }
    public int getY() { return y; }
}
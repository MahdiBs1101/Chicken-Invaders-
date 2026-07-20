import java.awt.*;

public class Bullet {
    private int x, y;
    private int width = 6, height = 14;
    private int speed = 10;
    private boolean active = true;
    private double damage;

    public Bullet(int x, int y, double damage) {
        this.x = x - width / 2;
        this.y = y;
        this.damage = damage;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        if (!active) return;
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public double getDamage() {
        return damage;
    }

    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
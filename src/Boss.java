import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Boss {
    protected int x, y;
    protected int width = 120, height = 120;
    protected int maxHealth;
    protected int health;
    protected Image image;

    protected List<Egg> eggs = new ArrayList<>();
    protected long attackIntervalMs;
    private long lastAttackTime = 0;

    public Boss(int x, int y, int maxHealth, long attackIntervalMs, Image image) {
        this.x = x;
        this.y = y;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackIntervalMs = attackIntervalMs;
        this.image = image;
    }

    public abstract void move(int panelWidth, int panelHeight);
    public abstract void attack();

    public void update(int panelWidth, int panelHeight) {
        move(panelWidth, panelHeight);

        long now = System.currentTimeMillis();
        if (now - lastAttackTime >= attackIntervalMs) {
            lastAttackTime = now;
            attack();
        }

        eggs.forEach(Egg::update);
        eggs.removeIf(e -> !e.isActive() || e.isOffScreen(panelWidth, panelHeight));
    }

    public void takeDamage(double amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, width, height);
        }

        int barWidth = 200, barHeight = 15;
        int barX = x + width / 2 - barWidth / 2;
        int barY = y - 25;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);
        g.setColor(Color.GREEN);
        int filled = (int) ((health / (double) maxHealth) * barWidth);
        g.fillRect(barX, barY, filled, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);

        for (Egg egg : eggs) {
            egg.draw(g);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public List<Egg> getEggs() { return eggs; }
    public int getCenterX() { return x + width / 2; }
    public int getCenterY() { return y + height / 2; }
}
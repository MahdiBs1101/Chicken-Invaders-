import java.awt.*;

public class Egg {
    private double x, y;
    private int width = 10, height = 16;
    private double vx, vy;
    private boolean active = true;

    public Egg(int x, int y) {
        this(x, y, 0, 4);
    }

    public Egg(int x, int y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public void draw(Graphics g) {
        if (!active) return;
        g.setColor(Color.YELLOW.darker());
        g.fillOval((int) x, (int) y, width, height);
    }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }

    public boolean isOffScreen(int panelWidth, int panelHeight) {
        return y > panelHeight || y < -20 || x < -20 || x > panelWidth + 20;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
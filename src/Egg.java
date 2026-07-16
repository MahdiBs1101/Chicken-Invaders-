import java.awt.*;

public class Egg {
    private int x, y;
    private int width = 10, height = 16;
    private int speed = 4;
    private boolean active = true;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        if (!active) return;
        g.setColor(Color.YELLOW.darker());
        g.fillOval(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BossLevel8 extends Boss {
    private static Image sharedImage;
    static {
        try { sharedImage = ImageIO.read(new File("assets/chicken/boss2.png")); }
        catch (IOException e) { sharedImage = null; }
    }

    private int direction = 1;
    private double horizontalSpeed = 2.0;

    public BossLevel8(int x, int y) {
        super(x, y, 100, 1000, sharedImage);
    }

    @Override
    public void move(int panelWidth, int panelHeight) {
        x += direction * horizontalSpeed;
        if (x <= 0 || x + width >= panelWidth) {
            direction *= -1;
        }
    }

    @Override
    public void attack() {
        double speed = 5;
        int cx = getCenterX();
        int cy = getCenterY();

        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            double vx = speed * Math.cos(angle);
            double vy = speed * Math.sin(angle);
            eggs.add(new Egg(cx, cy, vx, vy));
        }
    }
}
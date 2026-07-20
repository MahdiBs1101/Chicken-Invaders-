import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BossLevel4 extends Boss {
    private static Image sharedImage;
    static {
        try { sharedImage = ImageIO.read(new File("assets/chicken/boss1.png")); }
        catch (IOException e) { sharedImage = null; }
    }

    private int direction = 1;
    private double horizontalSpeed = 3;

    public BossLevel4(int x, int y) {
        super(x, y, 100, 1500, sharedImage);
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
        double speed = 4;
        int cx = getCenterX();
        int cy = getCenterY();

        eggs.add(new Egg(cx, cy, 0, -speed));
        eggs.add(new Egg(cx, cy, 0, speed));
        eggs.add(new Egg(cx, cy, -speed, 0));
        eggs.add(new Egg(cx, cy, speed, 0));
    }
}
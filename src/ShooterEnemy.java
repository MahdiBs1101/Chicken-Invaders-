import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShooterEnemy extends Enemy {
    private static Image sharedImage;
    static {
        try { sharedImage = ImageIO.read(new File("assets/enemy/shooter.png")); }
        catch (IOException e) { sharedImage = null; }
    }

    public ShooterEnemy(int x, int y, int hitsRequired) {
        super(x, y, 1, hitsRequired, 25, sharedImage);
    }

    @Override
    public void move() {
        x += speed;
    }
}
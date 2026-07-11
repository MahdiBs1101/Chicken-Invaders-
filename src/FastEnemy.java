import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FastEnemy extends Enemy {
    private static Image sharedImage;
    static {
        try { sharedImage = ImageIO.read(new File("assets/enemy/fast.png")); }
        catch (IOException e) { sharedImage = null; }
    }

    public FastEnemy(int x, int y, int hitsRequired) {
        super(x, y, 2, hitsRequired, 15, sharedImage);
    }

    @Override
    public void move() {
        x += speed;
    }
}
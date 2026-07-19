import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ZigzagEnemy extends Enemy {
    private static Image sharedImage;
    static {
        try { sharedImage = ImageIO.read(new File("assets/chicken/zigzag_chicken.png")); }
        catch (IOException e) { sharedImage = null; }
    }

    private int zigzagTick = 0;

    public ZigzagEnemy(int x, int y, int hitsRequired) {
        super(x, y, 1, hitsRequired, 20, sharedImage);
    }

    @Override
    public void move() {
        zigzagTick++;
        x += speed;
        y += (zigzagTick % 20 < 10) ? 1 : -1;
    }
}
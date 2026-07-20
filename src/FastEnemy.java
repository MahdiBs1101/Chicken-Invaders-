import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FastEnemy extends Enemy {
    public FastEnemy(int x, int y, int hitsRequired) {
        super(x, y, 2, hitsRequired, 15, loadSharedImage());
        this.spawnSpeed = 8.0;
    }

    private static Image sharedImage;
    private static Image loadSharedImage() {
        if (sharedImage == null) {
            try {
                sharedImage = javax.imageio.ImageIO.read(
                        new java.io.File("assets/chicken/fast_chicken.png"));
            } catch (Exception e) {
                System.out.println("Error loading fast chicken image: " + e.getMessage());
            }
        }
        return sharedImage;
    }

    @Override
    public void move() {
    }
}
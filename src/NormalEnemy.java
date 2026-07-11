import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NormalEnemy extends Enemy {
    private static Image sharedImage;

    static {
        try {
            sharedImage = ImageIO.read(new File("assets/chicken/normal_chicken.png"));
        } catch (IOException e) {
            System.out.println("Error loading normal enemy image: " + e.getMessage());
            sharedImage = null;
        }
    }

    public NormalEnemy(int x, int y, int hitsRequired) {
        super(x, y, 1, hitsRequired, 10, sharedImage);
    }

    @Override
    public void move() {
        x += speed;
    }
}
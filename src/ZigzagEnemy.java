import java.awt.*;

public class ZigzagEnemy extends Enemy {
    private double zigzagPhase = 0;
    private static final double ZIGZAG_AMPLITUDE = 30.0;
    private static final double ZIGZAG_FREQUENCY = 0.15;

    public ZigzagEnemy(int x, int y, int hitsRequired) {
        super(x, y, 1, hitsRequired, 20, loadSharedImage());
    }

    private static Image sharedImage;
    private static Image loadSharedImage() {
        if (sharedImage == null) {
            try {
                sharedImage = javax.imageio.ImageIO.read(
                        new java.io.File("assets/chicken/zigzag_chicken.png"));
            } catch (Exception e) {
                System.out.println("Error loading zigzag chicken image: " + e.getMessage());
            }
        }
        return sharedImage;
    }

    @Override
    public void move() {
    }

    @Override
    public void updateSpawnMovement() {
        if (!spawning) return;

        double dy = targetY - y;
        double dist = Math.abs(dy);

        if (dist < spawnSpeed) {
            x = targetX;
            y = targetY;
            spawning = false;
            return;
        }

        y += (dy > 0) ? spawnSpeed : -spawnSpeed;
        zigzagPhase += ZIGZAG_FREQUENCY;
        x = (int) (targetX + Math.sin(zigzagPhase) * ZIGZAG_AMPLITUDE);
    }
}
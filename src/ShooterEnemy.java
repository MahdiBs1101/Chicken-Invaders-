import java.awt.*;

public class ShooterEnemy extends Enemy {
    private static final long SHOOT_INTERVAL_MS = 2500;
    private static final double SHOOT_CHANCE = 0.3;
    private static final double EGG_SPEED = 5.0;

    private long lastShotTime = 0;

    public ShooterEnemy(int x, int y, int hitsRequired) {
        super(x, y, 1, hitsRequired, 25, loadSharedImage());
    }

    private static Image sharedImage;
    private static Image loadSharedImage() {
        if (sharedImage == null) {
            try {
                sharedImage = javax.imageio.ImageIO.read(
                        new java.io.File("assets/chicken/shooter_chicken.png"));
            } catch (Exception e) {
                System.out.println("Error loading shooter chicken image: " + e.getMessage());
            }
        }
        return sharedImage;
    }

    @Override
    public void move() {
    }

    @Override
    public void maybeShoot(java.util.List<Egg> eggs, int planeX, int planeY) {
        if (!alive) return;

        long now = System.currentTimeMillis();
        if (now - lastShotTime < SHOOT_INTERVAL_MS) return;
        if (Math.random() > SHOOT_CHANCE) return;

        lastShotTime = now;

        int dx = planeX - x;
        double vx = (dx >= 0) ? EGG_SPEED : -EGG_SPEED;

        eggs.add(new Egg(x + width / 2, y + height / 2, vx, 0));
    }
}
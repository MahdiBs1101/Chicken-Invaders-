import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PowerUp {
    public static final String RAPID_FIRE = "RAPID_FIRE";
    public static final String FREEZE_BOMB = "FREEZE_BOMB";
    public static final String EXTRA_LIFE = "EXTRA_LIFE";
    public static final String SHIELD = "SHIELD";
    public static final String ADD_FIRE = "ADD_FIRE";

    private static final String[] ALL_TYPES = {
            RAPID_FIRE, FREEZE_BOMB, EXTRA_LIFE, SHIELD, ADD_FIRE
    };

    private static final Map<String, Image> IMAGES = new HashMap<>();

    static {
        loadImage(RAPID_FIRE, "assets/powerup1/fast_shot.png");
        loadImage(FREEZE_BOMB, "assets/powerup1/freeze.png");
        loadImage(EXTRA_LIFE, "assets/powerup1/heal.png");
        loadImage(SHIELD, "assets/powerup1/sheild.png");
        loadImage(ADD_FIRE, "assets/powerup1/add_shot.png");
    }

    private static void loadImage(String type, String path) {
        try {
            IMAGES.put(type, ImageIO.read(new File(path)));
        } catch (IOException e) {
            System.out.println("Error loading power-up image (" + type + "): " + e.getMessage());
            IMAGES.put(type, null);
        }
    }

    private int x, y;
    private int width = 24, height = 24;
    private int speed = 2;
    private String type;
    private boolean active = true;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public static String randomType() {
        return ALL_TYPES[(int) (Math.random() * ALL_TYPES.length)];
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        if (!active) return;

        Image img = IMAGES.get(type);
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public String getType() { return type; }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
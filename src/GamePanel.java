import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private GameMain gameMain;
    private Image backgroundImage;
    private Plane plane;
    private Grid grid;
    private boolean up, down, left, right, shooting;
    private boolean gameOver = false;

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        try {
            backgroundImage = ImageIO.read(new File("assets/background/background.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        plane = new DefaultPlane(375, 500);
        grid = new Grid(50, 50, 1.0, 20, 2, 800, 3000);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });

        this.setFocusable(true);
        this.addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        plane.draw(g);
        grid.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + plane.getLives() + "   Bullets: " + plane.getBulletCount(), 20, 20);

        if (grid.isStageComplete()) {
            g.drawString("Complete!", 350, 300);
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.RED);
            String msg = "Game Over";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.setColor(Color.WHITE);
            String hint = "Press Esc to return to menu";
            FontMetrics fm2 = g.getFontMetrics();
            g.drawString(hint, (getWidth() - fm2.stringWidth(hint)) / 2, getHeight() / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        if (left)  plane.moveLeft(getWidth());
        if (right) plane.moveRight(getWidth());
        if (up)    plane.moveUp(getHeight());
        if (down)  plane.moveDown(getHeight());
        if (shooting) plane.shoot();

        plane.update(getHeight());
        grid.update(getHeight());

        checkBulletEnemyCollisions();
        checkEggPlaneCollisions();

        if (plane.isDead()) {
            triggerGameOver();
        }

        repaint();
    }

    private void triggerGameOver() {
        gameOver = true;
        timer.stop();
    }

    private void checkBulletEnemyCollisions() {
        for (Bullet b : plane.getBullets()) {
            if (!b.isActive()) continue;
            for (Enemy en : grid.getAllEnemies()) {
                if (b.getBounds().intersects(en.getBounds())) {
                    en.takeHit();
                    b.deactivate();
                    break;
                }
            }
        }
    }

    private void checkEggPlaneCollisions() {
        for (Egg egg : grid.getEggs()) {
            if (!egg.isActive()) continue;
            if (egg.getBounds().intersects(plane.getBounds())) {
                plane.loseLife();
                egg.deactivate();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver) {
            if (key == KeyEvent.VK_ESCAPE) {
                gameMain.switchPanel("MainMenu");
            }
            return;
        }

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = true;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = true;
        if (key == KeyEvent.VK_SPACE) shooting = true;
        if (key == KeyEvent.VK_ESCAPE) gameMain.switchPanel("MainMenu");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = false;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = false;
        if (key == KeyEvent.VK_SPACE) shooting = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
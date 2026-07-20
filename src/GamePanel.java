import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private GameMain gameMain;
    private Image backgroundImage;
    private Plane plane;
    private Grid grid;
    private Boss boss;
    private boolean up, down, left, right, shooting;
    private boolean gameOver = false;
    private boolean victory = false;

    private int currentStage = 1;
    private boolean stageTransitioning = false;
    private long stageCompleteTime = 0;
    private static final long STAGE_TRANSITION_DELAY_MS = 2000;

    private long freezeUntil = 0;

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        try {
            backgroundImage = ImageIO.read(new File("assets/background/background.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        plane = new DefaultPlane(375, 500);
        startStage(currentStage);

        SoundManager.get().playMusic("assets/sound-effects/main_theme.wav");

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                if (SwingUtilities.isRightMouseButton(e)) {
                    shooting = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    shooting = false;
                }
            }
        });

        this.setFocusable(true);
        this.addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    private void startStage(int stage) {
        if (stage == 4) {
            boss = new BossLevel4(340, 50);
            grid = null;
        } else if (stage == 8) {
            boss = new BossLevel8(320, 50);
            grid = null;
        } else {
            grid = new Grid(50, 50, 800, stage);
            boss = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        plane.draw(g);
        if (grid != null) grid.draw(g);
        if (boss != null) boss.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + plane.getLives() + "   Bullets: " + plane.getBulletCount()
                + "   Stage: " + currentStage, 20, 20);

        if (stageTransitioning && !victory) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.GREEN);
            String msg = "Stage Complete!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(victory ? Color.YELLOW : Color.RED);
            String msg = victory ? "Win!" : "Game Over";
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

        if (stageTransitioning) {
            handleStageTransition();
            repaint();
            return;
        }

        if (left)  plane.moveLeft(getWidth());
        if (right) plane.moveRight(getWidth());
        if (up)    plane.moveUp(getHeight());
        if (down)  plane.moveDown(getHeight());
        if (shooting) {
            if (plane.shoot()) {
                SoundManager.get().playShot();
            }
        }

        plane.update(getHeight());

        boolean frozen = System.currentTimeMillis() < freezeUntil;

        if (grid != null) {
            if (!frozen) grid.update(getHeight());
            checkBulletEnemyCollisions();
            checkEggPlaneCollisions(grid.getEggs());
            checkPowerUpCollisions();

            if (grid.isStageComplete()) {
                stageTransitioning = true;
                stageCompleteTime = System.currentTimeMillis();
            }
        }

        if (boss != null) {
            if (!frozen) boss.update(getWidth(), getHeight());
            checkBulletBossCollisions();
            checkEggPlaneCollisions(boss.getEggs());

            if (boss.isDead()) {
                stageTransitioning = true;
                stageCompleteTime = System.currentTimeMillis();
            }
        }

        if (plane.isDead()) {
            triggerGameOver(false);
            repaint();
            return;
        }

        repaint();
    }

    private void checkPowerUpCollisions() {
        if (grid == null) return;
        for (PowerUp p : grid.getPowerUps()) {
            if (!p.isActive()) continue;
            if (p.getBounds().intersects(plane.getBounds())) {
                applyPowerUp(p.getType());
                p.deactivate();
            }
        }
    }

    private void applyPowerUp(String type) {
        switch (type) {
            case PowerUp.RAPID_FIRE:
                plane.activateRapidFire(8000);
                break;
            case PowerUp.FREEZE_BOMB:
                freezeUntil = System.currentTimeMillis() + 3000;
                break;
            case PowerUp.EXTRA_LIFE:
                plane.addLife();
                break;
            case PowerUp.SHIELD:
                plane.activateShield(10000);
                break;
            case PowerUp.ADD_FIRE:
                plane.addBulletSlot();
                break;
        }
    }

    private void handleStageTransition() {
        if (System.currentTimeMillis() - stageCompleteTime < STAGE_TRANSITION_DELAY_MS) {
            return;
        }

        if (currentStage == 8) {
            triggerGameOver(true);
            return;
        }

        currentStage++;
        startStage(currentStage);
        stageTransitioning = false;
    }

    private void triggerGameOver(boolean won) {
        gameOver = true;
        victory = won;
        timer.stop();
        SoundManager.get().stopMusic();
        SoundManager.get().playGameOver();

        if (won) {
            SoundManager.get().playMusic("assets/sound-effects/ending-theme.wav");
        }
    }

    private void checkBulletEnemyCollisions() {
        for (Bullet b : plane.getBullets()) {
            if (!b.isActive()) continue;
            for (Enemy en : grid.getAllEnemies()) {
                if (b.getBounds().intersects(en.getBounds())) {
                    en.takeHit();
                    b.deactivate();
                    if (!en.isAlive()) {
                        SoundManager.get().playCrash();
                    }
                    break;
                }
            }
        }
    }

    private void checkBulletBossCollisions() {
        for (Bullet b : plane.getBullets()) {
            if (!b.isActive()) continue;
            if (b.getBounds().intersects(boss.getBounds())) {
                boss.takeDamage(b.getDamage());
                b.deactivate();
            }
        }
    }

    private void checkEggPlaneCollisions(List<Egg> eggs) {
        for (Egg egg : eggs) {
            if (!egg.isActive()) continue;
            if (egg.getBounds().intersects(plane.getBounds())) {
                if (!plane.hasShield()) {
                    plane.loseLife();
                    SoundManager.get().playCrash();
                }
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

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)  left = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = true;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)    up = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)  down = true;
        if (key == KeyEvent.VK_SPACE) shooting = true;
        if (key == KeyEvent.VK_ESCAPE) gameMain.switchPanel("MainMenu");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)  left = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = false;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)    up = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)  down = false;
        if (key == KeyEvent.VK_SPACE) shooting = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
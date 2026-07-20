import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private static SoundManager instance;

    private Clip musicClip;

    private SoundManager() {}

    public static SoundManager get() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private Clip loadClip(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            System.out.println("Error loading sound (" + path + "): " + e.getMessage());
            return null;
        }
    }

    public void playMusic(String path) {
        if (!SettingsPanel.musicOn) return;
        stopMusic();
        musicClip = loadClip(path);
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    public void playShot() {
        if (!SettingsPanel.shotOn) return;
        playOnce("assets/sound-effects/mixkit-short-laser-gun-shot-1670.wav");
    }

    public void playCrash() {
        if (!SettingsPanel.crashOn) return;
        playOnce("assets/sound-effects/mixkit-epic-impact-afar-explosion-2782.wav");
    }

    public void playGameOver() {
        if (!SettingsPanel.gameOverOn) return;
        playOnce("assets/sound-effects/mixkit-retro-arcade-game-over-470.wav");
    }


    private void playOnce(String path) {
        Clip clip = loadClip(path);
        if (clip != null) {
            clip.start();
        }
    }
}
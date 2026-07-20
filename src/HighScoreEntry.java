public class HighScoreEntry {
    private String username;
    private int score;
    private int level;

    public HighScoreEntry(String username, int score, int level) {
        this.username = username;
        this.score = score;
        this.level = level;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
}
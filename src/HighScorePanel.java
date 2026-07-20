import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {
    private GameMain gameMain;
    private DefaultTableModel tableModel;
    private JTable table;

    public HighScorePanel(GameMain gameMain) {
        this.gameMain = gameMain;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("High Scores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Rank", "Username", "Score", "Level Reached"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> gameMain.switchPanel("MainMenu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<HighScoreEntry> scores = DatabaseManager.db().getHighScores();

        int rank = 1;
        for (HighScoreEntry entry : scores) {
            tableModel.addRow(new Object[]{
                    rank++,
                    entry.getUsername(),
                    entry.getScore(),
                    entry.getLevel()
            });
        }

        if (scores.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "No scores yet", "-", "-"});
        }
    }
}
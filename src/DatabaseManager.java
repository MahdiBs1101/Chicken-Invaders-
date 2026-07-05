import java.sql.*;

public class DatabaseManager {
    private static final String dbPath = "jdbc:sqlite:DataBase/game.db";
    private static DatabaseManager dataBase;
    private static Connection conn;

    private DatabaseManager() {
        try {
            conn = DriverManager.getConnection(dbPath);
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseManager db() {
        if (dataBase == null) {
            dataBase = new DatabaseManager();
        }
        return dataBase;
    }

    private void createTables() {
        String usersTableSQL = """
                CREATE TABLE IF NOT EXISTS Users (
                username TEXT PRIMARY KEY, 
                password TEXT NOT NULL, 
                high_score INTEGER DEFAULT 0, 
                last_level INTEGER DEFAULT 1, 
                setting_music INTEGER DEFAULT 1, 
                setting_shot INTEGER DEFAULT 1, 
                setting_crash INTEGER DEFAULT 1, 
                setting_gameover INTEGER DEFAULT 1
                );""";

        String historyTableSQL = """
                CREATE TABLE IF NOT EXISTS History (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                username TEXT NOT NULL, 
                final_score INTEGER NOT NULL, 
                last_level INTEGER NOT NULL, 
                play_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
                active_settings TEXT, 
                FOREIGN KEY(username) REFERENCES Users(username)
                );""";

        try (Statement st = conn.createStatement()) {
            st.execute(usersTableSQL);
            st.execute(historyTableSQL);
            System.out.println("Database tables checked/created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

}
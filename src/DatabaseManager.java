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


    private Connection connect() throws SQLException {
        return DriverManager.getConnection(dbPath);
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

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }


    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
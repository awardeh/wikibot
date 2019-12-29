package warbot;

import java.sql.*;

public class Profiles {

    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            String URL = "jdbc:sqlite:mydb.sqlite";
            conn = DriverManager.getConnection(URL);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void insert(String id) throws AlreadyRegisteredException {
        String sql = "INSERT INTO users(id) VALUES(?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new AlreadyRegisteredException();
        }
    }

    static String getProfile(String id) {
        String sql = "SELECT id, coins FROM users WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.getInt("coins");
            return "coins: " + rs.getInt("coins");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        giveMoney("659855056568320000", 5);
    }

    static boolean inDatabase(String id) {
        String sql = "SELECT id FROM users WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }

    }

    static void giveMoney(String id, int value) {
        String sql = "UPDATE users SET coins = coins + ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


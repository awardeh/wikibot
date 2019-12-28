package warbot;

import java.sql.*;

public class DB {

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

    public static void insert(String id) {
        String sql = "INSERT INTO users(id) VALUES(?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    static String getProfile(String id) {
        String sql = "SELECT id, coins FROM users WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.getInt("coins");
            return "ID: " + rs.getString("id") + "\ncoins: " + rs.getInt("coins");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        System.out.println(getProfile("659855056568320000"));
    }

//    boolean inDatabase(String id) throws SQLException {
//        String sql = "SELECT id FROM users WHERE id = ?";
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, id);
//            ResultSet rs = pstmt.executeUpdate();
//            return rs.next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


}

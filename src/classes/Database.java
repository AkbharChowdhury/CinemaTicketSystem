package classes;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String DB_NAME = "cinema";
    private static Database instance;

    private Database() throws SQLException {
        createAllTables();

    }

    public static Database getInstance() throws SQLException {

        return instance == null ? new Database() : null;
    }

    private static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }
    private void createAllTables() throws SQLException{
        createRatingTable();
    }

    private void createRatingTable() throws SQLException {

        Connection con = getConnection();
        String sql = new Rating().createTable();
        Statement stmt = con.createStatement();
        stmt.execute(sql);
        con.close();

    }




    public Connection getConnection() {

        final String DATABASE_DRIVER = "org.sqlite.JDBC";
        final String CONN_STR = String.format("jdbc:sqlite:%s.db", DB_NAME);
        Connection connection;

        try {
            Class.forName(DATABASE_DRIVER);
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true); // enables foreign key constraint as sqlite disables them by default for compatibility

            connection = DriverManager.getConnection(CONN_STR, config.toProperties());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;

        }
        return connection;

    }

//    public List<Movie> showData(String table) {
//        try (Connection con = getConnection()) {
//            List<Movie> movieList = new ArrayList<>();
//
//            String sql = "SELECT * FROM " + table;
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//
//            if (isResultSetEmpty(rs)) {
//                con.close();
//                return movieList;
//            }
//
//            while (rs.next()) {
//                String title = rs.getString("title");
//                String director = rs.getString("director");
//                int year = rs.getInt("year");
//                String rating = rs.getString("rating");
//                movieList.add(new Movie(title, director, year, rating));
//            }
//
//            return movieList;
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }







}

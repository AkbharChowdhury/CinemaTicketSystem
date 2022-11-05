package classes;

import enums.Files;
import org.sqlite.SQLiteConfig;
import tables.GenreTable;
import tables.RatingTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String DB_NAME = "cinema.db";
    private static Database instance;

    private Database() throws SQLException, FileNotFoundException {

        // if database file does not exist create the database file and populate tables with default values
        File databaseFile = new File(DB_NAME);
        if (!databaseFile.exists()) {
            createAllTables();
            insertDefaultValues();

        }

    }

    public static Database getInstance() throws SQLException, FileNotFoundException {

        return instance == null ? new Database() : null;
    }

    private static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }

    private void insertDefaultValues() throws FileNotFoundException {

                String ratingsFile = Helper.getCSVPath() + Files.Ratings.DESCRIPTION;
                List<String> ratingList = FileHandler.readSingleColumn(ratingsFile);
                insertSingleColumnTable(ratingList, new Rating().insert());
//        insertMovies();

    }

    private void createAllTables() throws SQLException {
        createTable(new Rating().createTable());
        createTable(new Movie().createTable());

    }

    private void createTable(String createTableSQL) throws SQLException {

        Connection con = getConnection();
        String sql = createTableSQL;
        Statement stmt = con.createStatement();
        stmt.execute(sql);
        con.close();

    }

    // used for the lookup tables e.g. Movies, Genres, Ratings, and Tickets tables
    private void insertSingleColumnTable(List<String> list, String insertSQL) {

        try (Connection con = getConnection()) {
            String sql = insertSQL;

            for (String item : list) {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setNull(1, java.sql.Types.NULL);
                stmt.setString(2, item);
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void insertMovies() throws FileNotFoundException {

        String movieFile = Helper.getCSVPath() + Files.Movies.DESCRIPTION;
        var movieList = FileHandler.getMovieData(movieFile);



        try (Connection con = getConnection()) {
            String sql = new Movie().insert();
            for (var movie : movieList) {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setNull(1, java.sql.Types.NULL);
                stmt.setString(2, movie.getTitle());
                stmt.setInt(3, movie.getDuration());
                stmt.setInt(4, movie.getRatingID());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    public Connection getConnection() {

        final String DATABASE_DRIVER = "org.sqlite.JDBC";
        final String CONN_STR = String.format("jdbc:sqlite:%s", DB_NAME);
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


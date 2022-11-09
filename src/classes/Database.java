package classes;

import enums.Files;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.MessageFormat;
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

        insertRatings();
        insertMovies();
        insertGenres();
        insertMovieGenres();
        insertTickets();

    }

    private void insertRatings() throws FileNotFoundException {
        List<String> ratingList = Helper.readSingleLineCSV(Files.Ratings.DESCRIPTION);
        insertSingleColumnTable(ratingList, new Rating().insert());
    }

    private void insertGenres() throws FileNotFoundException {
        List<String> GenreList = Helper.readSingleLineCSV(Files.Genres.DESCRIPTION);
        insertSingleColumnTable(GenreList, new Genre().insert());

    }


    private void createAllTables() throws SQLException {
        createTable(new Rating().createTable());
        createTable(new Movie().createTable());
        createTable(new Genre().createTable());
        createTable(new MovieGenres().createTable());
        createTable(new ShowTimes().createTable());
        createTable(new MovieShowTimes().createTable());
        createTable(new Customer().createTable());
        createTable(new Sales().createTable());
        createTable(new Ticket().createTable());
        createTable(new SalesDetails().createTable());


    }

    private void createTable(String createTableSQL) throws SQLException {

        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.execute(createTableSQL);
        con.close();

    }

    // used for the lookup tables e.g. Movies, Genres, Ratings, and Tickets tables
    private void insertSingleColumnTable(List<String> list, String insertSQL) {

        try (Connection con = getConnection()) {

            for (String item : list) {
                PreparedStatement stmt = con.prepareStatement(insertSQL);
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
        List<Movie> movieList = FileHandler.getMovieData(movieFile);


        try (Connection con = getConnection()) {
            for (var movie : movieList) {
                PreparedStatement stmt = con.prepareStatement(new Movie().insert());
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

    private void insertMovieGenres() throws FileNotFoundException {

        String movieGenreFile = Helper.getCSVPath() + Files.MovieGenres.DESCRIPTION;
        List<MovieGenres> movieGenres = FileHandler.getMovieGenreData(movieGenreFile);


        try (Connection con = getConnection()) {
            for (var item : movieGenres) {
                PreparedStatement stmt = con.prepareStatement(new MovieGenres().insert());
                stmt.setInt(1, item.getMovieID());
                stmt.setInt(2, item.getGenreID());
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

    private void insertTickets() throws FileNotFoundException {
        String ticketFile = Helper.getCSVPath() + Files.Movies.DESCRIPTION;
        List<Ticket> ticketList = FileHandler.getTicketData(Helper.getCSVPath() + Files.Tickets.DESCRIPTION);

        try (Connection con = getConnection()) {

            for (var ticket : ticketList) {
                PreparedStatement stmt = con.prepareStatement(new Ticket().insert());
                stmt.setNull(1, java.sql.Types.NULL);
                stmt.setString(2, ticket.getType());
                stmt.setDouble(3, ticket.getPrice());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public List<MovieGenres> showMovieList(boolean search, int genres, String movieTitle) {
        List<MovieGenres> list = new ArrayList<>();
        try (Connection con = getConnection()) {
            String sql;
            ResultSet rs;

            if (search){
                PreparedStatement stmt = con.prepareStatement(new MovieGenres().showMovieList(search));
                stmt.setString(1,  '%'+ String.valueOf(genres) + '%');
                stmt.setString(2,  '%'+ String.valueOf(movieTitle) + '%');

                rs = stmt.executeQuery();
            } else {
                sql = new MovieGenres().showMovieList(search);
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

            }

            if (isResultSetEmpty(rs)) {
                return list;
            }

            // add movies to list
            while (rs.next()) {
                int movieID = rs.getInt("movie_id");
                String title = rs.getString("title");
                int duration = Integer.parseInt(rs.getString("duration"));
                String genreList = rs.getString("genre_list");

                list.add(new MovieGenres(movieID, title, duration, genreList));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // for search
    public List<String> getMovieGenreList() {
        List<String> genreList = new ArrayList<>();
        try (Connection con = getConnection()) {

            String sql = new MovieGenres().getMovieGenreList();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);



            if (isResultSetEmpty(rs)) {
                con.close();
                return genreList;
            }
            while (rs.next()) {
                genreList.add(rs.getString("genre"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return genreList;
    }


}


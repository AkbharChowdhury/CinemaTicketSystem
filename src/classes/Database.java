package classes;

import classes.models.*;
import classes.utils.FileHandler;
import classes.utils.Helper;
import enums.Files;
import enums.FormDetails;
import org.apache.commons.lang3.StringUtils;
import org.sqlite.SQLiteConfig;
import tables.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String DB_NAME = "cinema.db";
    private static volatile Database instance;

    private Database() {

        // if database file does not exist create the database file and populate tables with default values
       try {
           File databaseFile = new File(DB_NAME);
           if (!databaseFile.exists()) {
               createAllTables();
               insertDefaultValues();

           }

       } catch (Exception e){

       }

    }

    public static Database getInstance() {
        try {
            if (instance == null) {
                synchronized (Database.class) {
                    if (instance == null) {
                        instance = new Database();
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        return instance;


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
        insertShowTimes();
        insertCustomer();


    }

    private void insertRatings() throws FileNotFoundException {
        insertSingleColumnTable(Helper.readSingleLineCSV(Files.Ratings.DESCRIPTION), new Rating().insert());
    }

    private void insertGenres() throws FileNotFoundException {
        insertSingleColumnTable(Helper.readSingleLineCSV(Files.Genres.DESCRIPTION), new Genre().insert());

    }


    private void createAllTables() {
        createTable(new Ticket().createTable());
        createTable(new Rating().createTable());
        createTable(new Movie().createTable());
        createTable(new Genre().createTable());
        createTable(new MovieGenres().createTable());
        createTable(new ShowTimes().createTable());
        createTable(new Customer().createTable());
        createTable(new Sales().createTable());

    }

    private void createTable(String createTableSQL) {
        try (var con = getConnection(); Statement stmt = con.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (Exception e) {
            getErrorMessage(e);

        }

    }

    // used for the lookup tables e.g. Movies, Genres, Ratings, and Tickets tables
    private void insertSingleColumnTable(List<String> list, String insertSQL) {

        try (var con = getConnection();
             var stmt = con.prepareStatement(insertSQL)) {
            for (String item : list) {
                var c = new Counter();
                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setString(c.getCounter(), item);
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }

    private void getErrorMessage(Exception ex) {
        System.err.println(ex.getMessage());


    }

    private void insertMovies() throws FileNotFoundException {

        String movieFile = Helper.getCSVPath() + Files.Movies.DESCRIPTION;
        List<Movie> movieList = FileHandler.getMovieData(movieFile);


        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Movie().insert())) {
            for (var movie : movieList) {

                var c = new Counter();
                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setString(c.getCounter(), movie.getTitle());
                stmt.setInt(c.getCounter(), movie.getDuration());
                stmt.setInt(c.getCounter(), movie.getRatingID());
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }

    private void insertShowTimes() throws FileNotFoundException {

        var showTimeList = FileHandler.getShowTimeData();


        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new ShowTimes().insert())) {
            for (var showtime : showTimeList) {
                var c = new Counter();
                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setInt(c.getCounter(), showtime.getMovieID());
                stmt.setString(c.getCounter(), showtime.getDate());
                stmt.setString(c.getCounter(), showtime.getTime());
                stmt.setInt(c.getCounter(), showtime.getNumTicketsLeft());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }

    private void insertCustomer() throws FileNotFoundException {

        var customerList = FileHandler.getCustomerData();


        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Customer().insert())) {
            for (var customer : customerList) {
                customerDataParams(customer, stmt);

                stmt.executeUpdate();

            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }


    public boolean addCustomer(Customer customer) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Customer().insert())) {
            customerDataParams(customer, stmt);
            return stmt.executeUpdate() == 1;

        } catch (Exception ex) {
            getErrorMessage(ex);
        }
        return false;

    }

    private void customerDataParams(Customer customer, PreparedStatement stmt) throws SQLException {
        var c = new Counter();
        stmt.setNull(c.getCounter(), Types.NULL);
        stmt.setString(c.getCounter(), customer.getFirstname());
        stmt.setString(c.getCounter(), customer.getLastname());
        stmt.setString(c.getCounter(), customer.getEmail());
        stmt.setString(c.getCounter(), customer.getPassword());
        stmt.setInt(c.getCounter(), customer.getTicketID());

    }


    private void insertMovieGenres() throws FileNotFoundException {

        String movieGenreFile = Helper.getCSVPath() + Files.MovieGenres.DESCRIPTION;
        List<MovieGenres> movieGenres = FileHandler.getMovieGenreData(movieGenreFile);


        try (var con = getConnection();
             var stmt = con.prepareStatement(new MovieGenres().insert())) {

            for (var item : movieGenres) {
                var c = new Counter();
                stmt.setInt(c.getCounter(), item.getMovieID());
                stmt.setInt(c.getCounter(), item.getGenreID());
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }


    private Connection getConnection() {

        final String DATABASE_DRIVER = "org.sqlite.JDBC";
        final String CONN_STR = String.format("jdbc:sqlite:%s", DB_NAME);
        Connection connection;

        try {
            Class.forName(DATABASE_DRIVER);
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true); // enables foreign key constraint as sqlite disables them by default for compatibility
            connection = DriverManager.getConnection(CONN_STR, config.toProperties());
        } catch (ClassNotFoundException | SQLException e) {
            getErrorMessage(e);
            return null;

        }
        return connection;

    }

    private void insertTickets() throws FileNotFoundException {
        List<Ticket> ticketList = FileHandler.getTicketData(Helper.getCSVPath() + Files.Tickets.DESCRIPTION);

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Ticket().insert())) {

            for (var ticket : ticketList) {
                var c = new Counter();

                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setString(c.getCounter(), ticket.getType());
                stmt.setDouble(c.getCounter(), ticket.getPrice());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

    }

    public List<MovieGenres> showMovieList(MovieGenres movieGenres) {
        List<MovieGenres> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement(MovieGenres.showMovieList(movieGenres))) {
            // search by genre and movie title
            String genre = movieGenres.getGenre();
            var c = new Counter();
            stmt.setString(c.getCounter(), "%" + movieGenres.getTitle() + "%");

            if (!FormDetails.defaultGenre().equalsIgnoreCase(genre)) {
                stmt.setString(c.getCounter(), '%' + genre + '%');
            }

            ResultSet rs = stmt.executeQuery();


            if (isResultSetEmpty(rs)) {
                return list;
            }

            // add movies to list
            while (rs.next()) {
                int movieID = rs.getInt(MovieTable.COLUMN_ID);
                String title = rs.getString(MovieTable.COLUMN_TITLE);
                int duration = Integer.parseInt(rs.getString(MovieTable.COLUMN_DURATION));
                String genreList = rs.getString(MovieGenresTable.COLUMN_GENRE_LIST);
                String rating = rs.getString(RatingTable.COLUMN_RATING);
                list.add(new MovieGenres(movieID, title, duration, genreList, rating));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return list;
    }

    public List<MovieGenres> showMovieList12() {
        List<MovieGenres> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement("""
                     SELECT m.title,
                            m.duration,
                            r.rating,
                            Group_concat(g.genre, '/') genre_list,
                            Group_concat(g.genre_id)   genre_id_list,
                            mg.movie_id,
                            mg.genre_id
                     FROM   MovieGenres mg
                            JOIN Movies m
                              ON mg.movie_id = m.movie_id
                            JOIN genres g
                              ON mg.genre_id = g.genre_id
                            JOIN Ratings r
                              ON m.rating_id = r.rating_id
                               
                     GROUP BY m.movie_id
                                                  
                     """)) {


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int movieID = rs.getInt(MovieTable.COLUMN_ID);
                String title = rs.getString(MovieTable.COLUMN_TITLE);
                int duration = Integer.parseInt(rs.getString(MovieTable.COLUMN_DURATION));
                String genreList = rs.getString(MovieGenresTable.COLUMN_GENRE_LIST);
                String rating = rs.getString(RatingTable.COLUMN_RATING);
                list.add(new MovieGenres(movieID, title, duration, genreList, rating));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return list;
    }


    public List<MovieGenres> showMovieList1() {
        List<MovieGenres> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement("""
                                  
                                     SELECT m.title,
                                            m.duration,
                                            r.rating,
                                            Group_concat(g.genre, '/') genre_list,
                                            Group_concat(g.genre_id)   genre_id_list,
                                            mg.movie_id,
                                            mg.genre_id
                                     FROM   MovieGenres mg
                                            JOIN Movies m
                                              ON mg.movie_id = m.movie_id
                                            JOIN genres g
                                              ON mg.genre_id = g.genre_id
                                            JOIN Ratings r
                                              ON m.rating_id = r.rating_id                         
                                     GROUP BY m.movie_id
                     """
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int movieID = rs.getInt(MovieTable.COLUMN_ID);
                String title = rs.getString(MovieTable.COLUMN_TITLE);
                int duration = Integer.parseInt(rs.getString(MovieTable.COLUMN_DURATION));
                String genreList = rs.getString(MovieGenresTable.COLUMN_GENRE_LIST);
                String rating = rs.getString(RatingTable.COLUMN_RATING);
                list.add(new MovieGenres(movieID, title, duration, genreList, rating));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return list;
    }


    public List<ShowTimes> showMovieTimes(ShowTimes movieShowTimes) {
        String showDate = movieShowTimes.getDate();
        List<ShowTimes> list = new ArrayList<>();

        try (var con = getConnection();
             var stmt = con.prepareStatement(ShowTimes.getSelectedMovieShowTimes(movieShowTimes))) {
            var c = new Counter();

            // selected movie
            stmt.setInt(c.getCounter(), movieShowTimes.getMovieID());
            // selected date
            if (!StringUtils.isEmpty(showDate)) stmt.setString(c.getCounter(), showDate);
            ResultSet rs = stmt.executeQuery();
            if (isResultSetEmpty(rs)) return list;

            while (rs.next()) {
                String title = rs.getString(MovieTable.COLUMN_TITLE);
                String date = rs.getString(ShowTimesTable.COLUMN_SHOW_DATE);
                String time = rs.getString(ShowTimesTable.COLUMN_SHOW_TIME);
                int ticketsLeft = rs.getInt(ShowTimesTable.COLUMN_NUM_TICKETS_LEFT);
                int showTimeID = rs.getInt(ShowTimesTable.COLUMN_ID);
                int movieID = rs.getInt(ShowTimesTable.COLUMN_MOVIE_ID);

                list.add(new ShowTimes(date, time, title, ticketsLeft, showTimeID, movieID));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return list;
    }


    // for movie search
    public List<String> getMovieGenreList() {
        List<String> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(MovieGenres.getMovieGenreList());
            if (isResultSetEmpty(rs)) return list;

            while (rs.next()) {
                list.add(rs.getString(GenreTable.COLUMN_GENRE));
            }

        } catch (Exception e) {
            getErrorMessage(e);
        }

        return list.stream().sorted().toList();
    }


    public boolean SalesExists(Sales sales) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(Sales.salesExists())) {
            var c = new Counter();
            stmt.setInt(c.getCounter(), sales.showTimeID());
            stmt.setInt(c.getCounter(), sales.customerID());
            stmt.setString(c.getCounter(), sales.salesDate());

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            getErrorMessage(e);
        }
        return false;

    }


    public boolean addSales(Sales sales) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Sales().insert())
        ) {
            var c = new Counter();

            stmt.setInt(c.getCounter(), sales.showTimeID());
            stmt.setInt(c.getCounter(), sales.customerID());
            stmt.setString(c.getCounter(), sales.salesDate());
            stmt.setInt(c.getCounter(), sales.totalTicketsSold());

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return false;

    }


    public List<Invoice> getInvoice(int customerID) {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement(classes.models.Invoice.getInvoiceDetails())) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setFirstname(rs.getString(CustomerTable.COLUMN_FIRSTNAME));
                invoice.setLastname(rs.getString(CustomerTable.COLUMN_LASTNAME));
                invoice.setSalesDate(rs.getString(SalesTable.COLUMN_SALES_DATE));
                invoice.setShowDate(rs.getString(ShowTimesTable.COLUMN_SHOW_DATE));
                invoice.setShowTime(rs.getString(ShowTimesTable.COLUMN_SHOW_TIME));
                invoice.setMovieTitle(rs.getString(MovieTable.COLUMN_TITLE));
                invoice.setRating(rs.getString(RatingTable.COLUMN_RATING));
                invoice.setTotalTicket(rs.getInt(SalesTable.COLUMN_TOTAL_TICKETS_SOLD));
                invoices.add(invoice);


            }
            return invoices;

        } catch (Exception ex) {
            getErrorMessage(ex);
        }

        return invoices;

    }


    public List<Movie> getAllMovieShowTimes() {
        List<Movie> movies = new ArrayList<>();

        try (Connection con = getConnection();
             var stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(ShowTimes.getAllMovieShowTimes());


            while (rs.next()) {

                movies.add(new Movie(rs.getInt(MovieTable.COLUMN_ID), rs.getString(MovieTable.COLUMN_TITLE)));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return movies;
    }


    public List<Ticket> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(Ticket.getTickets());

            if (isResultSetEmpty(rs)) return ticketList;

            while (rs.next()) {
                int ticketID = rs.getInt(TicketsTable.COLUMN_ID);
                String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
                double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
                ticketList.add(new Ticket(ticketID, ticketType, price));

            }

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return ticketList;
    }

    public String getMovieName(int movieID) {


        try (var con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ MovieTable.COLUMN_TITLE } FROM \{ MovieTable.TABLE_NAME } WHERE \{ MovieTable.COLUMN_ID } = ?" )) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return "Error: there are no movies that exists with the specified id!";
            }

            return rs.getString(MovieTable.COLUMN_TITLE);


        } catch (Exception e) {
            getErrorMessage(e);
        }
        return "error fetching movie name by movie id";

    }

    public int getMovieID(String title) {


        try (Connection con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ MovieTable.COLUMN_ID } FROM \{ MovieTable.TABLE_NAME } WHERE \{ MovieTable.COLUMN_TITLE } = ?" )) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) return 0;

            return rs.getInt(MovieTable.COLUMN_ID);


        } catch (Exception e) {
            getErrorMessage(e);
        }
        return 0;

    }


    public boolean isAuthorised(String email, String password) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ CustomerTable.COLUMN_EMAIL }, \{ CustomerTable.COLUMN_PASSWORD } FROM \{ CustomerTable.TABLE_NAME } WHERE \{ CustomerTable.COLUMN_EMAIL } = ? AND \{ CustomerTable.COLUMN_PASSWORD } = ?" )) {

            var c = new Counter();
            stmt.setString(c.getCounter(), email);
            stmt.setString(c.getCounter(), password);
            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            getErrorMessage(e);

        }
        return false;

    }


    public boolean emailExists(String email) {
        try (var con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ CustomerTable.COLUMN_EMAIL } FROM \{ CustomerTable.TABLE_NAME } WHERE \{ CustomerTable.COLUMN_EMAIL } LIKE ?" )) {

            stmt.setString(1, email);

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            getErrorMessage(e);
        }
        return false;

    }


    public boolean customerInvoiceExists(int customerID) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ SalesTable.COLUMN_CUSTOMER_ID } FROM \{ SalesTable.TABLE_NAME } WHERE \{ SalesTable.COLUMN_CUSTOMER_ID } = ?" )) {
            stmt.setInt(1, customerID);

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            getErrorMessage(e);
        }

        return false;

    }

    public int getCustomerID(String email) {


        try (var con = getConnection();
             var stmt = con.prepareStatement(STR. "SELECT \{ CustomerTable.COLUMN_ID } FROM \{ CustomerTable.TABLE_NAME } WHERE \{ CustomerTable.COLUMN_EMAIL } =?" )) {
            stmt.setString(1, email);
            ResultSet rs3 = stmt.executeQuery();
            return rs3.getInt(CustomerTable.COLUMN_ID);


        } catch (Exception e) {
            getErrorMessage(e);
        }
        return 0;

    }


    public Ticket getCustomerTicketType(int customerID) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(Customer.getCustomerTicketType())
        ) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            int ticketId = rs.getInt(TicketsTable.COLUMN_ID);
            String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
            double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
            return new Ticket(ticketId, ticketType, price);


        } catch (Exception e) {
            getErrorMessage(e);
        }

        return null;
    }

    public int getNumTickets(ShowTimes movieShowTimes) {
        try (Connection con = getConnection();
             var stmt = con.prepareStatement(ShowTimes.getSelectedShowDetails())) {
            stmt.setInt(1, movieShowTimes.getShowTimeID());
            ResultSet r = stmt.executeQuery();

            if (isResultSetEmpty(r)) return 0;

            return r.getInt(ShowTimesTable.COLUMN_NUM_TICKETS_LEFT);

        } catch (Exception e) {
            getErrorMessage(e);
        }
        return 0;
    }


    public boolean updateNumTickets(ShowTimes movieShowTimes) {

        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(ShowTimes.updateNumTickets())) {
            int numTicketsLeft = getNumTickets(movieShowTimes);
            int remainingTickets = numTicketsLeft - movieShowTimes.getNumTicketsSold();
            var c = new Counter();
            stmt.setInt(c.getCounter(), remainingTickets);
            stmt.setInt(c.getCounter(), movieShowTimes.getShowTimeID());
            // update
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            getErrorMessage(e);
        }
        return false;
    }

}









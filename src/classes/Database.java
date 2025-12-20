package classes;

import classes.models.*;
import classes.utils.FileHandler;
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
import static  classes.utils.Helper.error;

public class Database {
    private static final String DB_NAME = "cinema.db";
    private static volatile Database instance;

    private Database() {
        try {
            File databaseFile = new File(DB_NAME);
            if (!databaseFile.exists()) {
                createAllTables();
                insertDefaultValues();

            }

        } catch (Exception e) {
            error.accept(e);

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
        } catch (Exception e) {
            error.accept(e);
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
        insertSingleColumnTable(FileHandler.readSingleLineCSV(Files.Ratings.DESCRIPTION), new Rating().insert());
    }

    private void insertGenres() throws FileNotFoundException {
        insertSingleColumnTable(FileHandler.readSingleLineCSV(Files.Genres.DESCRIPTION), new Genre().insert());

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
            error.accept(e);


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

        } catch (Exception e) {
            error.accept(e);

        }

    }

    private void insertMovies() throws FileNotFoundException {

        String movieFile = FileHandler.CSV_PATH + Files.Movies.DESCRIPTION;
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

        } catch (Exception e) {
            error.accept(e);

        }

    }

    private void insertShowTimes() throws FileNotFoundException {

        var showTimeList = FileHandler.getShowTimeData();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new ShowTimes().insert())) {
            for (ShowTimes showtime : showTimeList) {
                var c = new Counter();
                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setInt(c.getCounter(), showtime.getMovieID());
                stmt.setString(c.getCounter(), showtime.getDate());
                stmt.setString(c.getCounter(), showtime.getTime());
                stmt.setInt(c.getCounter(), showtime.getNumTicketsLeft());
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            error.accept(e);

        }

    }

    private void insertCustomer() throws FileNotFoundException {

        var customerList = FileHandler.getCustomerData();
        try (var con = getConnection();
             var stmt = con.prepareStatement(new Customer().insert())) {
            for (var customer : customerList) {
                customerDataParams(customer, stmt);
                stmt.executeUpdate();

            }

        } catch (Exception e) {
            error.accept(e);

        }

    }


    public boolean addCustomer(Customer customer) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Customer().insert())) {
            customerDataParams(customer, stmt);
            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            error.accept(e);

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

        String movieGenreFile = FileHandler.CSV_PATH + Files.MovieGenres.DESCRIPTION;
        List<MovieGenres> movieGenres = FileHandler.getMovieGenreData(movieGenreFile);


        try (var con = getConnection();
             var stmt = con.prepareStatement(new MovieGenres().insert())) {

            for (MovieGenres item : movieGenres) {
                var c = new Counter();
                stmt.setInt(c.getCounter(), item.getMovieID());
                stmt.setInt(c.getCounter(), item.getGenreID());
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            error.accept(e);

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
            error.accept(e);
            return null;

        }
        return connection;

    }

    private void insertTickets() throws FileNotFoundException {
        List<Ticket> ticketList = FileHandler.getTicketData(FileHandler.CSV_PATH + Files.Tickets.DESCRIPTION);

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(new Ticket().insert())) {

            for (var ticket : ticketList) {
                var c = new Counter();

                stmt.setNull(c.getCounter(), java.sql.Types.NULL);
                stmt.setString(c.getCounter(), ticket.getType());
                stmt.setDouble(c.getCounter(), ticket.getPrice());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            error.accept(e);
        }

    }

//    public List<MovieGenres> showMovieList(MovieGenres movieGenres) {
//        List<MovieGenres> list = new ArrayList<>();
//        try (Connection con = getConnection();
//             var stmt = con.prepareStatement(MovieGenres.showMovieList(movieGenres))) {
//            // search by genre and movie title
//            String genre = movieGenres.getGenre();
//            var c = new Counter();
//            stmt.setString(c.getCounter(), "%" + movieGenres.getTitle() + "%");
//
//            if (!FormDetails.defaultGenre.get().equalsIgnoreCase(genre)) {
//                stmt.setString(c.getCounter(), '%' + genre + '%');
//            }
//
//            ResultSet rs = stmt.executeQuery();
//
//            if (isResultSetEmpty(rs)) {
//                return list;
//            }
//
//            // add movies to list
//            while (rs.next()) {
//                int movieID = rs.getInt(MovieTable.COLUMN_ID);
//                String title = rs.getString(MovieTable.COLUMN_TITLE);
//                int duration = Integer.parseInt(rs.getString(MovieTable.COLUMN_DURATION));
//                String genreList = rs.getString(MovieGenresTable.COLUMN_GENRE_LIST);
//                String rating = rs.getString(RatingTable.COLUMN_RATING);
//                list.add(new MovieGenres(movieID, title, duration, genreList, rating));
//
//            }
//
//        } catch (Exception e) {
//            error.accept(e);
//        }
//        return list;
//    }

    public List<MovieGenres> getMovies() {
        List<MovieGenres> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.prepareStatement("""
                     SELECT m.title,
                            m.duration,
                            r.rating,
                            Group_concat(genre, '/') genre_list,
                            mg.movie_id,
                            mg.genre_id
                     FROM   MovieGenres mg
                            NATURAL JOIN Movies m
                            NATURAL JOIN genres g
                            NATURAL JOIN Ratings r
                     
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
            error.accept(e);
        }
        return list;
    }


    public List<ShowTimes> showMovieTimes(ShowTimes movieShowTimes) {
        String showDate = movieShowTimes.getDate();
        List<ShowTimes> list = new ArrayList<>();

        try (var con = getConnection();
             var stmt = con.prepareStatement(ShowTimes.getSelectedMovieShowTimes(movieShowTimes))) {
            var c = new Counter();

            stmt.setInt(c.getCounter(), movieShowTimes.getMovieID());
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
            error.accept(e);
        }
        return list;
    }


    public List<String> getMovieGenreList() {
        List<String> list = new ArrayList<>();
        try (Connection con = getConnection();
             var stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery( """
                SELECT DISTINCT(genre)
                FROM MovieGenres 
                    
                NATURAL JOIN Genres
                ORDER BY genre                         
                """);
            if (isResultSetEmpty(rs)) return list;

            while (rs.next()) {
                list.add(rs.getString(GenreTable.COLUMN_GENRE));
            }

        } catch (Exception e) {
            error.accept(e);
        }

        return list.stream().sorted().toList();
    }


    public boolean salesExists(Sales sales) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(Sales.salesExists())) {
            var c = new Counter();
            stmt.setInt(c.getCounter(), sales.showTimeID());
            stmt.setInt(c.getCounter(), sales.customerID());
            stmt.setString(c.getCounter(), sales.salesDate());

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
           error.accept(e);
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
           error.accept(e);
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

        } catch (Exception e) {
            error.accept(e);
        }

        return invoices;

    }


    public List<Movie> getAllMovieShowTimes() {
        List<Movie> movies = new ArrayList<>();

        try (Connection con = getConnection();
             var stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("""
                                   SELECT DISTINCT s.movie_id,
                                                    m.title
                                    FROM   showtimes s
                                           NATURAL JOIN movies m
                                    WHERE  show_date BETWEEN Date('NOW') AND Date('now', 'start of month','+1 month','-1 day')
                                           AND num_tickets_left > 0
                                    ORDER  BY m.title
                    
                    """);

            while (rs.next()) {

                movies.add(new Movie(rs.getInt(MovieTable.COLUMN_ID), rs.getString(MovieTable.COLUMN_TITLE)));

            }

        } catch (Exception e) {
           error.accept(e);
        }
        return movies;
    }


    public List<Ticket> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(STR."SELECT * FROM \{TicketsTable.TABLE_NAME}");

            if (isResultSetEmpty(rs)) return ticketList;

            while (rs.next()) {
                int ticketID = rs.getInt(TicketsTable.COLUMN_ID);
                String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
                double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
                ticketList.add(new Ticket(ticketID, ticketType, price));

            }

        } catch (Exception e) {
            error.accept(e);
        }
        return ticketList;
    }

    public String getMovieName(int movieID) {


        try (var con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{MovieTable.COLUMN_TITLE} FROM \{MovieTable.TABLE_NAME} WHERE \{MovieTable.COLUMN_ID} = ?")) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return "Error: there are no movies that exists with the specified id!";
            }

            return rs.getString(MovieTable.COLUMN_TITLE);


        } catch (Exception e) {
           error.accept(e);
        }
        return "error fetching movie name by movie id";

    }

    public int getMovieID(String title) {

        try (var con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{MovieTable.COLUMN_ID} FROM \{MovieTable.TABLE_NAME} WHERE \{MovieTable.COLUMN_TITLE} = ?")) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) return 0;

            return rs.getInt(MovieTable.COLUMN_ID);


        } catch (Exception e) {
           error.accept(e);
        }
        return 0;

    }


    public boolean isAuthorised(String email, String password) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{CustomerTable.COLUMN_EMAIL}, \{CustomerTable.COLUMN_PASSWORD} FROM \{CustomerTable.TABLE_NAME} WHERE \{CustomerTable.COLUMN_EMAIL} = ? AND \{CustomerTable.COLUMN_PASSWORD} = ?")) {

            Counter c = new Counter();
            stmt.setString(c.getCounter(), email);
            stmt.setString(c.getCounter(), password);
            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
           error.accept(e);

        }
        return false;

    }


    public boolean emailExists(String email) {
        try (var con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{CustomerTable.COLUMN_EMAIL} FROM \{CustomerTable.TABLE_NAME} WHERE \{CustomerTable.COLUMN_EMAIL} LIKE ?")) {

            stmt.setString(1, email);

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            error.accept(e);
        }
        return false;

    }


    public boolean customerInvoiceExists(int customerID) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{SalesTable.COLUMN_CUSTOMER_ID} FROM \{SalesTable.TABLE_NAME} WHERE \{SalesTable.COLUMN_CUSTOMER_ID} = ?")) {
            stmt.setInt(1, customerID);

            ResultSet rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            error.accept(e);
        }

        return false;

    }

    public int getCustomerID(String email) {


        try (var con = getConnection();
             var stmt = con.prepareStatement(STR."SELECT \{CustomerTable.COLUMN_ID} FROM \{CustomerTable.TABLE_NAME} WHERE \{CustomerTable.COLUMN_EMAIL} =?")) {
            stmt.setString(1, email);
            ResultSet rs3 = stmt.executeQuery();
            return rs3.getInt(CustomerTable.COLUMN_ID);


        } catch (Exception e) {
            error.accept(e);
        }
        return 0;

    }


    public Ticket getCustomerTicketType(int customerID) {

        try (Connection con = getConnection();
             var stmt = con.prepareStatement("""
                                     SELECT c.customer_id, t.type, t.price, t.ticket_id                        
                                     FROM Customers c
                                     NATURAL JOIN Tickets t
                                     WHERE customer_id = ?
                     """)
        ) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            int ticketId = rs.getInt(TicketsTable.COLUMN_ID);
            String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
            double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
            return new Ticket(ticketId, ticketType, price);


        } catch (Exception e) {
            error.accept(e);
        }

        return null;
    }

    public int getNumTickets(int showTimeID) {
        String sql = STR."SELECT \{ShowTimesTable.COLUMN_NUM_TICKETS_LEFT} FROM \{ShowTimesTable.TABLE_NAME} WHERE \{ShowTimesTable.COLUMN_ID} =?";
        try (Connection con = getConnection(); var stmt = con.prepareStatement(sql)){
            stmt.setInt(1, showTimeID);
            ResultSet r = stmt.executeQuery();
            if (isResultSetEmpty(r)) return 0;

            return r.getInt(ShowTimesTable.COLUMN_NUM_TICKETS_LEFT);


    } catch (Exception e) {

        }
        return 0;

    }





    public boolean updateNumTickets(ShowTimes movieShowTimes) {

        try (var conn = getConnection();
             var stmt = conn.prepareStatement(STR."UPDATE \{ShowTimesTable.TABLE_NAME} SET \{ShowTimesTable.COLUMN_NUM_TICKETS_LEFT} = ? WHERE \{ShowTimesTable.COLUMN_ID} = ?")) {
            int numTicketsLeft = getNumTickets(movieShowTimes.getShowTimeID());
            int remainingTickets = numTicketsLeft - movieShowTimes.getNumTicketsSold();
            var c = new Counter();
            stmt.setInt(c.getCounter(), remainingTickets);
            stmt.setInt(c.getCounter(), movieShowTimes.getShowTimeID());
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            error.accept(e);
        }
        return false;
    }

}










package classes;

import enums.Files;
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

    private Database() throws SQLException, FileNotFoundException {

        // if database file does not exist create the database file and populate tables with default values
        File databaseFile = new File(DB_NAME);
        if (!databaseFile.exists()) {
            createAllTables();
            insertDefaultValues();

        }

    }

    public static Database getInstance() throws SQLException, FileNotFoundException {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
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
        List<String> ratingList = Helper.readSingleLineCSV(Files.Ratings.DESCRIPTION);
        insertSingleColumnTable(ratingList, new Rating().insert());
    }

    private void insertGenres() throws FileNotFoundException {
        List<String> GenreList = Helper.readSingleLineCSV(Files.Genres.DESCRIPTION);
        insertSingleColumnTable(GenreList, new Genre().insert());

    }


    private void createAllTables() throws SQLException {
        createTable(new Ticket().createTable());
        createTable(new Rating().createTable());
        createTable(new Movie().createTable());
        createTable(new Genre().createTable());
        createTable(new MovieGenres().createTable());
        createTable(new ShowTimes().createTable());
        createTable(new Customer().createTable());
        createTable(new Sales().createTable());

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

    private void insertShowTimes() throws FileNotFoundException {

        var showTimeList = FileHandler.getShowTimeData();


        try (Connection con = getConnection()) {
            for (var showtime : showTimeList) {
                PreparedStatement stmt = con.prepareStatement(new ShowTimes().insert());
                stmt.setNull(1, java.sql.Types.NULL);
                stmt.setInt(2, showtime.getMovieID());
                stmt.setString(3, showtime.getDate());
                stmt.setString(4, showtime.getTime());
                stmt.setInt(5, showtime.getNumTicketsLeft());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void insertCustomer() throws FileNotFoundException {

        var customerList = FileHandler.getCustomerData();


        try (Connection con = getConnection()) {
            for (var customer : customerList) {
                PreparedStatement stmt = con.prepareStatement(new Customer().insert());
                stmt.setNull(1, java.sql.Types.NULL);
                stmt.setString(2, customer.getFirstname());
                stmt.setString(3, customer.getLastname());
                stmt.setString(4, customer.getEmail());
                stmt.setString(5, customer.getPassword());
                stmt.setInt(6, customer.getTicketID());

                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public boolean addCustomer(Customer customer) {

        try (Connection con = getConnection()) {
            PreparedStatement stmt = con.prepareStatement(new Customer().insert());
            stmt.setNull(1, java.sql.Types.NULL);
            stmt.setString(2, customer.getFirstname());
            stmt.setString(3, customer.getLastname());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPassword());
            stmt.setInt(6, customer.getTicketID());
            return stmt.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

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


//    public List<MovieGenres> showMovieList(MovieGenres movieGenres) {
//        List<MovieGenres> list = new ArrayList<>();
//        try (Connection con = getConnection()) {
//            String sql;
//            ResultSet rs;
//            // search by genre and movie title
//            int genreID = movieGenres.getGenreID();
//            String movieTitle = movieGenres.getTitle();
//
//            if (genreID == 0 && movieTitle.isEmpty()) {
//                sql = new MovieGenres().showMovieList(movieGenres);
//                Statement stmtAllMovies = con.createStatement();
//                rs = stmtAllMovies.executeQuery(sql);
//
//            } else {
//                PreparedStatement stmt = con.prepareStatement(new MovieGenres().showMovieList(movieGenres));
//                int x = 0;
//
//                if (genreID != 0) {
//                    x++;
//                    stmt.setString(x, +'%' + String.valueOf(genreID)); // check if genre id starts with the id
//                    x++;
//                    stmt.setString(x, String.valueOf(genreID) + '%'); // check if genre id ens with
//
//
//
//                }
//                if (!movieTitle.isEmpty()) {
//                    x++;
//                    stmt.setString(x, '%' + movieTitle + '%');
//                }
//                rs = stmt.executeQuery();
//            }
//
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
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
public List<MovieGenres> showMovieList(MovieGenres movieGenres) {
    List<MovieGenres> list = new ArrayList<>();
    try (Connection con = getConnection()) {
        ResultSet rs;
        // search by genre and movie title
        int genreID = movieGenres.getGenreID();
        String movieTitle = movieGenres.getTitle();
        String sql = new MovieGenres().showMovieList(movieGenres);

        if (genreID == 0 && movieTitle.isEmpty()) {
            Statement stmtAllMovies = con.createStatement();
            rs = stmtAllMovies.executeQuery(sql);

        } else {
            PreparedStatement stmt = con.prepareStatement(sql);
            int param = 0;

            if (genreID != 0) {
//                param++;
//                stmt.setString(param, String.valueOf(genreID));
//                stmt.setString(x, +'%' + String.valueOf(genreID)); // check if genre id starts with the id
//                x++;
//                stmt.setString(x, String.valueOf(genreID) + '%'); // check if genre id ends with



            }
            if (!movieTitle.isEmpty()) {
                param++;
                stmt.setString(param, '%' + movieTitle + '%');
            }
            rs = stmt.executeQuery();
        }


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
        e.printStackTrace();
    }
    return list;
}


    public List<ShowTimes> showMovieTimes(ShowTimes movieShowTimes) {
        String showDate = movieShowTimes.getDate();
        List<ShowTimes> list = new ArrayList<>();

        try (Connection con = getConnection()) {
            ResultSet rs;
            String sql = new ShowTimes().getSelectedMovieShowTimes(movieShowTimes);
            PreparedStatement stmt = con.prepareStatement(sql);
            int param = 1;

            // selected movie id
            stmt.setInt(param, movieShowTimes.getMovieID());
            if (!showDate.isEmpty()) {
                param++;
                stmt.setString(param, showDate);
            }


            rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return list;
            }

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
            e.printStackTrace();
        }
        return list;
    }


    // for movie search
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


                genreList.add(rs.getString(GenreTable.COLUMN_GENRE));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return genreList;
    }




    public boolean customerSalesExists(Sales sales) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?",
                SalesTable.COLUMN_CUSTOMER_ID,
                SalesTable.TABLE_NAME,
                SalesTable.COLUMN_CUSTOMER_ID
        );

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, sales.getShowTimeID());
            stmt.setInt(2, sales.getCustomerID());
            stmt.setString(3, sales.getSalesDate());

            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean SalesExists(Sales sales) {

        String sql = new Sales().salesExists();

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, sales.getShowTimeID());
            stmt.setInt(2, sales.getCustomerID());
            stmt.setString(3, sales.getSalesDate());


            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean addSales(Sales sales) {

        try (Connection con = getConnection()) {
            int param = 1;

            PreparedStatement stmt1 = con.prepareStatement(new Sales().insert());

            stmt1.setInt(param, sales.getShowTimeID());
            param++;

            stmt1.setInt(param, sales.getCustomerID());
            param++;

            stmt1.setString(param, sales.getSalesDate());
            param++;

            stmt1.setInt(param, sales.getTotalTicketsSold());

            return stmt1.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }




    public List<Invoice> getInvoice(int customerID) {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection con = getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(new Invoice().getInvoiceDetails())) {
                stmt.setInt(1, customerID);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int price = rs.getInt(TicketsTable.COLUMN_PRICE);
                    Invoice invoice = new Invoice();
                    invoice.setFirstname(rs.getString(CustomerTable.COLUMN_FIRSTNAME));
                    invoice.setLastname(rs.getString(CustomerTable.COLUMN_LASTNAME));
                    invoice.setSalesDate(rs.getString(SalesTable.COLUMN_SALES_DATE));
                    invoice.setShowDate(rs.getString(ShowTimesTable.COLUMN_SHOW_DATE));
                    invoice.setShowTime(rs.getString(ShowTimesTable.COLUMN_SHOW_TIME));
                    invoice.setMovieTitle(rs.getString(MovieTable.COLUMN_TITLE));
                    invoice.setType(rs.getString(TicketsTable.COLUMN_TYPE));
                    invoice.setRating(rs.getString(RatingTable.COLUMN_RATING));
                    invoice.setTotalTicket(rs.getInt(SalesTable.COLUMN_TOTAL_TICKETS_SOLD));
                    invoice.setPrice(price);

                    invoices.add(invoice);


                }
                return invoices;

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public List<Movie> getAllMovieShowTimes() {
        List<Movie> movies = new ArrayList<>();

        try (Connection con = getConnection()) {

            String sql = new ShowTimes().getAllMovieShowTimes();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                movies.add(new Movie(rs.getInt(MovieTable.COLUMN_ID), rs.getString(MovieTable.COLUMN_TITLE)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
//
//    public List<String> getMovieTitle() {
//        List<String> movieTitleList = new ArrayList<>();
//        try (Connection con = getConnection()) {
//
//            String sql = new Movie().getMovieList();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//
//
//            if (isResultSetEmpty(rs)) {
//                con.close();
//                return movieTitleList;
//            }
//            while (rs.next()) {
//                movieTitleList.add(rs.getString("title"));
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return movieTitleList;
//    }


    public List<Ticket> getTicket() {
        List<Ticket> ticketList = new ArrayList<>();
        try (Connection con = getConnection()) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(new Ticket().getTickets());


            if (isResultSetEmpty(rs)) {
                con.close();
                return ticketList;
            }
            while (rs.next()) {
                int ticketID = rs.getInt(TicketsTable.COLUMN_ID);
                String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
                double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
                ticketList.add(new Ticket(ticketID, ticketType, price));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticketList;
    }

    public String getMovieName(int movieID) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", MovieTable.COLUMN_TITLE, MovieTable.TABLE_NAME, MovieTable.COLUMN_ID);

        try (Connection con = getConnection()) {
            ResultSet rs;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, movieID);

            rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return "Error: there are no movies that exists with the specified id!";
            }

            return rs.getString(MovieTable.COLUMN_TITLE);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";

    }

    public int getMovieID(String title) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", MovieTable.COLUMN_ID, MovieTable.TABLE_NAME, MovieTable.COLUMN_TITLE);

        try (Connection con = getConnection()) {
            ResultSet rs;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, title);

            rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return 0;
            }

            return rs.getInt(MovieTable.COLUMN_ID);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public String getFirstname(int customerID) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", CustomerTable.COLUMN_FIRSTNAME, CustomerTable.TABLE_NAME, CustomerTable.COLUMN_ID);

        try (Connection con = getConnection()) {
            ResultSet rs;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, customerID);

            rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return null;
            }

            return rs.getString(CustomerTable.COLUMN_FIRSTNAME);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public int getGenreID(String genre) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", GenreTable.COLUMN_ID, GenreTable.TABLE_NAME, GenreTable.COLUMN_GENRE);

        try (Connection con = getConnection()) {
            ResultSet rs;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, genre);

            rs = stmt.executeQuery();

            if (isResultSetEmpty(rs)) {
                return 0;
            }

            return rs.getInt(GenreTable.COLUMN_ID);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public boolean isAuthorised(String email, String password) {

        String sql = "SELECT * FROM Customers WHERE email = ? AND password = ?";

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);


            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean emailExists(String email) {

        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
                CustomerTable.COLUMN_EMAIL,
                CustomerTable.TABLE_NAME,
                CustomerTable.COLUMN_EMAIL
        );

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);

            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean customerInvoiceExists(int customerID) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?",
                SalesTable.COLUMN_CUSTOMER_ID,
                SalesTable.TABLE_NAME,
                SalesTable.COLUMN_CUSTOMER_ID
        );

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, customerID);

            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }



    public boolean movieIDExists(int movieID) {

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?",
                MovieTable.COLUMN_ID,
                MovieTable.TABLE_NAME,
                MovieTable.COLUMN_ID
        );

        try (Connection con = getConnection()) {
            ResultSet rs2;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, movieID);

            rs2 = stmt.executeQuery();

            return !isResultSetEmpty(rs2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public int getCustomerID(String email) {
        String sql = "SELECT " + CustomerTable.COLUMN_ID + " FROM " + CustomerTable.TABLE_NAME + " WHERE " + CustomerTable.COLUMN_EMAIL + " =?";


        try (Connection con = getConnection()) {

            ResultSet rs3;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);

            rs3 = stmt.executeQuery();


            return rs3.getInt(CustomerTable.COLUMN_ID);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }


    public Ticket getCustomerTicketType(int customerID) {
        Ticket ticket = null;

        try (Connection con = getConnection()) {

            String sql = new Customer().getCustomerTicketType();
            ResultSet rs;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, customerID);

            rs = stmt.executeQuery();


            while (rs.next()) {
                String ticketType = rs.getString(TicketsTable.COLUMN_TYPE);
                double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
                ticket = new Ticket(ticketType, price);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public int getNumTickets(ShowTimes movieShowTimes) {
        try (Connection con = getConnection()) {

            String sql = new ShowTimes().getSelectedShowDetails();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, movieShowTimes.getShowTimeID());
            ResultSet r = stmt.executeQuery();

            if (isResultSetEmpty(r)) {
                con.close();
                return 0;
            }
            return r.getInt(ShowTimesTable.COLUMN_NUM_TICKETS_LEFT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public boolean updateNumTickets(ShowTimes movieShowTimes) {


        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(new ShowTimes().updateNumTickets())) {
            int numTicketsLeft = getNumTickets(movieShowTimes);
            int remainingTickets = numTicketsLeft - movieShowTimes.getNumTicketsSold();

            System.out.println("left " + remainingTickets);

            // set the corresponding param
            pstmt.setInt(1, remainingTickets);
            pstmt.setInt(2, movieShowTimes.getShowTimeID());
            // update
            int result = pstmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}









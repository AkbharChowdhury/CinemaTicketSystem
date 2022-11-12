package classes;

import interfaces.Queries;
import interfaces.TableProperties;
import tables.*;

import java.util.ArrayList;
import java.util.List;

public class MovieShowTimes extends ShowTimes implements Queries, TableProperties {
    private int movieId;
    private int showTimeID;
    private int numTicketLeft;
    private String movieTitle;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public int getNumTicketLeft() {
        return numTicketLeft;
    }

    public void setNumTicketLeft(int numTicketLeft) {
        this.numTicketLeft = numTicketLeft;
    }
    public MovieShowTimes(){

    }

    public MovieShowTimes(int movieId, int showTimeID, int numTicketLeft) {
        this.movieId = movieId;
        this.showTimeID = showTimeID;
        this.numTicketLeft = numTicketLeft;
    }
    public MovieShowTimes(String showDate, String ShowTime, String movieTitle, int numTicketLeft, int showTimeID){
        super(showDate, ShowTime);
        this.movieTitle = movieTitle;
        this.numTicketLeft = numTicketLeft;
        this.showTimeID = showTimeID;


    }


    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        PRIMARY KEY(%s, %s),
                        FOREIGN KEY(%s) REFERENCES %s(%s),
                        FOREIGN KEY(%s) REFERENCES %s(%s)                       
                        );
                        """,
                MovieShowTimesTable.TABLE_NAME,
                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.COLUMN_SHOW_TIME_ID,
                MovieShowTimesTable.COLUMN_NUM_TICKETS_LEFT,

                // primary keys
                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.COLUMN_SHOW_TIME_ID,

                // first key
                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieTable.TABLE_NAME,
                MovieTable.COLUMN_ID,

                // second key
                ShowTimesTable.COLUMN_ID,
                ShowTimesTable.TABLE_NAME,
                ShowTimesTable.COLUMN_ID

        );
    }



    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?);
                        """,
                MovieShowTimesTable.TABLE_NAME
        );

    }

//    public String getMovieShowTimes(){
//        return """
//                SELECT mst.movie_id,
//                 m.title,
//                st.*,
//                 mst.num_tickets_left
//                FROM MovieShowTimes mst
//                JOIN Movies m ON m.movie_id = mst.movie_id
//                JOIN ShowTimes st ON st.show_time_id = mst.show_time_id
//                WHERE mst.movie_id =?
//                """;
//    }

    public  String getMovieShowTimes(MovieShowTimes movieShowTimes) {

        String sql = """
                 SELECT mst.movie_id,
                                  m.title,
                                 st.*,
                                  mst.num_tickets_left
                                 FROM MovieShowTimes mst
                                 JOIN Movies m ON m.movie_id = mst.movie_id
                                 JOIN ShowTimes st ON st.show_time_id = mst.show_time_id
                                 WHERE m.movie_id = ?
                                                  """;

        if (!movieShowTimes.getShowDate().isEmpty()) {
            sql += " AND st.show_date LIKE ?";
        }

        return sql;

    }


    public  String getAllMovieShowTimes() {

        return  """
                    SELECT 
                     DISTINCT(mst.movie_id),
                     m.title
                  FROM MovieShowTimes mst
               JOIN Movies m ON m.movie_id = mst.movie_id
                """;



    }


    @Override
    public List<String> tableColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("Date");
        columns.add("Time");
        columns.add("No of Tickets left");
        return columns;
    }
    public List<String> tableColumnsWithID() {
        List<String> columns = new ArrayList<>();
        columns.add("Date");
        columns.add("Time");
        columns.add("No of Tickets left");
        columns.add("ID");

        return columns;
    }


    public String getMovieTitle() {
        return movieTitle;
    }
}

package classes;

import interfaces.Queries;
import interfaces.TableProperties;
import tables.MovieTable;
import tables.ShowTimesTable;

import java.util.ArrayList;
import java.util.List;

public class ShowTimes implements Queries, TableProperties {
    private int showTimeID;
    private int movieID;
    private String date;
    private String time;
    private int numTicketsLeft;
    private String movieTitle;

    private int numTicketsSold;

    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }

    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumTicketsLeft() {
        return numTicketsLeft;
    }

    public void setNumTicketsLeft(int numTicketsLeft) {
        this.numTicketsLeft = numTicketsLeft;
    }

    @Override
    public List<String> tableColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("Date");
        columns.add("Time");
        columns.add("No of tickets left");
        return columns;
    }
    public List<String> tableColumnsWithID() {
        List<String> columns = new ArrayList<>();
        columns.add("Show ID");
//        columns.add("Movie ID");
        columns.add("Date");
        columns.add("Time");
        columns.add("No of Tickets left");
        return columns;
    }


    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT,
                        %s INTEGER NOT NULL,
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL,
                        %s INTEGER NOT NULL,
                        FOREIGN KEY(%s) REFERENCES %s(%s)
                        );
                        """,
                ShowTimesTable.TABLE_NAME,
                ShowTimesTable.COLUMN_ID,
                ShowTimesTable.COLUMN_MOVIE_ID,
                ShowTimesTable.COLUMN_SHOW_DATE,
                ShowTimesTable.COLUMN_SHOW_TIME,
                ShowTimesTable.COLUMN_NUM_TICKETS_LEFT,

                // movie fk
                // first key
                ShowTimesTable.COLUMN_MOVIE_ID,
                MovieTable.TABLE_NAME,
                MovieTable.COLUMN_ID


        );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?, ?, ?);
                        """,
                ShowTimesTable.TABLE_NAME
        );
    }





    public String getAllMovieShowTimes() {
        return  """
                    SELECT DISTINCT
                        (s.movie_id),
                        m.title
                    FROM
                        ShowTimes s
                    JOIN Movies m ON
                        m.movie_id = s.movie_id
                    WHERE show_date >= DATE('NOW') AND DATE('now', 'start of month', '+1 month' , '-1 day')        
                """;
    }

    public  String getSelectedMovieShowTimes(ShowTimes movieShowTimes) {
        String sql = """
                SELECT
                    m.title,
                    s.*
                FROM
                    ShowTimes s
                JOIN Movies m ON
                    m.movie_id = s.movie_id
                WHERE
                    m.movie_id = ? AND num_tickets_left > 0    
                    AND show_date >= DATE('NOW') AND DATE('now', 'start of month', '+1 month' , '-1 day')                                                     
                """;

        if (!movieShowTimes.getDate().isEmpty()) {
            sql += " AND show_date LIKE ?";
        }

        return sql;

    }

    public String getSelectedShowDetails() {
        return  "SELECT "+ ShowTimesTable.COLUMN_NUM_TICKETS_LEFT + " FROM " + ShowTimesTable.TABLE_NAME + " WHERE  " + ShowTimesTable.COLUMN_ID + "=?";

    }



    public String updateNumTickets(){
        return String.format( "UPDATE %s SET %s = ? WHERE %s = ?",
                ShowTimesTable.TABLE_NAME,
                ShowTimesTable.COLUMN_NUM_TICKETS_LEFT,
                ShowTimesTable.COLUMN_MOVIE_ID
        );

    }


    public ShowTimes(String date, String time, String movieTitle, int numTicketLeft, int showTimeID, int movieId){
        this.date = date;
        this.time = time;
        this.movieTitle = movieTitle;
        this.numTicketsLeft = numTicketLeft;
        this.showTimeID = showTimeID;
        this.movieID = movieId;



    }
    public ShowTimes(){

    }

}

package classes.models;

import classes.utils.Helper;
import interfaces.Queries;
import org.apache.commons.lang3.StringUtils;
import tables.MovieTable;
import tables.ShowTimesTable;

import java.util.ArrayList;
import java.util.List;

import static classes.utils.Helper.fieldSep;

public class ShowTimes implements Queries {
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

    //    @Override
    public List<String> tableColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("Date");
        columns.add("Time");
        columns.add("No of tickets left");
        return columns;
    }


    @Override
    public String createTable() {
        return STR. """
                        CREATE TABLE IF NOT EXISTS \{ ShowTimesTable.TABLE_NAME } (
                            \{ ShowTimesTable.COLUMN_ID } INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{ ShowTimesTable.COLUMN_MOVIE_ID } INTEGER NOT NULL,
                            \{ ShowTimesTable.COLUMN_SHOW_DATE } TEXT NOT NULL,
                            \{ ShowTimesTable.COLUMN_SHOW_TIME } TEXT NOT NULL,
                            \{ ShowTimesTable.COLUMN_NUM_TICKETS_LEFT } INTEGER NOT NULL,
                            FOREIGN KEY(\{ ShowTimesTable.COLUMN_MOVIE_ID }) REFERENCES \{ MovieTable.TABLE_NAME }(\{ MovieTable.COLUMN_ID })
                        );
                        """ ;


    }

    @Override
    public String insert() {
        return STR. """
                        INSERT INTO \{ ShowTimesTable.TABLE_NAME }
                        VALUES (?, ?, ?, ?, ?);
                        """ ;


    }


    public static String getAllMovieShowTimes() {
        return """
                SELECT DISTINCT
                    s.movie_id,
                    m.title
                FROM
                    ShowTimes s
                JOIN Movies m ON
                    m.movie_id = s.movie_id
                WHERE
                    show_date >= DATE('NOW')
                    AND DATE('now','start of month',  '+1 month',  '-1 day')
                    AND num_tickets_left > 0
                ORDER BY
                    m.title
                """;
    }


    public static String getSelectedMovieShowTimes(ShowTimes movieShowTimes) {

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
                     AND show_date >= DATE('NOW')
                     AND DATE('now', 'start of month', '+1 month' , '-1 day')
                     """;

        if (!StringUtils.isEmpty(movieShowTimes.getDate())) {
            sql += " AND show_date LIKE ?";
        }
        sql += """
                GROUP BY show_time_id
                HAVING show_time >= TIME('NOW')
                OR show_date > DATE('NOW')
                ORDER BY show_date, show_time
                """;
        return sql;

    }

    public static String getSelectedShowDetails() {
        return STR. "SELECT \{ ShowTimesTable.COLUMN_NUM_TICKETS_LEFT } FROM \{ ShowTimesTable.TABLE_NAME } WHERE \{ ShowTimesTable.COLUMN_ID } =?" ;

    }


    public static String updateNumTickets() {
        return STR. "UPDATE \{ ShowTimesTable.TABLE_NAME } SET \{ ShowTimesTable.COLUMN_NUM_TICKETS_LEFT } = ? WHERE \{ ShowTimesTable.COLUMN_ID } = ?" ;
    }


    public ShowTimes(String date, String time, String movieTitle, int numTicketLeft, int showTimeID, int movieId) {
        this.date = date;
        this.time = time;
        this.movieTitle = movieTitle;
        this.numTicketsLeft = numTicketLeft;
        this.showTimeID = showTimeID;
        this.movieID = movieId;


    }

    public ShowTimes() {

    }

    public static String toShowTimeList(ShowTimes showTime) {

        return STR. """
                \{ fieldSep(Helper.formatDate(showTime.getDate())) }
                \{ fieldSep(Helper.formatTime(showTime.getTime())) }
                 \{ showTime.getNumTicketsLeft() }
                """ ;
    }
}

package classes.models;

import classes.utils.CalendarUtils;
import interfaces.Queries;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tables.MovieTable;
import tables.ShowTimesTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static classes.utils.Helper.fieldSep;

@Data

public class ShowTimes implements Queries {
    private int showTimeID;

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumTicketsLeft(int numTicketsLeft) {
        this.numTicketsLeft = numTicketsLeft;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    private int movieID;
    private String date;
    private String time;
    private int numTicketsLeft;
    private String movieTitle;

    private int numTicketsSold;


    public List<String> tableColumns() {
        return Arrays.asList("Date", "Time", "No of tickets left");
    }


    @Override
    public String createTable() {
        return STR."""
                        CREATE TABLE IF NOT EXISTS \{ShowTimesTable.TABLE_NAME} (
                            \{ShowTimesTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{ShowTimesTable.COLUMN_MOVIE_ID} INTEGER NOT NULL,
                            \{ShowTimesTable.COLUMN_SHOW_DATE} TEXT NOT NULL,
                            \{ShowTimesTable.COLUMN_SHOW_TIME} TEXT NOT NULL,
                            \{ShowTimesTable.COLUMN_NUM_TICKETS_LEFT} INTEGER NOT NULL,
                            FOREIGN KEY(\{ShowTimesTable.COLUMN_MOVIE_ID}) REFERENCES \{MovieTable.TABLE_NAME}(\{MovieTable.COLUMN_ID})
                        );
                        """;


    }

    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{ShowTimesTable.TABLE_NAME}
                        VALUES (?, ?, ?, ?, ?);
                        """;


    }


    public static String getSelectedMovieShowTimes(ShowTimes movieShowTimes) {

        String sql = """
                SELECT
                                    m.title,
                                    m.movie_id,
                                    s.*
                                FROM
                                    ShowTimes s
                                NATURAL JOIN Movies m
                                WHERE
                                    m.movie_id = ? AND num_tickets_left > 0
                                   			AND	show_date BETWEEN Date('NOW') AND Date('now', 'start of month', '+1 month', '-1 day')
                """;

        if (!StringUtils.isEmpty(movieShowTimes.getDate())) {
            sql += " AND show_date LIKE ?";
        }
        sql += " ORDER BY show_date, show_time";
//        sql += """
//                GROUP BY show_time_id
//                HAVING show_time >= TIME('NOW')
//                OR show_date > DATE('NOW')
//                ORDER BY show_date, show_time
//                """;
        return sql;

    }


    public static String updateNumTickets() {
        return STR."UPDATE \{ShowTimesTable.TABLE_NAME} SET \{ShowTimesTable.COLUMN_NUM_TICKETS_LEFT} = ? WHERE \{ShowTimesTable.COLUMN_ID} = ?";
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

        return STR."""
                \{fieldSep(CalendarUtils.formatDate.apply(showTime.getDate()))}
                \{fieldSep(CalendarUtils.formatTime.apply(showTime.getTime()))}
                 \{showTime.getNumTicketsLeft()}
                """;
    }

    public int getShowTimeID() {
        return showTimeID;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getNumTicketsLeft() {
        return numTicketsLeft;
    }

    public int getNumTicketsSold() {
        return numTicketsSold;
    }
}

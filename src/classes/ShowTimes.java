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
                ShowTimesTable.NUM_TICKETS_LEFT,

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

}

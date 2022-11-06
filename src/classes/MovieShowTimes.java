package classes;

import interfaces.Queries;
import tables.*;

public class MovieShowTimes implements Queries {
    private int movieId;
    private int showTimeID;
    private int numTicketLeft;

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
                        VALUES (?, ?);
                        """,
                GenreTable.TABLE_NAME
        );

    }


}

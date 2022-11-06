package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieShowTimesTable;
import tables.MovieTable;

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
                        PRIMARY KEY(%s, %s)
                        
                        );
                        """,
                MovieShowTimesTable.TABLE_NAME,
                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.COLUMN_SHOW_TIME_ID,
                MovieShowTimesTable.COLUMN_NUM_TICKETS_LEFT,


                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.COLUMN_SHOW_TIME_ID

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

package classes.models;

import interfaces.Queries;
import tables.MovieTable;
import tables.RatingTable;

public class Movie implements Queries {
    protected int movieID;
    protected String title;
    protected int duration;
    protected int ratingID;


    public Movie() {

    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public Movie(String title, int duration, int ratingID) {
        this.title = title;
        this.duration = duration;
        this.ratingID = ratingID;
    }

    @Override
    public String toString() {
        return STR."""
                ID: \{movieID}
                Movie: \{title}
                duration: \{duration}
                ratingID: \{ratingID}

                """;
    }

    public Movie(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public Movie(int movieID) {
        this.movieID = movieID;
    }


    public Movie(int movieID, String title) {
        this.movieID = movieID;
        this.title = title;
    }

    public Movie(int movieID, String title, int duration) {
        this.movieID = movieID;
        this.title = title;
        this.duration = duration;
    }

    @Override
    public String createTable() {
        return STR. """
                        CREATE TABLE IF NOT EXISTS \{ MovieTable.TABLE_NAME } (
                            \{ MovieTable.COLUMN_ID } INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{ MovieTable.COLUMN_TITLE } TEXT NOT NULL UNIQUE,
                            \{ MovieTable.COLUMN_DURATION } TEXT NOT NULL,
                            \{ MovieTable.COLUMN_RATING_ID } INTEGER NOT NULL,
                            FOREIGN KEY(\{ MovieTable.COLUMN_RATING_ID }) REFERENCES \{ RatingTable.TABLE_NAME }(\{ RatingTable.COLUMN_ID }) ON UPDATE CASCADE ON DELETE CASCADE
                        );
                        """ ;
    }

    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{ MovieTable.TABLE_NAME}
                        VALUES (?, ?, ?, ?);
                        """;


    }
}

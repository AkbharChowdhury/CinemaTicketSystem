package classes;

import interfaces.Queries;
import tables.MovieTable;
import tables.RatingTable;

public class Movie implements Queries {
    protected int movieID;
    protected String title;
    protected int duration;
    protected int ratingID;


    public Movie(){

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

    public Movie(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }
    public Movie( int movieID) {
        this.movieID = movieID;
    }

    public Movie(int movieID, String title, int duration) {
        this.movieID = movieID;
        this.title = title;
        this.duration = duration;
    }


    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT,
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL, 
                        %s INTEGER NOT NULL,
                        FOREIGN KEY(%s) REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE
                        );
                        """,
                MovieTable.TABLE_NAME,
                MovieTable.COLUMN_ID,
                MovieTable.COLUMN_TITLE,
                MovieTable.COLUMN_DURATION,
                MovieTable.COLUMN_RATING_ID,

                // rating fk
                MovieTable.COLUMN_RATING_ID,
                RatingTable.TABLE_NAME,
                RatingTable.COLUMN_ID

                );
    }

    @Override
    public String insert() {
        return String.format("""
                            INSERT INTO %s
                            VALUES (?, ?, ?, ?);
                            """,
                MovieTable.TABLE_NAME
        );
    }
}

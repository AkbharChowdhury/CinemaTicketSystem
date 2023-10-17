package classes.models;

import interfaces.Queries;
import tables.*;

public class Rating implements Queries {
    private int ratingID;
    private String rating;

    public Rating() {

    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Rating(int ratingID, String rating) {
        this.ratingID = ratingID;
        this.rating = rating;
    }

    @Override
    public String createTable() {
        return STR. """
                 CREATE TABLE IF NOT EXISTS \{ RatingTable.TABLE_NAME } (
                        \{ RatingTable.COLUMN_ID } INTEGER PRIMARY KEY AUTOINCREMENT,
                        \{ RatingTable.COLUMN_RATING } TEXT NOT NULL UNIQUE);
                """ ;

    }

    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{RatingTable.TABLE_NAME}
                        VALUES (?, ?);
                        """;


    }
}

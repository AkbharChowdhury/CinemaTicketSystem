package classes;

import interfaces.Queries;
import tables.*;

public class Rating implements Queries {
    private int ratingID;
    private String rating;
    public Rating(){

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
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT, 
                        %s TEXT NOT NULL UNIQUE);
                        """,
                RatingTable.TABLE_NAME,
                RatingTable.COLUMN_ID,
                RatingTable.COLUMN_RATING


        );
    }





    @Override
    public String insert() {
        return String.format("""
                            INSERT INTO %s
                            VALUES (?, ?);
                            """,
                RatingTable.TABLE_NAME
        );
    }
}

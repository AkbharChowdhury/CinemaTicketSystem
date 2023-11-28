package classes.models;

import interfaces.Queries;
import tables.*;

public record Rating(int ratingID, String rating) implements Queries {


    public Rating() {
        this(0, "");

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
        return STR. """
                        INSERT INTO \{ RatingTable.TABLE_NAME }
                        VALUES (?, ?);
                        """ ;


    }
}

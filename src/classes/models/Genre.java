package classes.models;

import interfaces.Queries;
import tables.GenreTable;

public record Genre(int genreID, String genre) implements Queries {


    public Genre() {
        this(0, "");

    }


    @Override
    public String createTable() {
        return STR. """
                        CREATE TABLE IF NOT EXISTS \{ GenreTable.TABLE_NAME } (
                            \{ GenreTable.COLUMN_ID } INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{ GenreTable.COLUMN_GENRE } TEXT NOT NULL UNIQUE
                        );
                        """ ;

    }

    @Override
    public String insert() {
        return STR. """
                        INSERT INTO \{ GenreTable.TABLE_NAME }
                        VALUES (?, ?);
                        """ ;
    }
}

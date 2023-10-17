package classes.models;

import interfaces.Queries;
import tables.GenreTable;

public class Genre implements Queries {
    private int genreID;
    private String genre;

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Genre() {

    }

    public Genre(int genreID, String genre) {
        this.genreID = genreID;
        this.genre = genre;
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

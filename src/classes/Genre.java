package classes;

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
    public Genre(){

    }

    public Genre(int genreID, String genre) {
        this.genreID = genreID;
        this.genre = genre;
    }


    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT, 
                        %s TEXT NOT NULL UNIQUE);
                        """,
                GenreTable.TABLE_NAME,
                GenreTable.COLUMN_ID,
                GenreTable.COLUMN_GENRE

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

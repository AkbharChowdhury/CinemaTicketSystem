package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieTable;
import tables.RatingTable;

public class MovieGenres implements Queries {
    private int movieID;
    private int genreID;

    public MovieGenres() {

    }

    public MovieGenres(int movieID, int genreID) {
        this.movieID = movieID;
        this.genreID = genreID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        PRIMARY KEY(%s, %s),                   
                        FOREIGN KEY(%s) REFERENCES %s(%s),
                        FOREIGN KEY(%s) REFERENCES %s(%s)
                        
                        );
                        """,
                MovieGenresTable.TABLE_NAME,

                MovieGenresTable.COLUMN_MOVIE_ID,
                MovieGenresTable.COLUMN_GENRE_ID,
                // primary keys
                MovieGenresTable.COLUMN_MOVIE_ID,
                MovieGenresTable.COLUMN_GENRE_ID,

                // compound keys
                // first key
                MovieGenresTable.COLUMN_MOVIE_ID,
                MovieTable.TABLE_NAME,
                MovieTable.COLUMN_ID,

                // second key
                MovieGenresTable.COLUMN_GENRE_ID,
                GenreTable.TABLE_NAME,
                GenreTable.COLUMN_ID






                );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?);
                        """,
                MovieGenresTable.TABLE_NAME
        );

    }
}

package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieTable;
import tables.RatingTable;

public class MovieGenres extends Movie implements Queries {
    private int movieID;
    private int genreID;

    public MovieGenres() {

    }

    public MovieGenres(int movieID, int genreID) {
        this.movieID = movieID;
        this.genreID = genreID;
    }

    public String getGenres() {
        return genres;
    }

    //    private String title,duration,genres;
    private String genres;

    public MovieGenres(String title, int duration, String genres) {
        super(title, duration);
        this.title = title;
        this.duration = duration;
        this.genres = genres;

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
                GenreTable.COLUMN_ID);
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



    public String showMovieList(boolean search) {

        // show full movie list without filter
        if (!search) {
            return """
                              
                    SELECT
                        m.title,
                        m.duration,
                        r.rating,
                        GROUP_CONCAT(g.genre,'/') genre_list,
                        GROUP_CONCAT(g.genre_id) genre_id_list,
                        mg.movie_id
                    FROM MovieGenres mg
                    JOIN Movies m ON mg.movie_id = m.movie_id
                    JOIN Genres g ON mg.genre_id = g.genre_id
                    JOIN Ratings r ON m.rating_id = r.rating_id
                    GROUP BY m.movie_id
                                  
                    """;
        }
        return """           
                SELECT
                                        m.title,
                                        m.duration,
                                        r.rating,
                                        GROUP_CONCAT(g.genre,'/') genre_list,
                                        GROUP_CONCAT(g.genre_id) genre_id_list,
                                        mg.movie_id
                                    FROM MovieGenres mg
                                    JOIN Movies m ON mg.movie_id = m.movie_id
                                    JOIN Genres g ON mg.genre_id = g.genre_id
                                    JOIN Ratings r ON m.rating_id = r.rating_id
                                    GROUP BY m.movie_id
                					
                					HAVING genre_id_list LIKE ? AND m.title LIKE ?
                    """;
    }

    public String getMovieGenreList(){
        return """
                SELECT DISTINCT( g.genre)
                FROM MovieGenres mg
                JOIN Genres g ON mg.genre_id = g.genre_id                              
                """;
    }

}

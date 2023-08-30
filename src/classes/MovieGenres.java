package classes;

import enums.FormDetails;
import interfaces.Queries;
import interfaces.TableProperties;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieTable;
import tables.RatingTable;

import java.util.ArrayList;
import java.util.List;

public class MovieGenres extends Movie implements Queries, TableProperties {
    private int genreID;
    private String rating;
    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public MovieGenres() {

    }


    public String getRating() {
        return rating;
    }

    public MovieGenres(int movieID, int genreID) {
        super(movieID);
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

    public MovieGenres(int movieID, String title, int duration, String genres, String rating) {
        super(title, duration);
        this.movieID = movieID;
        this.title = title;
        this.duration = duration;
        this.genres = genres;
        this.rating = rating;


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



    public String showMovieList(MovieGenres movieGenres) {
        String genre = movieGenres.getGenre();

        String sql = """
                SELECT m.title,
                       m.duration,
                       r.rating,
                       Group_concat(g.genre, '/') genre_list,
                       Group_concat(g.genre_id)   genre_id_list,
                       mg.movie_id,
                       mg.genre_id
                FROM   MovieGenres mg
                       JOIN Movies m
                         ON mg.movie_id = m.movie_id
                       JOIN genres g
                         ON mg.genre_id = g.genre_id
                       JOIN Ratings r
                         ON m.rating_id = r.rating_id
                         WHERE title LIKE ?
                          
                GROUP BY m.movie_id
                                             
                """;


        if (!genre.equalsIgnoreCase(FormDetails.defaultGenre())){
            sql+= " HAVING genre_list LIKE ?";
        }

        return sql;

    }

    public String getMovieGenreList(){
        return """
                SELECT DISTINCT(g.genre), g.genre_id
                FROM MovieGenres mg
                JOIN Genres g ON mg.genre_id = g.genre_id
                ORDER BY g.genre                              
                """;
    }

    @Override
    public List<String> tableColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("Movie");
        columns.add("Duration");
        columns.add("Rating");
        columns.add("Genre");
        return columns;
    }


}

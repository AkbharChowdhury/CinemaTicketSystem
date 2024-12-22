package classes.models;

import classes.utils.Helper;
import enums.FormDetails;
import interfaces.Queries;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static classes.utils.Helper.fieldSep;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieGenres extends Movie implements Queries {
    private int genreID;
    private String rating;
    private String genre = FormDetails.defaultGenre();
    private String genres;

    public MovieGenres() {
    }

    public static final LinkedHashMap<Integer, Integer> TABLE_WIDTHS = new LinkedHashMap<>() {{
        put(0, 135);
        put(1, 30);
        put(2, 20);
        put(3, 140);

    }};

    public static String toMovieList(MovieGenres m) {

        return STR."""
        \{fieldSep(m.getTitle())}
        \{fieldSep(Helper.calcDuration(m.getDuration()))}
        \{fieldSep(m.getRating())}
        \{m.getGenres()}
        """;
    }


    public MovieGenres(int movieID, int genreID) {
        super(movieID);
        this.genreID = genreID;
    }

    public MovieGenres(int movieID, String title, int duration, String genres, String rating) {
        super(title, duration);
        this.movieID = movieID;
        this.genres = genres;
        this.rating = rating;


    }

    @Override
    public String createTable() {
        return STR."""
                        CREATE TABLE IF NOT EXISTS \{MovieGenresTable.TABLE_NAME} (
                            \{MovieGenresTable.COLUMN_MOVIE_ID} INTEGER NOT NULL,
                            \{MovieGenresTable.COLUMN_GENRE_ID} INTEGER NOT NULL,
                            PRIMARY KEY(\{MovieGenresTable.COLUMN_MOVIE_ID}, \{MovieGenresTable.COLUMN_GENRE_ID}),
                            FOREIGN KEY(\{MovieGenresTable.COLUMN_MOVIE_ID}) REFERENCES \{MovieTable.TABLE_NAME}(\{MovieTable.COLUMN_ID}),
                            FOREIGN KEY(\{MovieGenresTable.COLUMN_GENRE_ID}) REFERENCES \{GenreTable.TABLE_NAME}(\{GenreTable.COLUMN_ID})
                        );
                        """;

    }


    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{MovieGenresTable.TABLE_NAME}
                        VALUES (?, ?);
                        """;
    }


    public static String showMovieList(MovieGenres movieGenres) {
        String genre = movieGenres.getGenre();

        String sql = """
                SELECT m.title,
                       m.duration,
                       r.rating,
                       Group_concat(genre, '/') genre_list,
                       mg.movie_id,
                       mg.genre_id
                FROM   MovieGenres mg
                       NATURAL JOIN Movies m
                       NATURAL JOIN genres g
                       NATURAL JOIN Ratings r
                 WHERE title LIKE ?                        
                GROUP BY m.movie_id
                                             
                """;


        if (!genre.equalsIgnoreCase(FormDetails.defaultGenre())) {
            sql += " HAVING genre_list LIKE ?";
        }

        return sql;

    }

    public static String getMovieGenreList() {
        return """
                SELECT DISTINCT(genre)
                FROM MovieGenres 
                NATURAL JOIN Genres
                ORDER BY genre                         
                """;
    }

    public static List<String> tableColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("Movie");
        columns.add("Duration");
        columns.add("Rating");
        columns.add("Genre");
        return columns;
    }


}

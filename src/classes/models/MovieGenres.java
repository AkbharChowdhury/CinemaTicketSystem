package classes.models;

import classes.utils.Helper;
import enums.FormDetails;
import interfaces.Queries;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tables.GenreTable;
import tables.MovieGenresTable;
import tables.MovieTable;

import java.util.*;
import java.util.function.Supplier;

import static classes.utils.Helper.fieldSep;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieGenres extends Movie implements Queries {
    private int genreID;
    private String rating;
    private String genre = FormDetails.defaultGenre.get();
    private String genres;

    public MovieGenres() {
    }


    public static Supplier<LinkedHashMap<Integer, Integer>> TABLE_WIDTHS = () -> {
        int[] widths = {145, 30, 20, 140};
        Counter c = new Counter(true);
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        Arrays.stream(widths).forEach(width -> map.put(c.getCounter(), width));
        return map;
    };

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


        if (!genre.equalsIgnoreCase(FormDetails.defaultGenre.get())) {
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


    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}

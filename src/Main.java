import classes.Database;
import classes.models.MovieGenres;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        MovieGenres movieGenre = new MovieGenres();

        List<MovieGenres> movieList = Database.getInstance().showMovieList(movieGenre);
        movieList.stream()
                .filter(m ->m.getTitle().startsWith(String.valueOf("A".equalsIgnoreCase(m.getTitle()))))
                .forEach(m ->System.out.println(m.getTitle())
                );


    }

}


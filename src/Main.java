import classes.*;
import enums.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Database db;
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        db = Database.getInstance();
        assert db != null;
        MovieGenres m = new MovieGenres();
        m.setTitle("cu");
        m.setGenreID(13);



        for(var movie: db.showMovieList(m)){
                            System.out.println(MessageFormat.format("{0},{1} {2}, {3}",
                                    movie.getMovieID(),
                                    movie.getTitle(),
                                    Helper.calcDuration(movie.getDuration()),
                                    movie.getGenres()
                ));


        }

//        db.showMovieList(false,2);
//        db.showMovieList(true,11);
//        db.showMovieGenreList();


//        db.showMovieList(true,2);

//        new File("cinema.db").delete();

    }
}

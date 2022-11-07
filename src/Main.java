import classes.Database;
import classes.FileHandler;
import classes.Helper;
import classes.Ticket;
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
        for(var movie: db.showMovieList(true,5, "mi")){
                            System.out.println(MessageFormat.format("{0}, {1}, {2}",
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

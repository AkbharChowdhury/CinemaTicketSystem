import classes.*;
import enums.Files;

import java.io.*;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Database db;

    public static void main(String[] args) throws SQLException, IOException {
        db = Database.getInstance();
        System.out.println(db.getMovieName(20));
        System.exit(0);
        MovieShowTimes ms = new MovieShowTimes();
        ms.setMovieId(20);
//        ms.setShowDate("2022-12-06");


//        ms.setShowDate("2022-12-06");
        ms.setShowDate("2022-12-06");

        for (var s: db.showMovieTimes(ms)){
            var f =MessageFormat.format("{0}, {1}, {2} {3}", s.getMovieTitle(), s.getShowDate(), s.getShowTime(), s.getNumTicketLeft());

            System.out.println(f);


        }
    }
}

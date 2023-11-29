import classes.Database;
import classes.models.Customer;
import classes.models.Movie;
import classes.models.SearchMovie;
import classes.models.Ticket;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        var m = new SearchMovie(Database.getInstance().showMovieList12());
        m.setTitle("a");
        m.setGenre("Adventure");
        var x = m.filterResults();
        x.forEach(i -> System.out.println(i.getTitle() + " " + i.getGenres()));
//        var drinks = new ArrayList<>(Arrays.asList("mango juice deluxe", "hot chocolate"));
//        var foods = new ArrayList<>(Arrays.asList("croissants", "cookies and cream desserts"));
//        var groceries = new ArrayList<>(Arrays.asList(foods, drinks));
//        groceries.forEach(list-> list.forEach(System.out::println));


    }

}


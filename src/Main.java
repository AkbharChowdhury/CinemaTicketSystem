import classes.Database;
import classes.MovieShowTimes;
import classes.Validation;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Database db = Database.getInstance();
        MovieShowTimes movieShowTimes = new MovieShowTimes();

        movieShowTimes.setMovieId(20);
        movieShowTimes.setShowTimeId(9);
        int numTicketsLeft = db.getNumTickets(movieShowTimes);

        System.out.println("Number of tickets left = " + numTicketsLeft);
        movieShowTimes.setNumTicketsSold(10);
//        db.updateNumTickets(movieShowTimes);
//        System.out.println("The num of tickets left is " +  db.getNumTickets(movieShowTimes));
//        int remainingTickets = numTicketsLeft - movieShowTimes.getNumTicketsSold();
//        if (remainingTickets <0){
//            System.out.println("Error: you cannot exceed above " + numTicketsLeft + " tickets");
//            return;
//        }
        if(!Validation.isValidNumTicketsSold(db, movieShowTimes)){
            System.out.println("Error: you cannot exceed above " + numTicketsLeft + " tickets");
            return;


        }
        db.updateNumTickets(movieShowTimes);
        System.out.println("The new total tickets left is " + db.getNumTickets(movieShowTimes));

//        if (remainingTickets)


//        movieShowTimes.setNumTicketsSold(2);






    }
}

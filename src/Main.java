import classes.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Database db = Database.getInstance();

        List<ShowTimes> showTimeList = FileHandler.getShowTimeData();
        for(var i: showTimeList){
            System.out.println(i.getMovieID());
        }

//        Database db = Database.getInstance();
//        MovieShowTimes movieShowTimes = new MovieShowTimes();
//        movieShowTimes.setMovieId(20);
//        movieShowTimes.setShowTimeId(9);
//
//
//        int numTicketsLeft = db.getNumTickets(movieShowTimes);
//
//        System.out.println("Number of tickets left = " + numTicketsLeft);
//        movieShowTimes.setNumTicketsSold(48);
//
//        if(!Validation.isValidNumTicketsSold(db, movieShowTimes)){
//            if (numTicketsLeft == 0){
//                System.out.println("There are no tickets to purchase");
//                return;
//            }
//
//            System.out.println("Error: you cannot exceed above " +  numTicketsLeft + " tickets");
//            return;
//
//
//        }
//        db.updateNumTickets(movieShowTimes);
//        System.out.println("The new total tickets left is " + db.getNumTickets(movieShowTimes));

//        if (remainingTickets)


//        movieShowTimes.setNumTicketsSold(2);






    }
}

import classes.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException, ParseException {
        Database db = Database.getInstance();
        System.out.println(db.getFirstname(2));
        String dateStr = "13 Nov 1999";
//
//
//        System.out.println(Helper.convertMediumDateToYYMMDD(dateStr));

        System.exit(0);


//        String s;
//        var formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = formatter.parse("2013-07-17");
//        formatter = new SimpleDateFormat("yyyy-MM-dd");
//        s = formatter.format(date1);
//        System.out.println(s);

//        https://www.baeldung.com/java-string-to-date
//
//
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");


//        System.out.println(LocalDate.parse(text, formatter).format(formatter2));

//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy");
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////
//
//        System.out.println(LocalDate.parse("13-Nov-1999", formatter).format(formatter2));


//        LocalDate date = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
//        String text = date.format(formatter);
//        LocalDate parsedDate = LocalDate.parse(text, formatter);
//        System.out.println(parsedDate);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        System.out.println(LocalDate.parse(startDateString, formatter).format(formatter2));
//
//        List<ShowTimes> showTimeList = FileHandler.getShowTimeData();
//        for(var i: showTimeList){
//            System.out.println(i.getMovieID());
//        }

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

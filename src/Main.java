import classes.*;
import enums.Files;

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static Database db;
    public static String timeCoversion12to24(String twelveHoursTime) throws ParseException {

        //Date/time pattern of input date (12 Hours format - hh used for 12 hours)
        DateFormat df = new SimpleDateFormat("hh:mm:ssaa");

        //Date/time pattern of desired output date (24 Hours format HH - Used for 24 hours)
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String output = null;

        //Returns Date object
        date = df.parse(twelveHoursTime);

        //old date format to new date format
        output = outputformat.format(date);
        System.out.println(output);

        return output;
    }

    public static String formatTime(String time) throws ParseException {
        DateFormat f1 = new SimpleDateFormat("hh:mm"); //12:30
        Date unFormattedTime = f1.parse(time);

        DateFormat formattedTime = new SimpleDateFormat("h:mm a"); // e.g. 12:30 AM
        return formattedTime.format(unFormattedTime); // "23:00"
    }

    public static void main(String[] args) throws SQLException, IOException, ParseException {
        db = Database.getInstance();
//        System.out.println(db.getMovieName(20));
//        String pattern = "hh:mm a";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        Date date = simpleDateFormat.parse("22:00");
//
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat simpleformat = new SimpleDateFormat("hh:mm");
//        System.out.println("Today's date = "+simpleformat.format("20:00"));
//        System.out.println(date);

//
//        String result =                                       // Text representing the value of our date-time object.
//                LocalTime.parse(                                  // Class representing a time-of-day value without a date and without a time zone.
//                                "12:30" ,                                  // Your `String` input text.
//                                DateTimeFormatter.ofPattern(                  // Define a formatting pattern to match your input text.
//                                        "hh:mm" ,
//                                        Locale.US                                 // `Locale` determines the human language and cultural norms used in localization. Needed here to translate the `AM` & `PM` value.
//                                )                                             // Returns a `DateTimeFormatter` object.
//                        )                                                 // Return a `LocalTime` object.
//                        .format( DateTimeFormatter.ofPattern("HH:mm a") )   // Generate text in a specific format. Returns a `String` object.
//                ;
//        System.out.println(result);
//        timeCoversion12to24("12:21");
        System.out.println(formatTime("14:30"));

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

import javax.swing.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;

public class Main {
    public static void main(String[] args) {
//        https://howtodoinjava.com/java/date-time/java8-datetimeformatter-example/
        var date = LocalDate.now();
//        String text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

//        System.out.println(text);
        var d = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(String.valueOf(date)));
        System.out.println(d);
        System.out.println(convertDate("17 Oct 2023"));

    }
    private static String convertDate(String strDate)
    {
    //https://howtodoinjava.com/java/date-time/java8-datetimeformatter-example/
        return LocalDate.parse(strDate, new DateTimeFormatterBuilder().appendPattern("dd MMM yyyy").toFormatter()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}

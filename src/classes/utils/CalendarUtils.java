package classes.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CalendarUtils {
    private CalendarUtils() {}

    public static Function<String, String> formatDate = date -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(date));
    public static BiFunction<String, FormatStyle, String> formatDateStyle = (date, formatStyle) -> DateTimeFormatter.ofLocalizedDate(formatStyle).format(LocalDate.parse(date));
    public static Function<String, String> convertMediumDateToYYMMDD = date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy")).toString();
    public static Function<String, String> formatTime = time -> DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.parse(time));


}

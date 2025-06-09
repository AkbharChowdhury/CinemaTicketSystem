package enums;


import org.apache.commons.lang3.text.WordUtils;

import java.util.function.Function;
import java.util.function.Supplier;

public enum FormDetails {
    ;
    private static final String APP_NAME = "Cinema Ticket Machine - ";

    private static Function<String, String> windowTitle = title-> STR."\{APP_NAME} \{title}";



    public static final Supplier<String> movieList = () ->  windowTitle.apply(WordUtils.capitalizeFully("list movies"));

    public static final Supplier<String> register = () -> WordUtils.capitalizeFully("customer registration");


    public static final Supplier<String> login = () -> WordUtils.capitalize("login");


    public static final Supplier<String> showTimes = () ->  windowTitle.apply(WordUtils.capitalizeFully("show times"));







    public static final Supplier<String> purchaseTicket = () ->  windowTitle.apply(WordUtils.capitalizeFully("purchase ticket"));
    public static final Supplier<String> showReceipt = () ->  windowTitle.apply(WordUtils.capitalizeFully("show receipt"));







    public static final Supplier<String> getInvoiceTitle = () ->  windowTitle.apply(WordUtils.capitalizeFully("customer invoice"));



    public static final Supplier<String> defaultGenre = () -> "Any Genre";


    public static final Supplier<String> defaultMovie = () -> "Select Movie";


    public static final Supplier<String> defaultShowDate = () -> "Show All Dates";


}

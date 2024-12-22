package enums;


public enum FormDetails {
    ;
    private static final String APP_NAME = "Cinema Ticket Machine - ";



    public static String movieList() {
        return STR."\{APP_NAME}List Movies";

    }

    public static String register() {
        return "Customer registration";

    }

    public static String login() {
        return STR."\{APP_NAME}Login";

    }



    public static String showTimes() {
        return STR."\{APP_NAME}Show Times";

    }

    public static String purchaseTicket() {
        return STR."\{APP_NAME}Purchase Ticket";

    }

    public static String showReceipt() {
        return STR."\{APP_NAME}Show Receipt";

    }

    public static String getInvoiceTitle() {
        return "Cinema Ticket System Customer Invoice";
    }

    public static String defaultGenre() {
        return "Any Genre";

    }

    public static String defaultMovie() {
        return "Select Movie";

    }

    public static String defaultShowDate() {
        return "Show All Dates";

    }


}

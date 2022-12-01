package enums;


public enum FormDetails {
    ;
    private static final String APP_NAME = "Cinema Ticket Machine - ";
    public final String DESCRIPTION;

    FormDetails(String title) {
        this.DESCRIPTION = title;

    }

    public static String movieList() {
        return APP_NAME + "List Movies";

    }
    public static String register() {
        return APP_NAME + "Customer registration";

    }

    public static String login() {
        return APP_NAME + "Customer Login";

    }

    public static String showTimes() {
        return APP_NAME + "Show Times";

    }

    public static String purchaseTicket() {
        return APP_NAME + "Purchase Ticket";

    }

    public static String showReceipt() {
        return APP_NAME + "Show Receipt";

    }

    public static String defaultGenre() {
        return "Any Genre";

    }

    public static String defaultMovie() {
        return "Select Movie";

    }



}

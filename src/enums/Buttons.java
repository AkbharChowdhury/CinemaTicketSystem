package enums;



public enum Buttons {
    ;
    public final String DESCRIPTION;

    Buttons(String title) {
        this.DESCRIPTION = title;

    }

    public static String listMovies() {
        return "List Movies";

    }
    public static String showTimes() {
        return "Show Times";

    }
    public static String purchaseTicket() {
        return "Purchase Ticket";

    }
    public static String showReceipt() {
        return "Show Receipt";

    }



}

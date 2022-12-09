package classes;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

public class Invoice {

    public static final String INVOICE_FILE = "invoice.txt";
    private double totalTicket;

    public double getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(double totalTicket) {
        this.totalTicket = totalTicket;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private double price;
    private String salesDate;
    private String showDate;
    private String showTime;
    private String firstname;
    private String lastname;
    private String movieTitle;
    private String type;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;


    public String getInvoiceDetails(){
        return """
                 
                SELECT
                    s.*,
                    sh.show_date,
                    sh.show_time,
                    c.firstname,
                    c.lastname,
                    m.title,
                    t.type,
                    t.price,
                    r.rating
                FROM
                    Sales s
                JOIN ShowTimes sh ON
                    sh.show_time_id = s.show_time_id
                JOIN Movies m ON
                    m.movie_id = sh.movie_id
                JOIN Ratings r ON
                    r.rating_id = m.rating_id
                JOIN Customers c ON
                    c.customer_id = s.customer_id
                JOIN Tickets t ON
                    t.ticket_id = c.ticket_id
                WHERE
                    s.customer_id = ?
                                                           
                """;
    }


    public static String getSelectedInvoiceDetails(List<Invoice> INVOICES, int i) throws ParseException {
        double total = INVOICES.get(i).getPrice() * INVOICES.get(i).getTotalTicket();

        return MessageFormat.format("""
                            -------------------------- Movie Details --------------------
                            Movie: {0} 
                            Rating: {1}
                            show date and time ({2}, {3})
                            -------------------------- Ticket Details --------------------
                            {4} {5} (x{6}}
                            Total {7} 
                            -------------------------- Customer Details --------------------
                            {8} {9}
                            Purchase date: {10}                                                                                                                                                 
                            """,

                // movie details
                INVOICES.get(i).getMovieTitle(),
                INVOICES.get(i).getRating(),
                Helper.formatDate(INVOICES.get(i).getShowDate()),
                Helper.formatTime(INVOICES.get(i).getShowTime()),

                // ticket details
                INVOICES.get(i).getType(),
                Helper.formatMoney(INVOICES.get(i).getPrice()),
                INVOICES.get(i).getTotalTicket(),
                Helper.formatMoney(total),

                // customer details
                INVOICES.get(i).getFirstname(),
                INVOICES.get(i).getLastname(),
                Helper.formatDate(INVOICES.get(i).getSalesDate()));

    }

}

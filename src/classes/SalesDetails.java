package classes;

public class SalesDetails {
    private int salesID;
    private int movieID;
    private int ticketID;
    private int totalTicketsSold;
    public SalesDetails(){

    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public SalesDetails(int salesID, int movieID, int ticketID, int totalTicketsSold) {
        this.salesID = salesID;
        this.movieID = movieID;
        this.ticketID = ticketID;
        this.totalTicketsSold = totalTicketsSold;
    }
}

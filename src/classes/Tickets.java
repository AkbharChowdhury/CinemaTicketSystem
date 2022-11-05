package classes;

public class Tickets {
    private int ticketID;
    private String type;
    public Tickets(){

    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Tickets(int ticketID, String type) {
        this.ticketID = ticketID;
        this.type = type;
    }
}

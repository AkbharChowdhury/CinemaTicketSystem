package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.TicketsTable;

public class Ticket implements Queries {
    private int ticketID;
    private String type;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setTicketPrice(double ticketPrice) {
        this.price = ticketPrice;
    }

    public Ticket() {

    }

    public Ticket(int ticketID, String type, double price) {
        this.ticketID = ticketID;
        this.type = type;
        this.price = price;

    }

    public Ticket(String type, double price) {
        this.type = type;
        this.price = price;

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

    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT, 
                        %s TEXT NOT NULL UNIQUE,
                        %s NUMERIC NOT NULL

                        );
                        """,
                TicketsTable.TABLE_NAME,
                TicketsTable.COLUMN_ID,
                TicketsTable.COLUMN_TYPE,
                TicketsTable.COLUMN_PRICE


        );
    }
    public String getTickets(){
        return "SELECT * FROM " + TicketsTable.TABLE_NAME;
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?);
                        """,
                TicketsTable.TABLE_NAME
        );
    }
}

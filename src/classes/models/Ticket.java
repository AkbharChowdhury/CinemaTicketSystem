package classes.models;

import interfaces.Queries;
import tables.GenreTable;
import tables.TicketsTable;

public class Ticket implements Queries {
    protected int ticketID;
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

    public Ticket(int ticketID) {
        this.ticketID = ticketID;

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
        return STR."""
                        CREATE TABLE IF NOT EXISTS \{TicketsTable.TABLE_NAME} (
                            \{TicketsTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{TicketsTable.COLUMN_TYPE} TEXT NOT NULL UNIQUE,
                            \{TicketsTable.COLUMN_PRICE} NUMERIC NOT NULL

                        );
                        """;

    }
    public static String getTickets(){
        return STR."SELECT * FROM \{TicketsTable.TABLE_NAME}" ;
    }

    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{TicketsTable.TABLE_NAME}
                        VALUES (?, ?, ?);
                        """;


    }
}

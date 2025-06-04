package classes.models;

import interfaces.Queries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tables.TicketsTable;
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Ticket implements Queries {
    protected int ticketID;
    private String type;
    private double price;

    public Ticket(int ticketID, String type, double price) {
        this.ticketID = ticketID;
        this.type = type;
        this.price = price;
    }

    public Ticket(){}
    public Ticket(String type, double price) {
        this.type = type;
        this.price = price;

    }

    public Ticket(int ticketID) {
        this.ticketID = ticketID;

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


    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{TicketsTable.TABLE_NAME}
                        VALUES (?, ?, ?);
                        """;


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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
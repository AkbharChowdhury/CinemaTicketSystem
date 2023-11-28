package classes.models;

import interfaces.Queries;
import lombok.Data;
import tables.GenreTable;
import tables.TicketsTable;
@Data
public class Ticket implements Queries {
    protected int ticketID;
    private String type;
    private double price;


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

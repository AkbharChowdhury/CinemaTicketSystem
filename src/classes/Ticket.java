package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.TicketsTable;

public class Ticket implements Queries {
    private int ticketID;
    private String type;

    public Ticket() {

    }

    public Ticket(int ticketID, String type) {
        this.ticketID = ticketID;
        this.type = type;
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
                        %s TEXT NOT NULL UNIQUE);
                        """,
                TicketsTable.TABLE_NAME,
                TicketsTable.COLUMN_ID,
                TicketsTable.COLUMN_TYPE

        );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?);
                        """,
                GenreTable.TABLE_NAME
        );
    }
}

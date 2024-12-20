package classes.models;

import interfaces.Queries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tables.GenreTable;
import tables.TicketsTable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Queries {
    protected int ticketID;
    private String type;
    private double price;
    

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
}

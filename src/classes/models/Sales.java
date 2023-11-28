package classes.models;

import interfaces.Queries;
import tables.CustomerTable;
import tables.SalesTable;
import tables.ShowTimesTable;

public record Sales(int showTimeID, int customerID, String salesDate, int totalTicketsSold) implements Queries {
    public Sales() {
        this(0, 0, "", 0);
    }


    public static String salesExists() {
        return "SELECT * FROM Sales WHERE show_time_id = ? AND customer_id = ? AND sales_date =?";

    }

    @Override
    public String createTable() {

        return STR. """
                CREATE TABLE IF NOT EXISTS \{ SalesTable.TABLE_NAME }(
                	\{ SalesTable.COLUMN_SHOW_TIME_ID }	INTEGER,
                	\{ SalesTable.COLUMN_CUSTOMER_ID }  INTEGER,
                	\{ SalesTable.COLUMN_SALES_DATE } TEXT,
                	\{ SalesTable.COLUMN_TOTAL_TICKETS_SOLD } INTEGER,
                	PRIMARY KEY(\{ SalesTable.COLUMN_SHOW_TIME_ID },\{ SalesTable.COLUMN_CUSTOMER_ID },\{ SalesTable.COLUMN_SALES_DATE }),
                	FOREIGN KEY(\{ SalesTable.COLUMN_CUSTOMER_ID }) REFERENCES \{ CustomerTable.TABLE_NAME }(\{ CustomerTable.COLUMN_ID }) ON UPDATE CASCADE ON DELETE CASCADE,
                	FOREIGN KEY(\{ SalesTable.COLUMN_SHOW_TIME_ID }) REFERENCES \{ ShowTimesTable.TABLE_NAME }(\{ ShowTimesTable.COLUMN_ID }) ON UPDATE CASCADE ON DELETE CASCADE
                    );
                """ ;
    }


    @Override
    public String insert() {
        return STR. """
                        INSERT INTO \{ SalesTable.TABLE_NAME }
                        VALUES (?,?,?,?);
                        """ ;


    }
}

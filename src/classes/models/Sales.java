package classes.models;

import interfaces.Queries;
import tables.*;

public class Sales implements Queries {
    private String salesDate;
    private int showTimeID;

    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public Sales() {

    }


    public Sales(int showTimeID, int customerID, String salesDate, int totalTicketsSold) {
        this.showTimeID = showTimeID;
        this.salesDate = salesDate;
        this.customerID = customerID;
        this.totalTicketsSold = totalTicketsSold;
    }

    private int customerID;
    private int totalTicketsSold;

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
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
        return STR."""
                        INSERT INTO \{ SalesTable.TABLE_NAME}
                        VALUES (?,?,?,?);
                        """;


    }
}

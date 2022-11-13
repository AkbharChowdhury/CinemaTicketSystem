package classes;

import interfaces.Queries;
import tables.CustomerTable;
import tables.MovieTable;
import tables.RatingTable;
import tables.SalesTable;

public class Sales implements Queries {
    private int salesID;
    private String salesDate;
    private int customerID;

    public Sales() {

    }


    public Sales(int salesID, String salesDate, int customerID) {
        this.salesID = salesID;
        this.salesDate = salesDate;
        this.customerID = customerID;
    }
    public Sales(String salesDate, int customerID) {
        this.salesDate = salesDate;
        this.customerID = customerID;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

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


    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT,
                        %s TEXT NOT NULL,
                        %s INTEGER NOT NULL,
                        FOREIGN KEY(%s) REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE
                        );
                        """,
                SalesTable.TABLE_NAME,
                SalesTable.COLUMN_ID,
                SalesTable.COLUMN_SALES_DATE,
                SalesTable.COLUMN_CUSTOMER_ID,
                // rating fk
                SalesTable.COLUMN_CUSTOMER_ID,
                CustomerTable.TABLE_NAME,
                CustomerTable.COLUMN_ID
        );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?);
                        """,
                SalesTable.TABLE_NAME
        );
    }
}

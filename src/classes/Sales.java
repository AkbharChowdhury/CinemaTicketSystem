package classes;

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

    public Sales(){

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


    public String salesExists() {
        return  "SELECT * FROM Sales WHERE show_time_id = ? AND customer_id = ? AND sales_date =?" ;

    }

    @Override
    public String createTable() {

        return String.format("""
                             
                CREATE TABLE %s(
                	"show_time_id"	INTEGER,
                	"customer_id"	INTEGER,
                	"sales_date"	TEXT,
                	"total_tickets_sold" INTEGER,
                	PRIMARY KEY("show_time_id","customer_id","sales_date"),
                	FOREIGN KEY("customer_id") REFERENCES "Customers"("customer_id") ON UPDATE CASCADE
                   ON DELETE CASCADE,
                	FOREIGN KEY("show_time_id") REFERENCES "ShowTimes"("show_time_id") ON UPDATE CASCADE
                    ON DELETE CASCADE    
                    );               
                """, SalesTable.TABLE_NAME);
    }



    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?,?,?,?);
                        """,
                SalesTable.TABLE_NAME
        );
    }
}

package classes;

import interfaces.Queries;
import tables.*;

public class Sales extends MovieShowTimes  implements Queries {
    private String salesDate;
    public Sales(){

    }


    public Sales(String salesDate, int movieId, int showTimeId, int customerID, int total_tickets_sold) {
        super(movieId, showTimeId);
        this.salesDate = salesDate;
        this.customerID = customerID;
        this.totalTicketsSold = total_tickets_sold;
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

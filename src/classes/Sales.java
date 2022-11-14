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
        return """
                
                CREATE TABLE "Sales2" (
                	"sales_date"	TEXT NOT NULL,
                	"movie_id"	INTEGER NOT NULL,
                	"show_time_id"	INTEGER NOT NULL,
                	"customer_id"	INTEGER NOT NULL,
                	"total_tickets_sold"	INTEGER NOT NULL,
                	--FOREIGN KEY("movie_id") REFERENCES "MovieShowTimes"("movie_id"),
                	FOREIGN KEY("customer_id") REFERENCES "Customers"("customer_id"),
                	--FOREIGN KEY("show_time_id") REFERENCES "MovieShowTimes"("show_time_id"),
                	PRIMARY KEY("sales_date","movie_id","customer_id","show_time_id")
                );
                """;
    }



    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?, ?, ?);
                        """,
                SalesTable.TABLE_NAME
        );
    }
}

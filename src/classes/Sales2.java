package classes;

import interfaces.Queries;
import tables.*;

public class Sales2 extends MovieShowTimes  implements Queries {
    private String salesDate;
    public Sales2(){

    }


    public Sales2(String salesDate, int movieId, int showTimeId, int customerID, int total_tickets_sold) {
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
//    public String createTable() {
//        return String.format("""
//                        CREATE TABLE IF NOT EXISTS %s (
//                        %s TEXT NOT NULL,
//                        %s INTEGER NOT NULL,
//                        show_time_id INTEGER NOT NULL,
//                        %s INTEGER NOT NULL,
//                        %s INTEGER NOT NULL,
//
//
//                        PRIMARY KEY(%s, %s, %s, show_time_id),
//                        FOREIGN KEY(%s) REFERENCES %s(%s),
//                        FOREIGN KEY(%s) REFERENCES %s(%s),
//                        FOREIGN KEY(show_time_id) REFERENCES MovieShowTimes(show_time_id)
//
//                        );
//                        """,
//                Sales2Table.TABLE_NAME,
//
//                // columns
//
//                Sales2Table.COLUMN_SALES_DATE,
//                Sales2Table.COLUMN_MOVIE_ID,
//                Sales2Table.COLUMN_CUSTOMER_ID,
//                Sales2Table.COLUMN_TOTAL_TICKS_SOLD,
//
//
//                // primary keys
//                Sales2Table.COLUMN_SALES_DATE,
//                Sales2Table.COLUMN_MOVIE_ID,
//                Sales2Table.COLUMN_CUSTOMER_ID,
//
//                // compound keys
//                // first key
//                Sales2Table.COLUMN_MOVIE_ID,
//                MovieShowTimesTable.TABLE_NAME, // fk mismatch issue so we must link tp movie table
//                MovieShowTimesTable.COLUMN_MOVIE_ID,
//
//
//                // second key
//                Sales2Table.COLUMN_CUSTOMER_ID,
//                CustomerTable.TABLE_NAME,
//                CustomerTable.COLUMN_ID
//        );
//    }


    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?, ?, ?);
                        """,
                Sales2Table.TABLE_NAME
        );
    }
}

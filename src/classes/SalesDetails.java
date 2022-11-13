package classes;

import interfaces.Queries;
import tables.*;

public class SalesDetails implements Queries {
    private int salesID;
    private int movieID;
    private int ticketID;
    private int totalTicketsSold;
    public SalesDetails(){

    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public SalesDetails(int salesID, int movieID, int totalTicketsSold) {
        this.salesID = salesID;
        this.movieID = movieID;
        this.totalTicketsSold = totalTicketsSold;
    }

    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        
                        PRIMARY KEY(%s, %s)
                        
                        
                        
                        
                        );
                        """,
                SalesDetailsTable.TABLE_NAME,
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID,
                SalesDetailsTable.COLUMN_TOTAL_TICKETS_SOLD,

                // primary keys
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID

                //FOREIGN KEY(%s) REFERENCES %s(%s),
                //                        FOREIGN KEY(%s) REFERENCES %s(%s)


//                // first
//                SalesDetailsTable.COLUMN_SALES_ID,
//                SalesTable.TABLE_NAME,
//                SalesTable.COLUMN_ID,
//
//
//                // second key
//                SalesDetailsTable.COLUMN_MOVIE_ID,
//                MovieShowTimesTable.TABLE_NAME,
//                MovieShowTimesTable.COLUMN_MOVIE_ID




                //FOREIGN KEY(%s) REFERENCES %s(%s),
                //                        FOREIGN KEY(%s) REFERENCES %s(%s)


                );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?,?);
                        """,
                SalesDetailsTable.TABLE_NAME
        );

    }


    public String getInvoice(){
       return  """
               SELECT
                 sd.*,
                 s.sales_date,
                 sh.show_date,
                 sh.show_time,
                 s.customer_id,
                 c.firstname,
                 c.lastname,
                 m.title,
                 t.type,
                 t.price
               FROM
                 SalesDetails sd
                 JOIN Sales s ON s.sales_id = sd.sales_id
                 JOIN Customers c ON c.customer_id = s.sales_id
                 JOIN Tickets t ON t.ticket_id = c.ticket_id
                 JOIN MovieShowTimes mst ON mst.movie_id = sd.movie_id
                 JOIN Movies m ON m.movie_id = mst.movie_id
                 JOIN ShowTimes sh ON sh.show_time_id = mst.show_time_id
               WHERE
                 c.customer_id = ?
                              
                               
                                         
                """;
    }
}

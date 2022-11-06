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

    public SalesDetails(int salesID, int movieID, int ticketID, int totalTicketsSold) {
        this.salesID = salesID;
        this.movieID = movieID;
        this.ticketID = ticketID;
        this.totalTicketsSold = totalTicketsSold;
    }

    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,
                        %s INTEGER NOT NULL,

                        PRIMARY KEY(%s, %s, %s),
                        
                        FOREIGN KEY(%s) REFERENCES %s(%s),
                        FOREIGN KEY(%s) REFERENCES %s(%s),
                        FOREIGN KEY(%s) REFERENCES %s(%s)                      
                        );
                        """,
                SalesDetailsTable.TABLE_NAME,
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID,
                SalesDetailsTable.COLUMN_TICKET_ID,
                SalesDetailsTable.COLUMN_TOTAL_TICKETS_SOLD,

                // primary keyss
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID,
                SalesDetailsTable.COLUMN_TICKET_ID,


                // first
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesTable.TABLE_NAME,
                SalesTable.COLUMN_ID,


                // second key
                SalesDetailsTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.TABLE_NAME,
                MovieShowTimesTable.COLUMN_MOVIE_ID,


                // third key
                SalesDetailsTable.COLUMN_TICKET_ID,
                TicketsTable.TABLE_NAME,
                TicketsTable.COLUMN_ID




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

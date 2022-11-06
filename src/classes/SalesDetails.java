package classes;

import interfaces.Queries;
import tables.GenreTable;
import tables.MovieShowTimesTable;
import tables.SalesDetailsTable;

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

                        PRIMARY KEY(%s, %s, %s)
                        
                        );
                        """,
                SalesDetailsTable.TABLE_NAME,
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID,
                SalesDetailsTable.COLUMN_TICKET_ID,
                SalesDetailsTable.COLUMN_TOTAL_TICKETS_SOLD,

                // primary keys
                SalesDetailsTable.COLUMN_SALES_ID,
                SalesDetailsTable.COLUMN_MOVIE_ID,
                SalesDetailsTable.COLUMN_TICKET_ID,






                MovieShowTimesTable.COLUMN_MOVIE_ID,
                MovieShowTimesTable.COLUMN_SHOW_TIME_ID

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

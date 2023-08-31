package classes;

import interfaces.Queries;
import tables.CustomerTable;
import tables.TicketsTable;

public class Customer extends Ticket implements Queries {
    private int customerID;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public Customer() {

    }

    public Customer(String firstname, String lastname, String email, String password, int ticketID) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.ticketID = ticketID;
    }



    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT, 
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL UNIQUE,
                        %s TEXT NOT NULL,
                        %s INTEGER NOT NULL,
                        FOREIGN KEY(%s) REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE              
                        );
                        """,
                CustomerTable.TABLE_NAME,
                CustomerTable.COLUMN_ID,
                CustomerTable.COLUMN_FIRSTNAME,
                CustomerTable.COLUMN_LASTNAME,
                CustomerTable.COLUMN_EMAIL,
                CustomerTable.COLUMN_PASSWORD,
                CustomerTable.COLUMN_TICKET_ID,
                TicketsTable.COLUMN_ID,
                TicketsTable.TABLE_NAME,
                TicketsTable.COLUMN_ID
        );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?, ?, ?, ?);
                        """,
                CustomerTable.TABLE_NAME
        );
    }

    public  static String getCustomerTicketType(){
        return """
                SELECT  c.customer_id, t.type, t.price, t.ticket_id                         
                FROM Customers c JOIN Tickets t ON t.ticket_id = c.ticket_id
                WHERE customer_id = ?
                """;
    }


}

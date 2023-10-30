package classes.models;

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

        return STR."""
                        CREATE TABLE IF NOT EXISTS \{CustomerTable.TABLE_NAME} (
                            \{CustomerTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{CustomerTable.COLUMN_FIRSTNAME} TEXT NOT NULL,
                            \{CustomerTable.COLUMN_LASTNAME} TEXT NOT NULL,
                            \{CustomerTable.COLUMN_EMAIL} TEXT NOT NULL UNIQUE,
                            \{CustomerTable.COLUMN_PASSWORD} TEXT NOT NULL,
                            \{CustomerTable.COLUMN_TICKET_ID} INTEGER NOT NULL,
                            FOREIGN KEY(\{CustomerTable.COLUMN_TICKET_ID}) REFERENCES \{ TicketsTable.TABLE_NAME}(\{TicketsTable.COLUMN_ID}) ON UPDATE CASCADE ON DELETE CASCADE
                        );
                        """;

    }

    @Override
    public String insert() {
        return STR."""
                        INSERT INTO \{CustomerTable.TABLE_NAME}
                        VALUES (?, ?, ?, ?, ?, ?);
                        """;


    }

    public  static String getCustomerTicketType(){
        return """
                SELECT  c.customer_id, t.type, t.price, t.ticket_id                         
                FROM Customers c JOIN Tickets t ON t.ticket_id = c.ticket_id
                WHERE customer_id = ?
                """;
    }



}

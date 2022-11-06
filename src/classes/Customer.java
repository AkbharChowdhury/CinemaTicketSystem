package classes;

import interfaces.Queries;
import tables.CustomerTable;
import tables.TicketsTable;

public class Customer implements Queries {
    private int customerID;
    private String firstname;
    private String lastname;
    private String dob;
    private String email;
    private String password;

    public Customer() {

    }

    public Customer(String firstname, String lastname, String dob, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.email = email;
        this.password = password;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL UNIQUE,
                        %s TEXT NOT NULL                  
                        );
                        """,
                CustomerTable.TABLE_NAME,
                CustomerTable.COLUMN_ID,
                CustomerTable.COLUMN_FIRSTNAME,
                CustomerTable.COLUMN_LASTNAME,
                CustomerTable.COLUMN_DOB,
                CustomerTable.COLUMN_EMAIL,
                CustomerTable.COLUMN_PASSWORD
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


}

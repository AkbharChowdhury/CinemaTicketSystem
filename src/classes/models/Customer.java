package classes.models;

import classes.Form;
import classes.LoginInfo;
import classes.utils.Helper;
import enums.Pages;
import enums.RedirectPage;
import forms.Login;
import interfaces.Queries;
import lombok.Data;
import lombok.NoArgsConstructor;
import tables.CustomerTable;
import tables.TicketsTable;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

//@Data
//@NoArgsConstructor
public class Customer extends Ticket implements Queries {
    private int customerID;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer(String firstname, String lastname, String email, String password, int ticketID) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.ticketID = ticketID;
    }
    public Customer(){}
    
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
                            FOREIGN KEY(\{CustomerTable.COLUMN_TICKET_ID}) REFERENCES \{TicketsTable.TABLE_NAME}(\{TicketsTable.COLUMN_ID}) ON UPDATE CASCADE ON DELETE CASCADE
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


    public static boolean isLoggedIn(JFrame frame, RedirectPage page) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (LoginInfo.getCustomerID() == 0) {
            if (JOptionPane.showConfirmDialog(null, "You must be logged in to purchase tickets or print invoices, do you want to login?", "WARNING", JOptionPane.YES_NO_OPTION)
                    != JOptionPane.YES_OPTION
                    && !LoginInfo.hasOpenFormOnStartUp()) {
                System.err.println("You must be logged in to view invoices or purchase tickets!");
                System.exit(0);
            }

            Form.setRedirectPage(page);
            if (LoginInfo.hasOpenFormOnStartUp()) {
                Helper.gotoForm(frame, Pages.LOGIN);
                return false;
            }
            new Login();
            frame.dispose();
        }

        return LoginInfo.getCustomerID() != 0;

    }


    public int getCustomerID() {
        return customerID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

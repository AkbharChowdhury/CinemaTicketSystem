package classes.utils;

import classes.models.Customer;
import classes.Database;
import classes.models.ShowTimes;
import tables.CustomerTable;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class Validation {
    private static final String NAME_ERROR = " must not contain numbers, spaces or special characters";
    private static final String REQUIRED = " is required";
    private static final String TICKET_REQUIRED = "Please select a ticket type";
    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private static String validateName(String name, String column) {
        if (name.isEmpty()) {
            return column + REQUIRED;
        }
        if (!isValidName.apply(name)) {
            return name + NAME_ERROR;
        }
        return "";

    }

    public static boolean validateRegisterForm(Customer customer, Database db) {
        List<String> errors = new ArrayList<>();
        String firstname = customer.getFirstname().trim();
        String lastname = customer.getLastname().trim();
        String password = customer.getPassword();
        boolean isTicketRequired = customer.getTicketID() == 0;
        String firstnameError = validateName(firstname, CustomerTable.COLUMN_FIRSTNAME);
        String lastnameError = validateName(lastname, CustomerTable.COLUMN_LASTNAME);
        if (!firstnameError.isEmpty()) errors.add(firstnameError);
        if (!lastnameError.isEmpty()) errors.add(lastnameError);


        List<String> emailErrors = validateEmail(customer, db);
        List<String> passwordErrors = validatePassword(password);

        if (!passwordErrors.isEmpty()) errors.addAll(passwordErrors);
        if (!emailErrors.isEmpty()) errors.addAll(emailErrors);
        if (isTicketRequired) errors.add(TICKET_REQUIRED);


        if (!errors.isEmpty()) {
            var output = new StringBuilder("This form contains the following errors: \n");
            errors.forEach(error -> output.append(error).append("\n"));
            Helper.showErrorMessage(output.toString(), "Register error");

        }

        return errors.isEmpty();

    }

    private static List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        if (password.isEmpty()) {
            errors.add(CustomerTable.COLUMN_PASSWORD + REQUIRED);

        } else if (password.length() < 8) {

            errors.add(STR."\{CustomerTable.COLUMN_PASSWORD} must be 8 characters long");

        }
        return errors;
    }

    private static List<String> validateEmail(Customer customer, Database db) {
        List<String> errors = new ArrayList<>();
        if (customer.getEmail().isEmpty()) {
            errors.add(CustomerTable.COLUMN_EMAIL + REQUIRED);
        } else if (!isValidEmail(customer.getEmail())) {
            errors.add("Please enter a valid email");

        } else if (db.emailExists(customer.getEmail())) {
            errors.add("This email already exists");
        }
        return errors;
    }

    private static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public static boolean validateLoginForm(String email, String password) {
        if (email.isEmpty() | password.isEmpty()) {
            Helper.showErrorMessage("Email and password is required!", "login Error");
            return false;
        }
        return true;


    }

    public static boolean isValidNumTicketsSold(Database db, ShowTimes showTimes) {
        int numTicketsLeft = db.getNumTickets(showTimes.getShowTimeID());
        int remainingTickets = numTicketsLeft - showTimes.getNumTicketsSold();
        return remainingTickets >= 0;

    }

    private static final Function<String, Boolean> isValidName = name -> name.matches("^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*$");


}

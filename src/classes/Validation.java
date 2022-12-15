package classes;

import tables.CustomerTable;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validation {
    private static final String NAME_ERROR = " must not contain numbers, spaces or special characters";
    private static final String REQUIRED = " is required";
    private static final String TICKET_REQUIRED = "Please select a ticket type";

    public static boolean validateRegisterForm(Customer customer) throws SQLException, FileNotFoundException {
        Database db = Database.getInstance();
        List<String> errors = new ArrayList<>();
        String firstname = customer.getFirstname().trim();
        String lastname = customer.getLastname().trim();
        String password = customer.getPassword();
        boolean isTicketRequired = customer.getTicketID() == 0;

        if (firstname.isEmpty()) {
            errors.add(CustomerTable.COLUMN_FIRSTNAME + REQUIRED);
        } else if (!isValidName(firstname)) {
            errors.add(CustomerTable.COLUMN_FIRSTNAME + NAME_ERROR);
        }

        if (lastname.isEmpty()) {
            errors.add(CustomerTable.COLUMN_LASTNAME + REQUIRED);
        } else if (!isValidName(lastname)) {
            errors.add(CustomerTable.COLUMN_LASTNAME + NAME_ERROR);

        }

        if (customer.getEmail().isEmpty()) {
            errors.add(CustomerTable.COLUMN_EMAIL + REQUIRED);
        } else if (!isValidEmail(customer.getEmail())) {
            errors.add("Please enter a valid email");

        } else if (db.emailExists(customer.getEmail())) {
            errors.add("This email already exists");
        }

        if (password.isEmpty()) {
            errors.add(CustomerTable.COLUMN_PASSWORD + REQUIRED);

        } else if (password.length() < 8) {

            errors.add(CustomerTable.COLUMN_PASSWORD + " must be 8 characters long");

        }

        if (isTicketRequired) {
            errors.add(TICKET_REQUIRED);
        }

        if (errors.size() > 0) {
            var output = new StringBuilder("This form contains the following errors: \n");
            errors.forEach(error -> output.append(error).append("\n"));
            Helper.showErrorMessage(output.toString(), "Register error");

        }

        return errors.size() == 0;

    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateLoginForm(String email, String password) {
        if (email.isEmpty() | password.isEmpty()) {
            Helper.showErrorMessage("Email and password is required!", "login Error");
            return false;
        }
        return true;


    }

    public static boolean isValidNumTicketsSold(Database db, ShowTimes showTimes) {
        int numTicketsLeft = db.getNumTickets(showTimes);
        int remainingTickets = numTicketsLeft - showTimes.getNumTicketsSold();
        return remainingTickets >= 0;


    }

    private static boolean isValidName(String str) {
        return str.matches("^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*$");
    }


}

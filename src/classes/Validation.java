package classes;

import tables.CustomerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validation {
    public static boolean validateRegisterForm(Customer customer){
        List<String> errors = new ArrayList<>();
        if (customer.getFirstname().isEmpty()){
            errors.add(CustomerTable.COLUMN_FIRSTNAME + " is required");
        }
        if (customer.getLastname().isEmpty()){
            errors.add(CustomerTable.COLUMN_LASTNAME + " is required");
        }
        if (customer.getEmail().isEmpty()){
            errors.add(CustomerTable.COLUMN_EMAIL + " is required");
        } else if (!isValidEmail(customer.getEmail())){
            errors.add("Please enter a valid email");
        }
        if (customer.getPassword().isEmpty()){
            errors.add(CustomerTable.COLUMN_PASSWORD + " is required");

        } else if (customer.getPassword().length()<8){

            errors.add(CustomerTable.COLUMN_PASSWORD + " must be 8 characters long");

        }
        if (customer.getTicketID() == 0){
            errors.add("Please select a ticket type");
        }

        if (errors.size() > 0){
            StringBuilder output = new StringBuilder("This form contains the following errors: \n");
            errors.forEach(error -> output.append(error).append("\n"));
            Helper.showErrorMessage(output.toString(),"Register error");

        }


        return errors.size() == 0;

    }

    private static boolean isValidEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validateLoginForm(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            Helper.showErrorMessage("Email and password is required!", "login Error");
            return false;
        }
        return true;


    }
}

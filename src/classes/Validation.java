package classes;

import tables.CustomerTable;

import java.util.ArrayList;
import java.util.List;

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
}

import classes.Database;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    // email regex https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        var d = Database.getInstance().getCustomerTicketType(5);
        System.out.println(d.getType());
        System.out.println(d.getPrice());


    }
    static boolean isValidEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

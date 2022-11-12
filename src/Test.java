import classes.Database;
import classes.Helper;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    // email regex https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
    public static void main(String[] args) throws SQLException, FileNotFoundException {
//        System.out.println(Helper.formatDate("1999-12-12"));
        for (var d : Database.getInstance().getMovieAllShowTimes()){
            System.out.println(d.getTitle() + " " + d.getMovieID());
        }


    }
    static boolean isValidEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

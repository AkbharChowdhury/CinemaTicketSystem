import classes.Database;
import classes.Helper;
import org.codehaus.janino.CompileException;
import org.codehaus.janino.ScriptEvaluator;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    // email regex https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
    public static void main(String[] args) throws SQLException, FileNotFoundException, CompileException, InvocationTargetException {
//        System.out.println(Helper.formatDate("1999-12-12"));
//        String x = "System.out.println(\"X\");"; //<-- dont forget the ; in the string here
//        ScriptEvaluator se = new ScriptEvaluator();
//        se.cook(x);
//        se.evaluate(new Object[0]);


    }
    static boolean isValidEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

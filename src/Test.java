import classes.Database;
import classes.Helper;
import classes.Sales;
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
        var db = Database.getInstance();

       var rs = db.getInvoice(1);
        System.out.println(rs.size());
       for (var i : db.getInvoice(1)){
           System.out.println(i.getPrice());
       }

//       rs.getString("firstname");


//

        /*
        *
        *
        * SELECT
sd.*,
s.sales_date,
s.customer_id,
c.firstname,
c.lastname,
m.title,
t.type,
t.price
FROM SalesDetails sd
JOIN Sales s ON s.sales_id = sd.sales_id
JOIN Customers c ON c.customer_id = s.sales_id
JOIN Tickets t ON t.ticket_id = c.ticket_id
JOIN MovieShowTimes mst ON mst.movie_id = sd.movie_id
JOIN Movies m ON m.movie_id = mst.movie_id
WHERE c.customer_id = 1

*/


    }
    static boolean isValidEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

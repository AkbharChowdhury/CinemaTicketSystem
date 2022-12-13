import classes.Database;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class M {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Database db =Database.getInstance();
        var t = db.getCustomerTicketType(1);
        System.out.println(t.getType());
        System.out.println(t.getPrice());
        System.out.println(t.getTicketID());

    }
}

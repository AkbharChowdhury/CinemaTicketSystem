import classes.Database;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        var f = Database.getInstance();
    }
}

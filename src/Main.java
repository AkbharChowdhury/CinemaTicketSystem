import classes.Database;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class Main {
     public  static void main(String[] args) throws SQLException, FileNotFoundException {
         System.out.println(Database.getInstance().getMovieName(2));
    }


}

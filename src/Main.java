import classes.Database;

import java.sql.SQLException;

public class Main {
    private static Database db;
    public static void main(String[] args) throws SQLException {
        db = Database.getInstance();
    }
}
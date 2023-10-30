package interfaces;

import java.sql.SQLException;
import java.text.ParseException;
@FunctionalInterface
public interface ListGUI {
    void populateList() throws ParseException, SQLException;
}

package interfaces;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;

public interface ListGUI {
    void clearList(JList<String> table);
    void populateList() throws ParseException, SQLException;


}

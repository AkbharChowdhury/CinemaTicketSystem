package interfaces;

import javax.swing.*;
import java.text.ParseException;

public interface TableGUI {
    void clearTable(JTable table);
    void showColumn();
    void populateTable() throws ParseException;
}

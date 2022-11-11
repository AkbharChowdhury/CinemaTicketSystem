package interfaces;

import javax.swing.*;
import java.text.ParseException;

public interface ListGUI {
    void clearList(JList table);
    void populateList() throws ParseException;


}

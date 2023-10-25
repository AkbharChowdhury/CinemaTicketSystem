package classes.models;

import classes.utils.Helper;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomTableModel {
    DefaultTableModel model;

    public CustomTableModel(DefaultTableModel model) {
        this.model = model;

    }

    public void populateTable(List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            model.addRow(new Object[0]);
            var c = new Counter(true);
            int index = i;
            Arrays.stream(list.get(i).split(Helper.SEP)).toList().forEach(customItem -> model.setValueAt(customItem, index, c.getCounter()));

        }

    }


}
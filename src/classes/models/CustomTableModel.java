package classes.models;

import classes.utils.Helper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.List;

public class CustomTableModel {
    public static void setFirstColumnAlignment(JTable table, int labelAlignment) {
        var cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(labelAlignment);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
    }


    private final DefaultTableModel model;

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
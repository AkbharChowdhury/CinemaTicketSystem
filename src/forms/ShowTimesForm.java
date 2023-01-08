package forms;

import classes.*;
import enums.FormDetails;
import interfaces.MenuNavigation;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Set;


public final class ShowTimesForm extends JFrame implements ActionListener, TableGUI, MenuNavigation {
    Navigation nav = new Navigation();

    Database db = Database.getInstance();
    ShowTimes movieShowTimes = new ShowTimes();
    JTable table = new JTable();
    JComboBox<String> cbMovies = new JComboBox<>();
    JComboBox<String> cbDate = new JComboBox<>();
    DefaultTableModel model;
    boolean hasSelectedMovie = false;


    public ShowTimesForm() throws SQLException, FileNotFoundException {
//        db = Database.getInstance();
        if (LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID())) {
            nav.btnShowReceipt.setEnabled(false);
        }
        movieShowTimes.setDate("");

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        showColumn();

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.showTimes());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        navigation(top);


        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        cbMovies.addItem(FormDetails.defaultMovie());
        populateMovieComboBox();
        middle.add(cbMovies);

        middle.add(new Label("Filter Date:"));

        populateShowDateComboBox();

        cbDate.addItem(FormDetails.defaultShowDate());
        middle.add(cbDate);


        JPanel south = new JPanel();
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);

        south.add(movieScrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);

        cbMovies.addActionListener(this);
        cbDate.addActionListener(this);

        setVisible(true);
    }


    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new ShowTimesForm();

    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == cbDate && cbDate.getSelectedItem() != null) {
            showFilteredDateResults();

        }

        if (e.getSource() == cbMovies) {
            handleMovieCB();
        }


    }

    private void handleMovieCB() {
        if (!hasSelectedMovie) {
            cbMovies.removeItemAt(0);
            hasSelectedMovie = true;

        }

        movieShowTimes.setMovieID(db.getMovieID(cbMovies.getSelectedItem().toString()));
        populateShowDateComboBox();
        populateTable();
    }

    private void showFilteredDateResults() {
        // filter show times by date
        if (cbDate.getSelectedIndex() != 0) {

            try {
                movieShowTimes.setDate(Helper.convertMediumDateToYYMMDD(cbDate.getSelectedItem().toString()));
                populateTable();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            return;

        }

        // display all show times
        movieShowTimes.setDate("");
        populateTable();
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new ShowTimes().tableColumns().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            int i = 0;
            for (var showTime : db.showMovieTimes(movieShowTimes)) {
                model.addRow(new Object[0]);
                model.setValueAt(Helper.formatDate(showTime.getDate()), i, 0);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 1);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 2);
                i++;

            }

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void populateMovieComboBox() {

        for (var movie : db.getAllMovieShowTimes()) {
            cbMovies.addItem(movie.getTitle());
        }

    }

    void populateShowDateComboBox() {
        cbDate.removeAllItems();
        var showTimesList = db.showMovieTimes(movieShowTimes);

        // get unique dates
        Set<String> linkedHashSet = new LinkedHashSet<>();

        for (var show : showTimesList) {
            linkedHashSet.add(show.getDate());
        }
        cbDate.addItem(FormDetails.defaultShowDate());
        for (var date : linkedHashSet) {
            cbDate.addItem(Helper.formatDate(date));
        }

    }

    @Override
    public void navigation(JPanel top) {
        top.add(nav.btnListMovies);
        top.add(nav.btnShowTimes);
        top.add(nav.btnPurchase);
        top.add(nav.btnShowReceipt);

        nav.btnListMovies.addActionListener(this::navClick);
        nav.btnShowTimes.addActionListener(this::navClick);
        nav.btnPurchase.addActionListener(this::navClick);
        nav.btnShowReceipt.addActionListener(this::navClick);
    }

    @Override
    public void navClick(ActionEvent e) {
        if (nav.handleNavClick(e)) {
            dispose();
        }
    }
}

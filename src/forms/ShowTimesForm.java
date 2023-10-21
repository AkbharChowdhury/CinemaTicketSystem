package forms;

import classes.*;
import classes.models.Counter;
import classes.models.Movie;
import classes.models.ShowTimes;
import classes.utils.Helper;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;


public final class ShowTimesForm extends JFrame implements ActionListener, TableGUI, MenuNavigation {
    Navigation nav = new Navigation(this);

    Database db = Database.getInstance();
    ShowTimes movieShowTimes = new ShowTimes();
    JTable table = new JTable();
    JComboBox<String> cbMovies = new JComboBox<>();
    JComboBox<String> cbDate = new JComboBox<>();
    DefaultTableModel model;
    boolean hasSelectedMovie;
    List<Movie> movieList;


    public ShowTimesForm() throws SQLException, FileNotFoundException {
        if (LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID()))
            nav.btnShowReceipt.setEnabled(false);

        movieList = db.getAllMovieShowTimes();
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
        movieList.forEach(movie -> cbMovies.addItem(movie.getTitle()));

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


    public static void main(String[] args) throws SQLException, FileNotFoundException {
        new ShowTimesForm();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cbDate && cbDate.getSelectedItem() != null) showFilteredDateResults();
        if (e.getSource() == cbMovies) handleMovieCB();
    }

    private void handleMovieCB() {
        Movie.movieComboBoxStatus(cbMovies);
        movieShowTimes.setMovieID(movieList.get(cbMovies.getSelectedIndex()).getMovieID());
        populateShowDateComboBox();
        populateTable();

    }

    private void showFilteredDateResults() {
        try {
            movieShowTimes.setDate(cbDate.getSelectedIndex() != 0 ? Helper.convertMediumDateToYYMMDD(cbDate.getSelectedItem().toString()) : "");
            populateTable();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new ShowTimes().tableColumns().forEach(model::addColumn);

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            var showTimes = db.showMovieTimes(movieShowTimes);
            final int size = showTimes.size();
            for (int i = 0; i < size; i++) {
                var c = new Counter(true);
                ShowTimes showTime = showTimes.get(i);
                model.addRow(new Object[0]);
                model.setValueAt(Helper.formatDate(showTime.getDate()), i, c.getCounter());
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, c.getCounter());
                model.setValueAt(showTime.getNumTicketsLeft(), i, c.getCounter());

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }


    }


    void populateShowDateComboBox() {
        cbDate.removeAllItems();
        cbDate.addItem(FormDetails.defaultShowDate());
        // get unique dates
        Set<String> linkedHashSet = new LinkedHashSet<>();
        db.showMovieTimes(movieShowTimes).forEach(show -> linkedHashSet.add(show.getDate()));
        linkedHashSet.forEach(date -> cbDate.addItem(Helper.formatDate(date)));


    }

    @Override
    public void navigation(JPanel top) {
        Arrays.stream(nav.navButtons()).forEach(top::add);
    }


}

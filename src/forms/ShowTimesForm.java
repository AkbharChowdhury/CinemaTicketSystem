package forms;

import classes.Database;
import classes.Navigation;
import classes.models.CustomTableModel;
import classes.models.Movie;
import classes.models.ShowTimes;
import classes.utils.CalendarUtils;
import classes.utils.Helper;
import enums.FormDetails;
import interfaces.MenuNavigation;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public final class ShowTimesForm extends JFrame implements ActionListener, TableGUI, MenuNavigation {
    Navigation nav = new Navigation(this);
    Database db = Database.getInstance();
    ShowTimes movieShowTimes = new ShowTimes();
    JTable table = new JTable();
    JComboBox<String> cbMovies = new JComboBox<>();
    JComboBox<String> cbDate = new JComboBox<>();
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    List<Movie> movieList;
    CustomTableModel tableModel = new CustomTableModel(model);


    public ShowTimesForm() {
        table.setEnabled(false);

        if (Helper.disableReceipt.apply(db)) nav.btnShowReceipt.setEnabled(false);

        movieList = db.getAllMovieShowTimes();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        showColumn();

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.showTimes.get());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        navigation(top);

        CustomTableModel.setFirstColumnAlignment(table, JLabel.CENTER);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        cbMovies.addItem(FormDetails.defaultMovie.get());
        movieList.forEach(movie -> cbMovies.addItem(movie.getTitle()));

        middle.add(cbMovies);

        middle.add(new Label("Filter Date:"));

        populateShowDateComboBox();

        cbDate.addItem(FormDetails.defaultShowDate.get());
        middle.add(cbDate);


        JPanel south = new JPanel();
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);

        south.add(movieScrollPane);

        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        cbMovies.addActionListener(this);
        cbDate.addActionListener(this);

        setVisible(true);
    }


    public static void main() {
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
            movieShowTimes.setDate(cbDate.getSelectedIndex() != 0 ? CalendarUtils.convertMediumDateToYYMMDD.apply(cbDate.getSelectedItem().toString()) : "");
            populateTable();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {

        new ShowTimes().tableColumns().forEach(model::addColumn);

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            tableModel.populateTable(db.showMovieTimes(movieShowTimes).stream().map(ShowTimes::toShowTimeList).toList());

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }
    }


    private void populateShowDateComboBox() {
        cbDate.removeAllItems();
        cbDate.addItem(FormDetails.defaultShowDate.get());
        Set<String> uniqueDates = new LinkedHashSet<>();
        db.showMovieTimes(movieShowTimes).forEach(show -> uniqueDates.add(show.getDate()));
        uniqueDates.forEach(date -> cbDate.addItem(CalendarUtils.formatDate.apply(date)));


    }

    @Override
    public void navigation(JPanel top) {
        nav.addButtons(top);
    }


}


package forms;

import classes.*;
import enums.FormDetails;
import interfaces.MenuNavigation;
import interfaces.TableGUI;
import interfaces.TableProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public final class MovieList extends JFrame implements ActionListener, KeyListener, TableProperties, TableGUI, MenuNavigation {
    private final Database db;
    private final MovieGenres movieGenre = new MovieGenres();

    private final JTable table = new JTable();
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> cbGenres = new JComboBox<>();
    private final Navigation nav = new Navigation();
    private DefaultTableModel model;

    public MovieList() throws SQLException, FileNotFoundException {
        db = Database.getInstance();
        if (Helper.disableReceipt(db)) {
            nav.btnShowReceipt.setEnabled(false);
        }

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(table);
        setupTableProperties();

        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.movieList());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpMovieListInit();
        navigation(top);

        genreCB();


        populateTable();

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);

        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);


        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(cbGenres);


        JPanel south = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        south.add(scrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);


        cbGenres.addActionListener(this);
        autofocus();
        setVisible(true);


    }

    private void genreCB() {
        cbGenres.addItem(FormDetails.defaultGenre());
        db.getMovieGenreList().forEach(cbGenres::addItem);

    }


    public static void main(String[] args) throws SQLException, FileNotFoundException {
        new MovieList();

    }

    private void autofocus() {

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                txtMovieTitle.requestFocus();
            }
        });
    }

    private void setupTableProperties() {
        model = (DefaultTableModel) table.getModel();
        new MovieGenres().tableColumns().forEach(column -> model.addColumn(column));

    }


    private void setUpMovieListInit() {
        movieGenre.setGenre(FormDetails.defaultGenre());
        movieGenre.setTitle("");
        populateTable();

    }

    private void populateGenreComboBox() {

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cbGenres) {
            movieGenre.setGenre(Objects.requireNonNull(cbGenres.getSelectedItem()).toString());
            populateTable();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        movieGenre.setTitle(txtMovieTitle.getText());
        populateTable();

    }


    @Override
    public List<String> tableColumns() {
        return null;
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        new MovieGenres().tableColumns().forEach(i -> model.addColumn(i));
    }

    @Override
    public void populateTable() {
        clearTable(table);
        var movieList = db.showMovieList(movieGenre);
        final int movieSize = movieList.size();
        for (int i = 0; i < movieSize; i++) {
            MovieGenres movie = movieList.get(i);
            model.addRow(new Object[0]);
            model.setValueAt(movie.getTitle(), i, 0);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 1);
            model.setValueAt(movie.getRating(), i, 2);
            model.setValueAt(movie.getGenres(), i, 3);
        }


    }

    @Override
    public void navigation(JPanel top) {
        Arrays.stream(nav.navButtons()).forEach(top::add);
        Arrays.stream(nav.navButtons()).forEach(button -> button.addActionListener(this::navClick));
    }

    @Override
    public void navClick(ActionEvent e) {
        if (nav.handleNavClick(e)) {
            dispose();
        }
    }
}



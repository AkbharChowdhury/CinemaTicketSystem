package forms;

import classes.Database;
import classes.Navigation;
import classes.models.CustomTableModel;
import classes.models.MovieGenres;
import classes.utils.Helper;
import enums.FormDetails;
import interfaces.MenuNavigation;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;


public final class MovieList extends JFrame implements ActionListener, KeyListener, TableGUI, MenuNavigation {


    private final Database db = Database.getInstance();
    private final MovieGenres movieGenre = new MovieGenres();

    private final JTable table = new JTable();
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> cbGenres = new JComboBox<>();
    private final Navigation nav = new Navigation(this);
    private final DefaultTableModel model = (DefaultTableModel) table.getModel();

    private final CustomTableModel tableModel = new CustomTableModel(model);


    public MovieList() {
        table.setEnabled(false);

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
        populateTable();

        navigation(top);

        cbGenres.addItem(FormDetails.defaultGenre());
        db.getMovieGenreList().forEach(cbGenres::addItem);

        populateTable();


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


    public static void main(String[] args) {
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
        showColumn();
        displayWidths();

    }
    private void displayWidths() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(movieGenre.widths.get(i));
        }
        var cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
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
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        new MovieGenres().tableColumns().forEach(model::addColumn);
    }

    @Override
    public void populateTable() {

        clearTable(table);
        tableModel.populateTable(db.showMovieList(movieGenre).stream().map(MovieGenres::toMovieList).toList());
    }


    @Override
    public void navigation(JPanel top) {
        Arrays.stream(nav.navButtons()).forEach(top::add);

    }


}



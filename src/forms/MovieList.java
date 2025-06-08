package forms;

import classes.Database;
import classes.Navigation;
import classes.models.Counter;
import classes.models.CustomTableModel;
import classes.models.MovieGenres;
import classes.models.SearchMovie;
import classes.utils.Helper;
import enums.FormDetails;
import interfaces.MenuNavigation;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Supplier;


public final class MovieList extends JFrame implements ActionListener, TableGUI, MenuNavigation {


    private final Database db = Database.getInstance();
    private final SearchMovie movies = new SearchMovie(db.getMovies());
    private final JTable table = new JTable();
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> cbGenres = new JComboBox<>();
    private final Navigation nav = new Navigation(this);
    private final DefaultTableModel model = (DefaultTableModel) table.getModel();

    private final CustomTableModel tableModel = new CustomTableModel(model);


    public MovieList() {
        if (Helper.disableReceipt.apply(db)) nav.btnShowReceipt.setEnabled(false);

        new JScrollPane().setViewportView(table);
        setupTableProperties();


        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.movieList.get());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        navigation(top);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(cbGenres);
        JPanel south = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        south.add(scrollPane);

        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);


        cbGenres.addActionListener(this);
        autofocus();
        setVisible(true);

        cbGenres.addItem(FormDetails.defaultGenre.get());
        db.getMovieGenreList().forEach(cbGenres::addItem);
        populateTable();

        txtMovieTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                movies.setTitle(txtMovieTitle.getText());
                populateTable();
            }
        });

    }


    public static void main() {
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
        table.setEnabled(false);
        showColumn();
        displayWidths();

    }

    public static Supplier<LinkedHashMap<Integer, Integer>> TABLE_WIDTHS1 = () -> {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        Counter c = new Counter(true);
        LinkedHashMap<String, String> lhm
                = new LinkedHashMap<String, String>();

        // Add mappings to Map
        // using put() method
        lhm.put("3", "Geeks");
       lhm.get("");


        return map;
    };

//    private void displayWidths() {
//
//        LinkedHashMap<String, String> tableWidths = MovieGenres.TABLE_WIDTHS.get();
//        int tableColumnLength = table.getColumnCount();
//        for (int i = 0; i < tableColumnLength; i++) {
//            System.out.println(i);
//            int width = Integer.parseInt(tableWidths.get(String.valueOf(i)));
//            System.out.println(width);
//            table.getColumnModel().getColumn(i).setPreferredWidth(width);
//
//        }
//
//        var cellRenderer = new DefaultTableCellRenderer();
//        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
//        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
//    }


    private void displayWidths() {

        LinkedHashMap<Integer, Integer> tableWidths = MovieGenres.TABLE_WIDTHS.get();
//        int tableColumnLength = table.getColumnCount();
//        for (int i = 0; i < tableColumnLength; i++) {
//            int width = tableWidths.get(i);
//            table.getColumnModel().getColumn(i).setPreferredWidth(width);
//
//        }

        for (var tableData: tableWidths.entrySet()){
            int column = tableData.getKey();
            int width = tableData.getValue();
            table.getColumnModel().getColumn(column).setPreferredWidth(width);

        }



        var cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cbGenres) {
            movies.setGenre(Objects.requireNonNull(cbGenres.getSelectedItem()).toString());
            populateTable();
        }
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        MovieGenres.tableColumns().forEach(model::addColumn);
    }

    @Override
    public void
    populateTable() {
        clearTable(table);
        tableModel.populateTable(movies.filterResults().stream().map(MovieGenres::toMovieList).toList());
    }


    @Override
    public void navigation(JPanel top) {
        nav.addButtons(top);
    }


}



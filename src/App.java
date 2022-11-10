import classes.Database;
import classes.Helper;
import classes.MovieGenres;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;


public class App extends JFrame implements ActionListener, KeyListener {
    private static Database db;
    private DefaultTableModel model;

    private final MovieGenres movieGenre = new MovieGenres();
    private final JTable table = new JTable();
    private JScrollPane scrollPane = new JScrollPane();
    private DefaultTableCellRenderer cellRenderer;
    private String movieTitle = "";

    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");

    private final JTextField txtMovieID = new JTextField(2);
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> comboBoxGenres = new JComboBox<>();

    public App() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        scrollPane.setViewportView(table);
        setupTableProperties();

        txtMovieID.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle("Cinema Ticket Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpMovieListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


        populateGenreComboBox();

        getMovieList();
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(comboBoxGenres);

        JPanel south = new JPanel();
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);
        south.add(movieScrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        comboBoxGenres.addActionListener(this);

        setVisible(true);
    }

    private void setupTableProperties() {
        model = (DefaultTableModel)table.getModel();
        for (String column : MovieGenres.MovieGenreTableColumns()){
            model.addColumn(column);

        }

    }

    private void getMovieList() {
        var movieList = db.showMovieList(movieGenre);
        int i = 0;
        for(var movie: movieList) {
            model.addRow(new Object[0]);
            model.setValueAt(movie.getMovieID(), i, 0);
            model.setValueAt(movie.getTitle(), i, 1);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 2);
            model.setValueAt(movie.getRating(), i, 3);
            model.setValueAt( movie.getGenres(), i, 4);
            i++;
        }

    }

    private void setUpMovieListInit() {
        movieGenre.setGenreID(0);
        movieGenre.setTitle(movieTitle);
        getMovieList();

    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        new App();

    }



    private static void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }



    private void populateGenreComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        comboBoxGenres.addItem("Any Genre");
        for (var genre : db.getMovieGenreList()) {
            comboBoxGenres.addItem(genre);
        }

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnPurchaseTicket){

        }

        if (e.getSource() == btnListMovies){

        }

        if (e.getSource() == btnShowReceipt){

        }
        if (e.getSource() == btnShowTimes){

        }

        if (e.getSource() == comboBoxGenres) {
            System.out.println(comboBoxGenres.getSelectedIndex());
            movieGenre.setGenreID(comboBoxGenres.getSelectedIndex());
            filterMovieList();

        }
    }


    private void filterMovieList() {
        clearTable(table);
        getMovieList();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtMovieTitle) {
            movieGenre.setTitle(txtMovieTitle.getText());
            filterMovieList();

        }


        if (e.getSource() == txtMovieID) {
            char c = e.getKeyChar();
            if (Character.isLetter(c)) {
                // disable input if the value is not a number
                txtMovieID.setEditable(false);
            }

            boolean isNumber = !Character.isLetter(c);
            txtMovieID.setEditable(isNumber);

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

package forms;

import classes.Database;
import classes.Helper;
import classes.MovieGenres;
import enums.FormDetails;
import interfaces.FormAction;

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


public class ShowMovieTimes extends JFrame implements ActionListener, KeyListener, FormAction {
    private final Database db;
    private final MovieGenres movieGenre = new MovieGenres();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");
    private final JTextField txtMovieID = new JTextField(2);
    private final JTextField txtMovieTitle = new JTextField(20);
    private DefaultTableModel model;
    private final DefaultTableCellRenderer cellRenderer;
    private final String movieTitle = "";

    public ShowMovieTimes() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException {
        db = Database.getInstance();

        scrollPane.setViewportView(table);
        setupTableProperties();

        txtMovieID.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.showTimes());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpMovieListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


        getMovieList();
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Filter Date:"));
        middle.add(txtMovieTitle);

        JPanel south = new JPanel();
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);
        south.add(new Label("Spider man"));

        south.add(movieScrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new ShowMovieTimes();

    }

    private static void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    private void setupTableProperties() {
        model = (DefaultTableModel) table.getModel();
        for (String column : MovieGenres.MovieGenreTableColumns()) {
            model.addColumn(column);

        }

    }

    private void getMovieList() {
        var movieList = db.showMovieList(movieGenre);
        int i = 0;
        for (var movie : movieList) {
            model.addRow(new Object[0]);
            model.setValueAt(movie.getMovieID(), i, 0);
            model.setValueAt(movie.getTitle(), i, 1);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 2);
            model.setValueAt(movie.getRating(), i, 3);
            model.setValueAt(movie.getGenres(), i, 4);
            i++;
        }

    }

    private void setUpMovieListInit() {
        movieGenre.setGenreID(0);
        movieGenre.setTitle(movieTitle);
        getMovieList();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        handleButtonClick(e);


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


    @Override
    public void handleButtonClick(ActionEvent e) {
        if (e.getSource() == btnPurchaseTicket) {

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to purchase tickets", "Purchase ticket error");
                return;
            }
            try {
                new PurchaseTicket();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnListMovies) {
            try {
                new MovieList();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        if (e.getSource() == btnShowReceipt) {
            try {
                new ShowReceipt();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnShowTimes) {

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to view show times", "Movie show time error");
                return;
            }
            try {
                new ShowMovieTimes();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}

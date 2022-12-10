package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.FormAction;
import interfaces.TableGUI;
import interfaces.TableProperties;

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
import java.text.ParseException;
import java.util.List;


public class MovieList extends JFrame implements ActionListener, KeyListener, FormAction, TableProperties, TableGUI {
    private final Database db;
    private final MovieGenres movieGenre = new MovieGenres();

    private final JTable table = new JTable();
    JFrame frame = new JFrame();


    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());


    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> cbGenres = new JComboBox<>();
    private final String movieTitle = "";
    private DefaultTableModel model;

    public MovieList() throws  SQLException, FileNotFoundException {
        db = Database.getInstance();
        if (LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID())) {
            btnShowReceipt.setEnabled(false);
        }

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(table);
        setupTableProperties();

//        txtMovieTitle.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.movieList());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpMovieListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);

        populateGenreComboBox();

        populateTable();
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);

        table.getColumnModel().getColumn(3).setPreferredWidth(200);
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

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        cbGenres.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new MovieList();

    }


    private void setupTableProperties() {
        model = (DefaultTableModel) table.getModel();
        for (String column : new MovieGenres().tableColumns()) {
            model.addColumn(column);

        }

    }


    private void setUpMovieListInit() {
        movieGenre.setGenreID(0);
        movieGenre.setTitle(movieTitle);
        populateTable();

    }

    private void populateGenreComboBox() {
        cbGenres.addItem(FormDetails.defaultGenre());
        for (var genre : db.getMovieGenreList()) {
            cbGenres.addItem(genre);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (e.getSource() == cbGenres) {
            movieGenre.setGenreID(db.getGenreID(cbGenres.getSelectedItem().toString()));
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
        if (e.getSource() == txtMovieTitle) {
            movieGenre.setTitle(txtMovieTitle.getText());
            populateTable();

        }

    }


    @Override
    public void navigationMenu(ActionEvent e) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {


        if (e.getSource() == btnListMovies) {
            Helper.gotoForm(this, Pages.LIST_MOVIES);
        }
        if (e.getSource() == btnShowTimes) {
            Helper.gotoForm(this, Pages.SHOW_TIMES);
        }

        if (e.getSource() == btnPurchaseTicket) {
            if (LoginInfo.getCustomerID() == 0){
                LoginInfo.setHasOpenFormOnStartUp(true);
            }

            if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
                if (LoginInfo.getCustomerID() == 0){
//                    Helper.gotoForm(this, Pages.LOGIN);


                }
//                Helper.gotoForm(this, Pages.LOGIN);
                return;
            }

            Helper.gotoForm(this, Pages.PURCHASE_TICKET);

        }

        if (e.getSource() == btnShowReceipt) {
            LoginInfo.setHasOpenFormOnStartUp(true);
            Helper.gotoForm(this, Pages.SHOW_RECEIPT);
        }



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
        int i = 0;
        for (var movie : movieList) {
            model.addRow(new Object[0]);
            model.setValueAt(movie.getTitle(), i, 0);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 1);
            model.setValueAt(movie.getRating(), i, 2);
            model.setValueAt(movie.getGenres(), i, 3);
            i++;
        }


    }
}



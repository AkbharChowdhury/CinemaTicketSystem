import classes.Database;
import classes.Helper;
import classes.LoginInfo;
import classes.MovieGenres;
import tables.MovieTable;
import tables.SalesDetailsTable;
import tables.SalesTable;
import tables.TicketsTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;


public class App extends JFrame implements ActionListener, KeyListener {
    static Database db;
    private final DefaultListModel model;
    private final JList list; // the main list
    private MovieGenres movieGenre = new MovieGenres();
    String movieTitle = "";


    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");

    private final JTextField txtMovieID = new JTextField(2);
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> comboBoxGenres = new JComboBox<>();

    public App() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {


        model = new DefaultListModel();
        list = new JList(model);

        txtMovieID.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);

        setResizable(false);

        setLayout(new BorderLayout());
        setSize(700, 250);
        setTitle("Cinema Ticket Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);

        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);

        populateGenreComboBox();

        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(comboBoxGenres);

        JPanel south = new JPanel();

        setupMovieInit();

        JScrollPane movieScrollPane = new JScrollPane(list);
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

    private void setupMovieInit() throws SQLException {
        movieGenre.setGenreID(0);
        movieGenre.setTitle(movieTitle);
        getMovieList();

    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        new App();

    }

    private static void clearList(JList list) {
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();
    }

    private void getMovieList() throws SQLException {
        for (var invoice : db.getInvoice(1)) {
            model.addElement(MessageFormat.format("{0}, {1}",
                    Helper.formatDate(invoice.getSalesDate()),
                    invoice.getMovieTitle()

            ));
        }
//        var rs = db.getInvoice(1);
//
//        if (!rs.next()){
//            System.out.println("e");
//        }
//        do {
//            double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
//            int numTicket = rs.getInt(SalesDetailsTable.COLUMN_TOTAL_TICKETS_SOLD);
//            double total = numTicket * price;
//            model.addElement(
//                    MessageFormat.format("{0}, {1}, {2}",
//                            SalesTable.COLUMN_SALES_DATE,
//                            MovieTable.COLUMN_TITLE,
//                            Helper.formatMoney(total)
//
//                    ));
//            ;
//
//
//
//
//        } while (rs.last());


//        while (rs.next()){

    }

    private void populateGenreComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // add default value
        comboBoxGenres.addItem("Any Genre");
        for (var genre : db.getMovieGenreList()) {
            comboBoxGenres.addItem(genre);
        }

    }

    public void actionPerformed(ActionEvent e) {
        // combo
        if (e.getSource() == comboBoxGenres) {
            System.out.println(comboBoxGenres.getSelectedIndex());
            movieGenre.setGenreID(comboBoxGenres.getSelectedIndex());
            try {
                filterMovieList();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }



    private void filterMovieList() throws SQLException {
        clearList(list);
        getMovieList();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtMovieTitle) {
            movieGenre.setTitle(txtMovieTitle.getText());
            try {
                filterMovieList();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }


        // listen for movie id text-field
        if (e.getSource() == txtMovieID) {
            // link https://www.youtube.com/watch?v=cdPKsws5f-4
            char c = e.getKeyChar();
            if (Character.isLetter(c)) {
                // disable input if the value is not a number
                txtMovieID.setEditable(false);
            }

            boolean isNumber = !Character.isLetter(c);
            txtMovieID.setEditable(isNumber);

        }

//        if (e.getSource() == txtMovieTitle) {
//            boolean isSearchable = !txtMovieTitle.getText().isEmpty();
//            if (!isSearchable) {
//                filterMovieList(false);
//            } else {
//                String title = txtMovieTitle.getText();
//                System.out.println("title is " + title);
//                filterMovieList(true, title);
//            }
//
//        }
//        // listen for movie id text-field
//        if (e.getSource() == txtMovieID) {
//            // link https://www.youtube.com/watch?v=cdPKsws5f-4
//            char c = e.getKeyChar();
//            if (Character.isLetter(c)) {
//                // disable input if the value is not a number
//                txtMovieID.setEditable(false);
//            }
//
//            boolean isNumber = !Character.isLetter(c);
//            txtMovieID.setEditable(isNumber);
//
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }

}

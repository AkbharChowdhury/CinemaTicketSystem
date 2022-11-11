package forms;

import classes.*;
import enums.FormDetails;
import interfaces.FormAction;
import interfaces.TableGUI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;


public class PurchaseTicket extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI, ChangeListener {
    private final Database db;
    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");
    private final JButton btnAddTicket = new JButton("Add Ticket");
    private final JButton btnRemoveTicket = new JButton("Remove Ticket");
    private final JButton btnConfirm = new JButton("Confirm Order");

    private final List<Ticket> TICKETS_LIST;

    JLabel lblTicket = new JLabel("Ticket ");
    private final JComboBox<String> cbTicket = new JComboBox<>();


    private final JTextField txtMovieID = new JTextField(2);

    JTable tblShowTimes  = new JTable();
    JTable tblBill  = new JTable();

    JLabel lblMovieDetails = new JLabel();
    JLabel lblTotal = new JLabel("Total to pay: ");


    private final JComboBox<String> cbMovies = new JComboBox<>();

    private final DefaultTableCellRenderer cellRenderer;

    JLabel movieTitle = new JLabel();


    JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(0, 0, 8, 1));
    private DefaultTableModel model;


    public PurchaseTicket() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        // set movie title label style
        db = Database.getInstance();
        TICKETS_LIST = db.getTicket();

        movieTitle.setFont(new Font("Arial", Font.BOLD, 20));
        movieTitle.setText(db.getMovieName(MovieInfo.getMovieID()));


        scrollPane.setViewportView(table);
        showColumn();

        txtMovieID.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.purchaseTicket());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpTimesListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


        populateTable();

//        table.getColumnModel().getColumn(0).setPreferredWidth(5);
//        table.getColumnModel().getColumn(1).setPreferredWidth(150);
//
//        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        populateMovieComboBox();
        init();

        middle.add(cbMovies);
        lblMovieDetails.setText(getSelectedMovieTitle());
        middle.add(lblMovieDetails);

        JScrollPane showTimesShowPane = new JScrollPane(tblShowTimes);
        JScrollPane billScrollPane = new JScrollPane(tblBill);



        middle.add(showTimesShowPane);
        middle.add(billScrollPane);

//        movieTitle.setText(db.getMovieName(MovieInfo.getMovieID()));
//        middle.add(movieTitle);


        JPanel south = new JPanel();



        south.add(lblTicket);
        populateTicketComboBox();
        south.add(cbTicket);
        south.add(spNumTickets);
        south.add(btnAddTicket);
        south.add(btnRemoveTicket);
        south.add(btnConfirm);
        south.add(lblTotal);





        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        cbMovies.addActionListener(this);
        spNumTickets.addChangeListener(this);

        setVisible(true);
    }

    private void init() {
//        int selectedMovieID = cbMovies.getSelectedIndex() + 1;
//        cbMovies.setSelectedIndex();
        // combo box start from 0 index so if the first movie is selected set the default option
        if (MovieInfo.getMovieID() == 1){
            cbMovies.setSelectedIndex(0);
            return;
        }
        int selectedMovieID = MovieInfo.getMovieID() -1;

        cbMovies.setSelectedIndex(selectedMovieID);

//       JOptionPane.showMessageDialog(null,  MovieInfo.getMovieID());
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new PurchaseTicket();

    }


    private void setUpTimesListInit() throws ParseException {
        txtMovieID.setText(String.valueOf(MovieInfo.getMovieID()));
        movieShowTimes.setMovieId(MovieInfo.getMovieID());
        movieShowTimes.setShowDate("");

        populateTable();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        handleButtonClick(e);
        // update movie label (title and date and time
        if (e.getSource() == cbMovies){
            updateMovieTitle();

        }



    }
    private void updateMovieTitle(){
        lblMovieDetails.setText(getSelectedMovieTitle());

    }
    private String getSelectedMovieTitle(){
        return String.valueOf(cbMovies.getSelectedItem());
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == spNumTickets){
            System.out.println("PK");
        }



        if (e.getSource() == txtMovieID) {
            Helper.validateNumber(e, txtMovieID);



            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int movieID = Integer.parseInt(txtMovieID.getText());
                String title = db.getMovieName(Integer.parseInt(txtMovieID.getText()));
                movieTitle.setText(title);
                movieShowTimes.setMovieId(movieID);


                populateTable();


            }

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


        }

        if (e.getSource() == btnShowTimes) {

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to view show times", "Movie show time error");
                return;
            }
            movieShowTimes.setMovieId(Integer.parseInt(txtMovieID.getText()));
            populateTable();
            String title = db.getMovieName(Integer.parseInt(txtMovieID.getText()));
            movieTitle.setText(title);

        }
    }

    @Override
    public void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new MovieShowTimes().tableColumns().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            var showTimesList = db.showMovieTimes(movieShowTimes);
            int i = 0;
            for (var showTime : showTimesList) {
                model.addRow(new Object[0]);
                model.setValueAt(showTime.getShowDate(), i, 0);
                model.setValueAt(Helper.formatTime(showTime.getShowTime()), i, 1);
                model.setValueAt(showTime.getNumTicketLeft(), i, 2);
                i++;
            }

        } catch (ParseException e){
            e.printStackTrace();

        }


    }


    private void populateTicketComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // add default value
        for (var ticket : TICKETS_LIST) {
            String ticketStr = MessageFormat.format("{0} {1}",ticket.getType(), Helper.formatMoney(ticket.getPrice()));
            cbTicket.addItem(ticketStr);
        }

    }

    private void populateMovieComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // add default value
        for (var movie : db.getMovieTitle()) {
            cbMovies.addItem(movie);
        }

    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets){
            System.out.println(spNumTickets.getValue());


        }

    }
}

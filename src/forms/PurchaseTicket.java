package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.FormAction;
import interfaces.TableGUI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;

public class PurchaseTicket extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI, ChangeListener, MouseListener {
    final String TOTAL_MSG = "Total to pay: ";
    private  Database db;
    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());
    private final JLabel lblMovieDetails = new JLabel();
    private  Ticket TICKET_DETAILS;
    private  DefaultTableCellRenderer cellRenderer;
    private final JComboBox<String> cbMovies = new JComboBox<>();
    private final JLabel lblTicket = new JLabel();
    private final JLabel lblTotal = new JLabel();
    private final JButton btnConfirm = new JButton("Confirm Order");
    private final JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
    private int movieIDIndex;
    private int selectedShowTimeID;
    private DefaultTableModel model;

    public PurchaseTicket() throws  SQLException, FileNotFoundException, ParseException {
        Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET);
        db = Database.getInstance();
        movieShowTimes.setDate("");
        TICKET_DETAILS = db.getCustomerTicketType(LoginInfo.getCustomerID());


        scrollPane.setViewportView(table);
        showColumn();

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.purchaseTicket());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);

        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Select Movies: "));
        cbMovies.addItem("Select Movie");
        populateMovieComboBox();
        middle.add(cbMovies);
        middle.add(lblMovieDetails);
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);
        middle.add(movieScrollPane);


        JPanel south = new JPanel();

        showTicketPricesLabel();
        south.add(lblTicket);
        south.add(spNumTickets);

        south.add(lblTotal);
        updateTotalLabel();

        south.add(btnConfirm);
        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        cbMovies.addActionListener(this);
        btnConfirm.addActionListener(this);
        spNumTickets.addChangeListener(this);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleTableClickEvent();


            }

            private void handleTableClickEvent() {
                try {
                    selectedShowTimeID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
                    int selectedMovieID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 1).toString());
                    var showDetails = db.getSelectedShowDetails(new MovieShowTimes(selectedMovieID, selectedShowTimeID));
                    updateMovieLabel(db.getMovieName(getMovieID()),
                            Helper.formatDate(showDetails.getDate()),
                            Helper.formatTime(showDetails.getTime())
                    );
                } catch (ParseException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });


        setVisible(true);
    }


    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new PurchaseTicket();

    }

    private void showTicketPricesLabel() {
        String output = MessageFormat.format("Ticket: {0} ({1})",
                TICKET_DETAILS.getType(),
                Helper.formatMoney(TICKET_DETAILS.getPrice())
        );
        lblTicket.setText(output);


    }

    private void updateTotalLabel() {
        double ticketPrice = TICKET_DETAILS.getPrice();
        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        lblTotal.setText(TOTAL_MSG + Helper.formatMoney(Helper.calcPrice(numTickets, ticketPrice)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showNavigation(e);


        try {
            if (e.getSource() == cbMovies) {
                handleMovieComboBox();
            }
            if (e.getSource() == btnConfirm) {
                handlePurchase();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showNavigation(ActionEvent e) {
        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void handleMovieComboBox() {

        String movieTitle = cbMovies.getSelectedItem().toString();
        int movieID = cbMovies.getSelectedIndex();

        if (movieID == 0) {
            Helper.showErrorMessage("Please select a movie", "Movie Error");
            cbMovies.setSelectedIndex(movieIDIndex);
            return;

        }
        movieShowTimes.setMovieId(db.getMovieID(movieTitle));
        String title = db.getMovieName(movieID);
        lblMovieDetails.setText(title);
        populateTable();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


    }

    @Override
    public void keyReleased(KeyEvent e) {

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
            if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
                Helper.gotoForm(this, Pages.LOGIN);
                return;
            }
            Helper.gotoForm(this, Pages.PURCHASE_TICKET);

        }

        if (e.getSource() == btnShowReceipt) {
            if (!Helper.isCustomerLoggedIn(this, RedirectPage.SHOW_RECEIPT)) {
                Helper.gotoForm(this, Pages.LOGIN);
                return;
            }

            Helper.gotoForm(this, Pages.SHOW_RECEIPT);

        }
    }

    private void handlePurchase() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (selectedShowTimeID == 0) {
            Helper.showErrorMessage("Please select a show time from the table", "Show time required");
            return;
        }

        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        int customerID = LoginInfo.getCustomerID();
        String salesDate = LocalDate.now().toString();
        int movieID = getMovieID();

        if (!this.isValidNumTickets(movieID, numTickets)) {
            return;
        }

        Sales sales = new Sales(salesDate, movieID, selectedShowTimeID, customerID, numTickets);

        if (db.addSales(sales)) {
            updateNumTicksSold(numTickets);
            Helper.message("Thank you for your purchase. you will now be redirected to the receipt page");
            Helper.gotoForm(this, Pages.SHOW_RECEIPT);

        }
    }


    private void updateNumTicksSold(int numTickets) {
        MovieShowTimes validateShowTimes = new MovieShowTimes();
        validateShowTimes.setMovieId(getMovieID());
        validateShowTimes.setShowTimeId(selectedShowTimeID);
        validateShowTimes.setNumTicketsSold(numTickets);

        db.updateNumTickets(validateShowTimes);
    }

    private boolean isValidNumTickets(int movieID, int numTickets) {
        MovieShowTimes validateShowTimes = new MovieShowTimes();
        validateShowTimes.setMovieId(movieID);
        validateShowTimes.setShowTimeId(selectedShowTimeID);
        validateShowTimes.setNumTicketsSold(numTickets);

        if (!Validation.isValidNumTicketsSold(db, validateShowTimes)) {
            int numTicketsLeft = db.getNumTickets(validateShowTimes);
            Helper.showErrorMessage("Ypu cannot exceed above " + numTicketsLeft + " tickets", "Ticket Quantity Error");
            return false;
        }
        return true;
    }

    private int getMovieID() {
        return db.getMovieID(cbMovies.getSelectedItem().toString());
    }


    @Override
    public void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new MovieShowTimes().tableColumnsWithID().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            var showTimesList = db.showMovieTimes(movieShowTimes);
            int i = 0;
            for (var showTime : showTimesList) {
                model.addRow(new Object[0]);
                model.setValueAt(showTime.getShowTimeID(), i, 0);
                model.setValueAt(showTime.getMovieID(), i, 1);


                model.setValueAt(showTime.getDate(), i, 2);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 3);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 4);
                i++;
            }


        } catch (ParseException e) {
            e.printStackTrace();

        }


    }

    private void populateMovieComboBox() {

        for (var movie : db.getAllMovieShowTimes()) {
            cbMovies.addItem(movie.getTitle());
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) {
            updateTotalLabel();

        }

    }

    private void updateMovieLabel(String title, String date, String time) {
        lblMovieDetails.setText(String.format("%s- %s:%s", title, date, time));
    }


}

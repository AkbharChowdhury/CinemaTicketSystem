package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.FormAction;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


public class PurchaseTicket extends JFrame implements ActionListener, FormAction, TableGUI, ChangeListener {
    final String TOTAL_MSG = "Total to pay: ";

    private final Database db;
    private final ShowTimes movieShowTimes = new ShowTimes();
    private final JTable table = new JTable();
    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());
    private final JButton btnConfirm = new JButton("Confirm Order");
    private final JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));

    private final JComboBox<String> cbMovies = new JComboBox<>();

    private final JLabel lblMovieDetails = new JLabel();


    private Ticket TICKET_DETAILS;

    private final JLabel lblTicket = new JLabel();
    private final JLabel lblTotal = new JLabel();

    private DefaultTableModel model;

    private boolean hasSelectedMovie = false;

    private int selectedShowTimeID;



    public PurchaseTicket() throws SQLException, FileNotFoundException {
        db = Database.getInstance();
        movieShowTimes.setDate("");
        TICKET_DETAILS = db.getCustomerTicketType(LoginInfo.getCustomerID());
        lblMovieDetails.setFont(new Font("Calibri", Font.BOLD, 15));


        JScrollPane scrollPane = new JScrollPane();
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

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        cbMovies.addItem(FormDetails.defaultMovie());
        populateMovieComboBox();
        middle.add(cbMovies);


        middle.add(lblMovieDetails);
        middle.add(new JScrollPane(scrollPane));


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
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleTableClickEvent();


            }

            private void handleTableClickEvent() {
                try {
                    String id = model.getValueAt(table.getSelectedRow(), 0).toString();
                    String date = model.getValueAt(table.getSelectedRow(), 1).toString();
                    String time = model.getValueAt(table.getSelectedRow(), 2).toString();
                    String numTickets = model.getValueAt(table.getSelectedRow(), 3).toString();
                    selectedShowTimeID = Integer.parseInt(id);
                    System.out.println(selectedShowTimeID);

                    int movieID = db.getMovieID(cbMovies.getSelectedItem().toString());

                    lblMovieDetails.setText(String.format("%s-%s: %s", db.getMovieName(movieID), date, time));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        try {
            navigationMenu(e);
            if (e.getSource() == btnConfirm) {
                handlePurchase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (e.getSource() == cbMovies) {
            handleMovieCB();
        }


    }

    private void handleMovieCB() {
        if (!hasSelectedMovie) {
            cbMovies.removeItemAt(0);
            hasSelectedMovie = true;

        }

        movieShowTimes.setMovieID(db.getMovieID(cbMovies.getSelectedItem().toString()));
        populateTable();
    }
    private void updateNumTicksSold(int numTickets) {
        var updater = new ShowTimes();
//        validateShowTimes.setMovieId(getMovieID());
        updater.setShowTimeID(selectedShowTimeID);
        updater.setNumTicketsSold(numTickets);

        db.updateNumTickets(updater);
    }


    private void handlePurchase() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (selectedShowTimeID == 0) {
            Helper.showErrorMessage("Please select a show time from the table", "Show time required");
            return;
        }

        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        int customerID = LoginInfo.getCustomerID();
        String salesDate = LocalDate.now().toString();

        if (!isValidNumTickets(numTickets)) {
            return;
        }

        var sales = new SalesNew(selectedShowTimeID, customerID, salesDate, numTickets);


        if (db.addSales(sales)) {
            updateNumTicksSold(numTickets);
            Helper.message("Thank you for your purchase. you will now be redirected to the receipt page");
            Helper.gotoForm(this, Pages.SHOW_RECEIPT);

        }
    }


    private boolean isValidNumTickets(int numTickets) {
        ShowTimes validateShowTimes = new ShowTimes();
        validateShowTimes.setShowTimeID(selectedShowTimeID);
        validateShowTimes.setNumTicketsSold(numTickets);

        if (!Validation.isValidNumTicketsSold(db, validateShowTimes)) {
            int numTicketsLeft = db.getNumTickets(validateShowTimes);
            String errorMessage = numTicketsLeft == 1 ? "There is only one ticket left to purchase":"You cannot exceed above " + numTicketsLeft + " tickets";
            Helper.showErrorMessage(errorMessage, "Ticket Quantity Error");

            return false;
        }
        return true;
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

    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new ShowTimes().tableColumnsWithID().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            int i = 0;
            for (var showTime : db.showMovieTimes(movieShowTimes)) {
                model.addRow(new Object[0]);
                model.setValueAt(showTime.getShowTimeID(), i, 0);
                model.setValueAt(Helper.formatDate(showTime.getDate()), i, 1);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 2);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 3);
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
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) {
            updateTotalLabel();

        }

    }

    private void updateMovieLabel(String title, String date, String time) {
        lblMovieDetails.setText(String.format("%s- %s:%s", title, date, time));
    }

}


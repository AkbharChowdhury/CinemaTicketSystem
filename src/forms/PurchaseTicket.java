package forms;

import classes.Database;
import classes.Form;
import classes.LoginInfo;
import classes.Navigation;
import classes.models.*;
import classes.utils.Helper;
import classes.utils.Validation;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.MenuNavigation;
import interfaces.TableGUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static classes.utils.Helper.fieldSep;

public final class PurchaseTicket extends JFrame implements ActionListener, TableGUI, ChangeListener, MenuNavigation {
    private final Navigation nav = new Navigation(this);
    private final Database db = Database.getInstance();
    private final ShowTimes movieShowTimes = new ShowTimes();
    private final JTable table = new JTable();

    private final JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
    private final JComboBox<String> cbMovies = new JComboBox<>();

    private final JLabel lblMovieDetails = new JLabel();
    private final JLabel lblTotal = new JLabel();
    private Ticket ticketDetails;
    private final DefaultTableModel model = (DefaultTableModel) table.getModel();

    private final CustomTableModel tableModel = new CustomTableModel(model);

    private List<ShowTimes> list;


    public PurchaseTicket() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (!Customer.isLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
            return;
        }


        if (Helper.disableReceipt.apply(db)) {
            nav.btnShowReceipt.setEnabled(false);
        }

        Helper.disableSpinnerInput(spNumTickets);

        ticketDetails = db.getCustomerTicketType(LoginInfo.getCustomerID());
        lblMovieDetails.setFont(new Font("Calibri", Font.BOLD, 15));


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        showColumn();

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.purchaseTicket.get());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        navigation(top);


        CustomTableModel.setFirstColumnAlignment(table, JLabel.LEFT);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        cbMovies.addItem(FormDetails.defaultMovie.get());
        db.getAllMovieShowTimes().forEach(movie -> cbMovies.addItem(movie.getTitle()));
        middle.add(cbMovies);


        middle.add(lblMovieDetails);
        middle.add(new JScrollPane(scrollPane));

        JButton btnConfirm = new JButton("Confirm Order");

        JPanel south = new JPanel();

        south.add(new JLabel(STR."Ticket: \{ticketDetails.getType()} (\{Helper.formatMoney.apply(ticketDetails.getPrice())})"));
        south.add(spNumTickets);
        south.add(btnConfirm);
        south.add(lblTotal);

        updateTotalLabel();
        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        cbMovies.addActionListener(this);
        spNumTickets.addChangeListener(this);
        btnConfirm.addActionListener(this);
        setVisible(true);
        tableEvent();
    }

    private void tableEvent() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    ShowTimes showTime = list.get(table.getSelectedRow());
                    lblMovieDetails.setText(STR."\{db.getMovieName(showTime.getMovieID())}- \{showTime.getDate()}: \{showTime.getTime()}");
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

    }


    public static void main() {
        try {
            new PurchaseTicket();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void updateTotalLabel() {
        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        lblTotal.setText(STR."Total to pay: \{Helper.formatMoney.apply(numTickets * ticketDetails.getPrice())}");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cbMovies) {
            handleMovieCB();
            return;
        }
        handlePurchase();
    }

    private void handleMovieCB() {
        Movie.movieComboBoxStatus(cbMovies);
        movieShowTimes.setMovieID(db.getMovieID(cbMovies.getSelectedItem().toString()));
        populateTable();
    }


    private boolean updateNumTicksSold(int numTickets) {
        var updater = new ShowTimes();
        updater.setShowTimeID(getSelectedShowTimeID());
        updater.setNumTicketsSold(numTickets);
        return db.updateNumTickets(updater);
    }


    private void handlePurchase() {

        if (table.getSelectedRow() == -1) {
            Helper.showErrorMessage("Please select a show time from the table", "Show time required");
            return;
        }

        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        int customerID = LoginInfo.getCustomerID();
        String salesDate = LocalDate.now().toString();
        int selectedShowTimeID = getSelectedShowTimeID();

        if (!isValidNumTickets(numTickets)) return;

        var sales = new Sales(selectedShowTimeID, customerID, salesDate, numTickets);
        if (db.salesExists(sales)) {
            Helper.showErrorMessage("You have already booked this show time", "booking error");
            return;
        }

        if (db.addSales(sales) && updateNumTicksSold(numTickets)) {
            Helper.message.accept("Thank you for your purchase. you will now be redirected to the receipt page");
            Form.gotoForm(this, Pages.SHOW_RECEIPT);
        }

    }


    private boolean isValidNumTickets(int numTickets) {
        ShowTimes validateShowTimes = new ShowTimes();
        validateShowTimes.setShowTimeID(getSelectedShowTimeID());
        validateShowTimes.setNumTicketsSold(numTickets);
        if (!Validation.isValidNumTicketsSold(db, validateShowTimes)) {
            int numTicketsLeft = db.getNumTickets(validateShowTimes);
            String errorMessage = numTicketsLeft == 1 ? "There is only one ticket left to purchase" : STR."You cannot exceed above \{numTicketsLeft} tickets";
            Helper.showErrorMessage(errorMessage, "Ticket Quantity Error");
            return false;
        }

        return true;
    }


    @Override
    public void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
    }

    @Override
    public void showColumn() {

        var list = new ShowTimes().tableColumns();
        list.addFirst("Show ID");
        list.forEach(model::addColumn);

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            tableModel.populateTable(db.showMovieTimes(movieShowTimes).stream().map(PurchaseTicket::setField).toList());
            list = db.showMovieTimes(movieShowTimes);


        } catch (IndexOutOfBoundsException _) {

        }

    }

    private static String setField(ShowTimes showTime) {

        return STR."""
                      \{fieldSep(showTime.getShowTimeID())}
                        \{ShowTimes.toShowTimeList(showTime)}
                """
                ;


    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) updateTotalLabel();
    }


    @Override
    public void navigation(JPanel top) {
        nav.addButtons(top);
    }


    private int getSelectedShowTimeID() {
        return list.get(table.getSelectedRow()).getShowTimeID();
    }
}


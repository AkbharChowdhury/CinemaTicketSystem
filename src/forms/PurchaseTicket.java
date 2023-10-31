package forms;

import classes.Database;
import classes.LoginInfo;
import classes.Navigation;
import classes.models.*;
import classes.utils.Helper;
import classes.utils.Validation;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.MenuNavigation;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;

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
    ;
    private final CustomTableModel tableModel = new CustomTableModel(model);
    ;

    public PurchaseTicket() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
            return;
        }


        if (Helper.disableReceipt(db)) {
            nav.btnShowReceipt.setEnabled(false);
        }


        disableSpinnerInput();

        ticketDetails = db.getCustomerTicketType(LoginInfo.getCustomerID());
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
        navigation(top);


        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel middle = new JPanel();
        middle.add(new Label("Movie: "));
        cbMovies.addItem(FormDetails.defaultMovie());
        db.getAllMovieShowTimes().forEach(movie -> cbMovies.addItem(movie.getTitle()));
        middle.add(cbMovies);


        middle.add(lblMovieDetails);
        middle.add(new JScrollPane(scrollPane));


        JPanel south = new JPanel();

        south.add(new JLabel(STR. "Ticket: \{ ticketDetails.getType() } (\{ Helper.formatMoney(ticketDetails.getPrice()) })" ));
        south.add(spNumTickets);

        south.add(lblTotal);
        updateTotalLabel();

        JButton btnConfirm = new JButton(Buttons.confirmOrder());
        south.add(btnConfirm);


        add("North", top);
        add("Center", middle);
        add("South", south);

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
                    var c = new Counter();
                    String date = model.getValueAt(table.getSelectedRow(), c.getCounter()).toString();
                    String time = model.getValueAt(table.getSelectedRow(), c.getCounter()).toString();
                    int movieID = db.getMovieID(cbMovies.getSelectedItem().toString());
                    lblMovieDetails.setText(STR. "\{ db.getMovieName(movieID) }- \{ date }: \{ time }" );
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
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


    public static void main(String[] args) {
        try {
            new PurchaseTicket();
        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

    }

    private void disableSpinnerInput() {
        var editor = ((JSpinner.DefaultEditor) spNumTickets.getEditor()).getTextField();
        editor.setEnabled(true);
        editor.setEditable(false);
    }


    private void updateTotalLabel() {
        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        lblTotal.setText(STR. "Total to pay: \{ Helper.formatMoney(Helper.calcPrice(numTickets, ticketDetails.getPrice())) }" );
    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == cbMovies) {
            handleMovieCB();
            return;
        }
        try {
            handlePurchase();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }


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


    private void handlePurchase() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

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
        if (db.SalesExists(sales)) {
            Helper.showErrorMessage("You have already booked this show time", "booking error");
            return;
        }

        if (db.addSales(sales) && updateNumTicksSold(numTickets)) {
            Helper.message("Thank you for your purchase. you will now be redirected to the receipt page");
            Helper.gotoForm(this, Pages.SHOW_RECEIPT);
            return;
        }
        System.err.println("There was an error updating the number of tickets remaining");

    }


    private boolean isValidNumTickets(int numTickets) {
        ShowTimes validateShowTimes = new ShowTimes();
        validateShowTimes.setShowTimeID(getSelectedShowTimeID());
        validateShowTimes.setNumTicketsSold(numTickets);

        if (!Validation.isValidNumTicketsSold(db, validateShowTimes)) {
            int numTicketsLeft = db.getNumTickets(validateShowTimes);
            String errorMessage = numTicketsLeft == 1 ? "There is only one ticket left to purchase" : STR. "You cannot exceed above \{ numTicketsLeft } tickets" ;
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

        } catch (IndexOutOfBoundsException _) {

        }

    }

    private static String setField(ShowTimes showTime) {

        return STR. """
                      \{ fieldSep(showTime.getShowTimeID()) }
                        \{ ShowTimes.toShowTimeList(showTime) }
                """
                ;


    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) updateTotalLabel();
    }


    @Override
    public void navigation(JPanel top) {
        Arrays.stream(nav.navButtons()).forEach(top::add);
    }


    private int getSelectedShowTimeID() {
        return Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString().trim());
    }
}


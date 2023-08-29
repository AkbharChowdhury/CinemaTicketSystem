package forms;

import classes.*;
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
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;


public final class PurchaseTicket extends JFrame implements ActionListener, TableGUI, ChangeListener, MenuNavigation {
    Navigation nav = new Navigation();
    String TOTAL_MSG = "Total to pay: ";
    SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 8, 1);
    Database db = Database.getInstance();
    ShowTimes movieShowTimes = new ShowTimes();
    JTable table = new JTable();

    JButton btnConfirm = new JButton(Buttons.confirmOrder());
    JSpinner spNumTickets = new JSpinner(spinnerModel);


    JComboBox<String> cbMovies = new JComboBox<>();

    JLabel lblMovieDetails = new JLabel();
    JLabel lblTicket = new JLabel();
    JLabel lblTotal = new JLabel();
    Ticket ticketDetails;
    DefaultTableModel model;
    boolean hasSelectedMovie = false;
    int selectedShowTimeID;


    public PurchaseTicket() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
            return;
        }


        if (Helper.disableReceipt(db)) {
            nav.btnShowReceipt.setEnabled(false);
        }

        disableSpinnerInput();

        movieShowTimes.setDate("");
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
                    selectedShowTimeID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
                    String date = model.getValueAt(table.getSelectedRow(), 1).toString();
                    String time = model.getValueAt(table.getSelectedRow(), 2).toString();
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


    public static void main(String[] args) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new PurchaseTicket();

    }

    private void disableSpinnerInput() {
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spNumTickets.getEditor();
        editor.getTextField().setEnabled(true);
        editor.getTextField().setEditable(false);
    }

    private void showTicketPricesLabel() {
        String output = MessageFormat.format("Ticket: {0} ({1})",
                ticketDetails.getType(),
                Helper.formatMoney(ticketDetails.getPrice())
        );

        lblTicket.setText(output);

    }

    private void updateTotalLabel() {
        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        lblTotal.setText(TOTAL_MSG + Helper.formatMoney(Helper.calcPrice(numTickets, ticketDetails.getPrice())));
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
            ex.printStackTrace();
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

    private boolean updateNumTicksSold(int numTickets) {
        var updater = new ShowTimes();
        updater.setShowTimeID(selectedShowTimeID);
        updater.setNumTicketsSold(numTickets);
        return db.updateNumTickets(updater);
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

        var sales = new Sales(selectedShowTimeID, customerID, salesDate, numTickets);
        if (db.SalesExists(sales)) {
            Helper.showErrorMessage("You have already booked this show time", "booking error");
            return;
        }

        if (db.addSales(sales)) {
            Helper.message("Thank you for your purchase. you will now be redirected to the receipt page");
            if (updateNumTicksSold(numTickets)) {
                Helper.gotoForm(this, Pages.SHOW_RECEIPT);
                return;
            }
            System.err.println("There was an error updating the number of tickets remaining");


        }
    }


    private boolean isValidNumTickets(int numTickets) {
        ShowTimes validateShowTimes = new ShowTimes();
        validateShowTimes.setShowTimeID(selectedShowTimeID);
        validateShowTimes.setNumTicketsSold(numTickets);

        if (!Validation.isValidNumTicketsSold(db, validateShowTimes)) {
            int numTicketsLeft = db.getNumTickets(validateShowTimes);
            String errorMessage = numTicketsLeft == 1 ? "There is only one ticket left to purchase" : "You cannot exceed above " + numTicketsLeft + " tickets";
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
        model = (DefaultTableModel) table.getModel();
        new ShowTimes().tableColumnsWithID().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            var list = db.showMovieTimes(movieShowTimes);
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                ShowTimes showTime = list.get(i);
                model.addRow(new Object[0]);
                model.setValueAt(showTime.getShowTimeID(), i, 0);
                model.setValueAt(Helper.formatDate(showTime.getDate()), i, 1);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 2);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 3);

            }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void populateMovieComboBox() {
        db.getAllMovieShowTimes().forEach(movie -> cbMovies.addItem(movie.getTitle()));

    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) updateTotalLabel();


    }


    @Override
    public void navigation(JPanel top) {
        Arrays.stream(nav.navButtons()).forEach(top::add);
        Arrays.stream(nav.navButtons()).forEach(button -> button.addActionListener(this::navClick));
    }

    @Override
    public void navClick(ActionEvent e) {
        if (nav.handleNavClick(e)) {
            dispose();
        }
    }
}


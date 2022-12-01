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

    private final JLabel lblMovieDetails = new JLabel("d");


    private  Ticket TICKET_DETAILS;

    private final JLabel lblTicket = new JLabel();
    private final JLabel lblTotal = new JLabel();



    private DefaultTableModel model;


    public PurchaseTicket() throws SQLException, FileNotFoundException {
        db = Database.getInstance();
        movieShowTimes.setDate("");
        TICKET_DETAILS = db.getCustomerTicketType(LoginInfo.getCustomerID());


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
                    String s = model.getValueAt(table.getSelectedRow(), 0).toString();
                    System.out.println(s);
//                    selectedShowTimeID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
//                    int selectedMovieID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 1).toString());
//                    var showDetails = db.getSelectedShowDetails(new MovieShowTimes(selectedMovieID, selectedShowTimeID));
//                    updateMovieLabel(db.getMovieName(getMovieID()),
//                            Helper.formatDate(showDetails.getDate()),
//                            Helper.formatTime(showDetails.getTime())
//                    );
                } catch (Exception ex){
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
    public void actionPerformed(ActionEvent e){
        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



        if (e.getSource() == cbMovies) {
            if (cbMovies.getSelectedIndex() == 0) {
                Helper.showErrorMessage("Please select a movie", "Movie Error");
                return;

            }

            movieShowTimes.setMovieID(db.getMovieID(cbMovies.getSelectedItem().toString()));
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
        new MovieShowTimes().tableColumns().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            int i = 0;
            for (var showTime : db.showMovieTimes(movieShowTimes)) {
                model.addRow(new Object[0]);
                model.setValueAt(Helper.formatDate(showTime.getDate()), i, 0);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 1);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 2);
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


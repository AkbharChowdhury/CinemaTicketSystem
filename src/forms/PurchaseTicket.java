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


public class PurchaseTicket extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI, ChangeListener, MouseListener
{
    private final Database db;
    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private int movieIDIndex;
    int selectedShowTimeID;


    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());

    private final JLabel lblMovieDetails = new JLabel();

    private final Ticket TICKET_DETAILS;

    final String TOTAL_MSG = "Total to pay: ";

    private final DefaultTableCellRenderer cellRenderer;

    private DefaultTableModel model;
    private final JComboBox<String> cbMovies = new JComboBox<>();
    private final JLabel lblTicket = new JLabel();
    private final JLabel lblTotal = new JLabel();
    private final JButton btnConfirm = new JButton("Confirm Order");

    private JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));;

    public PurchaseTicket() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        db = Database.getInstance();
        movieShowTimes.setShowDate("");
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


//        middle.add(txtShowDate);


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

              try {
                  int row = table.rowAtPoint(e.getPoint());
                  int col = table.columnAtPoint(e.getPoint());

                  var showTimeIDStr1  = model.getValueAt(table.getSelectedRow(), 0).toString();
                  String selectedCellValue = (String) table.getValueAt(table.getSelectedRow() , table.getSelectedColumn());
                  int tableID = table.getSelectedRow();

                  var showTimeIDStr  = model.getValueAt(table.getSelectedRow(), 3).toString();
                  selectedShowTimeID = Integer.parseInt(showTimeIDStr);

                  String date = model.getValueAt(table.getSelectedRow(), 0).toString();
                  updateMovieLabel(db.getMovieName(getMovieID()), Helper.formatDate(date));
              } catch (Exception ex){

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




    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new PurchaseTicket();

    }



    @Override
    public void actionPerformed(ActionEvent e) {

        String movieTitle = cbMovies.getSelectedItem().toString();
        int movieID = cbMovies.getSelectedIndex();

        if (e.getSource() == cbMovies){
            if(movieID == 0){
                Helper.showErrorMessage("Please select a movie","Movie Error");
                cbMovies.setSelectedIndex(movieIDIndex);
                return;

            }
            movieShowTimes.setMovieId(db.getMovieID(movieTitle));
            String title = db.getMovieName(movieID);
            lblMovieDetails.setText(title);
            populateTable();
        }

        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
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
    private void handlePurchase() {
//        if(selectedShowTimeID == 0){
//            Helper.showErrorMessage("Please enter the movie showtime", "Error");
//            return;
//        }

        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        int customerID = LoginInfo.getCustomerID();
        String salesDate = LocalDate.now().toString();
        int movieID = getMovieID();

        Sales sales = new Sales(salesDate,customerID);
        if (db.addSales(sales)){
            int salesID = db.lastInsertedID();
            SalesDetails salesDetails = new SalesDetails(salesID, 2,numTickets);
            if (db.addSalesDetails(salesDetails)){
                System.out.println("Added");

            }
        }

        String s = MessageFormat.format("""
                    Purchase details:
                    Sales Date : {0}
                   Num Tickets : {1}
                 customerID : {2}
                 Movie ID : {3}                                
                    """, salesDate, numTickets, customerID, movieID);
        System.out.println(s);
    }
    private int getMovieID(){
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
                model.setValueAt(showTime.getShowTimeId(), i, 0);

                model.setValueAt(showTime.getShowDate(), i, 1);
                model.setValueAt(Helper.formatTime(showTime.getShowTime()), i, 2);
                model.setValueAt(showTime.getNumTicketLeft(), i, 3);
                i++;
            }

        } catch (ParseException e){
            e.printStackTrace();

        }


    }

    private void populateMovieComboBox() {

        for (var movie : db.getAllMovieShowTimes()){
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
    private void updateMovieLabel(String title, String date){
        lblMovieDetails.setText(String.format("%s: %s", title, date));
    }
}

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
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class PurchaseTicket1 extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI, ChangeListener, MouseListener {
    final String TOTAL_MSG = "Total to pay: ";
    int selectedShowTimeID;

    private final Database db;
    private final JComboBox<String> cbMovies = new JComboBox<>();
    JSpinner spNumTickets = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));

    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");
    private final JTextField txtMovieID = new JTextField(2);
    //    private final JTextField txtMShowDate = new JTextField(20);
    private final DefaultTableCellRenderer cellRenderer;
    MaskFormatter mf1 = new MaskFormatter("####-##-##");
    JFormattedTextField txtMShowDate = new JFormattedTextField(mf1);
    JLabel lblMovieDetails = new JLabel();
    private DefaultTableModel model;

    JLabel lblTicket = new JLabel();
    JLabel lblTotal = new JLabel();
    private final JButton btnConfirm = new JButton("Confirm Order");
    private final Ticket TICKET_DETAILS;
    private int movieIndex;



    public PurchaseTicket1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        // set movie title label style
        db = Database.getInstance();

        TICKET_DETAILS = db.getCustomerTicketType(LoginInfo.getCustomerID());
        mf1.setPlaceholderCharacter('_');
        lblMovieDetails.setFont(new Font("Arial", Font.BOLD, 20));
        lblMovieDetails.setText(db.getMovieName(MovieInfo.getMovieID()));
        txtMovieID.setEditable(false);

        scrollPane.setViewportView(table);
        showColumn();

        txtMovieID.addKeyListener(this);
        txtMShowDate.addKeyListener(this);
        cbMovies.addActionListener(this);
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
        cbMovies.setSelectedIndex(movieIndex);
        middle.add(cbMovies);
//        getCBMovieID(MovieInfo.getMovieID());
        cbMovies.setSelectedIndex(getCBMovieID());
        lblMovieDetails.setText(db.getMovieName(MovieInfo.getMovieID()));
        middle.add(lblMovieDetails);
        JScrollPane scrollPane = new JScrollPane(table);

        middle.add(scrollPane);


        JPanel south = new JPanel();

//        south.add(movieScrollPane);

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
        btnShowReceipt.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnConfirm.addActionListener(this);

        spNumTickets.addChangeListener(this);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {

                String selectedCellValue = (String) table.getValueAt(table.getSelectedRow() , table.getSelectedColumn());
                int tableID = table.getSelectedRow();
//                selectedShowTimeID = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 3).toString());

                var showTimeIDStr  = model.getValueAt(table.getSelectedRow(), 3).toString();
                selectedShowTimeID = Integer.parseInt(showTimeIDStr);

                String date = model.getValueAt(table.getSelectedRow(), 0).toString();
                int movieID = Integer.parseInt(txtMovieID.getText());
                updateMovieLabel(db.getMovieName(movieID) ,Helper.formatDate(date));


                if (tableID == 0){

                }
//                System.out.println(selectedCellValue);
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

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new PurchaseTicket1();

    }


    private void setUpTimesListInit() throws ParseException {
        txtMovieID.setText(String.valueOf(MovieInfo.getMovieID()));
        movieShowTimes.setMovieId(MovieInfo.getMovieID());
        movieShowTimes.setShowDate("");

        populateTable();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cbMovies){
            movieShowTimes.setMovieId(getCBMovieID());
            movieShowTimes.setShowDate("");
            populateTable();

        }
        if (e.getSource() == btnConfirm){
            handlePurchase();



        }

        handleButtonClick(e);


    }

    private void handlePurchase() {
        if(selectedShowTimeID == 0){
            Helper.showErrorMessage("Please enter the movie showtime", "Error");
            return;
        }

        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        int customerID = LoginInfo.getCustomerID();
        String salesDate = LocalDate.now().toString();
        var movieID = db.getMovieID(cbMovies.getSelectedItem().toString());

        String s = MessageFormat.format("""
                    Purchase details:
                    Sales Date : {0}
                   Num Tickets : {1}
                 customerID : {2}
                 Movie ID : {3}                                
                    """, salesDate, numTickets, customerID, movieID);
        System.out.println(s);
    }

    private int getCBMovieID(){
        var movieIDTitle = db.getMovieID(cbMovies.getSelectedItem().toString());
        int selectedIndex = cbMovies.getSelectedIndex();
        if (selectedIndex == 0){
            selectedIndex = 1;

        } else {
            selectedIndex -=1;

        }
        return selectedIndex;
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


        if (e.getSource() == txtMovieID) {
            Helper.validateNumber(e, txtMovieID);
            if(!Helper.validateMovieID(db,Integer.parseInt(txtMovieID.getText()))){
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int movieID = Integer.parseInt(txtMovieID.getText());
                String title = db.getMovieName(Integer.parseInt(txtMovieID.getText()));
                lblMovieDetails.setText(title);
                movieShowTimes.setMovieId(movieID);
                movieShowTimes.setShowDate("");
                populateTable();


            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private void updateTotalLabel() {
        double ticketPrice = TICKET_DETAILS.getPrice();
        int numTickets = Integer.parseInt(spNumTickets.getValue().toString());
        lblTotal.setText(TOTAL_MSG + Helper.formatMoney(Helper.calcPrice(numTickets, ticketPrice)));
    }


    @Override
    public void handleButtonClick(ActionEvent e) {


        if (e.getSource() == btnListMovies) {
            try {
                new MovieListOld();
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
            lblMovieDetails.setText(title);

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
                model.setValueAt(showTime.getShowDate(), i, 0);
                model.setValueAt(Helper.formatTime(showTime.getShowTime()), i, 1);
                model.setValueAt(showTime.getNumTicketLeft(), i, 2);
                model.setValueAt(showTime.getShowTimeID(), i, 3);
                i++;
            }

        } catch (ParseException e){
            e.printStackTrace();

        }


    }

    private void populateMovieComboBox() {
        movieIndex = 0;
        List<Movie> movie = db.getAllMovieShowTimes();
        for (int i = 0; i < movie.size(); i++) {
            if (MovieInfo.getMovieID() == movie.get(i).getMovieID()) {
                if (MovieInfo.getMovieID() == 1) {
                    movieIndex = 0;
                } else {
                }
            }
            cbMovies.addItem(movie.get(i).getTitle());


        }


    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spNumTickets) {
            updateTotalLabel();


        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == table){
            int selectedRow = table.getSelectedRow();
            selectedRow = table.convertRowIndexToModel(selectedRow);
            System.out.println(selectedRow);
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

    private void updateMovieLabel(String title, String date){
        lblMovieDetails.setText(String.format("%s: %s", title, date));
    }
}


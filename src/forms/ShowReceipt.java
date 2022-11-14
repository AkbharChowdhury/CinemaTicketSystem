package forms;


import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.FormAction;
import interfaces.ListGUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

public class ShowReceipt extends JFrame implements ActionListener, KeyListener, FormAction, ListGUI {
    private final Database db;

    private final DefaultListModel model;
    private final JList list ;
    int salesID;
    private final List<Invoice> INVOICES;



    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());
    private final JButton btnCancel = new JButton(Buttons.cancel());
    private final JButton btnPrintReceipt = new JButton(Buttons.printReceipt());





    private final JComboBox<String> comboBoxGenres = new JComboBox<>();
    private final String movieTitle = "";

    public ShowReceipt() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        Helper.isCustomerLoggedIn(this, RedirectPage.SHOW_RECEIPT);

        db = Database.getInstance();
        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        INVOICES = db.getInvoice(LoginInfo.getCustomerID());
        JScrollPane scrollPane = new JScrollPane(list);








        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 250);
        setTitle(FormDetails.showReceipt());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);





        JPanel middle = new JPanel();


        middle.add(scrollPane);


        JPanel south = new JPanel();
        south.add(btnPrintReceipt);
        south.add(btnCancel);



        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        btnPrintReceipt.addActionListener(this);

        comboBoxGenres.addActionListener(this);
        populateList();
        list.addListSelectionListener((ListSelectionEvent e) -> {
             int salesID = list.getSelectedIndex() + 1;

//            printInvoice(salesID);
        });

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {

        new ShowReceipt();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt){
            printInvoice(salesID);
        }

        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void printInvoice(int salesID) {
//        System.out.println("the index is " + index);

        for (int i= 0; i< INVOICES.size(); i++){

            if (i == salesID){
                double total = INVOICES.get(i).getPrice() * INVOICES.get(i).getTotalTicket();
                String output = MessageFormat.format("""
                            -------------------------- Movie Details --------------------
                            Movie: {0} 
                            Rating -{1}
                            show date and time ({2}, {3})
                            -------------------------- Ticket Details --------------------
                            {4} {5} (x{6}}
                            Total {7}                                                                             
                            """,

                        // movie details
                        INVOICES.get(i).getMovieTitle(),
                        INVOICES.get(i).getRating(),
                        INVOICES.get(i).getShowDate(),
                        INVOICES.get(i).getShowTime(),

                        // ticket details
                        INVOICES.get(i).getType(),
                        Helper.formatMoney(INVOICES.get(i).getPrice()),
                        INVOICES.get(i).getTotalTicket(),
                        Helper.formatMoney(total)

                );

                if (saveInvoice(output)){
                    Helper.message("your invoice has been saved");
                }
            }
        }


//        FileHandler.printInvoice("", );
    }

    private boolean saveInvoice(String output) {
        try(var myWriter = new FileWriter("invoice.txt")) {

            myWriter.write(output);
            return true;
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
            return false;

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


    @Override
    public void clearList(JList table) {
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();

    }

    @Override
    public void populateList() throws ParseException, SQLException {
        for (var invoice : INVOICES) {
            model.addElement(MessageFormat.format("{0}, {1}",
                    Helper.formatDate(invoice.getSalesDate()),
                    invoice.getMovieTitle()

            ));
        }
//        while (rs.next()){
//            double price = rs.getDouble(TicketsTable.COLUMN_PRICE);
//            int numTicket = rs.getInt(SalesDetailsTable.COLUMN_TOTAL_TICKETS_SOLD);
//            double total = numTicket * price;
//            model.addElement(
//                      MessageFormat.format("{0}, {1}, {2}",
//                              SalesTable.COLUMN_SALES_DATE,
//                              MovieTable.COLUMN_TITLE,
//                              Helper.formatMoney(total)
//
//                              ));
//            ;
//
//
//
//
//        }
//        for (var purchases : db.getInvoice()) {
//            model.addElement(MessageFormat.format("{0}, {1}, {2}, {3}",
//                    movie.getMovieID(),
//                    movie.getTitle(),
//                    Helper.calcDuration(movie.getDuration()),
//                    movie.getGenres()
//            ));
    }

}








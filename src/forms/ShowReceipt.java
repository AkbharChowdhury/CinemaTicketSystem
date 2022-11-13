package forms;


import classes.*;
import enums.Buttons;
import enums.FormDetails;
import interfaces.FormAction;
import interfaces.ListGUI;
import tables.MovieTable;
import tables.SalesDetailsTable;
import tables.SalesTable;
import tables.TicketsTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
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
import java.util.List;

public class ShowReceipt extends JFrame implements ActionListener, KeyListener, FormAction, ListGUI {
    private final Database db;

    private final DefaultListModel model;
    private final JList list ;
    int index;
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
        db = Database.getInstance();
        model = new DefaultListModel();
        list = new JList(model);
        INVOICES = db.getInvoice(1);
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
             index = list.getSelectedIndex() + 1;

//            printInvoice();
        });

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new ShowReceipt();

    }











    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt){
            printInvoice();
        }

        handleButtonClick(e);


    }

    private void printInvoice() {
        System.out.println("the index is " + index);
        for (var item : INVOICES){
//            if (item.get)
        }


//        FileHandler.printInvoice("", );
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
    public void handleButtonClick(ActionEvent e) {
        if (e.getSource() == btnPurchaseTicket) {
            if (LoginInfo.getCustomerID() == 0){
                int dialogButton = JOptionPane.showConfirmDialog (null, "You must be logged in to purchase tickets, do you want to login?","WARNING",JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION){
                    try {
                        new Login();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                return;
            }


            try {
                new PurchaseTicket1();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnListMovies) {
            try {
                new MovieListOld();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        if (e.getSource() == btnShowReceipt) {
            if (LoginInfo.getCustomerID() == 0){
                int dialogButton = JOptionPane.showConfirmDialog (null, "You must be logged in to view your receipt, do you want to login?","WARNING",JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION){
                    try {
                        new Login();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                return;
            }

            try {
                new ShowReceipt();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnShowTimes) {


            try {
                new ShowTimes();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }



    @Override
    public void clearList(JList table) {
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();

    }

    @Override
    public void populateList() throws ParseException, SQLException {
        for (var invoice :INVOICES ) {
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








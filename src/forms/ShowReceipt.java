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
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

public class ShowReceipt extends JFrame implements ActionListener, FormAction, ListGUI {
    private final Database db;
    private final DefaultListModel model;
    private final JList list;
    int selectedListInvoiceItem;
    private final List<Invoice> INVOICES;
    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());

    private final JButton btnPrintReceipt = new JButton(Buttons.printReceipt());

    private final JComboBox<String> comboBoxGenres = new JComboBox<>();

    public ShowReceipt() throws SQLException, FileNotFoundException, ParseException {
        Helper.isCustomerLoggedIn(this, RedirectPage.SHOW_RECEIPT);
        db = Database.getInstance();
        model = new DefaultListModel();
        list = new JList(model);

        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        // get selected list id
        list.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                var item =  ((JList) e.getSource())
                        .getSelectedValue();
                System.out.println("ID is " + item);
            }

        });
        INVOICES = db.getInvoice(LoginInfo.getCustomerID());
        JScrollPane scrollPane = new JScrollPane(list);

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(600, 400);
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
        list.setPreferredSize(new Dimension(400, 600));
        list.addListSelectionListener((ListSelectionEvent e) -> selectedListInvoiceItem = list.getSelectedIndex());

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ParseException {

        new ShowReceipt();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt) {
            processSelectedListItem(selectedListInvoiceItem);
        }

        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean processSelectedListItem(int salesID) {
        for (int i = 0; i < INVOICES.size(); i++) {
            if (i == salesID) {
                printInvoice(i);
                return true;
            }
        }
        return false;

    }

    void printInvoice(int i){
        try{


            Invoice invoiceDetails = new Invoice(true);
            invoiceDetails.generatePDFInvoice(INVOICES, i);
            System.out.println(Invoice.getSelectedInvoiceDetails(INVOICES, i));

            Helper.message("your invoice has been saved as " + Invoice.INVOICE_FILE_NAME);


        } catch (ParseException ex){
            Helper.showErrorMessage("the time cannot be formatted", "time parse error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void clearList(JList table) {
        ((DefaultListModel) list.getModel()).removeAllElements();

    }

    @Override
    public void populateList() {
        for (var invoice : INVOICES) {
            double total = invoice.getPrice() * invoice.getTotalTicket();

            model.addElement(MessageFormat.format("{0}, {1} {2}",
                    Helper.formatDate(invoice.getShowDate()),
                    invoice.getMovieTitle(),
                    Helper.formatMoney(total)

            ));
        }
    }


}








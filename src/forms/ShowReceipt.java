package forms;


import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.ListGUI;
import interfaces.MenuNavigation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public final class ShowReceipt extends JFrame implements ActionListener, ListGUI, MenuNavigation {
    Database db;
    JButton btnPrintReceipt = new JButton(Buttons.printReceipt());
    Navigation nav = new Navigation();
    int selectedListInvoiceItem;
    DefaultListModel model = new DefaultListModel();
    JList list = new JList(model);
    List<Invoice> INVOICES;


    public ShowReceipt() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        if (!Helper.isCustomerLoggedIn(this, RedirectPage.SHOW_RECEIPT)) {
            return;
        }

        if (!db.customerInvoiceExists(LoginInfo.getCustomerID())) {
            Helper.gotoForm(this, Pages.LIST_MOVIES);
            return;
        }

        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        INVOICES = db.getInvoice(LoginInfo.getCustomerID());


        setResizable(false);
        setLayout(new BorderLayout());
        setSize(600, 300);
        setTitle(FormDetails.showReceipt());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        navigation(top);

        JPanel middle = new JPanel();
        middle.add(new JScrollPane(list));

        JPanel south = new JPanel();
        south.add(btnPrintReceipt);

        add("North", top);
        add("Center", middle);
        add("South", south);

        btnPrintReceipt.addActionListener(this);

        populateList();
        list.setPreferredSize(new Dimension(550, 600));
        list.addListSelectionListener((ListSelectionEvent e) -> selectedListInvoiceItem = list.getSelectedIndex());

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        new ShowReceipt();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt) {
            if (!processSelectedListItem()) {
                System.err.println("unable to print the selected invoice");
            }
        }


    }

    private boolean processSelectedListItem() {

        if (list.isSelectionEmpty()) {
            Helper.showErrorMessage("You must select an item from the invoice list!", "Receipt error");
            return false;
        }

        int salesID  = list.getSelectedIndex();
        for (int i = 0; i < INVOICES.size(); i++) {
            if (i == salesID) {
                printInvoice(i);
                return true;
            }
        }
        return false;

    }

    public void printInvoice(int i) {
        try {
            Invoice invoice = new Invoice(true);
            invoice.generatePDFInvoice(INVOICES, i);
            Helper.message("Your invoice has been saved as " + Invoice.INVOICE_FILE_NAME);

        } catch (ParseException ex) {
            Helper.showErrorMessage("The time cannot be formatted", "Time parse error");
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clearList(JList table) {
        ((DefaultListModel) list.getModel()).removeAllElements();

    }



    @Override
    public void populateList() {
        final double PRICE = db.getCustomerTicketType(LoginInfo.getCustomerID()).getPrice();
        INVOICES.forEach(invoice -> model.addElement(Invoice.getDetails(invoice, PRICE)));
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








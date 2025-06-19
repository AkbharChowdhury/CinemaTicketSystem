package forms;


import classes.Database;
import classes.Form;
import classes.LoginInfo;
import classes.Navigation;
import classes.models.Customer;
import classes.models.Invoice;
import classes.utils.Helper;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;
import interfaces.ListGUI;
import interfaces.MenuNavigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public final class ShowReceipt extends JFrame implements ActionListener, ListGUI, MenuNavigation {

    Database db = Database.getInstance();
    JButton btnPrintReceipt = new JButton("Print Receipt");
    Navigation nav = new Navigation(this);
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    List<Invoice> INVOICES;
    final int CUSTOMER_ID = LoginInfo.getCustomerID();


    public ShowReceipt() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        LoginInfo.setCustomerID(1);
        if (!Customer.isLoggedIn(this, RedirectPage.SHOW_RECEIPT)) return;
        if (!db.customerInvoiceExists(CUSTOMER_ID)) {
            Form.gotoForm(this, Pages.LIST_MOVIES);
            return;
        }

        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        INVOICES = Collections.unmodifiableList(db.getInvoice(CUSTOMER_ID));

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(600, 300);
        setTitle(FormDetails.showReceipt.get());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        navigation(top);

        JPanel middle = new JPanel();
        middle.add(new JScrollPane(list));

        JPanel south = new JPanel();
        south.add(btnPrintReceipt);

        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        btnPrintReceipt.addActionListener(this);

        populateList();
        list.setPreferredSize(new Dimension(550, 600));

        setVisible(true);
    }

    public static void main() {
        try {
            new ShowReceipt();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt) processSelectedListItem();

    }

    private void processSelectedListItem() {

        if (list.isSelectionEmpty()) {
            Helper.showErrorMessage("You must select an item from the invoice list!", "Receipt error");
            return;
        }

        int salesID = list.getSelectedIndex();
        for (int i = 0; i < INVOICES.size(); i++) {
            if (i == salesID) {
                printInvoice(i);
                return;
            }
        }

    }

    public void printInvoice(int i) {
        try {
            new Invoice().printInvoice(INVOICES.get(i));
            final String INVOICE = Invoice.INVOICE_FILE_NAME;
            Helper.message.accept(STR."Your invoice has been saved as \{INVOICE}");
            Desktop.getDesktop().open(new File(INVOICE));

        } catch (ParseException ex) {
            Helper.showErrorMessage("The time cannot be formatted", "Time parse error");
        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void populateList() {
        final double PRICE = db.getCustomerTicketType(CUSTOMER_ID).getPrice();
        INVOICES.forEach(invoice -> model.addElement(Invoice.getDetails(invoice, PRICE)));
    }


    @Override
    public void navigation(JPanel top) {
        nav.addButtons(top);
    }

}








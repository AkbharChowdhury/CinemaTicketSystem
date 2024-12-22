package forms;


import classes.Database;
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
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public final class ShowReceipt extends JFrame implements ActionListener, ListGUI, MenuNavigation {
    private final Database db = Database.getInstance();
    private final JButton btnPrintReceipt = new JButton("Print Receipt");
    private final Navigation nav = new Navigation(this);
    int selectedListInvoiceItem;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private List<Invoice> INVOICES;


    public ShowReceipt() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!Customer.isLoggedIn(this, RedirectPage.SHOW_RECEIPT)) return;
        if (!db.customerInvoiceExists(LoginInfo.getCustomerID())) {
            Helper.gotoForm(this, Pages.LIST_MOVIES);
            return;
        }

        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        INVOICES = Collections.unmodifiableList(db.getInvoice(LoginInfo.getCustomerID()));

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

        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        btnPrintReceipt.addActionListener(this);

        populateList();
        list.setPreferredSize(new Dimension(550, 600));
        list.addListSelectionListener((ListSelectionEvent _) -> selectedListInvoiceItem = list.getSelectedIndex());

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            new ShowReceipt();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrintReceipt) {
            if (!processSelectedListItem()) System.err.println("unable to print the selected invoice");

        }


    }

    private boolean processSelectedListItem() {

        if (list.isSelectionEmpty()) {
            Helper.showErrorMessage("You must select an item from the invoice list!", "Receipt error");
            return false;
        }

        int salesID = list.getSelectedIndex();
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
            new Invoice(true).generatePDFInvoice(INVOICES, i);
            Helper.message(STR."Your invoice has been saved as \{Invoice.INVOICE_FILE_NAME}");

        } catch (ParseException ex) {
            Helper.showErrorMessage("The time cannot be formatted", "Time parse error");
        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void populateList() {
        final double PRICE = db.getCustomerTicketType(LoginInfo.getCustomerID()).getPrice();
        INVOICES.forEach(invoice -> model.addElement(Invoice.getDetails(invoice, PRICE)));
    }


    @Override
    public void navigation(JPanel top) {
        nav.addButtons(top);
    }

}








package forms;


import classes.Database;
import classes.Form;
import classes.LoginInfo;
import classes.models.Customer;
import classes.models.Ticket;
import classes.utils.Encryption;
import classes.utils.Helper;
import classes.utils.Validation;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public final class Register extends JFrame implements ActionListener {
    JTextField txtFirstname = new JTextField();
    JTextField txtLastName = new JTextField();
    JTextField txtEmail = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JComboBox<String> cbTicket = new JComboBox<>();
    JButton btnRegister = new JButton(Buttons.register());
    JButton btnLogin = new JButton("Back to Login");
    List<Ticket> ticketList;
    Database db;

    LinkedHashMap<String, JTextField> textFields = new LinkedHashMap<>() {{
        put("Firstname", txtFirstname);
        put("LastName", txtLastName);
        put("Email", txtEmail);
        put("Password", txtPassword);
    }};


    public Register() {

        db = Database.getInstance();
        ticketList = Collections.unmodifiableList(db.getTickets());
        setLayout(new BorderLayout());
        setSize(310, 500);
        setTitle(FormDetails.register.get());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel middle = new JPanel();
        middle.setLayout(new GridLayout(10, 1, 3, 3));

        textFields.forEach((label, textField) -> {
            middle.add(new JLabel(label));
            middle.add(textField);
        });

        middle.add(new JLabel("Ticket Type:"));
        cbTicket.addItem("Select Ticket Type");
        ticketList.forEach(ticket -> cbTicket.addItem(ticket.getType()));
        middle.add(cbTicket);
        JPanel bottom = new JPanel();
        bottom.add(btnRegister);
        bottom.add(btnLogin);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, bottom);
        add(BorderLayout.WEST, new JPanel());
        add(BorderLayout.EAST, new JPanel());
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        setVisible(true);
    }

    public static void main() {
        try {
            new Register();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRegister) {
            try {
                handleRegister();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());

            }
        }

        Form.goTo(this, Pages.LOGIN);
    }


    private void handleRegister() {
        String firstname = txtFirstname.getText().trim();
        String lastname = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());
        int ticketID = cbTicket.getSelectedIndex();
        var customer = new Customer(firstname, lastname, email, password, ticketID);
        if (!Validation.validateRegisterForm(customer, db)) return;
        customer.setTicketID(ticketList.get(ticketID - 1).getTicketID());
        customer.setPassword(Encryption.encode(password));
        if (db.addCustomer(customer)) {
            LoginInfo.setEmail(email);
            Helper.message.accept("Your account has been created, you can now login");
            Form.gotoForm(this, Pages.LOGIN);

        }


    }

}
package forms;


import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class Register extends JFrame implements ActionListener {
    JTextField txtFirstname = new JTextField();
    JTextField txtLastName = new JTextField();
    JTextField txtEmail = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JComboBox<String> cbTicket = new JComboBox<>();
    JButton btnRegister = new JButton(Buttons.register());

    Database db;


    public Register() throws SQLException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        setLayout(new BorderLayout());
        setSize(310, 500);
        setTitle(FormDetails.register());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel middle = new JPanel();
        middle.setLayout(new GridLayout(10, 1, 3, 3));
        middle.add(new JLabel("Firstname"));
        middle.add(txtFirstname);
        middle.add(new JLabel("Lastname"));
        middle.add(txtLastName);
        middle.add(new JLabel("Email"));
        middle.add(txtEmail);
        middle.add(new JLabel("Password"));
        middle.add(txtPassword);
        middle.add(new JLabel("Ticket Type:"));
        cbTicket.addItem("Select Ticket Type");
        populateTicketComboBox();
        middle.add(cbTicket);
        add("Center", middle);
        JPanel bottom = new JPanel();
        bottom.add(btnRegister);
        JButton btnLogin = new JButton("Back to Login");
        bottom.add(btnLogin);
        add("South", bottom);
        add("West", new JPanel());
        add("East", new JPanel());
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            new Register();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRegister) {
            try {
                handleRegister();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        Helper.goTo(this, Pages.LOGIN);


    }


    private void handleRegister() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String firstname = txtFirstname.getText().trim();
        String lastname = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        Customer customer = new Customer(firstname, lastname, email, password, cbTicket.getSelectedIndex());
        if (Validation.validateRegisterForm(customer)) {

            // encrypt the password
            customer.setPassword(Encryption.encode(password));
            // add customer
            if (db.addCustomer(customer)) {
                LoginInfo.setEmail(email);
                Helper.message("Your account has been created, you can now login");
            }
            Helper.gotoForm(this, Pages.LOGIN);


        }
    }

    private void populateTicketComboBox() {
        List<Ticket> ticketList = db.getTicket();
        for (var ticket : ticketList) {
            cbTicket.addItem(ticket.getType());
        }

    }


}
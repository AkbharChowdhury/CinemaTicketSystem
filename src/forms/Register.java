package forms;


import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public final class Register extends JFrame implements ActionListener {
    JTextField txtFirstname = new JTextField();
    JTextField txtLastName = new JTextField();
    JTextField txtEmail = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JComboBox<String> cbTicket = new JComboBox<>();
    JButton btnRegister = new JButton(Buttons.register());
    List<Ticket> ticketList;

    Database db;


    public Register() throws SQLException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        ticketList = db.getTickets();
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
        ticketList.forEach(ticket -> cbTicket.addItem(ticket.getType()));
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
            System.err.println(e.getMessage());
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRegister) {
            try {
                handleRegister();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
            return;
        }

        Helper.goTo(this, Pages.LOGIN);


    }


    private void handleRegister() throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String firstname = txtFirstname.getText().trim();
        String lastname = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());
        int ticketID = cbTicket.getSelectedIndex();

        var customer = new Customer(firstname, lastname, email, password, ticketID);
        if (!Validation.validateRegisterForm(customer)) return;
        customer.setTicketID(ticketList.get(ticketID - 1).getTicketID());
        customer.setPassword(Encryption.encode(password));
        if (db.addCustomer(customer)) {
            LoginInfo.setEmail(email);
            Helper.message("Your account has been created, you can now login");
        }
        Helper.gotoForm(this, Pages.LOGIN);


    }

}
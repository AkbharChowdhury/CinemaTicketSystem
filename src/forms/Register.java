package forms;


import classes.*;
import enums.FormDetails;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

public class Register
        extends JFrame implements ActionListener {
    private final JTextField txtFirstname = new JTextField("");
    private final JTextField txtLastName = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JComboBox<String> cbTicket = new JComboBox<>();
    private final JButton btnRegister = new JButton("Register");
    private final List<Ticket> TICKETS_LIST;

    private final Database db;


    public Register() throws SQLException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        TICKETS_LIST = db.getTicket();
        setLayout(new BorderLayout());
        setSize(300, 250);
        setTitle(FormDetails.register());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel middle = new JPanel();
        middle.setLayout(new GridLayout(6, 1, 5, 5));
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
        add("South", bottom);
        add("West", new JPanel());
        add("East", new JPanel());
        btnRegister.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new Register();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRegister) {
            handleRegister();
        }


    }
    private void handleRegister(){
        String firstname = txtFirstname.getText();
        String lastname = txtLastName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        Customer customer = new Customer(firstname, lastname, email, password, cbTicket.getSelectedIndex());
        if (Validation.validateRegisterForm(customer)) {
            if (db.emailExists(customer.getEmail())) {
                Helper.showErrorMessage("This email already exists", "Email error");
                return;
            }
            // encrypt the password
            customer.setPassword(Encryption.encode(password));
            // add customer
            if (db.addCustomer(customer)) {
                Helper.message("Your account has been created, you can now login");
            }

            try {
                new Login();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private void populateTicketComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // add default value
        for (var ticket : TICKETS_LIST) {
            cbTicket.addItem(ticket.getType());
        }

    }


}
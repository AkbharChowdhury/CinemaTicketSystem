package forms;

import classes.*;
import enums.FormDetails;
import enums.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;


public class Login extends JFrame implements ActionListener, KeyListener {
    private final Database db;
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnRegister = new JButton("Register");

    private final JLabel lblUsername = new JLabel("Username");
    private final JLabel lblPassword = new JLabel("Password");
    private final JPanel panel = new JPanel();
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);


    public Login() throws SQLException, FileNotFoundException {

        db = Database.getInstance();
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(300, 250);
        setTitle(FormDetails.login());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(panel);

        panel.setLayout(null);

        lblUsername.setBounds(10, 20, 80, 25);
        txtEmail.setBounds(100, 20, 165, 25);

        lblPassword.setBounds(10, 50, 80, 25);
        txtPassword.setBounds(100, 50, 165, 25);
        btnLogin.setBounds(10, 80, 80, 25);
        btnRegister.setBounds(90, 80, 80, 25);


        panel.add(lblUsername);
        panel.add(txtEmail);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        txtEmail.setText("john@gmail.com");
        txtPassword.setText("password");
        setVisible(true);
    }


    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new Login();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
            return;
        }
        try {
            new Register();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        if (!Validation.validateLoginForm(email, password)) {
            return;
        }
        if (!db.emailExists(txtEmail.getText())) {
            Helper.showErrorMessage("This email does not exist!", "Login Error");
            return;
        }
        if (db.isAuthorised(email, Encryption.encode(password))) {
            LoginInfo.setCustomerID(db.getCustomerID(email));
            try {

                if (Form.getRedirectPage() == null) {
                    Helper.gotoForm(this, Pages.LIST_MOVIES);
                    return;

                }
                switch (Form.getRedirectPage()) {
                    case PURCHASE_TICKET -> new PurchaseTicket();
                    case SHOW_RECEIPT -> new ShowReceipt();
                    default -> new MovieList();


                }
                dispose();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        Helper.showErrorMessage("invalid username/password combination", "Login error");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            handleLogin();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}


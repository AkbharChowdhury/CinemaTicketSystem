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

public class Login2  extends JFrame implements ActionListener, KeyListener {
    private final Database db;
    JTextField txtEmail;

    private static JButton   btnLogin = new JButton("Login");

    JButton btnRegister = new JButton("Register");

    private static JPasswordField txtPassword;
    Login2() throws SQLException, FileNotFoundException {

        db = Database.getInstance();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        // JFrame class
        setTitle(FormDetails.login());
        setLocation(new Point(500, 300));
        add(panel);
        setSize(new Dimension(400, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Username label constructor
        JLabel label = new JLabel("Email");
        label.setBounds(100, 8, 70, 20);
        panel.add(label);



        txtEmail= new JTextField();
        txtEmail.setBounds(100, 27, 193, 28);
        panel.add(txtEmail);



        // Password Label constructor
        JLabel password1 = new JLabel("Password");
        password1.setBounds(100, 55, 70, 20);
        panel.add(password1);



        // Password TextField
        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 75, 193, 28);
        panel.add(txtPassword);


        // Button constructor
        btnLogin.setBounds(100, 110, 90, 25);

        btnRegister.setBounds(200, 110, 90, 25);


//        button.addActionListener((ActionListener) new Java_GUI());
        panel.add(btnLogin);
        panel.add(btnRegister);



        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);













        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new Login2();
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
                    new MovieList();
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
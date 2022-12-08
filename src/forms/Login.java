package forms;

import classes.*;
import enums.FormDetails;
import enums.Pages;
import enums.RedirectPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener, KeyListener {
    private final Database db;
    private JTextField txtEmail = new JTextField();

    private static JButton btnLogin = new JButton("Login");

    JButton btnRegister = new JButton("Register");
    JLabel hyperlink = new JLabel("Movie list");

    private static JPasswordField txtPassword;

    public Login() throws SQLException, FileNotFoundException {
        setupHyperLink();

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


        txtEmail.setBounds(100, 27, 193, 28);
        panel.add(txtEmail);


        JLabel password1 = new JLabel("Password");
        password1.setBounds(100, 55, 70, 20);
        panel.add(password1);


        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 75, 193, 28);
        panel.add(txtPassword);


        btnLogin.setBounds(100, 110, 90, 25);
        btnRegister.setBounds(200, 110, 90, 25);
        hyperlink.setBounds(100, 130, 90, 30);

        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(hyperlink);


        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        setVisible(true);

        setRegisteredCustomerDetails();
    }

    private void setupHyperLink() {
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlinkClick(this);


//        hyperlink.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//
//                try {
//                    currentPage.dispose();
//
//
//                    Helper.gotoForm(currentPage, Pages.LIST_MOVIES);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//
//            }
//        });
    }

    private void hyperlinkClick(JFrame currentPage) {

                hyperlink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                try {
                    Helper.gotoForm(currentPage, Pages.LIST_MOVIES);
                    currentPage.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        });


    }

    private void setRegisteredCustomerDetails() {
        if (!LoginInfo.getEmail().isEmpty()) {
            txtEmail.setText(LoginInfo.getEmail());
            LoginInfo.setEmail("");


        }
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
                    new MovieList();
                    return;

                }
                if (Form.getRedirectPage() == RedirectPage.PURCHASE_TICKET) {
                    new PurchaseTicket();
                } else {
                    new MovieList();
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
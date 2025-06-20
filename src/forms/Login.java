package forms;

import classes.*;
import classes.utils.Encryption;
import classes.utils.Helper;
import classes.utils.MyEventListener;
import classes.utils.Validation;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class Login extends JFrame implements ActionListener {
    private Database db = Database.getInstance();

    private JTextField txtEmail = new JTextField();
    private JButton btnLogin = new JButton("Login");
    private JButton btnRegister = new JButton(Buttons.REGISTER);
    private JLabel hyperlink = new JLabel("Return to movie list");
    private JPasswordField txtPassword;
    private Runnable loginAction = this::handleLogin;


    public Login() {
        setupHyperLink();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        setTitle(FormDetails.login.get());
        setLocation(new Point(500, 300));
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(100, 8, 70, 20);
        panel.add(lblEmail);


        txtEmail.setBounds(100, 27, 193, 28);
        panel.add(txtEmail);


        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(100, 55, 70, 20);
        panel.add(lblPassword);


        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 75, 193, 28);
        panel.add(txtPassword);


        btnLogin.setBounds(100, 110, 90, 25);
        btnRegister.setBounds(200, 110, 90, 25);
        hyperlink.setBounds(100, 130, 180, 30);

        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(hyperlink);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        Buttons.handCursor.accept(new JButton[]{btnLogin, btnRegister});
        setRegisteredCustomerDetails();
        MyEventListener.enterKeyStroke(txtEmail, loginAction);
        MyEventListener.enterKeyStroke(txtPassword, loginAction);
        setVisible(true);


    }

    public static void main() {
        new Login();
    }


    private void setupHyperLink() {
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlinkClick(this);

    }

    private void hyperlinkClick(JFrame currentPage) {
        hyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Form.gotoForm(currentPage, Pages.LIST_MOVIES);
            }
        });


    }

    private void setRegisteredCustomerDetails() {
        if (LoginInfo.getEmail() != null) {
            txtEmail.setText(LoginInfo.getEmail());
            LoginInfo.setEmail("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
            return;
        }

        Form.goTo(this, Pages.REGISTER);
    }


    private void handleLogin() {

        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());

        if (!Validation.validateLoginForm(email, password)) return;
        if (!db.isAuthorised(email, Encryption.encode(password))) {
            Helper.showErrorMessage("invalid email/password combination", "Login error");
            return;
        }
        LoginInfo.setCustomerID(db.getCustomerID(email));
        redirect();


    }

    private void redirect() {
        var redirectPage = switch (Form.getRedirectPage()) {
            case null -> Pages.LIST_MOVIES;
            case PURCHASE_TICKET -> Pages.PURCHASE_TICKET;
            case SHOW_RECEIPT -> Pages.SHOW_RECEIPT;
        };

        Form.gotoForm(this, redirectPage);
    }

}
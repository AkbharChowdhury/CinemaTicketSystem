package forms;

import classes.*;
import classes.utils.Encryption;
import classes.utils.Helper;
import classes.utils.Validation;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public final class Login extends JFrame implements ActionListener {
    private final Database db = Database.getInstance();
    private final JTextField txtEmail = new JTextField();
    private final JButton btnLogin = new JButton("Login");
    private final JLabel hyperlink = new JLabel(WordUtils.capitalize("return to movie list"));
    private final JPasswordField txtPassword;

    public Login()  {
        setupHyperLink();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        setTitle(FormDetails.login.get());
        setLocation(new Point(500, 300));
        add(panel);
        setSize(400, 200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JButton btnRegister = new JButton(Buttons.register());
        btnRegister.setBounds(200, 110, 90, 25);
        hyperlink.setBounds(100, 130, 180, 30);

        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(hyperlink);


        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        setRegisteredCustomerDetails();

        JTextFieldEnterKey();
        setVisible(true);


    }

    public static void main() throws SQLException, FileNotFoundException {
        new Login();
    }

    private void enterKey(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();

    }

    private void JTextFieldEnterKey() {
        txtEmail.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                enterKey(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        txtPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                enterKey(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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
        String password = txtPassword.getText();

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
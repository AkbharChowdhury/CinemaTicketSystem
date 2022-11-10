package forms;

import classes.Database;
import enums.FormDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;


public class Login extends JFrame implements ActionListener  {
    private Database db;
    private final JButton btnLogin = new JButton("forms.Login");
    JTextField txtUsername = new JTextField(20);
    JPasswordField txtPassword = new JPasswordField(20);

    private final JLabel lblUsername = new JLabel("Username");
    private final JLabel lblPassword = new JLabel("Password");

    private final JPanel panel = new JPanel();




    public Login() throws InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException {

        db = Database.getInstance();
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(300, 250);
        setTitle(FormDetails.login());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(panel);

        panel.setLayout(null);

        lblUsername.setBounds(10,20,80,25);
        txtUsername.setBounds(100,20,165,25);

        lblPassword.setBounds(10,50,80,25);
        txtPassword.setBounds(100,50,165,25);

        btnLogin.setBounds(10,80,80,25 );


        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        btnLogin.addActionListener(this);








        setVisible(true);
    }



    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
       new Login();


    }


    @Override
    public void actionPerformed(ActionEvent e) {
//        var page = new App.NewPage();
//        page.setVisible(true);
//
//        //create a welcome label and set it to the new page
//        LoginInfo.setCustomerID(1);
//        JLabel wel_label = new JLabel("Welcome: " + LoginInfo.getCustomerID());
//        page.getContentPane().add(wel_label);
//        dispose();

    }
}

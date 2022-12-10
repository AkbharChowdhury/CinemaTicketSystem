package forms;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {
    private final JTextField txtFirstname = new JTextField("");
    private final JTextField txtLastName = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JComboBox<String> cbTicket = new JComboBox<>();
    private final JButton btnRegister = new JButton("Register");


    public static void main(String[] args) {
        new Test();
    }
    public Test(){
        setLayout(new BorderLayout());
//        firstname = new JTextField();
//        lastname = new JTextField();
//        logID = new JTextField();
//        displayBtn = new JButton("S");
        JPanel middle = new JPanel();
        middle.setLayout(new GridLayout(6,1,5,5));
        middle.add(new JLabel("Firstname"));
        middle.add(txtFirstname);
        middle.add(new JLabel("Lastname"));
        middle.add(txtLastName);
        middle.add(new JLabel("Email"));
        middle.add(txtEmail);






        add("Center", middle);
        JPanel bottom = new JPanel();
        bottom.add(btnRegister);

        add("South", bottom);
        add("West", new JPanel());
        add("East", new JPanel());
        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);




    }

}

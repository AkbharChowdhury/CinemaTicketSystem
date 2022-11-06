import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class App extends JFrame implements ActionListener, KeyListener {

    JButton btnListMovies = new JButton("List Movies");
    JButton btnShowTimes = new JButton("Show times");
    JButton btnPurchaseTicket = new JButton("Purchase ticket");
    JButton btnShowReceipt = new JButton("Show receipt");
    JTextField txtMovieID = new JTextField(2);
    JTextField txtMovieTitle = new JTextField(20);
    JComboBox comboBoxGenres = new JComboBox();


    JTextField txtGrossSalary = new JTextField(10);

    JRadioButton rbMarried = new JRadioButton("Married");
    JRadioButton rbSingle = new JRadioButton("Single");
    ButtonGroup bgMartalStatus = new ButtonGroup();

    SpinnerModel spinnerModelChild = new SpinnerNumberModel(0, 0, 10, 1);

    JSpinner spChild = new JSpinner(spinnerModelChild);

    JTextField txtTaxable = new JTextField(10);
    JTextField txtTax = new JTextField(10);


    JButton btnCalcTax = new JButton("Calculate Tax");
    JButton btnClear = new JButton("Clear");


    public App() {
//        txtTax.setEnabled(false);
//        txtTaxable.setEnabled(false);
        txtMovieID.addKeyListener(this);
        setResizable(false);

        setLayout(new BorderLayout());
        setSize(700, 150);
        setTitle("Cinema Ticket Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);

        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


//        top.setLayout(new FlowLayout());
//
//
//        top.add(new Label("Gross Salary"));
//        top.add(txtGrossSalary);
//
//        top.add(rbMarried);
//        top.add(rbSingle);
//        bgMartalStatus.add(rbMarried);
//        bgMartalStatus.add(rbSingle);
//        top.add(new Label("Number of children"));
//        top.add(spChild);

        comboBoxGenres.addItem("Any Genre");
        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(comboBoxGenres);


//        middle.add(txtTaxable);
//        middle.add(new Label("Tax: "));
//        middle.add(txtTax);
        JPanel south = new JPanel();
        south.add(btnCalcTax);
        south.add(btnClear);


        add("North", top);
        add("Center", middle);
        add("South", south);
        btnCalcTax.addActionListener(this);
        btnClear.addActionListener(this);


        setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }

    public void actionPerformed(ActionEvent e) {


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        // listen for movie id text-field
        if (e.getSource() == txtMovieID) {
            // link https://www.youtube.com/watch?v=cdPKsws5f-4
            char c = e.getKeyChar();
            if (Character.isLetter(c)) {
                // disable input if the value is not a number
                txtMovieID.setEditable(false);
            }

            // check if number is boo
            boolean isNumber = !Character.isLetter(c);
            txtMovieID.setEditable(isNumber);

        }


    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
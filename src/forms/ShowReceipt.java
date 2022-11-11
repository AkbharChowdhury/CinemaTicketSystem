package forms;


import classes.*;
import enums.FormDetails;
import interfaces.FormAction;
import interfaces.ListGUI;
import interfaces.TableGUI;
import interfaces.TableProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;


public class ShowReceipt extends JFrame implements ActionListener, KeyListener, FormAction, ListGUI {
    private final Database db;
    private final MovieGenres movieGenre = new MovieGenres();
    private final DefaultListModel model2;
    private final JList list;



    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");
    private final JButton btnPrintReceipt = new JButton("Print Receipt");
    private final JButton btnCancel = new JButton("Cancel");


    private final JTextField txtMovieID = new JTextField(2);
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> comboBoxGenres = new JComboBox<>();
    private final String movieTitle = "";

    public ShowReceipt() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException {
        db = Database.getInstance();

        model2 = new DefaultListModel();
        list = new JList(model2);




        txtMovieID.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 250);
        setTitle(FormDetails.showReceipt());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);





        JPanel middle = new JPanel();
//        middle.add(new Label("Movie Title:"));
//        middle.add(txtMovieTitle);
//        middle.add(new Label("Genre"));
//        middle.add(comboBoxGenres);
//        middle.add(movieScrollPane);


        JScrollPane scrollPane1 = new JScrollPane(list);
        middle.add(scrollPane1);


        JPanel south = new JPanel();
        south.add(btnPrintReceipt);
        south.add(btnCancel);





//        JScrollPane movieScrollPane = new JScrollPane(scrollPane);
//        south.add(movieScrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        comboBoxGenres.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new ShowReceipt();

    }











    @Override
    public void actionPerformed(ActionEvent e) {

        handleButtonClick(e);


    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtMovieTitle) {
            movieGenre.setTitle(txtMovieTitle.getText());

        }


        if (e.getSource() == txtMovieID) {
            char c = e.getKeyChar();
            if (Character.isLetter(c)) {
                // disable input if the value is not a number
                txtMovieID.setEditable(false);
            }

            boolean isNumber = !Character.isLetter(c);
            txtMovieID.setEditable(isNumber);

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void handleButtonClick(ActionEvent e) {
        if (e.getSource() == btnPurchaseTicket) {
            if (LoginInfo.getCustomerID() == 0){
                int dialogButton = JOptionPane.showConfirmDialog (null, "You must be logged in to purchase tickets, do you want to login?","WARNING",JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION){
                    try {
                        new Login();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                return;
            }

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to purchase tickets", "Purchase ticket error");
                return;
            }
            try {
                new PurchaseTicket();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnListMovies) {
            try {
                new MovieList();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        if (e.getSource() == btnShowReceipt) {
            if (LoginInfo.getCustomerID() == 0){
                int dialogButton = JOptionPane.showConfirmDialog (null, "You must be logged in to view your receipt, do you want to login?","WARNING",JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION){
                    try {
                        new Login();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                return;
            }

            try {
                new ShowReceipt();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnShowTimes) {

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to view show times", "Movie show time error");
                return;
            }
            try {
                MovieInfo.setMovieID(Integer.parseInt(txtMovieID.getText()));
                new ShowMovieTimes();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }



    @Override
    public void clearList(JList table) {
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();

    }

    @Override
    public void populateList() throws ParseException {

    }
}




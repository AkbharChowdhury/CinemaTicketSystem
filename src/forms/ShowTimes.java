package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.RedirectPage;
import interfaces.FormAction;
import interfaces.TableGUI;

import javax.swing.text.MaskFormatter;
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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import classes.*;


public class ShowTimes extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI {
    private final Database db;
    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private int movieIDIndex;


    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());

    private final DefaultTableCellRenderer cellRenderer;
    private final MaskFormatter maskFormatter = new MaskFormatter("####-##-##");
    private final JFormattedTextField txtShowDate = new JFormattedTextField(maskFormatter);
//    private final JTextField txtShowDate = new JTextField();

    private DefaultTableModel model;
    private final JComboBox<String> cbMovies = new JComboBox<>();



    public ShowTimes() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        db = Database.getInstance();
        maskFormatter.setPlaceholderCharacter('_');
        movieShowTimes.setShowDate("");

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        showColumn();

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.showTimes());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);

        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Select Movie: "));
        cbMovies.addItem("Select Movie");
        populateMovieComboBox();
        middle.add(cbMovies);

        middle.add(new Label("Filter Date:"));
        middle.add(txtShowDate);


        JPanel south = new JPanel();
        JScrollPane movieScrollPane = new JScrollPane(scrollPane);

        south.add(movieScrollPane);

        add("North", top);
        add("Center", middle);
        add("South", south);

        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchaseTicket.addActionListener(this);
        btnShowReceipt.addActionListener(this);
        txtShowDate.addKeyListener(this);
        cbMovies.addActionListener(this);

        setVisible(true);
    }



    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new ShowTimes();

    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cbMovies){
            if(cbMovies.getSelectedIndex()== 0 ){
                Helper.showErrorMessage("Please select a movie","Movie Error");
                cbMovies.setSelectedIndex(movieIDIndex);
                return;

            }
            movieIDIndex = cbMovies.getSelectedIndex();
            movieShowTimes.setMovieId(db.getMovieID(cbMovies.getSelectedItem().toString()));
            populateTable();
        }

        handleButtonClick(e);


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtShowDate && !txtShowDate.getText().isEmpty()) {
            movieShowTimes.setShowDate(txtShowDate.getText());
            populateTable();
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void handleButtonClick(ActionEvent e) {

        if (e.getSource() == btnPurchaseTicket) {
            if (LoginInfo.getCustomerID() == 0){
                Form.setRedirectPage(RedirectPage.PURCHASE);
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









            populateTable();
//            String title = db.getMovieName("");
//            movieTitle.setText(title);

        }
    }

    @Override
    public void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

    }

    @Override
    public void showColumn() {
        model = (DefaultTableModel) table.getModel();
        new MovieShowTimes().tableColumns().forEach(i -> model.addColumn(i));

    }

    @Override
    public void populateTable() {
        try {
            clearTable(table);
            var showTimesList = db.showMovieTimes(movieShowTimes);
            int i = 0;
            for (var showTime : showTimesList) {
                model.addRow(new Object[0]);
//                model.setValueAt((Helper.formatDate(showTime.getShowDate())), i, 0);

//                model.setValueAt(Helper.formatDate(showTime.getShowDate()), i, 0);

                model.setValueAt(showTime.getShowDate(), i, 0);



                model.setValueAt(Helper.formatTime(showTime.getShowTime()), i, 1);
                model.setValueAt(showTime.getNumTicketLeft(), i, 2);
                i++;





            }

        } catch (ParseException e){
            e.printStackTrace();

        }




    }

    private void populateMovieComboBox() {

        for (var movie : db.getAllMovieShowTimes()){
            cbMovies.addItem(movie.getTitle());
        }


        }
}

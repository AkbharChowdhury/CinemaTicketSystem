package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import enums.Pages;
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


public class ShowTimesForm extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI {
    private final Database db;
    private final ShowTimes movieShowTimes = new ShowTimes();
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



    public ShowTimesForm() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        db = Database.getInstance();
        maskFormatter.setPlaceholderCharacter('_');
        movieShowTimes.setDate("");

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
        cbMovies.addItem(FormDetails.defaultMovie());
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
        new ShowTimesForm();

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            navigationMenu(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (e.getSource() == cbMovies){
            if(cbMovies.getSelectedIndex()== 0 ){
                Helper.showErrorMessage("Please select a movie","Movie Error");
                cbMovies.setSelectedIndex(movieIDIndex);
                return;

            }
            movieIDIndex = cbMovies.getSelectedIndex();

            movieShowTimes.setMovieID(db.getMovieID(cbMovies.getSelectedItem().toString()));
            populateTable();
        }



    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtShowDate && !txtShowDate.getText().isEmpty()) {
            movieShowTimes.setDate(txtShowDate.getText());
            populateTable();
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void navigationMenu(ActionEvent e) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (e.getSource() == btnListMovies) {
            Helper.gotoForm(this, Pages.LIST_MOVIES);

        }
        if (e.getSource() == btnShowTimes) {
            Helper.gotoForm(this, Pages.SHOW_TIMES);
        }

        if (e.getSource() == btnPurchaseTicket) {
            if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
                Helper.gotoForm(this, Pages.LOGIN);
                return;
            }
            Helper.gotoForm(this, Pages.PURCHASE_TICKET);

        }

        if (e.getSource() == btnShowReceipt) {
            if (!Helper.isCustomerLoggedIn(this, RedirectPage.SHOW_RECEIPT)) {
                Helper.gotoForm(this, Pages.LOGIN);
                return;
            }

            Helper.gotoForm(this, Pages.SHOW_RECEIPT);

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
                model.setValueAt(showTime.getDate(), i, 0);
                model.setValueAt(Helper.formatTime(showTime.getTime()), i, 1);
                model.setValueAt(showTime.getNumTicketsLeft(), i, 2);
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

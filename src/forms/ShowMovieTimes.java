package forms;

import classes.*;
import enums.FormDetails;
import interfaces.FormAction;
import interfaces.TableGUI;

import javax.swing.text.MaskFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;


public class ShowMovieTimes extends JFrame implements ActionListener, KeyListener, FormAction, TableGUI {
    private final Database db;
    private final MovieShowTimes movieShowTimes = new MovieShowTimes();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show times");
    private final JButton btnPurchaseTicket = new JButton("Purchase ticket");
    private final JButton btnShowReceipt = new JButton("Show receipt");
    private final JTextField txtMovieID = new JTextField(2);
//    private final JTextField txtMShowDate = new JTextField(20);
    private final DefaultTableCellRenderer cellRenderer;
    MaskFormatter mf1 = new MaskFormatter("####-##-##");
    JFormattedTextField txtMShowDate = new JFormattedTextField(mf1);
    JLabel movieTitle = new JLabel();
    private DefaultTableModel model;


    public ShowMovieTimes() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        // set movie title label style
        db = Database.getInstance();
        mf1.setPlaceholderCharacter('_');
        movieTitle.setFont(new Font("Arial", Font.BOLD, 20));
        movieTitle.setText(db.getMovieName(MovieInfo.getMovieID()));

        scrollPane.setViewportView(table);
        showColumn();

        txtMovieID.addKeyListener(this);
        txtMShowDate.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.showTimes());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpTimesListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


        populateTable();
//        table.getColumnModel().getColumn(0).setPreferredWidth(5);
//        table.getColumnModel().getColumn(1).setPreferredWidth(150);
//
//        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        JPanel middle = new JPanel();
        middle.add(new Label("Filter Date:"));
        middle.add(txtMShowDate);
        movieTitle.setText(db.getMovieName(MovieInfo.getMovieID()));
        middle.add(movieTitle);


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

        setVisible(true);
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new ShowMovieTimes();

    }


    private void setUpTimesListInit() {
        txtMovieID.setText(String.valueOf(MovieInfo.getMovieID()));
        movieShowTimes.setMovieId(MovieInfo.getMovieID());
        movieShowTimes.setShowDate("");

        populateTable();

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
        if (e.getSource() == txtMShowDate) {
            movieShowTimes.setShowDate(txtMShowDate.getText());
            populateTable();

        }


        if (e.getSource() == txtMovieID) {
            char c = e.getKeyChar();
            if (Character.isLetter(c)) {
                // disable input if the value is not a number
                txtMovieID.setEditable(false);
            }

            boolean isNumber = !Character.isLetter(c);
            txtMovieID.setEditable(isNumber);

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int movieID = Integer.parseInt(txtMovieID.getText());
                String title = db.getMovieName(Integer.parseInt(txtMovieID.getText()));
                movieTitle.setText(title);
                movieShowTimes.setMovieId(movieID);
                movieShowTimes.setShowDate(txtMShowDate.getText());


                populateTable();


            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void handleButtonClick(ActionEvent e) {
        if (e.getSource() == btnPurchaseTicket) {

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


        }

        if (e.getSource() == btnShowTimes) {

            if (txtMovieID.getText().isEmpty()) {
                Helper.showErrorMessage("Please enter a movie ID to view show times", "Movie show time error");
                return;
            }
            movieShowTimes.setMovieId(Integer.parseInt(txtMovieID.getText()));
            populateTable();
            String title = db.getMovieName(Integer.parseInt(txtMovieID.getText()));
            movieTitle.setText(title);

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
        clearTable(table);
        var showTimesList = db.showMovieTimes(movieShowTimes);
        int i = 0;
        for (var showTime : showTimesList) {
            model.addRow(new Object[0]);
            model.setValueAt(showTime.getShowDate(), i, 0);
            model.setValueAt(showTime.getShowTime(), i, 1);
            model.setValueAt(showTime.getNumTicketLeft(), i, 2);

            i++;
        }

    }
}

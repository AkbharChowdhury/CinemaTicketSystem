package forms;

import classes.*;
import enums.Buttons;
import enums.FormDetails;
import interfaces.FormAction;
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


public class MovieList extends JFrame implements ActionListener, KeyListener, FormAction, TableProperties, TableGUI {
    private final Database db;
    private final MovieGenres movieGenre = new MovieGenres();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton btnListMovies = new JButton(Buttons.listMovies());
    private final JButton btnShowTimes = new JButton(Buttons.showTimes());
    private final JButton btnPurchaseTicket = new JButton(Buttons.purchaseTicket());
    private final JButton btnShowReceipt = new JButton(Buttons.showReceipt());
    private final JTextField txtMovieTitle = new JTextField(20);
    private final JComboBox<String> comboBoxGenres = new JComboBox<>();
    private DefaultTableModel model;
    private final DefaultTableCellRenderer cellRenderer;
    private final String movieTitle = "";
//
//    MaskFormatter mf1 = new MaskFormatter("##");
//    JFormattedTextField txtMovieID = new JFormattedTextField(mf1);

    public MovieList() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, FileNotFoundException, ParseException {
        db = Database.getInstance();
        scrollPane.setViewportView(table);
        setupTableProperties();

        txtMovieTitle.addKeyListener(this);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(700, 550);
        setTitle(FormDetails.movieList());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        setUpMovieListInit();

        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


        populateGenreComboBox();

        populateTable();
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);

        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);


        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(comboBoxGenres);

        JPanel south = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        south.add(scrollPane);

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

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        new MovieList();

    }



    private void setupTableProperties() {
        model = (DefaultTableModel) table.getModel();
        for (String column : new MovieGenres().tableColumns()) {
            model.addColumn(column);

        }

    }



    private void setUpMovieListInit() {
        movieGenre.setGenreID(0);
        movieGenre.setTitle(movieTitle);
        populateTable();

    }

    private void populateGenreComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        comboBoxGenres.addItem("Any Genre");
        for (var genre : db.getMovieGenreList()) {
            comboBoxGenres.addItem(genre);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        handleButtonClick(e);

        if (e.getSource() == comboBoxGenres) {
            System.out.println(comboBoxGenres.getSelectedIndex());
            movieGenre.setGenreID(comboBoxGenres.getSelectedIndex());
            populateTable();


        }
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtMovieTitle) {
            movieGenre.setTitle(txtMovieTitle.getText());
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


//            if(!Helper.validateMovieID(db,Integer.parseInt(txtMovieID.getText()))){
//                return;
//            }




            try {
//                MovieInfo.setMovieID(Integer.parseInt(txtMovieID.getText()));
                new PurchaseTicket();

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (e.getSource() == btnListMovies) {
            try {
                new MovieListOld();
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




            try {
//                MovieInfo.setMovieID(Integer.parseInt(txtMovieID.getText()));
                new ShowMovieTimes();
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

//    private boolean validateShowTimes() {
//        int movieID = Integer.parseInt(txtMovieID.getText());
//        MovieShowTimes movieShowTimes = new MovieShowTimes();
//        movieShowTimes.setMovieId(movieID);
//        movieShowTimes.setShowDate("");
//
//        if (Helper.validateMovieShowTime(db, movieShowTimes, movieID)){
//            return false;
//        }
//        return true;
//    }

    @Override
    public List<String> tableColumns() {
        return null;
    }



    @Override
    public void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    @Override
    public void showColumn() {
        new MovieGenres().tableColumns().forEach(i -> model.addColumn(i));


    }

    @Override
    public void populateTable() {
        clearTable(table);
        var movieList = db.showMovieList(movieGenre);
        int i = 0;
        for (var movie : movieList) {
            model.addRow(new Object[0]);
//            model.setValueAt(movie.getMovieID(), i, 0);
            model.setValueAt(movie.getTitle(), i, 0);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 1);
            model.setValueAt(movie.getRating(), i, 2);
            model.setValueAt(movie.getGenres(), i, 3);
            i++;
        }


    }
}



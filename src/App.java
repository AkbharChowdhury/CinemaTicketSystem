import classes.Database;
import classes.Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame implements ActionListener, KeyListener {
    static Database db;
    DefaultListModel model;
    JList list;




    JButton btnListMovies = new JButton("List Movies");
    JButton btnShowTimes = new JButton("Show times");
    JButton btnPurchaseTicket = new JButton("Purchase ticket");
    JButton btnShowReceipt = new JButton("Show receipt");

    JTextField txtMovieID = new JTextField(2);
    JTextField txtMovieTitle = new JTextField(20);
    JComboBox<String> comboBoxGenres = new JComboBox<>();
    JList<Object> movieList;




    SpinnerModel spinnerModelChild = new SpinnerNumberModel(0, 0, 10, 1);

    public App() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        txtTax.setEnabled(false);
//        txtTaxable.setEnabled(false);

        model = new DefaultListModel();
        list = new JList(model);


        txtMovieID.addKeyListener(this);
        txtMovieTitle.addKeyListener(this);

        setResizable(false);

        setLayout(new BorderLayout());
        setSize(700, 250);
        setTitle("Cinema Ticket Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(btnListMovies);
        top.add(btnShowTimes);
        top.add(txtMovieID);

        top.add(btnPurchaseTicket);
        top.add(btnShowReceipt);


       populateGenreComboBox();


        JPanel middle = new JPanel();
        middle.add(new Label("Movie Title:"));
        middle.add(txtMovieTitle);
        middle.add(new Label("Genre"));
        middle.add(comboBoxGenres);

        JPanel south = new JPanel();

        getMovieList(false,0, "");

        JScrollPane movieScrollPane = new JScrollPane(list);
        south.add(movieScrollPane);

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

    private void getMovieList(boolean isSearchable, int genreID, String movieTitle) {
        for(var movie: db.showMovieList(isSearchable,genreID, movieTitle)){
            model.addElement(MessageFormat.format("{0}, {1}, {2}",
                    movie.getTitle(),
                    Helper.calcDuration(movie.getDuration()),
                    movie.getGenres()

            ));

//            model.addElement(movieList);


        }


    }

    private void populateGenreComboBox() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // add default value
        comboBoxGenres.addItem("Any Genre");
        for (var genre: db.getMovieGenreList()){
            comboBoxGenres.addItem(genre);
        }


    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        db = Database.getInstance();
        new App();

    }

    public void actionPerformed(ActionEvent e) {
        // combo
        if (e.getSource() == comboBoxGenres){
            boolean isSearchable = comboBoxGenres.getSelectedIndex() !=0;

            filterMovieList(isSearchable);


        }



    }

    private void filterMovieList(boolean isSearchable) {
        clearList(list);
        getMovieList(isSearchable, comboBoxGenres.getSelectedIndex(), txtMovieTitle.getText());

    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == txtMovieTitle){
            filterMovieList(true);
        }




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
    private static void clearList(JList list){
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();
    }



}
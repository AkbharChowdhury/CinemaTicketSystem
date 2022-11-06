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


    JButton btnListMovies = new JButton("List Movies");
    JButton btnShowTimes = new JButton("Show times");
    JButton btnPurchaseTicket = new JButton("Purchase ticket");
    JButton btnShowReceipt = new JButton("Show receipt");

    JTextField txtMovieID = new JTextField(2);
    JTextField txtMovieTitle = new JTextField(20);
    JComboBox<String> comboBoxGenres = new JComboBox<>();
    JList movieList;




    SpinnerModel spinnerModelChild = new SpinnerNumberModel(0, 0, 10, 1);

    public App() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        txtTax.setEnabled(false);
//        txtTaxable.setEnabled(false);
        txtMovieID.addKeyListener(this);
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

       populateMoviesList();
        JScrollPane movieScrollPane = new JScrollPane(movieList);
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

    private void populateMoviesList() {
        List<String> movies = new ArrayList<>();
        for(var movie: db.showMovieList(false,0)){
            movies.add(MessageFormat.format("{0}, {1}, {2}",
                    movie.getTitle(),
                    Helper.calcDuration(movie.getDuration()),
                    movie.getGenres()
            ));

        }
        movieList = new JList(movies.toArray());

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
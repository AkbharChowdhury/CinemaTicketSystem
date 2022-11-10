import classes.Database;
import classes.Helper;
import classes.Movie;
import classes.MovieGenres;

import java.awt.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
public class MovieApp extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private DefaultTableCellRenderer cellRenderer;
    public MovieApp() throws SQLException, FileNotFoundException {
        setTitle("JTable Test");
        setLayout(new FlowLayout());
        scrollPane = new JScrollPane();
        table = new JTable();
        scrollPane.setViewportView(table);
        model = (DefaultTableModel)table.getModel();
        model.addColumn("ID");
        model.addColumn("Movie");
        model.addColumn("Duration");
        model.addColumn("Rating");
        model.addColumn("Genre");


        List<Movie> m = new ArrayList<>();
        m.add(new Movie("Apple",32));
        m.add(new Movie("Orange",44));
        Database db = Database.getInstance();

        MovieGenres mg = new MovieGenres();
        mg.setTitle("");
        mg.setGenreID(0);

        var movieList = db.showMovieList(mg);


        int i = 0;
        for(var movie: movieList) {
            model.addRow(new Object[0]);
            model.setValueAt(movie.getMovieID(), i, 0);
            model.setValueAt(movie.getTitle(), i, 1);
            model.setValueAt(Helper.calcDuration(movie.getDuration()), i, 2);
            model.setValueAt(movie.getRating(), i, 3);
            model.setValueAt( movie.getGenres(), i, 4);
            i++;
        }

//        model.addRow(new Object[0]);
//        model.setValueAt(ID,0);
//        model.setValueAt("Tutorials", i, 1);
//        model.setValueAt("Point", i, 2);
//        model.setValueAt("@tutorialspoint.com", i, 3);
//        model.setValueAt("123456789", i, 4);

        // set the column width for each column
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        add(scrollPane);
        setSize(1200, 900);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        new MovieApp();
    }
}
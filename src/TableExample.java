
import javax.swing.*;
import java.awt.*;

public class TableExample {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("JTable Demo");

        String[] columns = {
                "ID",
                "Movie",
                "Duration",
                "Rating",
                "Genre",

        };

        Object[][] data = {
                {"1", "Child's play.", 32, "R", "Action"}
        };

        JTable movieTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        movieTable.setFillsViewportHeight(false);
        JScrollPane movieTableSP = new JScrollPane(movieTable);
//        movieTableSP.setPreferredSize(false,200,300);


        JLabel lblHeading = new JLabel("Stock Quotes");
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));

        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 200);
        frame.setVisible(true);
    }
}
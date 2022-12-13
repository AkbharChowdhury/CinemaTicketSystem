package classes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import enums.FormDetails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

public class Invoice {
    public static final String INVOICE_FILE = "invoice.txt";
    public static final String INVOICE_FILE_NAME = "invoice.pdf";
    private PDDocument invoiceDocument;
    private int n;
    private int totalTicket;
    private String salesDate;
    private String showDate;
    private String showTime;
    private String firstname;
    private String lastname;
    private String movieTitle;
    private String rating;

    public Invoice() {

    }

    public Invoice(boolean pdf) {
        //Create Document
        invoiceDocument = new PDDocument();
        //Create Blank Page
        PDPage newpage = new PDPage();
        //Add the blank page
        invoiceDocument.addPage(newpage);

    }

    public int getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(int totalTicket) {
        this.totalTicket = totalTicket;
    }



    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getInvoiceDetails() {
        return """
                SELECT
                                    s.*,
                                    sh.show_date,
                                    sh.show_time,
                                    c.firstname,
                                    c.lastname,
                                    m.title,
                                    r.rating
                                FROM
                                    Sales s
                                JOIN ShowTimes sh ON
                                    sh.show_time_id = s.show_time_id
                                JOIN Movies m ON
                                    m.movie_id = sh.movie_id
                                JOIN Ratings r ON
                                    r.rating_id = m.rating_id
                                JOIN Customers c ON
                                    c.customer_id = s.customer_id
                             
                                WHERE
                                    s.customer_id = ?  
                                                           
                """;
    }

    public void generatePDFInvoice(List<Invoice> invoice, int i) throws ParseException, SQLException, FileNotFoundException {
        Database db = Database.getInstance();
        Ticket ticketDetails = db.getCustomerTicketType(LoginInfo.getCustomerID());
        var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);


        //get the page
        PDPage page = invoiceDocument.getPage(0);
        try {
//            double total = invoice.get(i).getPrice() * invoice.get(i).getTotalTicket();
            double total = ticketDetails.getPrice() * invoice.get(i).getTotalTicket();


            //Prepare Content Stream
            PDPageContentStream cs = new PDPageContentStream(invoiceDocument, page);

            //Writing Single Line text
            //Writing the Invoice title
            cs.beginText();
            cs.setFont(font, 20);

            cs.newLineAtOffset(140, 750);
            cs.showText(FormDetails.getInvoiceTitle());

            cs.endText();

            cs.beginText();
            cs.setFont(font, 18);
            cs.newLineAtOffset(180, 690);
            cs.showText(invoice.get(i).getMovieTitle());
            cs.endText();

            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(180, 660);
            cs.showText(String.format("Rating (%s)",(invoice.get(i).getRating())));
            cs.endText();


            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(180, 630);
            cs.showText(MessageFormat.format("Show Date/time ({0}, {1})",
                    Helper.formatDate(invoice.get(i).getShowDate()),
                    Helper.formatTime(invoice.get(i).getShowTime())

            ));
            cs.endText();


            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 610);

            cs.showText(Helper.capitalise(invoice.get(i).getFirstname()) +" "+ Helper.capitalise(invoice.get(i).getLastname()));

            cs.newLine();
            cs.showText("Purchase date: ");
            cs.endText();


            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(170, 610);
            cs.newLine();
            cs.showText(Helper.formatDate(invoice.get(i).getSalesDate()));


            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(80, 540);
            cs.showText("Ticket Type:");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(200, 540);
            cs.showText("Unit Price");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(310, 540);
            cs.showText("Quantity");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(410, 540);
            cs.showText("Price");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(80, 520);

            cs.showText(ticketDetails.getType());
            cs.newLine();
            cs.endText();

            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(200, 520);
            cs.showText(Helper.formatMoney(ticketDetails.getPrice()));
            cs.newLine();
            cs.endText();

            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(310, 520);

            cs.showText(String.valueOf(invoice.get(i).getTotalTicket()));
            cs.newLine();
            cs.endText();

            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(410, 520);
            cs.showText(Helper.formatMoney(total));
            cs.newLine();

            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(310, (500 - (20 * n)));
            cs.showText("Total: ");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            //Calculating where total is to be written using number of products
            cs.newLineAtOffset(410, (500 - (20 * n)));
            cs.showText(Helper.formatMoney(total));
            cs.endText();

            //Close the content stream
            cs.close();
            //Save the PDF
            invoiceDocument.save(Invoice.INVOICE_FILE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

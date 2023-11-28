package classes.models;


import classes.Database;
import classes.LoginInfo;
import classes.models.Ticket;
import classes.utils.Helper;
import enums.FormDetails;
import lombok.Data;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.format.FormatStyle;
import java.util.List;
@Data
public class Invoice {
    public static final String INVOICE_FILE = "invoice.txt";
    public static final String INVOICE_FILE_NAME = "invoice.pdf";
    private int n;
    private int totalTicket;
    private String salesDate;
    private String showDate;
    private String showTime;
    private String firstname;
    private String lastname;
    private String movieTitle;
    private String rating;
    private boolean isPDF;

    public Invoice() {

    }

    public Invoice(boolean isPDF) {
        this.isPDF = isPDF;


    }

    private static String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&", "%", "'"};

        for (String metaCharacter : metaCharacters) {
            if (inputString.contains(metaCharacter)) {
                inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
            }
        }
        return inputString;
    }


    public static String getDetails(Invoice invoice, double price) {
        return STR."\{invoice.getMovieTitle()}, \{ Helper.formatDate(invoice.getShowDate(), FormatStyle.MEDIUM)}, \{Helper.formatTime(invoice.getShowTime())}, \{ Helper.formatMoney(price * invoice.getTotalTicket())}";
    }

    public static String getInvoiceDetails() {
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
                                    ORDER BY strftime('%s', sales_date) DESC
                                                           
                """;
    }

    public void generatePDFInvoice(List<Invoice> invoice, int i) throws ParseException, SQLException, IOException {
        if (!isPDF){
            Helper.showErrorMessage("cannot generate invoice as PDF option not selected","Invoice error");
            System.exit(0);
        }


        var invoiceDocument = new PDDocument();
        invoiceDocument.addPage(new PDPage());

        Database db = Database.getInstance();
        Ticket ticketDetails = db.getCustomerTicketType(LoginInfo.getCustomerID());
        var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        var mInvoice = invoice.get(i);


        try {
            double total = ticketDetails.getPrice() * mInvoice.getTotalTicket();


            //Prepare Content Stream
            PDPageContentStream cs = new PDPageContentStream(invoiceDocument, invoiceDocument.getPage(0));

            //Writing Single Line text
            //Writing the Invoice title
            cs.beginText();
            cs.setFont(font, 20);

            cs.newLineAtOffset(140, 750);
            cs.showText(String.format("%s", FormDetails.getInvoiceTitle()));

            cs.endText();

            cs.beginText();
            cs.setFont(font, 18);
            cs.newLineAtOffset(180, 690);
            cs.showText(mInvoice.getMovieTitle());
            cs.endText();

            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(180, 660);
            cs.showText(String.format("Rating (%s)", (mInvoice.getRating())));
            cs.endText();


            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(180, 630);
            cs.showText(STR."Show Date/time (\{Helper.formatDate(mInvoice.getShowDate())}, \{Helper.formatTime(mInvoice.getShowTime())})");
            cs.endText();


            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 610);

            cs.showText(WordUtils.capitalizeFully(STR."\{mInvoice.getFirstname()} \{mInvoice.getLastname()}"));

            cs.newLine();
            cs.showText("Purchase date: ");
            cs.endText();


            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(170, 610);
            cs.newLine();
            cs.showText(Helper.formatDate(mInvoice.getSalesDate()));


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

            cs.showText(String.valueOf(mInvoice.getTotalTicket()));
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
            System.err.println(e.getMessage());

        } finally {
            invoiceDocument.close();
        }
    }


}

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.itextpdf.text.Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class invoice {
    PDDocument invc;
    int n;
    Integer total = 0;
    Integer price;
    String CustName;
    String CustPh;
    List<String> ProductName = new ArrayList<String>();
    List<Integer> ProductPrice = new ArrayList<Integer>();
    List<Integer> ProductQty = new ArrayList<Integer>();
    String InvoiceTitle = "Cinema Ticket System Ticket Invoice";
    String SubTitle = "Mirrors 2 (15)";

    //Using the constructor to create a PDF with a blank page
    invoice() {
        //Create Document
        invc = new PDDocument();
        //Create Blank Page
        PDPage newpage = new PDPage();
        //Add the blank page
        invc.addPage(newpage);
    }

    public static void main(String[] args) {
        invoice i = new invoice();
        i.getdata();
        i.WriteInvoice();
        System.out.println("Invoice Generated!");
    }

    //getdata() method is used to get the customer information and the product details from the input stream
    void getdata() {
//        System.out.println("Enter the Customer Name: ");
//        CustName = sc.nextLine();
//        System.out.println("Enter the Customer Phone Number: ");
//        CustPh = sc.next();
//        System.out.println("Enter the Number of Products: ");
//        n = sc.nextInt();
//        System.out.println();
//        for(int i=0; i<n; i++) {
//            System.out.println("Enter the Product Name: ");
//            ProductName.add(sc.next());
//            System.out.println("Enter the Price of the Product: ");
//            ProductPrice.add(sc.nextInt());
//            System.out.println("Enter the Quantity of the Product: ");
//            ProductQty.add(sc.nextInt());
//            System.out.println();
//            //Calculating the total amount
//            total = total + (ProductPrice.get(i)*ProductQty.get(i));
//        }
    }

    void WriteInvoice() {
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE);

        //get the page
        PDPage mypage = invc.getPage(0);
        try {
            //Prepare Content Stream
            PDPageContentStream cs = new PDPageContentStream(invc, mypage);

            //Writing Single Line text
            //Writing the Invoice title
            cs.beginText();

            cs.setFont(font, 20);
            cs.newLineAtOffset(140, 750);
            cs.showText(InvoiceTitle);
            cs.endText();

            cs.beginText();
            cs.setFont(font, 18);
            cs.newLineAtOffset(270, 690);
            cs.showText(SubTitle);
            cs.endText();


            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(220, 650);
            cs.showText("Show Date/time (21 Dec 2022, 7:00 pm)");
            cs.endText();

            //Writing Multiple Lines
            //writing the customer details
            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 610);
            cs.showText("Customer Name: ");
            cs.newLine();
            cs.showText("Purchase date: ");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(170, 610);
            String a = "Alan";
            cs.showText(a + "  Smith");
            cs.newLine();
            cs.showText(" 8 Dec 2022");
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
//            for(int i =0; i<n; i++) {
//                cs.showText(ProductName.get(i));
//                cs.newLine();
//            }
            cs.showText("Senior");
            cs.newLine();
            cs.endText();
//
            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(200, 520);
//            for(int i =0; i<n; i++) {
            cs.showText("£10.00");
            cs.newLine();
//            }
            cs.endText();

            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(310, 520);
//            for(int i =0; i<n; i++) {
            cs.showText("6");
            cs.newLine();
//            }
            cs.endText();
//
            cs.beginText();
            cs.setFont(font, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(410, 520);
            cs.showText("23");
            cs.newLine();
//            for (int i = 0; i < n; i++) {
////                price = ProductPrice.get(i)*ProductQty.get(i);
//                cs.showText("23");
//                cs.newLine();
//            }
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            cs.newLineAtOffset(310, (500-(20*n)));
            cs.showText("Total: ");
            cs.endText();

            cs.beginText();
            cs.setFont(font, 14);
            //Calculating where total is to be written using number of products
            cs.newLineAtOffset(410, (500-(20*n)));
            cs.showText("50");
            cs.endText();

            //Close the content stream
            cs.close();
            //Save the PDF
            invc.save("myInvoice.pdf");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package classes.utils;

import classes.Database;
import classes.LoginInfo;
import enums.Pages;
import forms.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public final class Helper {
    public static final String SEP = "///";

    public static String fieldSep(Object obj) {
        return obj + SEP;
    }


    public  static Supplier<String> getCSVPath = () -> "src/csv/";


    public static JSpinner disableSpinnerInput(JSpinner spinner) {
        var editor = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        editor.setEnabled(true);
        editor.setEditable(false);
        return spinner;
    }

    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return FileHandler.readSingleColumn(getCSVPath.get() + filename);
    }

    public static boolean disableReceipt(Database db) {
        return LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID());
    }


    public static String calcDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%d:%02d", hours, minutes);
    }

    public static void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message,
                title, JOptionPane.ERROR_MESSAGE);
    }

    public static void message(String message) {
        JOptionPane.showMessageDialog(null, message);
    }


    public static String formatTime(String time) {
        return DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.parse(time));

    }

    public static String formatMoney(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.UK).format(amount);
    }


    public static double calcPrice(int numTickets, double price) {
        return numTickets * price;
    }

    public static String formatDate(String date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(date));

    }

    public static String formatDate(String date, FormatStyle formatStyle) {
        return DateTimeFormatter.ofLocalizedDate(formatStyle).format(LocalDate.parse(date));

    }

    public static String convertMediumDateToYYMMDD(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM yyyy")).toString();
    }


    public static void goTo(JFrame currentPage, Pages page) {
        try {
            gotoForm(currentPage, page);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void gotoForm(JFrame currentPage, Pages page){
        try {
            switch (page) {
                case LOGIN -> new Login();
                case REGISTER -> new Register();
                case LIST_MOVIES -> new MovieList();
                case PURCHASE_TICKET -> new PurchaseTicket();
                case SHOW_RECEIPT -> new ShowReceipt();
                case SHOW_TIMES -> new ShowTimesForm();
            }
            currentPage.dispose();
        } catch (Exception ex){
            System.err.println(ex.getMessage());


        }

    }


}

package classes.utils;

import classes.Database;
import classes.Form;
import classes.LoginInfo;
import enums.Pages;
import enums.RedirectPage;
import forms.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public final class Helper {

    public static String getCSVPath() {
        return "src/csv/";
    }

    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return FileHandler.readSingleColumn(getCSVPath() + filename);
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

    public static String convertMediumDateToYYMMDD(String dateStr) throws ParseException {

        var formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return new SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(dateStr));
    }


    public static void goTo(JFrame currentPage, Pages page) {
        try {
            gotoForm(currentPage, page);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void gotoForm(JFrame currentPage, Pages page) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        switch (page) {
            case LOGIN -> new Login();
            case REGISTER -> new Register();
            case LIST_MOVIES -> new MovieList();
            case PURCHASE_TICKET -> new PurchaseTicket();
            case SHOW_RECEIPT -> new ShowReceipt();
            case SHOW_TIMES -> new ShowTimesForm();
        }
        currentPage.dispose();

    }


    public static boolean isCustomerLoggedIn(JFrame frame, RedirectPage page) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (LoginInfo.getCustomerID() == 0) {
            if (JOptionPane.showConfirmDialog(null, "You must be logged in to purchase tickets or print invoices, do you want to login?", "WARNING", JOptionPane.YES_NO_OPTION)
                    != JOptionPane.YES_OPTION
                    && !LoginInfo.hasOpenFormOnStartUp()) {
                System.err.println("You must be logged in to view invoices or purchase tickets!");
                System.exit(0);
            }

            Form.setRedirectPage(page);
            if (LoginInfo.hasOpenFormOnStartUp()) {
                Helper.gotoForm(frame, Pages.LOGIN);
                return false;
            }
            new Login();
            frame.dispose();


        }

        return LoginInfo.getCustomerID() != 0;

    }


}

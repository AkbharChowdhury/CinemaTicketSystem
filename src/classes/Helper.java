package classes;

import enums.Pages;
import enums.RedirectPage;
import forms.*;
import forms.ShowTimesForm;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Helper {

    public static String getCSVPath() {
        return "src/csv/";
    }

    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return FileHandler.readSingleColumn(getCSVPath() + filename);
    }

    public static String calcDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%d:%02d", hours, minutes);
    }
    public static String capitalise(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    //      JComboBox populateComboBox(Database db, JComboBox comboBox){
//        for(var item: db.showMovieGenreList()){
//            comboBox.addItem(item);
//        }
//        return comboBox;
//    }
    public static void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message,
                title, JOptionPane.ERROR_MESSAGE);
    }

    public static void message(String message) {
        JOptionPane.showMessageDialog(null, message);
    }



    public static String formatTime(String time) {
        String[] timeSplit = time.split(":");
        String timeColonPattern = "hh:mm a";

        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);
        LocalTime colonTime = LocalTime.of(hour, minute);

        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
        return timeColonFormatter.format(colonTime);

    }

    public static String formatMoney(double amount) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.UK);
        return currency.format(amount);
    }

    public static void validateNumber(KeyEvent e, JTextField textField) {
        char c = e.getKeyChar();
        if (Character.isLetter(c)) {
            // disable input if the value is not a number
            textField.setEditable(false);
        }

        boolean isNumber = !Character.isLetter(c) && e.getKeyChar() != 0;
        textField.setEditable(isNumber);
    }

    public static boolean validateMovieID(Database db, int movieID) {
        if (!db.movieIDExists(movieID)) {
            showErrorMessage("This movie ID does not exists. Please enter an existing movie ID", "Movie ID error");
            return false;
        }
        return true;

    }

    public static double calcPrice(int numTickets, double price) {
        return numTickets * price;
    }

    public static String formatDate(String date) {
        String[] dateSplit = date.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        LocalDate selectedDate = LocalDate.of(year, month, day);
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(selectedDate);

    }

    public static String convertMediumDateToYYMMDD(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        Date date = formatter.parse(dateStr);
        DateFormat  formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        return formatter2.format(date);
    }



    public static void gotoForm(JFrame currentPage, Pages page) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        switch (page) {

            case LOGIN -> {
                new Login();
                currentPage.dispose();
            }

            case REGISTER -> {
                new Register();
                currentPage.dispose();
            }

            case LIST_MOVIES -> {
                new MovieList();
                currentPage.dispose();
            }

            case PURCHASE_TICKET -> {
                new PurchaseTicket();
                currentPage.dispose();
            }

            case SHOW_RECEIPT -> {
                new ShowReceipt();
                currentPage.dispose();
            }


            case SHOW_TIMES -> {
                new ShowTimesForm();
                currentPage.dispose();
            }
        }

    }

    public static boolean isCustomerLoggedIn(JFrame frame, RedirectPage page) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (LoginInfo.getCustomerID() == 0) {
            int dialogButton = JOptionPane.showConfirmDialog(null, "You must be logged in to purchase tickets or print invoices, do you want to login?", "WARNING", JOptionPane.YES_NO_OPTION);

            if (dialogButton == JOptionPane.YES_OPTION) {

                Form.setRedirectPage(page);



                if (!LoginInfo.hasOpenFormOnStartUp()) {
                    try {

                        new Login();
                        frame.dispose();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else{
                    // go to login
                    Helper.gotoForm(frame, Pages.LOGIN);

                }


            } else {
                if (!LoginInfo.hasOpenFormOnStartUp()) {
                    System.err.println("You must be logged in to view invoices or purchase tickets!");
                    System.exit(0);

                }

            }


        }

        return LoginInfo.getCustomerID() != 0;

    }


}

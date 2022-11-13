package classes;

import enums.Files;
import enums.RedirectPage;
import forms.Login;
import forms.MovieList;
import forms.PurchaseTicket;

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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Helper {

    public static String getCSVPath(){
        return "src/csv/";
    }
    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return FileHandler.readSingleColumn(getCSVPath() + filename);
    }
    public static String calcDuration(int duration){
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format( "%d:%02d", hours, minutes);
    }


//      JComboBox populateComboBox(Database db, JComboBox comboBox){
//        for(var item: db.showMovieGenreList()){
//            comboBox.addItem(item);
//        }
//        return comboBox;
//    }
    public static void showErrorMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message,
                title, JOptionPane.ERROR_MESSAGE);
    }

    public static void message(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static String formatTime(String time) throws ParseException {
        DateFormat f1 = new SimpleDateFormat("hh:mm"); //12:30
        Date unFormattedTime = f1.parse(time);

        DateFormat formattedTime = new SimpleDateFormat("h:mm a"); // e.g. 12:30 AM
        return formattedTime.format(unFormattedTime); // "23:00"
    }
    public static String formatMoney(double amount) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.UK);
        return currency.format(amount);
    }
    public static void validateNumber(KeyEvent e, JTextField textField){
        char c = e.getKeyChar();
        if (Character.isLetter(c)) {
            // disable input if the value is not a number
            textField.setEditable(false);
        }

        boolean isNumber = !Character.isLetter(c) && e.getKeyChar() !=0;
        textField.setEditable(isNumber);
    }

    public static boolean validateMovieID(Database db, int movieID){
        if (!db.movieIDExists(movieID)){
            showErrorMessage("This movie ID does not exists. Please enter an existing movie ID","Movie ID error");
            return false;
        }
        return true;

    }
    public static double calcPrice(int numTickets, double price){
        return numTickets * price;
    }

    public static String formatDate(String date) {
        String[] dateSplit = date.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        LocalDate selectedDate = LocalDate.of(year, month, day);
        String fullDay = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(selectedDate);
        return fullDay;
    }

    public static boolean validateMovieShowTime(Database db, MovieShowTimes movieShowTimes, int movieID){
        if (db.showMovieTimes(movieShowTimes).size() == 0 ){
            Helper.showErrorMessage("There are no show times for " + db.getMovieName(movieID),"Show time error");
            return true;
        }
        return false;
    }
    public  static void gotoForm(JFrame frame) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> className;


        try {
            new PurchaseTicket();

            frame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isCustomerLoggedIn(JFrame frame, RedirectPage page){

        if (LoginInfo.getCustomerID() == 0){
            int dialogButton = JOptionPane.showConfirmDialog (null, "You must be logged in to purchase tickets or print invoices, do you want to login?","WARNING",JOptionPane.YES_NO_OPTION);

            if (dialogButton == JOptionPane.YES_OPTION){
                Form.setRedirectPage(page);
                try {
                    frame.dispose();

                    new Login();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
//
//            if (page == RedirectPage.SHOW_RECEIPT || page == RedirectPage.PURCHASE){
//                try {
//                    frame.dispose();
//                    new MovieList();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            }

        }

        return LoginInfo.getCustomerID() != 0;

    }


}

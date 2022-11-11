package classes;

import enums.Files;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}

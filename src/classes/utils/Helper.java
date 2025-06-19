package classes.utils;

import classes.Database;
import classes.LoginInfo;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Helper {


    public static final String SEP = "///";

    public static String fieldSep(Object obj) {
        return obj + SEP;
    }




    public static void disableSpinnerInput(JSpinner spinner) {
        JFormattedTextField editor = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        editor.setEnabled(true);
        editor.setEditable(false);
    }



    public static Function<Database, Boolean> disableReceipt = db -> LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID());


    public static String calcDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%d:%02d", hours, minutes);
    }

    public static void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static BiConsumer<String, String> dis = (message, title) -> JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    public static Consumer<String> message = message -> JOptionPane.showMessageDialog(null, message);
    public static Function<Double, String> formatMoney = amount -> NumberFormat.getCurrencyInstance(Locale.UK).format(amount);


}

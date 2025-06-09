package classes;

import enums.Pages;
import enums.RedirectPage;
import forms.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

public final class Form {

    @Setter
    @Getter
    private static RedirectPage redirectPage;

    private Form() {
    }

    public static RedirectPage getRedirectPage() {
        return redirectPage;
    }

    public static void setRedirectPage(RedirectPage redirectPage) {
        Form.redirectPage = redirectPage;
    }


    public static void goTo(JFrame currentPage, Pages page) {
        try {
            gotoForm(currentPage, page);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void gotoForm(JFrame currentPage, Pages page) {
        redirectPage(page);
        currentPage.dispose();

    }

    private static void redirectPage(Pages page) {
        try {
            switch (page) {
                case LOGIN -> new Login();
                case REGISTER -> new Register();
                case LIST_MOVIES -> new MovieList();
                case PURCHASE_TICKET -> new PurchaseTicket();
                case SHOW_RECEIPT -> new ShowReceipt();
                case SHOW_TIMES -> new ShowTimesForm();
            }
        } catch (Exception ex) {
            System.err.println(STR."There was a problem redirecting to \{page}\n\{ex.getMessage()}");
        }
    }
}

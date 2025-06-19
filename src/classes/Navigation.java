package classes;

import classes.utils.Helper;
import enums.Buttons;
import enums.RedirectPage;
import forms.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;


public final class Navigation implements ActionListener {
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show Times");
    private final JButton btnPurchase = new JButton("Purchase Ticket");
    public final JButton btnShowReceipt = new JButton("Show Receipt");
    private final JFrame frame;


    private final Supplier<JButton[]> navButtons = () -> new JButton[]{
            btnListMovies,
            btnShowTimes,
            btnPurchase,
            btnShowReceipt
    };

    public void receiptStatus(Database database) {

        Function<Database, Boolean> isReceiptButtonDisabled = db -> LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID());
        if (isReceiptButtonDisabled.apply(database)) {
            btnShowReceipt.setEnabled(false);

        }

    }

    public Navigation(JFrame currentFrame) {
        frame = currentFrame;
        Arrays.stream(navButtons.get()).forEach(button -> button.addActionListener(this));
        Buttons.handCursor.accept(navButtons.get());
    }

    private void purchaseTicket() {
        try {
            if (LoginInfo.getCustomerID() != 0) {
                new PurchaseTicket();
                return;
            }

            LoginInfo.setHasOpenFormOnStartUp(true);

            if (JOptionPane.showConfirmDialog(null, "You must be logged in to purchase tickets or print invoices, do you want to login?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Form.setRedirectPage(RedirectPage.PURCHASE_TICKET);
                new Login();

            }

            if (!LoginInfo.hasOpenFormOnStartUp()) {
                System.err.println("You must be logged in to view invoices or purchase tickets!");
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showReceipt() {
        try {
            LoginInfo.setHasOpenFormOnStartUp(true);
            new ShowReceipt();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    public void handleNavClick(ActionEvent e) {
        try {

            if (e.getSource() == btnListMovies) new MovieList();
            if (e.getSource() == btnShowTimes) new ShowTimesForm();
            if (e.getSource() == btnPurchase) purchaseTicket();
            if (e.getSource() == btnShowReceipt) showReceipt();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleNavClick(e);
        frame.dispose();
    }

    public void addButtons(JPanel top) {
        Arrays.stream(navButtons.get()).forEach(top::add);
    }
}


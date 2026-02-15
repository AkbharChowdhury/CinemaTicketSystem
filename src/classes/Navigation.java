package classes;

import enums.Buttons;
import enums.RedirectPage;
import forms.*;

import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;


public final class Navigation implements ActionListener {
    private final JButton btnListMovies = new JButton("List Movies");
    private final JButton btnShowTimes = new JButton("Show Times");
    private final JButton btnPurchase = new JButton("Purchase Ticket");
    public final JButton btnShowReceipt = new JButton("Show Receipt");
    private final JFrame frame;
    private final Function<Database, Boolean> isReceiptButtonDisabled = db -> LoginInfo.getCustomerID() == 0 | !db.customerInvoiceExists(LoginInfo.getCustomerID());
    private final List<JButton> navButtons = List.of(btnListMovies, btnShowTimes, btnPurchase, btnShowReceipt);

    public void receiptStatus(Database database) {

        if (isReceiptButtonDisabled.apply(database)) {
            btnShowReceipt.setEnabled(false);

        }

    }

    public Navigation(JFrame currentFrame) {
        frame = currentFrame;
        navButtons.forEach(button -> button.addActionListener(this));
        Buttons.handCursor.accept(navButtons.toArray(new JButton[0]));
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

    private final Map<JButton, Runnable> buttonActions = Map.of(
            btnListMovies, this::openMovieList,
            btnShowTimes, this::openShowTimes,
            btnPurchase, this::purchaseTicket,
            btnShowReceipt, this::showReceipt
    );

    private void openMovieList() {
        new MovieList();

    }

    private void openShowTimes() {
        new ShowTimesForm();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Runnable action = buttonActions.get(e.getSource());
        if (action != null) {
            action.run();
        }
        // Close current frame after button click
        frame.dispose();
    }

    public void addButtons(JPanel top) {
        navButtons.forEach(top::add);
    }
}


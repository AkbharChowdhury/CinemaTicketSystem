package classes;

import enums.RedirectPage;
import forms.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public final class Navigation implements ActionListener {
    private final JButton btnListMovies = new JButton("List Movies");
    public final JButton btnShowTimes = new JButton("Show Times");
    public final JButton btnPurchase = new JButton("Purchase Ticket");
    public final JButton btnShowReceipt = new JButton("Show Receipt");
    private final JFrame frame;

    public JButton[] navButtons() {

        return new JButton[]{
                btnListMovies,
                btnShowTimes,
                btnPurchase,
                btnShowReceipt
        };
    }

    public Navigation(JFrame frame) {
        this.frame = frame;
        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchase.addActionListener(this);
        btnShowReceipt.addActionListener(this);
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
}


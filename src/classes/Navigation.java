package classes;

import enums.Buttons;
import enums.RedirectPage;
import forms.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Navigation extends JFrame implements ActionListener {
    public final JButton btnListMovies = new JButton(Buttons.listMovies());
    public final JButton btnShowTimes = new JButton(Buttons.showTimes());
    public final JButton btnPurchase = new JButton(Buttons.purchaseTicket());
    public final JButton btnShowReceipt = new JButton(Buttons.showReceipt());

    public Navigation() {
        btnListMovies.addActionListener(this);
        btnShowTimes.addActionListener(this);
        btnPurchase.addActionListener(this);
        btnShowReceipt.addActionListener(this);

    }


    public boolean handleNavClick(ActionEvent e) {
        try {

            if (e.getSource() == btnListMovies) {
                new MovieList();
                return true;
            }
            if (e.getSource() == btnShowTimes) {
                new ShowTimesForm();
                return true;
            }

            if (e.getSource() == btnPurchase) {

                if (LoginInfo.getCustomerID() == 0) {
                    LoginInfo.setHasOpenFormOnStartUp(true);
                }

                if (LoginInfo.getCustomerID() == 0) {

                    if (JOptionPane.showConfirmDialog(null, "You must be logged in to purchase tickets or print invoices, do you want to login?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        Form.setRedirectPage(RedirectPage.PURCHASE_TICKET);
                        new Login();
                        return true;

                    } else {

                        if (!LoginInfo.hasOpenFormOnStartUp()) {
                            System.err.println("You must be logged in to view invoices or purchase tickets!");
                            System.exit(0);
                        }

                        // do not close form
                        return false;

                    }
                }
                // user is logged in
                new PurchaseTicket();
                return true;
            }

            if (e.getSource() == btnShowReceipt) {
                LoginInfo.setHasOpenFormOnStartUp(true);
                new ShowReceipt();
                return true;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}


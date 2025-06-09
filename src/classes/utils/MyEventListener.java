package classes.utils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyEventListener {
    private MyEventListener(){

    }
     private static void enterKey(KeyEvent e, Runnable r) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) r.run();

    }


    public static void enterKeyStroke(JTextField textField, Runnable action) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                enterKey(e, action);

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }


        });

    }
}

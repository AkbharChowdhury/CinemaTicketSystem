package classes.utils;

import java.awt.event.KeyEvent;

public class MyEventListener {
    private MyEventListener(){

    }
    public static void enterKey(KeyEvent e, Runnable r) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) r.run();

    }
}

package enums;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class Buttons {
    public static final String REGISTER = "Register";
    public static Consumer<JButton[]> handCursor = (buttons) ->
            Arrays.stream(buttons)
                    .forEach(button -> button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

}

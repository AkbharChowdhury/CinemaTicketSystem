package enums;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class Buttons {
    public static final String REGISTER = "Register";
    private static final Cursor CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    public static Consumer<JButton[]> handCursor = (buttons) ->
            Arrays.stream(buttons)
                    .forEach(button -> button.setCursor(CURSOR));

}

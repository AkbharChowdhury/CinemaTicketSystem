import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var drinks = new ArrayList<>(Arrays.asList("mango juice deluxe", "hot chocolate"));
        var foods = new ArrayList<>(Arrays.asList("croissants", "cookies and cream desserts"));
        var groceries = new ArrayList<>(Arrays.asList(foods, drinks));
        groceries.forEach(list-> list.forEach(System.out::println));


    }

}


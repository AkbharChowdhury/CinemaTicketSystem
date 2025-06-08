import java.util.LinkedHashMap;

public class TS {
    public static void main(String[] args) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "S");
        map.put(2, null);
        map.put(3, null);
        map.put(4, null);
        map.put(5, null);
        System.out.println(map.get(1));


    }
}

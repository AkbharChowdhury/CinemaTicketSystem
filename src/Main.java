import classes.Counter;

public class Main {
    public static void main(String[] args) {
        var counter = new Counter();
        int count = counter.getCounter();
        System.out.println(count);
        System.out.println(counter.getCounter());


    }
}

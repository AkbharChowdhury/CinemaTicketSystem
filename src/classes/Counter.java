package classes;

public class Counter {
    private int counter = 0;

    public int getCounter() {
        return ++counter;
    }

    public Counter() {

    }
    public Counter(boolean isZero) {
        if (isZero) counter--;

    }
}

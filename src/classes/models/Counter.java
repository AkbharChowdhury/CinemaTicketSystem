package classes.models;

public class Counter {
    private int counter = 0;

    public int getCounter() {
        return ++counter;
    }

    public Counter() {

    }
    public Counter(boolean isZero) {
        counter--;
    }
}

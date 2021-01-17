package HomeWork01;

import java.util.ArrayList;

public class Box<T extends Fruit> {

    private final ArrayList<T> fruits;

    public Box() {
        fruits = new ArrayList<>();
    }

    public void add(T fruit) {
        fruits.add(fruit);
    }

    public float getWeight() {
        float sum = 0;
        for (T fruit : fruits) {
            sum += fruit.getWeight();
        }
        return sum;
    }

    public int getItemsNumber() {
        return fruits.size();
    }

    public boolean compare(Box<? extends Fruit> anotherFruitBox) {
        return getWeight() == anotherFruitBox.getWeight();
    }

    public void pourOver(Box<T> destinationBox) {
        for (int i = fruits.size()-1; i > -1; i--) {
        destinationBox.add(fruits.get(i));
        fruits.remove(i);
        }
    }
}

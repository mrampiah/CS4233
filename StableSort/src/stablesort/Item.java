package stablesort;

/**
 * Created by mrampiah on 2/4/17.
 * Class created to test bubble sort method.
 * Two items with different tags but same values
 */
public class Item implements Comparable<Item> {
    private int value;
    private String tag;

    public Item(int value, String tag) {
        this.value = value;
        this.tag = tag;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Item o) {
        return value - o.getValue();
    }
}

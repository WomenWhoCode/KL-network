package wwckl.projectmiki.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Receipt {

    /**
     * An array of receipt items.
     */
    public static List<Item> ITEMS = new ArrayList<>();

    /**
     * A map of receipt items, by ID.
     */
    public static Map<Integer, Item> ITEM_MAP = new HashMap<>();

    static {
        // Add 3 sample items.
        addItem(new Item(1, "Item 1"));
        addItem(new Item(2, "Item 2"));
        addItem(new Item(3, "Item 3"));
    }

    private static void addItem (Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }
}

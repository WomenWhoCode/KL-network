package wwckl.projectmiki.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Receipt model
 *
 * TODO : move more stuff from old model
 */
public class Receipt implements Serializable {

    // An array of receipt items.
    private List<Item> mItems = new ArrayList<>();

    // A map of receipt items, by ID.
    private Map<Integer, Item> mItemsMap = new HashMap<>();


    public int getTotalItems () {
        return getItems().size();
    }

    public Item getItem(int index) {
        if (index < getTotalItems()) {
            return getItems().get(index);
        }

        return null;
    }

    public void addItem (Item item) {
        getItems().add(item);
        getItemsMap().put(item.getId(), item);
    }

    public List<Item> getItems () {
        return mItems;
    }

    public Map<Integer, Item> getItemsMap () {
        return mItemsMap;
    }
}


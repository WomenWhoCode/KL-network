package wwckl.projectmiki.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Plain object to hold receipt details
 *
 * Reference:
 * Why Parcelable? - https://coderwall.com/p/vfbing/passing-objects-between-activities-in-android
 *
 * TODO : move more stuff from old model
 */
public class Receipt implements Parcelable {

    // An array of receipt items.
    private List<Item> mItems = new ArrayList<>();

    /**
     * Empty constructor
     */
    public Receipt () {

    }

    /**
     * Use when reconstructing User object from parcel
     * This will be used only by the 'CREATOR'
     *
     * @param in a parcel to read this object
     */
    private Receipt (Parcel in) {
        in.readList(mItems, Item.class.getClassLoader());
    }


    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel (Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray (int size) {
            return new Receipt[size];
        }
    };

    public int getTotalItems () {
        return getItems().size();
    }

    public Item getItem (int index) {
        if (index < getTotalItems()) {
            return getItems().get(index);
        }

        return null;
    }

    public void addItem (Item item) {
        getItems().add(item);
    }

    public List<Item> getItems () {
        return mItems;
    }

    @Override
    public int describeContents () {
        return 0;
    }


    /**
     * Actual object serialization happens here, Write object content
     * to parcel one by one, reading should be done according to this write order
     *
     * @param dest  parcel
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeList(mItems);
    }
}


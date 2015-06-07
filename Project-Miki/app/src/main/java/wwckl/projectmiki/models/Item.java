package wwckl.projectmiki.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cheeyim on 2/2/2015.
 *
 * For understanding of Parcelable usage see Receipt
 */
public class Item implements Parcelable {

    private int    id       = 0;
    private String name     = "";
    private Double price    = 0.0;
    private int    quantity = 0;

    public Item (int id, String name) {
        this.id = id;
        this.name = name;
    }

    private Item (Parcel in) {
        id = in.readInt();
        name = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel (Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray (int size) {
            return new Item[size];
        }
    };

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public Double getPrice () {
        return price;
    }

    public void setPrice (Double price) {
        this.price = price;
    }

    public int getQuantity () {
        return quantity;
    }

    public void setQuantity (int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString () {
        return this.name;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeDouble(price);
    }
}

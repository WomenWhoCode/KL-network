package wwckl.projectmiki.models;

/**
 * Created by cheeyim on 2/2/2015.
 */
public class Item {

    private int id;
    private String name;
    private Double price;
    private int quantity;

    public Item (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString () {
        return this.name;
    }
}

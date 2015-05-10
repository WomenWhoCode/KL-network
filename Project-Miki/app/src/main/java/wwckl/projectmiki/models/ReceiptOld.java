package wwckl.projectmiki.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheeyim on 2/2/2015.
 */
public class ReceiptOld {

    private Double total;
    private Double subTotal;
    private int noOfPplSharing;
    private List<Item> listOfAllItems = new ArrayList<Item>();
    private List<Item> listOfSelectedItems = new ArrayList<Item>();
    private List<Item> listOfRemainingItems = new ArrayList<Item>();

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public int getNoOfPplSharing() {
        return noOfPplSharing;
    }

    public void setNoOfPplSharing(int noOfPplSharing) {
        this.noOfPplSharing = noOfPplSharing;
    }

    public List<Item> getListOfAllItems() {
        return listOfAllItems;
    }

    public void setListOfAllItems(List<Item> listOfAllItems) {
        this.listOfAllItems = listOfAllItems;
    }

    public List<Item> getListOfSelectedItems() {
        return listOfSelectedItems;
    }

    public void setListOfSelectedItems(List<Item> listOfSelectedItems) {
        this.listOfSelectedItems = listOfSelectedItems;
    }

    public List<Item> getListOfRemainingItems() {
        return listOfRemainingItems;
    }

    public void setListOfRemainingItems(List<Item> listOfRemainingItems) {
        this.listOfRemainingItems = listOfRemainingItems;
    }
}

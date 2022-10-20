package model;

public class LineTable {

    // == Fields ==
    private HeaderTable invoice;
    private String itemName;
    private double itemPrice;
    private int count;

    // == Constructors ==
    public LineTable(HeaderTable invoice, String itemName, double itemPrice, int count) {
        this.invoice = invoice;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    // == Getter and Setter Methods ==
    public HeaderTable getInvoice() {
        return invoice;
    }

    public void setInvoice(HeaderTable invoice) {
        this.invoice = invoice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // == toString method ==
    @Override
    public String toString() {
        return "\t" + "InvoiceLine{itemName=" + itemName + ", itemPrice="
                + itemPrice + ", count=" + count + "}\n\t";
    }

}


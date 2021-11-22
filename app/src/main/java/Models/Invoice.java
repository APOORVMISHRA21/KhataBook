package Models;

public class Invoice {
    private String product_name, serialNumber;
    private int numberOfItems, costPerBox, totalAmount;
    private double billedDate;

    public Invoice(){ }

    public Invoice(String product_name, int numberOfItems,
                   int costPerBox, int totalAmount,
                   double billedDate){

        this.product_name = product_name;
        this.numberOfItems = numberOfItems;
        this.costPerBox = costPerBox;
        this.totalAmount = totalAmount;
        this.billedDate = billedDate;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getCostPerBox() {
        return costPerBox;
    }

    public void setCostPerBox(int costPerBox) {
        this.costPerBox = costPerBox;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getBilledDate() {
        return billedDate;
    }

    public void setBilledDate(double billedDate) {
        this.billedDate = billedDate;
    }
}

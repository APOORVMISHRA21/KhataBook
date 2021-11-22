package Models;

public class Inventory {

    String productId, product_name;
    int quantity, cp, sp;

    public Inventory(String productId, String product_name, int quantity,
                     int costPrice, int sellingPrice){

        this.productId = productId;
        this.product_name = product_name;
        this.quantity = quantity;
        this.cp = costPrice;
        this.sp = sellingPrice;
    }

    public Inventory(){}

    public String getProductId() {
        return productId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCp() {
        return cp;
    }

    public int getSp() {
        return sp;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }
}

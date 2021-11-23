package Models;

public class Invoice {
    private String customer_name, customer_mail, product_name;
    private int product_quantity, product_discount;
    private long timestamp;
    private float total_amount;


    public Invoice(){}

    public Invoice(String customer_name, String customer_mail,
                   String product_name,
                   int product_quantity, int product_discount,
                   long timestamp, float total_amount){
        this.product_name = product_name;
        this.customer_mail = customer_mail;
        this.customer_name = customer_name;
        this.product_quantity = product_quantity;
        this.product_discount = product_discount;
        this.timestamp = timestamp;
        this.total_amount = total_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mail() {
        return customer_mail;
    }

    public void setCustomer_mail(String customer_mail) {
        this.customer_mail = customer_mail;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(int product_discount) {
        this.product_discount = product_discount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

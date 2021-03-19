package model;

/**
 * Orders is the main entity we will be using to store information about the orders in the warehouse like: client, product and quantity.
 * The information can be accessed using getters and setters.
 */

public class Orders {
    private String client;
    private String product;
    private int quantity;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Orders(String client, String product, int quantity) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;
    }
}

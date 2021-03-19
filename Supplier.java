package model;

/**
 * Supplier is the main entity we will be using to store information about the suppliers the warehouse works with like companyName, product and quantity.
 * This information can be accessed using getters and setters.
 */

public class Supplier {
    private String companyName;
    private String product;
    private int quantity;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Supplier(String companyName, String product, int quantity) {
        this.companyName = companyName;
        this.product = product;
        this.quantity = quantity;
    }
}

package model;

/**
 * Client is the main entity we will be using to store information about clients in the warehouse like: name and address.
 * The information can be accessed using getters and setters.
 */
public class Client {
    private String name;
    private String address;

    public Client(String name, String address){
        this.name=name;
        this.address=address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

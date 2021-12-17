package bankomat.com;

public class User {

    String name;
    int customerID;

    public User(String name, int id) {
        this.name = name;
        this.customerID = id;
    }

    public String getName() {
        return name;
    }
}

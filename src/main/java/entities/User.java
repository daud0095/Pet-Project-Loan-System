package entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;
    private List<Product> products;

    public User (int id, String username, String password, boolean isAdmin){
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.products = new ArrayList<>();
    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getter
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public boolean isAdmin() {return isAdmin;}
    public List<Product> getProducts() {return products;}

    public int getId() {
        return id;
    }

    // Setter
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}

    public void setId(int id) {
        this.id = id;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void chanceProduct(Product product){

        for (Product p : products) {
            if (p.getId() == product.getId()) {
                p.setStatus(Status.Ledigt);
                p.setStock(product.getStock() + 1);
            }
        }


    }







}


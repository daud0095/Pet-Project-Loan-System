package entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;
    private List<Product> products;

    public User (String username, String password, boolean isAdmin){
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.products = new ArrayList<>();
    }



    // Getter
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public boolean isAdmin() {return isAdmin;}
    public List<Product> getProducts() {return products;}

    // Setter
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}

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
}


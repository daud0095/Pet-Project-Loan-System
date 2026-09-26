package services;

import entities.Product;
import entities.User;
import factories.ProductFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static List<Product> products;
    private static UserService userService;

    // Når man opretter et nyt objekt med ProduktService, tilføjes alle produkter automatisk til listen.
    public ProductService() {
        this.products = ProductFactory.createProducts();
        userService = UserService.getInstance();
    }

    // man kan tilføje et nyt produkt.
    public static void addProduct(Product product) {
        products.add(product);
    }

    // eller fjerne
    public static void removeProduct(Product product) {
        products.remove(product);
    }

    // man kan finde et product i forhold til name
    public Product findProduct(int id) {

        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    // vi kan hente alle produkter med get
    public static List<Product> getProducts() {
        return products;
    }


    // Vi kan finde flere produkter i forhold til name
    // F eks kan der være flere computer
    // Derfor finder vi produkter men vi skal holde en list.
    // Hvis man taster noget i søgefunction, kan flere værdi komme her
    // Søgelisten returneres.
    public static List<Product> searchProduct(String name){
        // Todo søg efter produkter i produkt factory.
        // Kig i javascript kode.
        List<Product> search = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toUpperCase().startsWith(name.toUpperCase())) {
                search.add(product);
            }
        }

        return search;
    }

    public static User findUser(String user){
        List<User> users = userService.getUsers();
        for(User user1 : users){
            if(user1.getUsername().equals(user)){
                return user1;
            }
        }
        return null;
    }
}

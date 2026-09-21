package services;

import entities.Product;
import factories.ProductFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private static List<Product> products;

    // Når man opretter et nyt objekt med ProduktService, tilføjes alle produkter automatisk til listen.
    public ProductService() {
        this.products = ProductFactory.createProducts();
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
    public static Product findProduct(String name) {

        for (Product product : products) {
            if (product.getName().toUpperCase().equals(name.toUpperCase())) {
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
}

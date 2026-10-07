package services;

import entities.Product;
import entities.User;
import exceptions.DatabaseException;
import persistence.ConnectionPool;
import persistence.ProductMapper;
import java.util.List;

public class ProductService {
    private UserService userService;
    private ProductMapper productMapper;
    private ConnectionPool connectionPool;

    // Når man opretter et nyt objekt med ProduktService, tilføjes alle produkter automatisk til listen.
    public ProductService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        productMapper = new ProductMapper(connectionPool);
        userService = new UserService(connectionPool);
    }

    // man kan finde et product i forhold til name
    public Product findProduct(int id) throws DatabaseException {
        return productMapper.getProductsById(id);
    }

    public Product findProductByName(String name) throws DatabaseException {
        return productMapper.getProductsByName(name);
    }

    // vi kan hente alle produkter med get
    public List<Product> getProducts() throws DatabaseException {
        return productMapper.getProducts();
    }

    // Vi kan finde flere produkter i forhold til name
    // F eks kan der være flere computer
    // Derfor finder vi produkter men vi skal holde en list.
    // Hvis man taster noget i søgefunction, kan flere værdi komme her
    // Søgelisten returneres.
    public List<Product> searchProduct(String name) throws DatabaseException {
        // Todo søg efter produkter i produkt factory.
        // Kig i javascript kode.
        return productMapper.getProductsByNameList(name);
    }

    public User findUser(String user) throws DatabaseException {
        return userService.getUser(user);
    }

    public Product findProductByName(User user, String name) {

        for (Product product : user.getProducts()) {
            if (product.getName().toUpperCase().equals(name.toUpperCase())) {
                return product;
            }
        }

        return null;
    }

    public void updateProductStockMinus(int id) throws DatabaseException {
        Product product = productMapper.getProductsById(id);
        productMapper.updateProductStockById(id, product.getStock()-1);
    }

    public void updateProductStockPlus(int id) throws DatabaseException {
        Product product = productMapper.getProductsById(id);
        productMapper.updateProductStockById(id, product.getStock()+1);
    }

    public void updateProductStatus(int id, String status) throws DatabaseException {
        Product product = productMapper.getProductsById(id);
        productMapper.updateProductStatusById(id, status);
    }

    public List<Product> getUsersProduct(User user) throws DatabaseException {
        return productMapper.getUsersProducts(user);
    }

    public List<Product> getUsersProductByStatus(User user, String status) throws DatabaseException {
        return productMapper.getUsersProductsByStatus(user, status);
    }

    public Product getUsersProduct(User user, Product product) throws DatabaseException {
        return productMapper.getUsersProductsByProduct(user, product);
    }


}

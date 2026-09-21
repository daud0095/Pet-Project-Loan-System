package controllers;

import entities.Product;
import io.javalin.config.JavalinConfig;
import services.ProductService;

import java.util.List;

public class ProductController {

    // vi opretter først ProduktService her for at hente alle produkter
    static ProductService productService = new ProductService();

    public static void setRoutes(JavalinConfig config){

        // for at sende alle udstyr til javascript
        // /api/products  ==>  javascript fanger denne url for at hente alle produkter
        // Denne routes sender alle produkter videre til javascript ved hjælpe af  "productService.getProducts()"
        // ProduktServices konstruktør har "ProductFactory.createProducts()"
        // ctx.json omdanner dataerne til json
        List<Product> products = productService.getProducts();

        config.routes.get("/products", ctx -> {
            ctx.json(products);
        });



    }


}

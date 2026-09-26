package controllers;

import entities.Product;
import entities.Status;
import entities.User;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import services.ProductService;
import services.UserService;

import java.time.LocalDate;
import java.util.List;

public class ProductController {

    // vi opretter først ProduktService her for at hente alle produkter
    static ProductService productService = new ProductService();
    static UserService userService = UserService.getInstance();

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

        // Denne router kalder lån-knap
        config.routes.get("/loan", ctx -> loan(ctx));  // for lån-knap
        config.routes.get("/confirm", ctx -> confirm(ctx));
        config.routes.get("/myloan", ctx -> myLoan(ctx));


    }

    public static void myLoan(Context ctx){
        User user = ctx.sessionAttribute("user");
        List<Product> products = user.getProducts();
        ctx.attribute("products", products);
        ctx.render("myLoan");
    }

    private static void loan(Context ctx) {

        // vi tilgår id
        int id = Integer.parseInt(ctx.queryParam("id"));
        ctx.attribute("id", id);

        Product product = productService.findProduct(id);
        ctx.attribute("product", product);

        ctx.attribute("date", LocalDate.now());

        List<User> users = userService.getUsers();
        ctx.attribute("users", users);

        // denne id bliver sendt til loan.html
        // fordi senere kan vi bruge denne id for at hente udstyr
        ctx.render("loan");

    }

    private static void confirm(Context ctx){

        // vi tilgår product-id
        int id = Integer.parseInt(ctx.queryParam("productid"));
        System.out.println(id);
        Product product = productService.findProduct(id);
        System.out.println(product);

        // vi updaterer stock
        product.setStock(product.getStock() - 1);

        // vi updaterer status, hvis stock = 0
        if(product.getStock() == 0){
            product.setStatus(Status.Udlånt);
        }

        // vi tilgår loan på hjemmeside
        String loan = ctx.queryParam("loan");
        User user = productService.findUser(loan);
        System.out.println(loan);

        // vi tilgår afleveringsdato på hjemmeside
        LocalDate afleveringsdato = LocalDate.parse(ctx.queryParam("afleveringsdato"));
        System.out.println(afleveringsdato);

        // Når brugeren låner udstyr, kan brugeren have på sin egen kurv
        user.addProduct(new Product(product.getId(), product.getPicturePath(), product.getName(), product.getDescription(), product.getStock(), product.getStatus(), afleveringsdato, product.getLoanDate()));

        System.out.println(user.getProducts());

        ctx.render("confirm");

    }


}

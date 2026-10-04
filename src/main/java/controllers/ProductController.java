package controllers;

import entities.Product;
import entities.Status;
import entities.User;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import services.ProductService;
import services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
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
        config.routes.get("/returnere", ctx->  adminreturn(ctx));

        // Denne routes fungerer for adminreturn.js
        // adminreturn.js fanger "findproduct" rotues
        config.routes.get("/findproduct", ctx->  getProduct(ctx));


        config.routes.get("/brugere", ctx -> ctx.render("brugere"));
        config.routes.get("/administration", ctx->  administration(ctx));
        config.routes.get("/returnConfirm", ctx -> returnConfirm(ctx));

    }

    public static void returnConfirm(Context ctx){
        String loan = ctx.queryParam("loan");
        User user = productService.findUser(loan);
        String product = ctx.queryParam("udstyr");
        Product product1 = productService.findProductByName(product);

        user.getProducts().remove(product1);
        product1.setStock(product1.getStock()+1);
        product1.setStatus(Status.Ledigt);
        ctx.attribute("user", user);
        ctx.attribute("product", product1);

        ctx.render("returnconfirm");


    }

    public static void administration(Context ctx) {
        ctx.render("administration");
    }

    public static void adminreturn(Context ctx) {

        // for search er method get. vi tilgår search fordi vi søge først efter en bruger
        String search = ctx.queryParam("search");
        if (search != null) {
            User user = productService.findUser(search);

            if (user == null) {
                String msg = "Findes ikke brugere. Prøv igen";
                ctx.attribute("msg", msg);
            } else {
                ctx.attribute("user", user);

                // vi finder users producter
                List<Product> products = user.getProducts();
                ctx.attribute("products", products);
                ctx.attribute("productlength", products.size());

            }

        }

        ctx.render("adminreturn");
    }

    // Denne method er for adminreturn.js
    // Json bliver sendt til adminreturn.js (product)
    public static void getProduct(Context ctx) {
        String udstyr = ctx.queryParam("udstyr");
        String loan = ctx.queryParam("loan");

        User user = productService.findUser(loan);
        Product product = productService.findProductByName(user, udstyr);

        if (product == null) {
            ctx.status(404).result("Produktet blev ikke fundet");
            return;
        }

        ctx.json(product);

    }

    public static void myLoan(Context ctx){

        // vi henter user
        User user = ctx.sessionAttribute("user");

        //  ************************
        //  Aktive loan og afleveret loan producter findes her

        // "Aktive", "Afleveret" eller null
        String filter = ctx.queryParam("filter");
        // Alle users product
        List<Product> products = new ArrayList<>();
        for (Product product : user.getProducts()){
            if("afleveret".equals(filter)){
                if(product.getStatus() != Status.Udlånt) products.add(product);
            } else {
                if(product.getStatus() == Status.Udlånt) products.add(product);
            }
        }

        // vi sender products videre til myloan.html
        ctx.attribute("products", products);
        ctx.attribute("filter", filter);
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
        Product product = productService.findProduct(id);

        // vi updaterer stock
        product.setStock(product.getStock() - 1);

        // vi updaterer status, hvis stock = 0
        if(product.getStock() == 0){
            product.setStatus(Status.Udlånt);
        }

        // vi tilgår loan på hjemmeside
        String loan = ctx.queryParam("loan");
        User user = productService.findUser(loan);

        // vi tilgår afleveringsdato på hjemmeside
        LocalDate afleveringsdato = LocalDate.parse(ctx.queryParam("afleveringsdato"));
        System.out.println(afleveringsdato);


        // Når brugeren låner udstyr, kan brugeren have på sin egen kurv
        user.addProduct(new Product(product.getId(), product.getPicturePath(), product.getName(), product.getDescription(), product.getStock(), Status.Udlånt, afleveringsdato, LocalDate.now()));

        System.out.println(user.getProducts());

        ctx.render("confirm");

    }



}
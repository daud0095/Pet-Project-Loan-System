package controllers;

import dto.UserAndProductDTO;
import entities.Product;
import entities.Status;
import entities.User;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.ProductService;
import services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    // vi opretter først ProduktService her for at hente alle produkter
    private ProductService productService;
    private UserService userService;
    private ConnectionPool connectionPool;

    public ProductController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.userService = new UserService(connectionPool);
        this.productService = new ProductService(connectionPool);
    }

    public void setRoutes(JavalinConfig config){

        // for at sende alle udstyr til javascript
        // /api/products  ==>  javascript fanger denne url for at hente alle produkter
        // Denne routes sender alle produkter videre til javascript ved hjælpe af  "productService.getProducts()"
        // ProduktServices konstruktør har "ProductFactory.createProducts()"
        // ctx.json omdanner dataerne til json

        config.routes.get("/products", ctx -> {
            List<Product> products = productService.getProducts();
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

    public void returnConfirm(Context ctx) throws DatabaseException {
        String loan = ctx.queryParam("loan");
        User user = productService.findUser(loan);

        String udstyr = ctx.queryParam("udstyr");
        Product product = productService.findProductByName(udstyr);
        UserAndProductDTO product1 = productService.getUsersProduct(user, product);

        productService.updateProductStockPlus(product1.getProductId());
        productService.updateUserProductStatus(user,product1);

        ctx.attribute("user", user);
        ctx.attribute("product", product1);
        ctx.render("returnconfirm");


    }

    public void administration(Context ctx) {
        ctx.render("administration");
    }

    public void adminreturn(Context ctx) throws DatabaseException {

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
                List<UserAndProductDTO> products = productService.getUsersProduct(user);
                List<UserAndProductDTO> loanProducts = productService.getUsersProductByStatus(user, String.valueOf(Status.Udlånt));

                ctx.attribute("products", loanProducts);
                ctx.attribute("productlength", products.size());

            }

        }

        ctx.render("adminreturn");
    }

    // Denne method er for adminreturn.js
    // Json bliver sendt til adminreturn.js (product)
    public void getProduct(Context ctx) throws DatabaseException {
        String udstyr = ctx.queryParam("udstyr");
        String loan = ctx.queryParam("loan");

        if (udstyr == null || udstyr.isBlank()
                || loan == null || loan.isBlank()) {
            ctx.status(400).result("Bruger eller udstyr mangler");
            return;
        }

        User user = productService.findUser(loan);

        if (user == null) {
            ctx.status(404).result("Brugeren blev ikke fundet: " + loan);
            return;
        }

        UserAndProductDTO product = productService.findProductByName(user, udstyr);

        if (product == null) {
            ctx.status(404).result("Produktet blev ikke fundet: " + udstyr);
            return;
        }

        ctx.json(product);
    }

    public void myLoan(Context ctx) throws DatabaseException {

        // vi henter user
        User user = ctx.sessionAttribute("user");

        //  ************************
        //  Aktive loan og afleveret loan producter findes her

        // "Aktive", "Afleveret" eller null
        String filter = ctx.queryParam("filter");
        // Alle users product
        List<UserAndProductDTO> products;
        if ("afleveret".equals(filter)) {
            products = productService.getUsersLoan(
                    user, String.valueOf(Status.Ledigt)
            );
        } else {
            filter = "aktive";
            products = productService.getUsersLoan(
                    user, String.valueOf(Status.Udlånt)
            );

        }

        // vi sender products videre til myloan.html
        ctx.attribute("products", products);
        ctx.attribute("filter", filter);
        ctx.render("myLoan");

    }

    private void loan(Context ctx) throws DatabaseException {

        // vi tilgår id
        int id = Integer.parseInt(ctx.queryParam("id"));
        ctx.attribute("id", id);

        Product product = productService.findProduct(id);
        ctx.attribute("product", product);

        ctx.attribute("date", LocalDate.now());

        List<User> users = userService.getUsers();
        ctx.attribute("users", users);

        String msg = ctx.queryParam("msg");
        ctx.attribute("msg", msg);

        // denne id bliver sendt til loan.html
        // fordi senere kan vi bruge denne id for at hente udstyr
        ctx.render("loan");

    }

    private void confirm(Context ctx) throws DatabaseException {

        // vi tilgår product-id
        int id = Integer.parseInt(ctx.queryParam("productid"));
        Product product = productService.findProduct(id);

        // vi tilgår loan på hjemmeside
        String loan = ctx.queryParam("loan");
        User user = productService.findUser(loan);

        // Vi finder users udstyr
        UserAndProductDTO userProduct = productService.getUsersProduct(user, product);

        if (userProduct == null) {

            // vi updaterer stock
            productService.updateProductStockMinus(product.getId());

            // vi updaterer status, hvis stock = 0
            if (product.getStock() == 0) {
                productService.updateProductStatus(product.getId(), 2);
            }

            // vi tilgår afleveringsdato på hjemmeside
            LocalDate afleveringsdato = LocalDate.parse(ctx.queryParam("afleveringsdato"));

            productService.insertLoan(user, product, afleveringsdato, null, null, 1, String.valueOf(Status.Udlånt));

            ctx.render("confirm");

        } else {
            ctx.attribute("msg", "Allerede har brugeren denne udstyr...");
            ctx.redirect("/loan?id=" + id + "&msg=Allerede%20har%20brugeren%20dette%20udstyr");
        }




    }



}
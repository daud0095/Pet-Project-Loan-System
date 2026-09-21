package controllers;

import entities.Product;
import entities.User;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import services.ProductService;
import services.UserService;

import java.util.List;

public class UserController {

    static UserService userService = new UserService();

    // vi opretter først ProduktService her for at hente alle produkter
    static ProductService productService = new ProductService();

    public static void setRoutes(JavalinConfig config){

        config.routes.get("/", ctx -> ctx.redirect("/Login function.html"));
        config.routes.post("/login", ctx -> loginController(ctx));
        config.routes.get("/ForgotPassword", ctx -> ctx.redirect("/ForgotPassword.html"));
        config.routes.get("/myloan", ctx -> ctx.redirect("/myloan.html"));
        config.routes.get("/produktside", ctx -> ctx.redirect("/produktside.html") );


        // for at sende alle udstyr til javascript
        // /api/products  ==>  javascript fanger denne url for at hente alle produkter
        // Denne routes sender alle produkter videre til javascript ved hjælpe af  "productService.getProducts()"
        // ProduktServices konstruktør har "ProductFactory.createProducts()"
        // ctx.json omdanner dataerne til json
        config.routes.get("/api/products", ctx -> { ctx.json(productService.getProducts());  });

        // Denne router kalder lån-knap
        config.routes.get("/loan", ctx -> loan(ctx));  // for lån-knap

    }


    private static void loginController(Context ctx){
        String schoolMail = ctx.formParam("skolemail");
        String password = ctx.formParam("password");

        User user = userService.login(schoolMail,password);

        if (user != null){
            ctx.redirect("/produktside");
        } else {
            String message = "Brugernavn eller adgangskode er forkert";
            ctx.attribute("msg", message);
            ctx.render("/templates/Login function.html");

        }

    }

    private static void loan(Context ctx) {

        // vi tilgår id
        String id = ctx.queryParam("id");
        ctx.attribute("id", id);

        // denne id bliver sendt til loan.html
        // fordi senere kan vi bruge denne id for at hente udstyr
        ctx.render("/templates/loan.html");

    }





}

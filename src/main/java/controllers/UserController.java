package controllers;

import entities.User;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import services.UserServices;

public class UserController {

    static UserServices userServices = new UserServices();

    public static void setRoutes(JavalinConfig config){

        config.routes.get("/", ctx -> ctx.redirect("/Login function.html"));
        config.routes.post("/login", ctx -> loginController(ctx));
        config.routes.get("/ForgotPassword", ctx -> ctx.redirect("/ForgotPassword.html"));
        config.routes.get("/myloan", ctx -> ctx.redirect("/myloan.html"));
        config.routes.get("/produktside", ctx -> ctx.redirect("/produktside.html"));

        // Denne router kalder lån-knap
        config.routes.get("/loan", ctx -> loan(ctx));  // for lån-knap

    }


    private static void loginController(Context ctx){
        String schoolMail = ctx.formParam("skolemail");
        String password = ctx.formParam("password");

        User user = userServices.login(schoolMail,password);

        if (user != null){
            ctx.redirect("/produktside.html");
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

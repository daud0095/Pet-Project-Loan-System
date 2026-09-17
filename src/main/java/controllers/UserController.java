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
}

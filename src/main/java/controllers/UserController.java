package controllers;

import entities.User;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import services.UserService;

public class UserController {

    static UserService userService = UserService.getInstance();



    public static void setRoutes(JavalinConfig config){

        config.routes.get("/", ctx -> ctx.render("Loginfunction"));
        config.routes.post("/login", ctx -> loginController(ctx));
        config.routes.get("/ForgotPassword", ctx -> ctx.render("ForgotPassword"));
        config.routes.get("/produktside", ctx -> ctx.render("produktside") );
        config.routes.get("/logout", ctx -> ctx.render("loginfunction"));




    }


    private static void loginController(Context ctx){
        String schoolMail = ctx.formParam("skolemail");
        String password = ctx.formParam("password");

        User user = userService.login(schoolMail,password);

        if (user != null){
            ctx.sessionAttribute("user", user);
            ctx.render("produktside");
        } else {
            String message = "Brugernavn eller adgangskode er forkert";
            ctx.attribute("msg", message);
            ctx.render("Loginfunction");

        }

    }







}

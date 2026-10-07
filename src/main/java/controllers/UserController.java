package controllers;

import entities.User;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.UserService;

public class UserController {

    private UserService userService;
    private ConnectionPool connectionPool;

    public UserController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        userService = new UserService(connectionPool);
    }



    public void setRoutes(JavalinConfig config){

        config.routes.get("/", ctx -> ctx.render("Loginfunction"));
        config.routes.post("/login", ctx -> loginController(ctx));
        config.routes.get("/ForgotPassword", ctx -> ctx.render("ForgotPassword"));
        config.routes.get("/produktside", ctx -> ctx.render("produktside") );
        config.routes.get("/logout", ctx -> ctx.render("loginfunction"));

    }

    private void loginController(Context ctx) throws DatabaseException {
        String schoolMail = ctx.formParam("skolemail");
        String password = ctx.formParam("password");

        User user = userService.login(schoolMail,password);

        if (user != null){
            ctx.sessionAttribute("user", user);
            ctx.render("produktside");
        } else {
            ctx.status(401);
            String message = "Brugernavn eller adgangskode er forkert";
            ctx.attribute("msg", message);
            ctx.render("Loginfunction");

        }

    }







}

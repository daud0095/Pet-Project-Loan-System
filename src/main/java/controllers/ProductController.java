package controllers;

import io.javalin.config.JavalinConfig;

public class ProductController {

    public static void setRoutes(JavalinConfig config){
        config.routes.get("/products", ctx -> {
            ctx.result("Produkter");
        });
    }
}

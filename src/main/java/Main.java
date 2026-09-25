import configuration.ThymeleafConfig;
import controllers.ProductController;
import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config ->  {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));

            UserController.setRoutes(config);
            ProductController.setRoutes(config);

        }).start(7070);

    }
}

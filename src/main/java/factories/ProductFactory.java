package factories;

import entities.Product;
import entities.Status;
import entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductFactory {

    public static List<Product> createProducts(){

        // Product er tilføjet manuelt
        // Senere kan vi hente udstyr fra databasen
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "computer.png", "Computer", "16 RAM", 1, Status.Ledigt, null, null));
        products.add(new Product(2, "kamera.png", "Kamera", "Canon", 0, Status.Udlånt, LocalDate.of(2026, 10, 5), null));
        products.add(new Product(3, "mikrofon.png", "Mikrofon", "USB mikrofon", 20, Status.Ledigt, null, null));
        products.add(new Product(4, "projektor.png", "Projektor", "Epson", 0, Status.Udlånt, LocalDate.of(2026, 10, 10), null));
        products.add(new Product(5, "earphone.png", "Høretelefoner", "Sony", 0, Status.Udlånt, LocalDate.of(2026, 10, 10), null));
        products.add(new Product(6, "tablet.png", "Tablet", "Ipad", 50, Status.Ledigt, null, null));
        products.add(new Product(7, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(8, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(9, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(10, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(11, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(12, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(13, "unknown.png", "unknown", "unknown", 50, Status.Udlånt, LocalDate.of(2026, 10, 1), null));
        products.add(new Product(14, "unknown.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(15, "unknown.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(16, "unknown.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(17, "unknown.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(18, "unknown.png", "unknown", "unknown", 50, Status.Udlånt, LocalDate.of(2026, 10, 15), null));
        products.add(new Product(19, "tablet.png", "unknown", "unknown", 50, Status.Udlånt, LocalDate.of(2026, 10, 30), null));
        products.add(new Product(20, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));
        products.add(new Product(21, "tablet.png", "unknown", "unknown", 50, Status.Ledigt, null, null));

        return products;
    }



}

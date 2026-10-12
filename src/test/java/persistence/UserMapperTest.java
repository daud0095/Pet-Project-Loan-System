package persistence;

import entities.Product;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL =
            "jdbc:postgresql://localhost:5432/Pet_Project?currentSchema=test";

    private static ConnectionPool connectionPool;
    private static ProductMapper productMapper;

    @BeforeAll
    static void setUpClass() {
        connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, "");
        productMapper = new ProductMapper(connectionPool);
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            // Tømmer alle tabeller og nulstiller id-sekvenser i én kommando.
            // CASCADE gør, at rækkefølgen og foreign keys ikke giver problemer.
            stmt.execute("""
                TRUNCATE test."Loan", test."Geby", test."Product", test."Status", test."User"
                RESTART IDENTITY CASCADE
                """);

            // 1. Tabeller uden afhængigheder først
            stmt.execute("""
                INSERT INTO test."Status" (status_code) VALUES
                ('Ledigt'),
                ('Udlånt'),
                ('Reservation')
                """);

            // Datoer i ISO-format (YYYY-MM-DD), så PostgreSQL altid læser dem korrekt
            stmt.execute("""
                INSERT INTO test."User" (username, password, "isAdmin", "create_Date") VALUES
                ('Admin@99', 1, true, '2026-05-10')
                """);

            // 2. Product skal findes, før et Loan kan pege på det
            stmt.execute("""
                INSERT INTO test."Product" (name, "picturePath", description, stock, status) VALUES
                ('Computer', 'Computer.img', '16 GB ram', 8, 2)
                """);

            // 3. Loan til sidst, da det refererer til Product og User.
            //    geby_id er null, fordi der ikke findes nogen Geby-række.
            //    status = 2 svarer til 'Udlånt' i Status-tabellen.
            stmt.execute("""
                INSERT INTO test."Loan" (product_id, user_id, loan_date, forventet_aflveveringsdato,
                                         return_date, bemærkning, geby_id, status) VALUES
                (1, 1, '2026-11-15', '2026-11-20', '2026-11-20', null, null, 2)
                """);

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @Test
    void getAllProduct() throws DatabaseException {
        List<Product> products = productMapper.getProducts();

        assertEquals(1, products.size());
        assertEquals("Computer", products.get(0).getName());
        assertEquals("Udlånt", products.get(0).getStatus().toString());
    }
}
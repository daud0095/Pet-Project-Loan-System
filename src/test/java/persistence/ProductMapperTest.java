package persistence;

import dto.UserAndProductDTO;
import entities.Product;
import entities.User;
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
    private static UserMapper userMapper;

    @BeforeAll
    static void setUpClass() {
        connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, "");
        productMapper = new ProductMapper(connectionPool);
        userMapper = new UserMapper(connectionPool);

        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM test.\"Geby\"");
            stmt.execute("DELETE FROM test.\"Loan\"");
            stmt.execute("DELETE FROM test.\"Product\"");
            stmt.execute("DELETE FROM test.\"Status\"");
            stmt.execute("DELETE FROM test.\"User\"");

            stmt.execute("ALTER SEQUENCE test.\"Geby_geby_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Loan_loan_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Product_product_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Status_status_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"user_id_seq\" RESTART WITH 1");


        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }

    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM test.\"Geby\"");
            stmt.execute("DELETE FROM test.\"Loan\"");
            stmt.execute("DELETE FROM test.\"Product\"");
            stmt.execute("DELETE FROM test.\"Status\"");
            stmt.execute("DELETE FROM test.\"User\"");

            stmt.execute("ALTER SEQUENCE test.\"Geby_geby_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Loan_loan_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Product_product_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"Status_status_id_seq\" RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.\"user_id_seq\" RESTART WITH 1");

            // 1. Tabeller uden afhængigheder først
            stmt.execute("""
                INSERT INTO test."Status" (status_code) VALUES
                ('Ledigt'),
                ('Udlånt'),
                ('Reservation')
                """);

            // Datoer i ISO-format (YYYY-MM-DD), så PostgreSQL altid læser dem korrekt
            stmt.execute("""
                INSERT INTO test."User" (user_id,username, password, "isAdmin", "create_Date") VALUES
                (1,'Admin@99', 1, true, '2026-05-10'),
                (2,'Feyzullah@99', 1, true, '2026-10-10'),
                (3,'Daud@99', 1, true, '2026-11-10')
                """);

            // 2. Product skal findes, før et Loan kan pege på det
            stmt.execute("""
                INSERT INTO test."Product" (name, "picturePath", description, stock, status) VALUES
                ('Computer', 'Computer.img', '16 GB ram', 8, 2),
                ('Tablet', 'Tablet.img', 'Medium', 18, 1),
                ('Kabler', 'Kabler.img', 'Stor', 28, 1)
                """);

            // 3. Loan til sidst, da det refererer til Product og User.
            //    geby_id er null, fordi der ikke findes nogen Geby-række.
            //    status = 2 svarer til 'Udlånt' i Status-tabellen.
            stmt.execute("""
                INSERT INTO test."Loan" (product_id, user_id, loan_date, forventet_aflveveringsdato,
                                         return_date, bemærkning, geby_id, status) VALUES
                (1, 1, '2026-11-15', '2026-11-20', '2026-11-20', null, null, 'Udlånt'),
                (2, 1, '2026-11-20', '2026-11-25', '2026-11-24', null, null, 'ledigt'),
                (3, 1, '2026-11-25', '2026-11-30', '2026-11-29', null, null, 'ledigt')
                """);

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @Test
    void getAllProduct() throws DatabaseException {
        List<Product> products = productMapper.getProducts();

        assertEquals(3, products.size());
        assertEquals("Computer", products.get(0).getName());
        assertEquals("Udlånt", products.get(0).getStatus().toString());
        assertEquals(8, products.get(0).getStock());
    }

    @Test
    void getUsersLoan() throws DatabaseException {

        User userAdmin = userMapper.getUsers().get(0);
        List<UserAndProductDTO> userProducts = productMapper.getAllLoans(userAdmin);

        assertEquals(3, userProducts.size());
        assertEquals("Computer", userProducts.get(0).getProductName());
        assertEquals("Tablet", userProducts.get(1).getProductName());
        assertEquals("Kabler", userProducts.get(2).getProductName());

    }

    @Test
    void getUsersLoanStatus() throws DatabaseException {

        User userAdmin = userMapper.getUsers().get(0);
        List<UserAndProductDTO> userProductsLedigt = productMapper.getLoans(userAdmin, "ledigt");
        List<UserAndProductDTO> userProductsudlånt = productMapper.getLoans(userAdmin, "Udlånt");

        assertEquals(2, userProductsLedigt.size());
        assertEquals(1, userProductsudlånt.size());
        assertEquals("Computer", userProductsudlånt.get(0).getProductName());
        assertEquals("Tablet", userProductsLedigt.get(0).getProductName());
        assertEquals("Kabler", userProductsLedigt.get(1).getProductName());


    }

    @Test
    void updateProductStockById() throws DatabaseException {

        productMapper.updateProductStockById(2,500);
        Product product = productMapper.getProductsById(2);
        assertEquals(500, product.getStock());

    }

    @Test
    void updateProductStatusById() throws DatabaseException {

        productMapper.updateProductStatusById(2,3);
        Product product = productMapper.getProductsById(2);
        assertEquals("Reservation", product.getStatus().toString());

    }








}
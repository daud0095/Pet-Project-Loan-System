package persistence;

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

public class UserMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL =
            "jdbc:postgresql://localhost:5432/Pet_Project?currentSchema=test";

    private static ConnectionPool connectionPool;
    private static UserMapper userMapper;

    @BeforeAll
    static void setUpClass() {
        connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, "");
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

            stmt.execute("""
                INSERT INTO test."User" (user_id,username, password, "isAdmin", "create_Date") VALUES
                (1,'Admin@99', 1, true, '2026-05-10'),
                (2,'Feyzullah@99', 1, false, '2026-10-10'),
                (3,'Daud@99', 1, false, '2026-11-10')
                """);


        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @Test
    void getAllUser() throws DatabaseException {
        List<User> users = userMapper.getUsers();
        assertEquals(3, users.size());
        assertTrue(users.get(0).isAdmin());


    }



}

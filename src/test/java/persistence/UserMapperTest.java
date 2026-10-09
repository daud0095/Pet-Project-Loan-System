package persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL =
            "jdbc:postgresql://localhost:5432/Pet_Project?currentSchema=test";

    private static ConnectionPool connectionPool;
  /*  private static BookMapper bookMapper;
*/
    @BeforeAll
    static void setUpClass() {
        connectionPool =
                ConnectionPool.getInstance(USER, PASSWORD, URL, "");

        /*bookMapper = new BookMapper(connectionPool);*/

        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM public.udlaan");
            stmt.execute("DELETE FROM public.bog");
            stmt.execute("DELETE FROM public.forfatter");
            stmt.execute("DELETE FROM public.laaner");
            stmt.execute("DELETE FROM public.postnummer");

            stmt.execute("ALTER SEQUENCE public.udlaan_udlaan_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.bog_bog_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.forfatter_forfatter_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.laaner_laaner_id_seq RESTART WITH 1");

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {

        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM public.udlaan");
            stmt.execute("DELETE FROM public.bog");
            stmt.execute("DELETE FROM public.laaner");
            stmt.execute("DELETE FROM public.forfatter");
            stmt.execute("DELETE FROM public.postnummer");

            stmt.execute("ALTER SEQUENCE public.udlaan_udlaan_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.bog_bog_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.laaner_laaner_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE public.forfatter_forfatter_id_seq RESTART WITH 1");

            stmt.execute("""
            INSERT INTO forfatter (navn) VALUES
            ('Karen Blixen'),
            ('H.C. Andersen'),
            ('Jostein Gaarder')
            """);

            stmt.execute("""
            INSERT INTO bog (titel, forfatter_id, isbn) VALUES
            ('Vintereventyr', 1, 1001),
            ('Fyrtøjet', 2, 1002),
            ('Sofies verden', 3, 1003),
            ('Den afrikanske farm', 1, 1004),
            ('Den lille havfrue', 2, 1005)
            """);

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }
}
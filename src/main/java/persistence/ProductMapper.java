package persistence;

import dto.UserAndProductDTO;
import entities.Product;
import entities.Status;
import entities.User;
import exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(UserMapper.class);

    public ProductMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public List<Product> getProducts() throws DatabaseException {
        String query = "SELECT product_id, name, \"picturePath\",  description, stock, status_code FROM \"Product\" " +
                "inner join \"Status\" on \"Product\".status = \"Status\".status_id";
        List<Product> products = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int product_id = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    Status status1 = Status.valueOf(rs.getString("status_code"));
                    products.add(new Product(product_id, name, picturePath, description, stock, status1));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette produkt findes ikke.");
        }
        return products;
    }

    public Product getProductsById(int id) throws DatabaseException {
        String query = "SELECT product_id, name, \"picturePath\",  description, stock, status_code FROM \"Product\" " +
                "inner join \"Status\" on \"Product\".status = \"Status\".status_id where product_id =?";
        Product product = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setInt(1,id);
            try (ResultSet rs = stm.executeQuery()) {

                if (rs.next()) {
                    int product_id = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    Status status1 = Status.valueOf(rs.getString("status_code"));
                    product = new Product(product_id, name, picturePath, description, stock, status1);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Der findes ikke produktet med denne id");
        }
        return product;
    }

    public Product getProductsByName(String name1) throws DatabaseException {
        String query = "SELECT product_id, name, \"picturePath\",  description, stock, status_code FROM \"Product\" " +
                "inner join \"Status\" on \"Product\".status = \"Status\".status_id where name =?";
        Product product = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1,name1);
            try (ResultSet rs = stm.executeQuery()) {

                if (rs.next()) {
                    int product_id = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    Status status1 = Status.valueOf(rs.getString("status_code"));
                    product = new Product(product_id, name, picturePath, description, stock, status1);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Der findes ikke produktet med dette navn");
        }
        return product;
    }

    public List<Product> getProductsByNameList(String name1) throws DatabaseException {
        String query = "SELECT product_id, name, \"picturePath\",  description, stock, status_code FROM \"Product\" " +
                "inner join \"Status\" on \"Product\".status = \"Status\".status_id where name =?";
        List<Product> product = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1,name1);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int product_id = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    Status status1 = Status.valueOf(rs.getString("status_code"));
                    product.add(new Product(product_id, name, picturePath, description, stock, status1));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Der findes ikke produktet med dette navn");
        }
        return product;
    }

    public void updateProductStockById(int id, int inputStock) throws DatabaseException
    {
        String sql = "UPDATE \"Product\" SET stock = ? WHERE product_id = ?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setInt(1, inputStock);
                prepareStatement.setInt(2, id);
                prepareStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke opdater stock");
        }
    }

    public void updateProductStatusById(int id, String status) throws DatabaseException
    {
        String sql = "UPDATE \"Product\" SET status = ? WHERE product_id = ?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setString(1, status);
                prepareStatement.setInt(1, id);
                prepareStatement.executeUpdate();

                int rows = prepareStatement.executeUpdate();
                if(rows == 0){
                    throw new DatabaseException("Produktet med id " + id + " findes ikke");
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke opdater status");
        }
    }

    public List<Product> getUsersProducts(User user) throws DatabaseException {
        String query = "SELECT * FROM \"Loan\" " +
                "INNER JOIN \"Product\" USING (product_id) " +
                "WHERE user_id = ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setInt(1, user.getId());

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    String status = rs.getString("status");
                    Status status1 = Status.valueOf(status);

                    Product product = new Product(productId, name, picturePath, description, stock, status1);
                    products.add(product);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke hente brugerens produkter");
        }

        return products;
    }

    public List<Product> getUsersProductsByStatus(User user, String inputStatus) throws DatabaseException {
        String query = "SELECT * FROM \"Loan\" " +
                "INNER JOIN \"Product\" USING (product_id) " +
                "WHERE user_id = ? AND \"Product\".status = ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setInt(1, user.getId());
            stm.setString(2, inputStatus);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    String status = rs.getString("status");
                    Status status1 = Status.valueOf(status);

                    Product product = new Product(productId, name, picturePath, description, stock, status1);
                    products.add(product);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke hente brugerens produkter");
        }

        return products;
    }

    public Product getUsersProductsByProduct(User user, Product product) throws DatabaseException {
        String query = "SELECT * FROM \"Loan\" " +
                "INNER JOIN \"Product\" USING (product_id) " +
                "WHERE user_id = ? AND product_id = ?";
        Product product1 = null;

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setInt(1, user.getId());
            stm.setInt(2, product.getId());

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String picturePath = rs.getString("picturePath");
                    String description = rs.getString("description");
                    int stock = rs.getInt("stock");
                    String status = rs.getString("status");
                    Status status1 = Status.valueOf(status);

                    product1 = new Product(productId, name, picturePath, description, stock, status1);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke hente brugerens produkter");
        }

        return product1;
    }

    public void createLoan(User user, Product product, LocalDate afleveringsdato, LocalDate returnDate, String remark, int geby_id, String status) throws DatabaseException
    {
        String sql = "INSERT INTO \"Loan\" (product_id, user_id, loan_date, forventet_aflveveringsdato, return_date, bemærkning, geby_id, status)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            {
                prepareStatement.setInt(1, product.getId());
                prepareStatement.setInt(2, user.getId());
                prepareStatement.setDate(3, Date.valueOf(LocalDate.now()));
                prepareStatement.setDate(4, Date.valueOf(afleveringsdato));

                if (returnDate != null) {
                    prepareStatement.setDate(5, Date.valueOf(returnDate));
                } else {
                    prepareStatement.setNull(5, Types.DATE);
                }

                prepareStatement.setString(6, remark);
                prepareStatement.setInt(7, geby_id);
                prepareStatement.setString(8, status);
                prepareStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Could not create user in the database");
        }
    }

    public List<UserAndProductDTO> getLoans(User user, String status) throws DatabaseException {
        String query = "SELECT \"Product\".name, loan_date, forventet_aflveveringsdato, return_date, \"Loan\".status FROM \"Loan\"\n" +
                "INNER JOIN \"User\" USING (user_id)\n" +
                "INNER JOIN \"Product\" USING (product_id)\n" +
                "WHERE user_id = ? AND \"Loan\".status = ?";
        List<UserAndProductDTO> userAndProductDTOS = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setInt(1, user.getId());
            stm.setString(2, status);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate loanDate = rs.getDate("Loan_date").toLocalDate();
                    LocalDate forventet_afleveringsdato = rs.getDate("forventet_afleveringsdato").toLocalDate();
                    LocalDate return_date = rs.getDate("return_date").toLocalDate();
                    String status1 = rs.getString("status");
                    userAndProductDTOS.add(new UserAndProductDTO(name, loanDate, forventet_afleveringsdato, return_date, status1));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette produkt findes ikke.");
        }
        return userAndProductDTOS;
    }

    public List<UserAndProductDTO> getAllLoans(User user) throws DatabaseException {
        String query = "SELECT \"Product\".name, loan_date, forventet_aflveveringsdato, return_date, \"Loan\".status FROM \"Loan\"\n" +
                "INNER JOIN \"User\" USING (user_id)\n" +
                "INNER JOIN \"Product\" USING (product_id)\n" +
                "WHERE user_id = ?";
        List<UserAndProductDTO> userAndProductDTOS = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setInt(1, user.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate loanDate = rs.getDate("Loan_date").toLocalDate();
                    LocalDate forventet_afleveringsdato = rs.getDate("forventet_afleveringsdato").toLocalDate();
                    LocalDate return_date = rs.getDate("return_date").toLocalDate();
                    String status1 = rs.getString("status");
                    userAndProductDTOS.add(new UserAndProductDTO(name, loanDate, forventet_afleveringsdato, return_date, status1));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette produkt findes ikke.");
        }
        return userAndProductDTOS;
    }



}

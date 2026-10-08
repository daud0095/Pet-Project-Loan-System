package persistence;

import entities.User;
import exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(UserMapper.class);

    public UserMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public User login (String userName, String password) throws DatabaseException {

        User user = getUserByUserName(userName);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        else return null;
    }

//    public UserAndAuthorsDTO getUserAndAuthors(User user) throws DatabaseException {
//        String query = "SELECT DISTINCT forfatter.forfatter_id, navn FROM udlaan JOIN bog USING (bog_id) JOIN forfatter USING (forfatter_id) " +
//                "WHERE laaner_id = ?";
//        List<Author> authors = new ArrayList<>();
//        UserAndAuthorsDTO result = new UserAndAuthorsDTO(user, authors);
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stm = connection.prepareStatement(query)) {
//            stm.setInt(1, user.getId());
//            try (ResultSet rs = stm.executeQuery()) {
//                while (rs.next()) {
//                    authors.add(new Author(rs.getString("navn"), rs.getInt("forfatter_id")));
//                }                ;
//            }
//
//        } catch (SQLException e) {
//            logger.error(e.getMessage());
//            throw new DatabaseException("Listen af forfattere kunne ikke hentes");
//        }
//        return result;
//    }

    public User createUser(User user) throws DatabaseException {
        String query = "INSERT INTO \"User\" (username, password, \"isAdmin\") VALUES (?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setBoolean(3, user.isAdmin());
            stm.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Brugeren blev ikke gemt ");
        }
        return user;
    }

    public User getUserByUserName(String userName) throws DatabaseException {
        User user = null;
        String query = "SELECT user_id, username, password, \"isAdmin\", \"create_Date\" FROM \"User\" WHERE username = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, userName);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    LocalDate createDate = rs.getObject("create_Date", LocalDate.class);
                    user = new User(id, username, password, isAdmin);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Søgning efter brugeren fejlede");
        }
        return user;
    }

    public List<User> getUsers() throws DatabaseException {
        String query = "SELECT user_id, username, password, \"isAdmin\", \"create_Date\" FROM \"User\"";
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    LocalDate createDate = rs.getObject("create_Date", LocalDate.class);
                    users.add(new User(id, username, password, isAdmin));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Søgning efter brugeren fejlede");
        }
        return users;
    }
}


package services;

import entities.User;
import exceptions.DatabaseException;
import persistence.ConnectionPool;
import persistence.UserMapper;

import java.util.List;

public class UserService {

    private ConnectionPool connectionPool;
    private UserMapper userMapper;

    public UserService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.userMapper = new UserMapper(connectionPool);
    }

    public List<User> getUsers() throws DatabaseException {
        return userMapper.getUsers();
    }

    public User getUser(String username) throws DatabaseException {
        return userMapper.getUserByUserName(username);
    }

    public  User login(String username, String password) throws DatabaseException {
        return userMapper.login(username, password);
    }

    public User createUser(String username, String password) throws DatabaseException {
        if(username == null || username.isEmpty()){
            return null;
        }
        if(password == null || password.isEmpty()){
            return null;
        }
        if(getUser(username) != null){
            return null; // Username er allerede i brug
        }
        User newUser = new User(username, password, false);
        return userMapper.createUser(newUser);
    }

    public boolean validatePassword(String password){
        if(password.length() < 8 || password.length() > 15){
            return false;
        }

        // Mindst et bogstav skal være med stort, et tal og 8-15 tegn i alt.
        String regex = "^(?=.*[A-Z])(?=.*[0-9]).{8,15}$";
        return password.matches(regex);
    }


}


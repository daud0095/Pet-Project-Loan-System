package services;

import entities.User;
import factories.UserFactory;

import java.util.List;

public class UserService {
    private List<User> users;

    public UserService(){
        this.users = UserFactory.createUsers();
    }

    public void addUser(User user){
        users.add(user);
    }

    public User getUser(String username){
        for(User user : users){
            if(user.getUsername().toUpperCase().equals(username.toUpperCase())){
                return user;
            }
        }
        return null;
    }

    public  User login(String username, String password){
        User user = getUser(username);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public User createUser(String username, String password){
        if(username == null || username.isEmpty()){
            return null;
        }
        if(password == null || password.isEmpty()){
            return null;
        }
        if(getUser(username) != null){
            return null; // Username er allerede i brug
        }
        User newUser = new User(username, password);
        return newUser;
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


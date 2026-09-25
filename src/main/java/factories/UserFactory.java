package factories;

import entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public static List<User> createUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User("Admin@99", "3892", true));
        users.add(new User("daud", "3948", false));

        return users;
    }
}

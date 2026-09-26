package factories;

import entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public static List<User> createUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User("Admin@99", "3892", true));
        users.add(new User("daud@99", "1234", false));
        users.add(new User("feyzullah@99", "1234", false));
        users.add(new User("ali", "1234", false));
        users.add(new User("mahdi", "1234", false));

        return users;
    }
}

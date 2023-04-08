package ClashRoyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Manager {
    public static Manager singleton = new Manager();
    public static User currentUser = null;

    private final HashMap<String , User> userIndex = new HashMap<>();
    private final ArrayList<User> users = new ArrayList<>();

    private Manager(){}
    public User getUserByUsername(String username){
        return userIndex.get(username);
    }
    public void addUser(User user){
        userIndex.put(user.getUsername() , user);
        users.add(user);
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<User> getSortedUsers() {
        ArrayList<User> users = new ArrayList<>(this.users);
        users.sort((User a , User b) -> {
            if(a.getLevel() != b.getLevel()) return Integer.compare(b.getLevel() , a.getLevel());
            if(a.getExperience() != b.getExperience()) return Integer.compare(b.getExperience() , a.getExperience());
            return a.getUsername().compareTo(b.getUsername());
        });
        return users;
    }
}

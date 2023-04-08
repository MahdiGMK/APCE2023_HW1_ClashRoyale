package ClashRoyale.Menu;

import ClashRoyale.Commands;
import ClashRoyale.Manager;
import ClashRoyale.User;
import ClashRoyale.Utils;

import java.util.regex.Matcher;

public class LoginMenu implements IMenu {
    private IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher registerMatcher = Commands.Register.getMatcher(line);
        Matcher loginMatcher = Commands.Login.getMatcher(line);
        Matcher exitMatcher = Commands.Exit.getMatcher(line);
        String msg = "";
        if (registerMatcher.matches())
            msg = register(registerMatcher);
        else if (loginMatcher.matches())
            msg = login(loginMatcher);
        else if (exitMatcher.matches())
            nextMenu = null;
        else
            msg = Utils.invalidCommand();

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "Register/Login Menu";
    }

    private String register(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");

        String userRes = Utils.validateUsername(username);
        if (userRes != null) return userRes;
        String passRes = Utils.validatePassword(password);
        if (passRes != null) return passRes;
        if (Manager.singleton.getUserByUsername(username) != null) return "Username already exists!";

        Manager.singleton.addUser(new User(username, password));
        return String.format("User %s created successfully!", username);
    }

    private String login(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");

        String userRes = Utils.validateUsername(username);
        if (userRes != null) return userRes;
        String passRes = Utils.validatePassword(password);
        if (passRes != null) return passRes;

        User user = Manager.singleton.getUserByUsername(username);
        if (user == null) return "Username doesn't exist!";
        if (!user.getPassword().equals(password)) return "Password is incorrect!";

        Manager.currentUser = user;
        nextMenu = new MainMenu();
        return String.format("User %s logged in!", username);
    }
}
;
package ClashRoyale.Menu;

import ClashRoyale.Commands;
import ClashRoyale.Manager;
import ClashRoyale.User;
import ClashRoyale.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class MainMenu implements IMenu {
    IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        String msg = "";
        Matcher listOfUsersMatcher = Commands.ListOfUsers.getMatcher(line);
        Matcher scoreBoardMatcher = Commands.ScoreBoard.getMatcher(line);
        Matcher logoutMatcher = Commands.Logout.getMatcher(line);
        Matcher profileMenuMatcher = Commands.ProfileMenu.getMatcher(line);
        Matcher shopMenuMatcher = Commands.ShopMenu.getMatcher(line);
        Matcher startGameMatcher = Commands.StartGame.getMatcher(line);
        if (listOfUsersMatcher.matches())
            msg = listOfUsers();
        else if (scoreBoardMatcher.matches())
            msg = scoreboard();
        else if (logoutMatcher.matches())
            msg = logout();
        else if (profileMenuMatcher.matches())
            msg = profileMenu();
        else if (shopMenuMatcher.matches())
            msg = shopMenu();
        else if (startGameMatcher.matches())
            msg = startGame(startGameMatcher);
        else
            msg = Utils.invalidCommand();
        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "Main Menu";
    }

    private String listOfUsers() {
        int id = 1;
        for (User user : Manager.singleton.getUsers())
            System.out.printf("user %d: %s\n", id++, user.getUsername());
        return "";
    }

    private String scoreboard() {
        ArrayList<User> users = Manager.singleton.getSortedUsers();

        for (int i = 0; i < 5 && i < users.size(); i++)
            System.out.printf("%d- username: %s level: %d experience: %d\n",
                    i + 1, users.get(i).getUsername(), users.get(i).getLevel(), users.get(i).getExperience());
        return "";
    }

    private String logout() {
        nextMenu = new LoginMenu();
        return String.format("User %s logged out successfully!", Manager.currentUser.getUsername());
    }

    private String profileMenu() {
        nextMenu = new ProfileMenu();
        return "Entered profile menu!";
    }

    private String shopMenu() {
        nextMenu = new ShopMenu();
        return "Entered shop menu!";
    }

    private String startGame(Matcher matcher) {
        int turnsCount = Integer.parseInt(matcher.group("turnsCount"));
        String username = matcher.group("username");
        User otherUser = Manager.singleton.getUserByUsername(username);

        if (turnsCount < 5 || turnsCount > 30) return "Invalid turns count!";
        String userRes = Utils.validateUsername(username);
        if (userRes != null) return userRes;
        if (otherUser == null) return "Username doesn't exist!";

        nextMenu = new GameMenu(turnsCount, otherUser);
        return String.format("Battle started with user %s", username);
    }
}

package ClashRoyale.Menu;

import ClashRoyale.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;

public class ProfileMenu implements IMenu {
    IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher backMatcher = Commands.Back.getMatcher(line);
        Matcher changePasswordMatcher = Commands.ChangePassword.getMatcher(line);
        Matcher infoMatcher = Commands.Info.getMatcher(line);
        Matcher removeFromBattleDeckMatcher = Commands.RemoveFromBattleDeck.getMatcher(line);
        Matcher addToBattleDeckMatcher = Commands.AddToBattleDeck.getMatcher(line);
        Matcher showBattleDeckMatcher = Commands.ShowBattleDeck.getMatcher(line);

        String msg = "";
        if (backMatcher.matches())
            msg = back();
        else if (changePasswordMatcher.matches())
            msg = changePassword(changePasswordMatcher);
        else if (infoMatcher.matches())
            msg = info();
        else if (removeFromBattleDeckMatcher.matches())
            msg = removeFromBattleDeck(removeFromBattleDeckMatcher);
        else if (addToBattleDeckMatcher.matches())
            msg = addToBattleDeck(addToBattleDeckMatcher);
        else if (showBattleDeckMatcher.matches())
            msg = showBattleDeck();
        else
            msg = Utils.invalidCommand();

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "Profile Menu";
    }

    private String back() {
        nextMenu = new MainMenu();
        return "Entered main menu!";
    }

    private String changePassword(Matcher matcher) {
        String oldPassword = matcher.group("oldPassword");
        String newPassword = matcher.group("newPassword");
        if (!oldPassword.equals(Manager.currentUser.getPassword())) return "Incorrect password!";
        String passRes = Utils.validatePassword(newPassword);
        if (passRes != null) return "Incorrect format for new password!";
        Manager.currentUser.setPassword(newPassword);
        return "Password changed successfully!";
    }

    private String info() {
        ArrayList<User> users = Manager.singleton.getSortedUsers();
        User user = Manager.currentUser;
        System.out.printf(
                "username: %s\n" +
                        "password: %s\n" +
                        "level: %d\n" +
                        "experience: %d\n" +
                        "gold: %d\n" +
                        "rank: %d\n",
                user.getUsername(),
                user.getPassword(),
                user.getLevel(),
                user.getExperience(),
                user.getGold(),
                users.indexOf(user) + 1);
        return "";
    }

    private String removeFromBattleDeck(Matcher matcher) {
        BattleDeck battleDeck = Manager.currentUser.getBattleDeck();
        String cardName = matcher.group("cardName");
        Card card = Utils.getCardByName(cardName);
        if (card == null) return "Invalid card name!";
        if (!battleDeck.hasCard(card)) return "This card isn't in your battle deck!";
        if (battleDeck.getCards().size() == 1) return "Invalid action: your battle deck will be empty!";
        battleDeck.removeCard(card);
        return String.format("Card %s removed successfully!", cardName);
    }

    private String addToBattleDeck(Matcher matcher) {
        BattleDeck battleDeck = Manager.currentUser.getBattleDeck();
        String cardName = matcher.group("cardName");
        Card card = Utils.getCardByName(cardName);
        if (card == null) return "Invalid card name!";
        if (!Manager.currentUser.hasCard(card)) return "You don't have this card!";
        if (battleDeck.hasCard(card)) return "This card is already in your battle deck!";
        if (battleDeck.getCards().size() == 4) return "Invalid action: your battle deck is full!";
        battleDeck.addCard(card);
        return String.format("Card %s added successfully!", cardName);
    }

    private String showBattleDeck() {
        BattleDeck battleDeck = Manager.currentUser.getBattleDeck();
        battleDeck.getCards().sort(Comparator.comparing(Card::toString));
        for (Card card : battleDeck.getCards()) {
            System.out.println(card);
        }
        return "";
    }
}

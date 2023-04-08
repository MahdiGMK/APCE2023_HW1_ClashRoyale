package ClashRoyale.Menu;

import ClashRoyale.*;

import java.util.regex.Matcher;

public class ShopMenu implements IMenu {
    IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;

        Matcher backMatcher = Commands.Back.getMatcher(line);
        Matcher buyCardMatcher = Commands.BuyCard.getMatcher(line);
        Matcher sellCardMatcher = Commands.SellCard.getMatcher(line);

        String msg = "";
        if (backMatcher.matches()) msg = back();
        else if (buyCardMatcher.matches()) msg = buyCard(buyCardMatcher);
        else if (sellCardMatcher.matches()) msg = sellCard(sellCardMatcher);
        else msg = Utils.invalidCommand();

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "Shop Menu";
    }

    private String back() {
        nextMenu = new MainMenu();
        return "Entered main menu!";
    }

    private String buyCard(Matcher matcher) {
        User user = Manager.currentUser;
        String cardName = matcher.group("cardName");
        Card card = null;
        int price = -1;
        switch (cardName) {
            case "Fireball":
                card = Card.Fireball;
                price = 100;
                break;
            case "Heal":
                card = Card.Heal;
                price = 150;
                break;
            case "Barbarian":
                card = Card.Barbarian;
                price = 100;
                break;
            case "Baby Dragon":
                card = Card.BabyDragon;
                price = 200;
                break;
            case "Ice Wizard":
                card = Card.IceWizard;
                price = 180;
                break;
        }

        if (card == null) return "Invalid card name!";
        if (user.getGold() < price) return String.format("Not enough gold to buy %s!", cardName);
        if (user.hasCard(card)) return "You have this card!";

        user.useGold(price);
        user.addCard(card);
        return String.format("Card %s bought successfully!", cardName);
    }

    private String sellCard(Matcher matcher) {
        User user = Manager.currentUser;
        String cardName = matcher.group("cardName");
        Card card = null;
        int price = -1;
        switch (cardName) {
            case "Fireball":
                card = Card.Fireball;
                price = 100;
                break;
            case "Heal":
                card = Card.Heal;
                price = 150;
                break;
            case "Barbarian":
                card = Card.Barbarian;
                price = 100;
                break;
            case "Baby Dragon":
                card = Card.BabyDragon;
                price = 200;
                break;
            case "Ice Wizard":
                card = Card.IceWizard;
                price = 180;
                break;
        }

        if (card == null) return "Invalid card name!";
        if (!user.hasCard(card)) return "You don't have this card!";
        if (user.getBattleDeck().hasCard(card)) return "You cannot sell a card from your battle deck!";
        price = 4 * price / 5;
        user.useGold(-price);
        user.removeCard(card);
        return String.format("Card %s sold successfully!", cardName);
    }
}

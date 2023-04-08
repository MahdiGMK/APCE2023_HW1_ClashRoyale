package ClashRoyale;

import java.util.ArrayList;

public class User {
    private String username, password;
    private int gold = 100, experience = 0, level = 1;
    private ArrayList<Card> cards = new ArrayList<>();
    private BattleDeck battleDeck = new BattleDeck();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        cards.add(Card.Fireball);
        cards.add(Card.Barbarian);
    }

    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addExperience(int add) {
        experience += add;
        while (experience >= 160 * level * level) {
            experience -= 160 * level * level;
            level++;
        }
    }

    public void useGold(int price) {
        gold -= price;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    // Getter Setter
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public int getGold() {
        return gold;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public BattleDeck getBattleDeck() {
        return battleDeck;
    }
// Getter Setter End
}

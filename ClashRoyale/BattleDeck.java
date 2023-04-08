package ClashRoyale;

import java.util.ArrayList;

public class BattleDeck {
    private ArrayList<Card> cards = new ArrayList<>();
    public BattleDeck(){
        cards.add(Card.Fireball);
        cards.add(Card.Barbarian);
    }

    public boolean hasCard(Card card){
        return cards.contains(card);
    }
    public void addCard(Card card) {
        if(hasCard(card)) Utils.safeNullThrow();
        cards.add(card);
    }
    public void removeCard(Card card){
        if(!hasCard(card) || cards.size() == 1) Utils.safeNullThrow();
        cards.remove(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}

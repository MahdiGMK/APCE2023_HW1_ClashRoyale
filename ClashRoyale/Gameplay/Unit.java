package ClashRoyale.Gameplay;

import ClashRoyale.Card;
import ClashRoyale.User;

public class Unit {
    private final User owner;
    private final Team team;
    private final Card card;
    private int hp;

    public Unit(Team team, User owner, Card card) {
        this.owner = owner;
        this.team = team;
        this.card = card;
        hp = getMaxHp(card);
    }

    public void dealDamage(int dmg) {
        hp -= dmg;
    }

    public void fight(Unit other) {
        if (other.team == team) return;
        if (card == Card.Barbarian && other.card == Card.BabyDragon) return;
        if (other.getDamage() >= getDamage()) return;
        other.dealDamage(getDamage() - other.getDamage());
    }

    public void heal(int heal) {
        hp = Math.min(getMaxHp(card), hp + heal);
    }

    // Getter Setter
    public User getOwner() {
        return owner;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return getDamage(card);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Team getTeam() {
        return team;
    }

    public Card getCard() {
        return card;
    }

    public int getMaxHp() {
        return hp;
    }
// Getter Setter End


    // Singleton
    public static int getDamage(Card card) {
        switch (card) {
            case Fireball:
                return 1600;
            case Heal:
                return 1000;
            case Barbarian:
                return 900;
            case IceWizard:
                return 1500;
            case BabyDragon:
                return 1200;
        }
        return 0;
    }

    public static int getMaxHp(Card card) {
        switch (card) {
            case Barbarian:
                return 2000;
            case IceWizard:
                return 3500;
            case BabyDragon:
                return 3300;
            case Heal:
                return 2;
        }
        return 0;
    }
// Singleton End
}

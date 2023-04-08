package ClashRoyale.Gameplay;

import ClashRoyale.Card;

import java.util.ArrayList;

public class Cell {
    private final ArrayList<Unit> units = new ArrayList<>();

    @Override
    public String toString() {
        return units.toString();
    }

    public void exec() {
        // fight
        for (Unit a : units)
            if (a.getCard() != Card.Heal) {
                for (Unit b : units)
                    if (b.getCard() != Card.Heal)
                        a.fight(b);
            }
        // heal
        for (Unit a : units)
            if (a.getCard() == Card.Heal) {
                for (Unit b : units)
                    if (b.getCard() != Card.Heal)
                        if (a.getTeam() == b.getTeam())
                            b.heal(a.getDamage());
            }
        // decrease healing time
        for (Unit a : units)
            if (a.getCard() == Card.Heal)
                a.dealDamage(1);
        // remove dead units
        units.removeIf(Unit::isDead);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    // Getter Setter
    public ArrayList<Unit> getUnits() {
        return units;
    }
// Getter Setter End
}

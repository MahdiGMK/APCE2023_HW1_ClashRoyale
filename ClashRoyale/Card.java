package ClashRoyale;

public enum Card {
    Fireball("Fireball", true),
    Heal("Heal", true),
    Barbarian("Barbarian", false),
    IceWizard("Ice Wizard", false),
    BabyDragon("Baby Dragon", false);
    private String name;
    private boolean spell;

    private Card(String name, boolean spell) {
        this.name = name;
        this.spell = spell;
    }

    @Override
    public String toString() {
        return name;
    }

    // Getter Setter
    public boolean isSpell() {
        return spell;
    }
// Getter Setter End
}

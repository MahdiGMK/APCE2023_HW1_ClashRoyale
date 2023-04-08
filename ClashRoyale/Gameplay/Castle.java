package ClashRoyale.Gameplay;

public class Castle {
    private final int damage;
    private int mainHp, leftHp, rightHp;

    public Castle(int level) {
        mainHp = 3600 * level;
        leftHp = rightHp = 2500 * level;
        damage = level * 500;
    }

    public void dealLeftDamage(int dmg) {
        leftHp -= dmg;
        if (leftHp <= 0) leftHp = 0;
    }

    public void dealMainDamage(int dmg) {
        mainHp -= dmg;
        if (mainHp <= 0) mainHp = 0;
    }

    public void dealRightDamage(int dmg) {
        rightHp -= dmg;
        if (rightHp <= 0) rightHp = 0;
    }

    public int countDead() {
        int res = 0;
        if (leftHp <= 0) res++;
        if (mainHp <= 0) res++;
        if (rightHp <= 0) res++;
        return res;
    }

    // Getter Setter
    public int getDamage() {
        return damage;
    }

    public boolean isMainDead() {
        return mainHp <= 0;
    }

    public boolean isRightDead() {
        return rightHp <= 0;
    }

    public boolean isLeftDead() {
        return leftHp <= 0;
    }

    public boolean isDead() {
        return getTotalHp() <= 0;
    }

    public int getTotalHp() {
        return mainHp + leftHp + rightHp;
    }

    public int getMainHp() {
        if (isMainDead()) return -1;
        return mainHp;
    }

    public int getLeftHp() {
        if (isLeftDead()) return -1;
        return leftHp;
    }

    public int getRightHp() {
        if (isRightDead()) return -1;
        return rightHp;
    }
// Getter Setter End
}

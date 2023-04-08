package ClashRoyale;

import java.util.regex.Pattern;

public class Utils {

    // Singleton
    public static final boolean DEBUG = false;

    public static String invalidCommand() {
        return "Invalid command!";
    }

    public static boolean matchesWith(String str, String pattern) {
        return Pattern.compile(pattern).matcher(str).matches();
    }

    public static boolean existsIn(String str, String pattern) {
        return Pattern.compile(pattern).matcher(str).find();
    }

    public static String validateUsername(String username) {
        if (!Utils.matchesWith(username, "[a-zA-Z]*")) return "Incorrect format for username!";
        return null;
    }

    public static String validatePassword(String password) {
        String inc = "Incorrect format for password!";
        if (password.length() < 8 || password.length() > 20) return inc;
        if (existsIn(password, " ")) return inc;
        if (!existsIn(password, "[a-z]") || !existsIn(password, "[A-Z]")) return inc;
        if (!existsIn(password, "\\d")) return inc;
        if (matchesWith(password, "\\d.*")) return inc;
        if (!existsIn(password, "[!@#$%^&*]")) return inc;
        return null;
    }

    public static void safeNullThrow() {
        User x = null;
        String user = x.getUsername();
    }

    public static Card getCardByName(String cardName) {
        switch (cardName) {
            case "Fireball":
                return Card.Fireball;
            case "Heal":
                return Card.Heal;
            case "Barbarian":
                return Card.Barbarian;
            case "Baby Dragon":
                return Card.BabyDragon;
            case "Ice Wizard":
                return Card.IceWizard;
        }
        return null;
    }
// Singleton End
}

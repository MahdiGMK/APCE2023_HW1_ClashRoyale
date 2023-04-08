package ClashRoyale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    WhiteLine(""),

    ShowCurrentMenu("show current menu"),

    Register("register username (?<username>.+) password (?<password>.+)"),
    Login("login username (?<username>.+) password (?<password>.+)"),
    Exit("Exit"),

    ListOfUsers("list of users"),
    ScoreBoard("scoreboard"),
    Logout("logout"),
    ProfileMenu("profile menu"),
    ShopMenu("shop menu"),
    StartGame("start game turns count (?<turnsCount>{z}) username (?<username>.+)"),

    Back("back"),
    ChangePassword("change password old password (?<oldPassword>.+) new password (?<newPassword>.+)"),
    Info("Info"),
    RemoveFromBattleDeck("remove from battle deck (?<cardName>.+)"),
    AddToBattleDeck("add to battle deck (?<cardName>.+)"),
    ShowBattleDeck("show battle deck"),

    BuyCard("buy card (?<cardName>.+)"),
    SellCard("sell card (?<cardName>.+)"),

    ShowHp("show the hitpoints left of my opponent"),
    ShowLineInfo("show line info (?<lineDirection>.+)"),
    RemainingCards("number of cards to play"),
    RemainingMoves("number of moves left"),
    Move("move troop in line (?<lineDirection>.+) and row (?<row>{z}) (?<direction>.+)"),
    DeployTroop("deploy troop (?<cardName>.+) in line (?<lineDirection>.+) and row (?<row>{z})"),
    DeployHeal("deploy spell Heal in line (?<lineDirection>.+) and row (?<row>{z})"),
    DeployFireball("deploy spell Fireball in line (?<lineDirection>.+)"),
    NextTurn("next turn");


    final Pattern pattern;

    private Commands(String regex) {
        regex = postProcess(regex);
        pattern = Pattern.compile(regex);
    }

    String postProcess(String str) {
        str = str.replace("{}", "\\s*");
        str = str.replace("{n}", "\\d+");
        str = str.replace("{z}", "-?\\d+");
        return str;
    }

    public Matcher getMatcher(String line) {
        return pattern.matcher(line);
    }

    public boolean matches(String line) {
        return getMatcher(line).matches();
    }
}

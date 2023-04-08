package ClashRoyale.Menu;

import ClashRoyale.*;
import ClashRoyale.Gameplay.*;

import java.util.regex.Matcher;

public class GameMenu implements IMenu {
    int turnIndex = 0;
    IMenu nextMenu;
    private User otherUser;
    private int turnCount;
    private GameState gameState;
    private User currentPlayer;
    private Team currentTeam;
    private int remainingCards = 1;
    private int remainingMoves = 3;

    public GameMenu(int turnsCount, User otherUser) {
        gameState = new GameState(Manager.currentUser, otherUser);
        this.turnCount = turnsCount;
        this.otherUser = otherUser;
        currentPlayer = Manager.currentUser;
        currentTeam = Team.Host;
    }

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher showHpMatcher = Commands.ShowHp.getMatcher(line);
        Matcher showLineInfoMatcher = Commands.ShowLineInfo.getMatcher(line);
        Matcher remainingCardsMatcher = Commands.RemainingCards.getMatcher(line);
        Matcher remainingMovesMatcher = Commands.RemainingMoves.getMatcher(line);
        Matcher moveMatcher = Commands.Move.getMatcher(line);
        Matcher deployTroopMatcher = Commands.DeployTroop.getMatcher(line);
        Matcher deployHealMatcher = Commands.DeployHeal.getMatcher(line);
        Matcher deployFireballMatcher = Commands.DeployFireball.getMatcher(line);
        Matcher nextTurnMatcher = Commands.NextTurn.getMatcher(line);

        String msg;
        if (showHpMatcher.matches()) msg = showHp();
        else if (showLineInfoMatcher.matches()) msg = showLineInfo(showLineInfoMatcher);
        else if (remainingCardsMatcher.matches()) msg = getRemainingCards();
        else if (remainingMovesMatcher.matches()) msg = getRemainingMoves();
        else if (moveMatcher.matches()) msg = moveTroop(moveMatcher);
        else if (deployTroopMatcher.matches()) msg = deployCard(deployTroopMatcher);
        else if (deployHealMatcher.matches()) msg = deployHeal(deployHealMatcher);
        else if (deployFireballMatcher.matches()) msg = deployFireball(deployFireballMatcher);
        else if (nextTurnMatcher.matches()) msg = nextTurn();
        else msg = Utils.invalidCommand();

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "Game Menu";
    }

    private String showHp() {
        Castle enemyCastle;
        if (currentTeam == Team.Host) enemyCastle = gameState.getGuest();
        else enemyCastle = gameState.getHost();
        System.out.printf("middle castle: %d\n" +
                        "left castle: %d\n" +
                        "right castle: %d\n",
                enemyCastle.getMainHp(), enemyCastle.getLeftHp(), enemyCastle.getRightHp());
        return "";
    }

    private int getLineDirIdx(String direction) {
        switch (direction) {
            case "left":
                return 0;
            case "middle":
                return 1;
            case "right":
                return 2;
        }
        return -1;
    }

    private int getDirIdx(String direction) {
        switch (direction) {
            case "upward":
                return 1;
            case "downward":
                return -1;
        }
        return 0;
    }

    private String showLineInfo(Matcher matcher) {
        String line = matcher.group("lineDirection");
        int lineDirection = getLineDirIdx(line);
        if (lineDirection == -1) return "Incorrect line direction!";
        int row = 1;
        System.out.printf("%s line:\n", line);
        for (Cell cell : gameState.getCells()[lineDirection]) {
            for (Unit unit : cell.getUnits())
                System.out.printf("row %d: %s: %s\n", row, unit.getCard().toString(), unit.getOwner().getUsername());
            row++;
        }
        return "";
    }

    private String getRemainingCards() {
        return String.format("You can play %d cards more!", remainingCards);
    }

    private String getRemainingMoves() {
        return String.format("You have %d moves left!", remainingMoves);
    }

    private String moveTroop(Matcher matcher) {
        String line = matcher.group("lineDirection");
        int lineDirection = getLineDirIdx(line);
        int row = Integer.parseInt(matcher.group("row"));
        int direction = getDirIdx(matcher.group("direction"));
        if (lineDirection == -1) return "Incorrect line direction!";
        if (row < 1 || row > 15) return "Invalid row number!";
        row--;
        if (direction == 0) return "you can only move troops upward or downward!";
        if (remainingMoves == 0) return "You are out of moves!";
        Cell cell = gameState.getCell(lineDirection, row);
        Unit unit = null;
        for (Unit unit1 : cell.getUnits())
            if (unit1.getTeam() == currentTeam && !unit1.getCard().isSpell()) {
                unit = unit1;
                break;
            }
        if (unit == null) return "You don't have any troops in this place!";
        if (row == 0 && direction == -1 || row == 14 && direction == 1) return "Invalid move!";
        remainingMoves--;
        Cell destination = gameState.getCell(lineDirection, row + direction);
        cell.removeUnit(unit);
        destination.addUnit(unit);
        return String.format("%s moved successfully to row %d in line %s",
                unit.getCard().toString(), row + direction + 1, line);
    }

    private String deployCard(Matcher matcher) {
        Card card = Utils.getCardByName(matcher.group("cardName"));
        int lineDirection = getLineDirIdx(matcher.group("lineDirection"));
        int row = Integer.parseInt(matcher.group("row"));
        if (card == null || card.isSpell()) return "Invalid troop name!";
        if (!currentPlayer.getBattleDeck().hasCard(card))
            return String.format("You don't have %s card in your battle deck!", card.toString());
        if (lineDirection == -1) return "Incorrect line direction!";
        if (row < 1 || row > 15) return "Invalid row number!";
        if (currentTeam == Team.Host && row > 4 || currentTeam == Team.Guest && row < 12)
            return "Deploy your troops near your castles!";
        row--;
        if (remainingCards == 0) return "You have deployed a troop or spell this turn!";
        remainingCards--;
        Cell cell = gameState.getCell(lineDirection, row);
        cell.addUnit(new Unit(currentTeam, currentPlayer, card));
        return String.format("You have deployed %s successfully!", card.toString());
    }

    private String deployHeal(Matcher matcher) {
        int lineDirection = getLineDirIdx(matcher.group("lineDirection"));
        int row = Integer.parseInt(matcher.group("row"));
        if (lineDirection == -1) return "Incorrect line direction!";
        if (!currentPlayer.getBattleDeck().hasCard(Card.Heal)) return "You don't have Heal card in your battle deck!";
        if (row < 1 || row > 15) return "Invalid row number!";
        row--;
        if (remainingCards == 0) return "You have deployed a troop or spell this turn!";
        remainingCards--;
        Cell cell = gameState.getCell(lineDirection, row);
        cell.addUnit(new Unit(currentTeam, currentPlayer, Card.Heal));
        return "You have deployed Heal successfully!";
    }

    private String deployFireball(Matcher matcher) {

        int lineDirection = getLineDirIdx(matcher.group("lineDirection"));
        if (lineDirection == -1) return "Incorrect line direction!";
        if (!currentPlayer.hasCard(Card.Fireball)) return "You don't have Fireball card in your battle deck!";
        if (remainingCards == 0) return "You have deployed a troop or spell this turn!";

        Castle enemyCastle;
        if (currentTeam == Team.Host) enemyCastle = gameState.getGuest();
        else enemyCastle = gameState.getHost();
        switch (lineDirection) {
            case 0:
                if (enemyCastle.isLeftDead()) return "This castle is already destroyed!";
                enemyCastle.dealLeftDamage(Unit.getDamage(Card.Fireball));
                break;
            case 1:
                if (enemyCastle.isMainDead()) return "This castle is already destroyed!";
                enemyCastle.dealMainDamage(Unit.getDamage(Card.Fireball));
                break;
            case 2:
                if (enemyCastle.isRightDead()) return "This castle is already destroyed!";
                enemyCastle.dealRightDamage(Unit.getDamage(Card.Fireball));
                break;
        }
        remainingCards--;
        return "You have deployed Fireball successfully!";
    }

    private String nextTurn() {
        if (currentTeam == Team.Host) currentTeam = Team.Guest;
        else currentTeam = Team.Host;
        if (currentTeam == Team.Host) currentPlayer = Manager.currentUser;
        else currentPlayer = otherUser;

        remainingCards = 1;
        remainingMoves = 3;

        if (currentTeam == Team.Guest)
            return String.format("Player %s is now playing!", currentPlayer.getUsername());

//        if (gameState.getGuest().isDead())
//            return String.format("Game has ended. Winner: %s", otherUser.getUsername());

        gameState.exec();

        turnIndex++;
        if (gameState.getHost().isDead() || gameState.getGuest().isDead() || turnCount == turnIndex) {
            nextMenu = new MainMenu();
            Manager.currentUser.addExperience(gameState.getHost().getTotalHp());
            otherUser.addExperience(gameState.getGuest().getTotalHp());

            Manager.currentUser.useGold(-25 * gameState.getGuest().countDead());
            otherUser.useGold(-25 * gameState.getHost().countDead());

            if (Utils.DEBUG)
                System.out.printf("host(%s) : %d %d %d\n" +
                                "guest(%s) : %d %d %d\n",
                        Manager.currentUser.getUsername(),
                        gameState.getHost().getLeftHp(),
                        gameState.getHost().getMainHp(),
                        gameState.getHost().getRightHp(),
                        otherUser.getUsername(),
                        gameState.getGuest().getLeftHp(),
                        gameState.getGuest().getMainHp(),
                        gameState.getGuest().getRightHp());
        }

        if (gameState.getHost().isDead() && gameState.getGuest().isDead())
            return "Game has ended. Result: Tie";
        if (gameState.getHost().isDead())
            return String.format("Game has ended. Winner: %s", otherUser.getUsername());
        if (gameState.getGuest().isDead())
            return String.format("Game has ended. Winner: %s", Manager.currentUser.getUsername());
        if (turnCount == turnIndex) {
            if (gameState.getHost().getTotalHp() > gameState.getGuest().getTotalHp())
                return String.format("Game has ended. Winner: %s", Manager.currentUser.getUsername());
            if (gameState.getHost().getTotalHp() < gameState.getGuest().getTotalHp())
                return String.format("Game has ended. Winner: %s", otherUser.getUsername());
            return "Game has ended. Result: Tie";
        }

        return String.format("Player %s is now playing!", currentPlayer.getUsername());
    }
}

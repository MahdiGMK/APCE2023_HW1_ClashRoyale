package ClashRoyale.Gameplay;

import ClashRoyale.User;

public class GameState {
    private final Cell[][] cells = new Cell[3][15];
    private final Castle host, guest;

    public GameState(User host, User guest) {
        this.host = new Castle(host.getLevel());
        this.guest = new Castle(guest.getLevel());
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 15; j++)
                cells[i][j] = new Cell();
    }

    public void exec() {
        // castle dmg
        if (!host.isLeftDead()) for (Unit unit : cells[0][0].getUnits())
            if (unit.getTeam() == Team.Guest && !unit.getCard().isSpell()) {
                unit.dealDamage(host.getDamage());
                host.dealLeftDamage(unit.getDamage());
            }
        if (!host.isMainDead()) for (Unit unit : cells[1][0].getUnits())
            if (unit.getTeam() == Team.Guest && !unit.getCard().isSpell()) {
                unit.dealDamage(host.getDamage());
                host.dealMainDamage(unit.getDamage());
            }
        if (!host.isRightDead()) for (Unit unit : cells[2][0].getUnits())
            if (unit.getTeam() == Team.Guest && !unit.getCard().isSpell()) {
                unit.dealDamage(host.getDamage());
                host.dealRightDamage(unit.getDamage());
            }

        if (!guest.isLeftDead()) for (Unit unit : cells[0][14].getUnits())
            if (unit.getTeam() == Team.Host && !unit.getCard().isSpell()) {
                unit.dealDamage(guest.getDamage());
                guest.dealLeftDamage(unit.getDamage());
            }
        if (!guest.isMainDead()) for (Unit unit : cells[1][14].getUnits())
            if (unit.getTeam() == Team.Host && !unit.getCard().isSpell()) {
                unit.dealDamage(guest.getDamage());
                guest.dealMainDamage(unit.getDamage());
            }
        if (!guest.isRightDead()) for (Unit unit : cells[2][14].getUnits())
            if (unit.getTeam() == Team.Host && !unit.getCard().isSpell()) {
                unit.dealDamage(guest.getDamage());
                guest.dealRightDamage(unit.getDamage());
            }

        // unit dmg
        for (Cell[] row : cells)
            for (Cell cell : row)
                cell.exec();
    }

    public Cell getCell(int col, int row) {
        return cells[col][row];
    }

    // Getter Setter
    public Castle getHost() {
        return host;
    }

    public Castle getGuest() {
        return guest;
    }

    public Cell[][] getCells() {
        return cells;
    }
// Getter Setter End
}

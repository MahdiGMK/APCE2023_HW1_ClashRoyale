import ClashRoyale.Commands;
import ClashRoyale.Menu.IMenu;
import ClashRoyale.Menu.LoginMenu;
import ClashRoyale.Utils;

import java.util.Scanner;

public class Main {
    // Singleton
    public static void main(String[] args) {
        IMenu menu = new LoginMenu();
        Scanner scanner = new Scanner(System.in);

        while (menu != null && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Commands.WhiteLine.matches(line)) System.out.println(Utils.invalidCommand());
            else if (Commands.ShowCurrentMenu.matches(line)) System.out.println(menu);
            else menu = menu.run(line);
        }
    }
// Singleton End
}
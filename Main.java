
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IMenu currentMenu = new LoginMenu();
        Scanner scanner = new Scanner(System.in);
        while (currentMenu.getClass() != ExitMenu.class && scanner.hasNextLine()) {
            currentMenu.run(scanner);
            currentMenu = currentMenu.getNextMenu();
        }
        scanner.close();
    }
}
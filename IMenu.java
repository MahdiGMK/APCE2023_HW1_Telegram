
import java.util.Scanner;
import java.util.regex.Pattern;

public interface IMenu {
    IMenu getNextMenu();

    void run(Scanner scanner);

    static String invalidCommand() {
        return "Invalid command!";
    }

    static boolean MatchToPattern(String str, String regex) {
        return Pattern.compile(regex).matcher(str).matches();
    }
}

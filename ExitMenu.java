
import java.util.Scanner;

public class ExitMenu implements IMenu {

    public ExitMenu() {
    }

    @Override
    public IMenu getNextMenu() {
        return this;
    }

    @Override
    public void run(Scanner scanner) {
    }

}

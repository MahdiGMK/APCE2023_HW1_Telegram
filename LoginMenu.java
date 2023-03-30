import java.util.Scanner;
import java.util.regex.Matcher;

// Done
public class LoginMenu implements IMenu {
    public LoginMenu() {
    }

    private IMenu nextMenu;

    @Override
    public IMenu getNextMenu() {
        return nextMenu;
    }

    @Override
    public void run(Scanner scanner) {
        String line = scanner.nextLine();
        nextMenu = this;

        Matcher loginMatcher = Commands.getMatcher(line, Commands.Login);
        Matcher registerMatcher = Commands.getMatcher(line, Commands.Register);
        Matcher exitMatcher = Commands.getMatcher(line, Commands.Exit);

        String msg;
        if (loginMatcher.matches())
            msg = login(loginMatcher);
        else if (registerMatcher.matches())
            msg = register(registerMatcher);
        else if (exitMatcher.matches()) {
            nextMenu = new ExitMenu();
            return;
        }
        else
            msg = IMenu.invalidCommand();

        System.out.println(msg);
    }

    private String login(Matcher matcher) {
        String id = matcher.group("id");
        String password = matcher.group("password");
        return login(id, password);
    }

    private String login(String id, String password) {
        User user = Messenger.singleton.getUserById(id);

        if (user == null)
            return "No user with this id exists!";
        if (!user.getPassword().equals(password))
            return "Incorrect password!";

        nextMenu = new MessengerMenu(user);
        return "User successfully logged in!";
    }

    private String register(Matcher matcher) {
        String id = matcher.group("id");
        String username = matcher.group("username");
        String password = matcher.group("password");
        return register(id, username, password);
    }

    private String register(String id, String username, String password) {
        boolean userOk = IMenu.MatchToPattern(username, "[a-zA-Z0-9_]*");

        if (!userOk)
            return "Username's format is invalid!";

        if (password.length() < 8 || password.length() > 32)
            return "Password is weak!";

        int passOk = 0;
        for (int i = 0; i < password.length(); i++) {
            String cstr = "" + password.charAt(i);
            if (IMenu.MatchToPattern(cstr, "\\d"))
                passOk |= 1;
            if (IMenu.MatchToPattern(cstr, "[a-z]"))
                passOk |= 2;
            if (IMenu.MatchToPattern(cstr, "[A-Z]"))
                passOk |= 4;
            if (IMenu.MatchToPattern(cstr,
                    "[\\*\\.\\!\\@\\$\\%\\^\\&\\(\\)\\{\\}\\[\\]\\:\\;\\<\\>\\,\\?\\/\\~\\_\\+\\-\\=\\|]"))
                passOk |= 8;
        }

        if (passOk != 15)
            return "Password is weak!";

        if (Messenger.singleton.getUserById(id) != null)
            return "A user with this ID already exists!";

        Messenger.singleton.addUser(new User(id, username, password));
        return "User has been created successfully!";
    }

}

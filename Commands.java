
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    Login("login i (?<id>\\S+) p (?<password>\\S+)"),
    Register("register i (?<id>\\S+) u (?<username>\\S+) p (?<password>\\S+)"),
    Exit("exit"),

    ShowAllChannels("show all channels"),
    CreateNewChannel("create new channel i (?<id>\\S+) n (?<name>\\S+)"),
    JoinChannel("join channel i (?<id>\\S+)"),
    ShowMyChats("show my chats"),
    CreateNewGroup("create new group i (?<id>\\S+) n (?<name>\\S+)"),
    StartPV("start a new private chat with i (?<id>\\S+)"),
    Logout("logout"),
    EnterChat("enter (?<type>private chat|group|channel) i (?<id>\\S+)"),

    SendMessage("send a message c (?<message>[\\s\\S]+)"),
    AddMember("add member i (?<id>\\S+)"),
    ShowAllMessages("show all messages"),
    ShowAllMembers("show all members"),
    Back("back"),
    ;

    private final Pattern pattern;

    public static String PostProcess(String in) {
        // in = in.replace(" ", "\\s+");
        in = in.replace("$", "{}$");
        in = in.replace("{}", "\\s*");
        in = in.replace("{z}", "-?\\d+");
        in = in.replace("{n}", "\\d+");
        return in;
    }

    Commands(String regex) {
        // regex = "{}" + regex + "{}";
        regex = PostProcess(regex);
        pattern = Pattern.compile(regex);
    }

    public static Matcher getMatcher(String input, Commands command) {
        return command.pattern.matcher(input);
    }
}
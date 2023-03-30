
import java.util.Scanner;
import java.util.regex.Matcher;

public class ChatMenu implements IMenu {
    private final Chat chat;
    private final User currentUser;

    public ChatMenu(Chat chat, User currentUser) {
        this.chat = chat;
        this.currentUser = currentUser;
    }

    private IMenu nextMenu;

    @Override
    public void run(Scanner scanner) {
        String line = scanner.nextLine();
        nextMenu = this;

        Matcher showMessagesMatcher = Commands.getMatcher(line, Commands.ShowAllMessages);
        Matcher showMembersMatcher = Commands.getMatcher(line, Commands.ShowAllMembers);
        Matcher addMemberMatcher = Commands.getMatcher(line, Commands.AddMember);
        Matcher sendMessageMatcher = Commands.getMatcher(line, Commands.SendMessage);
        Matcher backMatcher = Commands.getMatcher(line, Commands.Back);

        String msg = "";
        if (showMessagesMatcher.matches())
            msg = showMessages();
        else if (showMembersMatcher.matches())
            msg = showMembers();
        else if (addMemberMatcher.matches())
            msg = addMember(addMemberMatcher);
        else if (sendMessageMatcher.matches())
            msg = sendMessage(sendMessageMatcher);
        else if (backMatcher.matches()) {
            nextMenu = new MessengerMenu(currentUser);
        } else
            msg = IMenu.invalidCommand();
        if (!msg.isEmpty())
            System.out.println(msg);

    }

    @Override
    public IMenu getNextMenu() {
        return nextMenu;
    }

    private String showMessages() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Messages:");
        for (Message m : chat.getMessages()) {
            stringBuilder.append('\n');
            stringBuilder.append(m.getOwner().getName()).append("(").append(m.getOwner().getId()).append(")");
            stringBuilder.append(": \"").append(m.getContent()).append("\"");
        }
        return stringBuilder.toString();
    }

    private String showMembers() {
        if (chat.getType().equals("private chat"))
            return "Invalid command!";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Members:");
        for (User m : chat.getMembers()) {
            stringBuilder.append('\n');
            stringBuilder.append("name: ").append(m.getName()).append(", ");
            stringBuilder.append("id: ").append(m.getId());
            if (m == chat.getOwner())
                stringBuilder.append(" *owner");

        }
        return stringBuilder.toString();
    }

    private String addMember(Matcher matcher) {
        if (chat.getType().equals("private chat"))
            return "Invalid command!";

        if (currentUser != chat.getOwner())
            return "You don't have access to add a member!";

        String id = matcher.group("id");
        User user = Messenger.singleton.getUserById(id);
        if (user == null)
            return "No user with this id exists!";

        if (chat.getMembers().contains(user))
            return "This user is already in the chat!";

        if (chat.getType().equals("channel"))
            user.addChannel((Channel) chat);
        else {
            user.addGroup((Group) chat);
            chat.AddMessage(new Message(currentUser, user.getName() + " has been added to the group!"));
        }
        return "User has been added successfully!";
    }

    private String sendMessage(Matcher matcher) {
        if (chat.getType().equals("channel") && chat.getOwner() != currentUser)
            return "You don't have access to send a message!";
        Message message = new Message(currentUser, matcher.group("message"));
        chat.AddMessage(message);
        if(chat.getType().equals("private chat") && !chat.getId().equals(currentUser.getId())){
            User other = Messenger.singleton.getUserById(chat.getId());
            PrivateChat otherSide = other.getPrivateChatbyId(currentUser.getId());
            otherSide.AddMessage(message);
        }
        return "Message has been sent successfully!";
    }

}

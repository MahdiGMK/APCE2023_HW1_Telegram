import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MessengerMenu implements IMenu {

    private final User currentUser;

    public MessengerMenu(User currentUser) {
        this.currentUser = currentUser;
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

        Matcher showAllChannelsMatcher = Commands.getMatcher(line, Commands.ShowAllChannels);
        Matcher createNewChannelMatcher = Commands.getMatcher(line, Commands.CreateNewChannel);
        Matcher joinChannelMatcher = Commands.getMatcher(line, Commands.JoinChannel);
        Matcher showMyChatsMatcher = Commands.getMatcher(line, Commands.ShowMyChats);
        Matcher createNewGroupMatcher = Commands.getMatcher(line, Commands.CreateNewGroup);
        Matcher startPVMatcher = Commands.getMatcher(line, Commands.StartPV);
        Matcher logoutMatcher = Commands.getMatcher(line, Commands.Logout);
        Matcher enterChatMatcher = Commands.getMatcher(line, Commands.EnterChat);

        String msg;
        if (showAllChannelsMatcher.matches())
            msg = showAllChannels();
        else if (createNewChannelMatcher.matches())
            msg = createChannel(createNewChannelMatcher);
        else if (joinChannelMatcher.matches())
            msg = joinChannel(joinChannelMatcher);
        else if (showMyChatsMatcher.matches())
            msg = showChats();
        else if (createNewGroupMatcher.matches())
            msg = createGroup(createNewGroupMatcher);
        else if (startPVMatcher.matches())
            msg = createPrivateChat(startPVMatcher);
        else if (logoutMatcher.matches()) {
            msg = "Logged out";
            nextMenu = new LoginMenu();
        } else if (enterChatMatcher.matches())
            msg = enterChat(enterChatMatcher);
        else
            msg = IMenu.invalidCommand();

        System.out.println(msg);
    }

    private String showAllChannels() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("All channels:");
        int idx = 1;
        for (Channel c : Messenger.singleton.getChannels()) {
            stringBuilder.append('\n');
            stringBuilder.append(idx++).append(". ");
            stringBuilder.append(c.getName()).append(", ");
            stringBuilder.append("id: ").append(c.getId()).append(", ");
            stringBuilder.append("members: ").append(c.getMembers().size());
        }
        return stringBuilder.toString();
    }

    private String showChats() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chats:");
        int idx = 1;
        ArrayList<Chat> chats = currentUser.getChats();
        chats.sort(((chat1, chat2) -> Integer.compare(
                Integer.max(chat1.getLastAction() , currentUser.joinTime.get(chat1)) ,
                Integer.max(chat2.getLastAction() , currentUser.joinTime.get(chat2)))));
        Collections.reverse(chats);
        for (Chat c : chats) {
            stringBuilder.append('\n');
            stringBuilder.append(idx++).append(". ");
            stringBuilder.append(c.getName()).append(", ");
            stringBuilder.append("id: ").append(c.getId()).append(", ");
            stringBuilder.append(c.getType());
        }
        return stringBuilder.toString();
    }

    private String enterChat(Matcher matcher) {
        String type = matcher.group("type");
        String id = matcher.group("id");
        Chat chat;
        if (type.equals("channel"))
            chat = currentUser.getChannelById(id);
        else if (type.equals("group"))
            chat = currentUser.getGroupById(id);
        else
            chat = currentUser.getPrivateChatbyId(id);
        if (chat == null)
            return "You have no " + type + " with this id!";
        nextMenu = new ChatMenu(chat, currentUser);
        return "You have successfully entered the chat!";
    }

    private String createChannel(Matcher matcher) {
        String id = matcher.group("id");
        String name = matcher.group("name");
        boolean nameOk = IMenu.MatchToPattern(name, "[a-zA-Z0-9_]*");
        if (!nameOk)
            return "Channel name's format is invalid!";
        if (Messenger.singleton.getChannelById(id) != null)
            return "A channel with this id already exists!";
        Channel channel = new Channel(currentUser, id, name);
        Messenger.singleton.addChannel(channel);
        return "Channel " + name + " has been created successfully!";
    }

    private String createGroup(Matcher matcher) {
        String id = matcher.group("id");
        String name = matcher.group("name");
        boolean nameOk = IMenu.MatchToPattern(name, "[a-zA-Z0-9_]*");
        if (!nameOk)
            return "Group name's format is invalid!";
        if (Messenger.singleton.getGroupById(id) != null)
            return "A group with this id already exists!";
        Group group = new Group(currentUser, id, name);
        Messenger.singleton.addGroup(group);
        return "Group " + name + " has been created successfully!";
    }

    private String createPrivateChat(Matcher matcher) {
        String otherId = matcher.group("id");
        User other = Messenger.singleton.getUserById(otherId);
        if (currentUser.getPrivateChatbyId(otherId) != null)
            return "You already have a private chat with this user!";
        if (other == null)
            return "No user with this id exists!";

        if (other == currentUser) {
            PrivateChat to = new PrivateChat(currentUser, other.getId(), currentUser.getName());
            currentUser.addPrivateChat(to);
        } else {
            PrivateChat to = new PrivateChat(currentUser, other.getId(), other.getName());
            currentUser.addPrivateChat(to);
            PrivateChat from = new PrivateChat(other, currentUser.getId(), currentUser.getName());
            other.addPrivateChat(from);
        }

        return "Private chat with " + other.getName() + " has been started successfully!";
    }

    private String joinChannel(Matcher matcher) {
        String id = matcher.group("id");
        if (currentUser.getChannelById(id) != null)
            return "You're already a member of this channel!";
        Channel channel = Messenger.singleton.getChannelById(id);
        if (channel == null)
            return "No channel with this id exists!";
        currentUser.addChannel(channel);
        return "You have successfully joined the channel!";
    }

}

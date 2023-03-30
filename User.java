
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    // UML
    private final ArrayList<Chat> chats;
    private final String id;
    private final String name;
    private final String password;

    HashMap<Chat , Integer> joinTime;

    // Mine
    private final HashMap<String, Group> groupIndex;
    private final HashMap<String, Channel> channelIndex;
    private final HashMap<String, PrivateChat> privateChatIndex;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        chats = new ArrayList<>();

        groupIndex = new HashMap<>();
        channelIndex = new HashMap<>();
        privateChatIndex = new HashMap<>();
        joinTime = new HashMap<>();
    }

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.AddMember(this);
        joinTime.put(chat , new Message(null , null).getSentTime());
    }

    public void addGroup(Group group) {
        addChat(group);
        groupIndex.put(group.getId(), group);
    }

    public void addChannel(Channel channel) {
        addChat(channel);
        channelIndex.put(channel.getId(), channel);
    }

    public void addPrivateChat(PrivateChat privateChat) {
        addChat(privateChat);
        privateChatIndex.put(privateChat.getId(), privateChat);
    }

    public Group getGroupById(String id) {
        return groupIndex.get(id);
    }

    public Channel getChannelById(String id) {
        return channelIndex.get(id);
    }

    public PrivateChat getPrivateChatbyId(String id) {
        return privateChatIndex.get(id);
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("id : %s, name : %s, pass : %s" , id , name , password);
    }
}

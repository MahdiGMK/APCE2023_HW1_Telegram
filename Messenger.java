
import java.util.ArrayList;
import java.util.HashMap;

public class Messenger {
    // UML
    private final ArrayList<Channel> channels;
    private final ArrayList<Group> groups;
    private final ArrayList<User> users;
    private final User currentUser;

    // Mine
    private final HashMap<String, Channel> channelIndex;
    private final HashMap<String, Group> groupIndex;
    private final HashMap<String, User> userIndex;

    public static Messenger singleton = new Messenger();

    private Messenger() {
        channels = new ArrayList<>();
        groups = new ArrayList<>();
        users = new ArrayList<>();
        currentUser = null;

        channelIndex = new HashMap<>();
        groupIndex = new HashMap<>();
        userIndex = new HashMap<>();
    }

    public void addGroup(Group group) {
        groups.add(group);
        groupIndex.put(group.getId(), group);
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channelIndex.put(channel.getId(), channel);
    }

    public void addUser(User user) {
        users.add(user);
        userIndex.put(user.getId(), user);
    }

    public Group getGroupById(String id) {
        return groupIndex.get(id);
    }

    public Channel getChannelById(String id) {
        return channelIndex.get(id);
    }

    public User getUserById(String id) {
        return userIndex.get(id);
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

}

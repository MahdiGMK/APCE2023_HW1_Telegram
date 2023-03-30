import java.util.ArrayList;

public abstract class Chat {
    private final ArrayList<User> members;

    public ArrayList<User> getMembers() {
        return members;
    }

    private final ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return  messages;
    }

    public int getLastAction() {
        return lastAction;
    }

    private final User owner;

    public User getOwner() {
        return owner;
    }

    private final String id;

    public String getId() {
        return id;
    }

    private final String name;

    public String getName() {
        return name;
    }


    private  int lastAction;
    public Chat(User owner, String id, String name) {
        members = new ArrayList<>();
        messages = new ArrayList<>();
        this.owner = owner;
        this.id = id;
        this.name = name;

        lastAction = new Message(null , null).getSentTime();
    }

    public void AddMember(User user) {
        members.add(user);
    }

    public void AddMessage(Message message) {
        messages.add(message);
        lastAction = message.getSentTime();
    }

    public abstract String getType();

    @Override
    public String toString() {
        return String.format("owner : {%s}, member cnt : %d, message cnt : %d" , owner.toString() , members.size() , messages.size());
    }
}

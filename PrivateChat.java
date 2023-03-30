
public class PrivateChat extends Chat {
    public PrivateChat(User owner, String id, String name) {
        super(owner, id, name);
    }

    @Override
    public String getType() {
        return "private chat";
    }
}


public class Message {
    private final User owner;
    private final String content;

    public int getSentTime() {
        return sentTime;
    }

    private  static  int timer;
    private final int sentTime;

    public Message(User owner, String content) {
        this.owner = owner;
        this.content = content;
        sentTime = ++timer;
    }

    public User getOwner() {
        return owner;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("owner : {%s}, content : \"%s\"" , owner.toString() , content);
    }
}

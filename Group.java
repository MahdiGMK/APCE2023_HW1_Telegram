
public class Group extends Chat {
    public Group(User owner, String id, String name) {
        super(owner, id, name);
        owner.addGroup(this);
    }

    @Override
    public String getType() {
        return "group";
    }
}

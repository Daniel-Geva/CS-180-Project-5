package packets.response;

public class DeleteUserResponsePacket extends ResponsePacket{
    int id;

    public DeleteUserResponsePacket(int id) {
        super(true, true);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

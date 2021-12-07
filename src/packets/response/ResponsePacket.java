package packets.response;

import java.io.Serializable;

public class ResponsePacket implements Serializable {

    private static final long serialVersionUID = -5153672930902069453L;

    boolean success;

    public ResponsePacket() {
        this.success = true;
        this.push = false;
    }

    public ResponsePacket(boolean success) {
        this.success = success;
        this.push = false;
    }

    public ResponsePacket(boolean success, boolean push) {
        this.success = success;
        this.push = push;
    }

    public boolean wasSuccess() {
        return this.success;
    }

    private boolean push;

    public boolean getPush() {
        return push;
    }

}

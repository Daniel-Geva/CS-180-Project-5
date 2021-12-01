package packets.response;

import java.io.Serializable;

public class GradedQuizResponsePacket implements Serializable {
    boolean success;
    public GradedQuizResponsePacket() {
        this.success = true;
    }

    public GradedQuizResponsePacket(boolean success) {
        this.success = success;
    }

    public boolean wasSuccess() {
        return success;
    }


}

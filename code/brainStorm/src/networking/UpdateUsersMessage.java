package networking;

import java.util.LinkedList;

public class UpdateUsersMessage extends NetworkMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 2461216009649679019L;
    LinkedList<ClientInfo> activeUsers;
    protected UpdateUsersMessage(int id, LinkedList<ClientInfo> users) {
        super(id, Type.USER_UPDATE);
        activeUsers = users;
    }

}

package networking;

public class ActionMessage extends NetworkMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -1194026060302922403L;
    Object action;
    
    protected ActionMessage(Object _action) {
        super (1, Type.ACTION);
        action = _action;
    }

    protected ActionMessage(int id, Type type) {
        super(id, type);
        // TODO Auto-generated constructor stub
    }

}

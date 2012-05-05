package boardnodes;

import java.io.Serializable;

public class SerializedBoardElt implements Serializable {
    BoardEltType type;
    int UID;
    String body;
    /**
     * 
     */
    private static final long serialVersionUID = 1045659320280995307L;
    public BoardEltType getType() {return type;}
    public int getUID() {return UID;}
}

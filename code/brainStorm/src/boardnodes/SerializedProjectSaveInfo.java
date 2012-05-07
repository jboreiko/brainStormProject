package boardnodes;

public class SerializedProjectSaveInfo extends SerializedBoardElt{
    /**
     * 
     */
    private static final long serialVersionUID = -631659691934095308L;
    public String projectName;
    public int startUID;
    public SerializedProjectSaveInfo(String name, int uid) {
        this.UID = -1;
        startUID = uid;
        projectName = name;
        this.type = BoardEltType.INFO;
    }
}

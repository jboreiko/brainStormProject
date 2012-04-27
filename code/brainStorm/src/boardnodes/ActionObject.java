package boardnodes;

import java.io.Serializable;

public class ActionObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3357308980308622084L;
	Object changeInfo;
	public ActionObject(Object[] o) {
		changeInfo = o;
	}
}

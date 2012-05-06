package whiteboard;

import boardnodes.BoardElt;

public class SearchResult {
	public BoardElt elt;
	public int index;
	
	public SearchResult(BoardElt b, int i) {
		elt = b;
		index = i;
	}
}

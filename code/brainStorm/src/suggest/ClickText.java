package suggest;

public class ClickText {
	
	private String _query;
	private int _start;
	private int _end;
	
	public ClickText(String query, int start, int end) {
		_end = end;
		_query = query;
		_start = start;
	}

	public void setQuery(String query) {
		this._query = query;
	}

	public String getQuery() {
		return _query;
	}

	public int getStart() {
		return _start;
	}

	public void setStart(int start) {
		this._start = start;
	}

	public int getEnd() {
		return _end;
	}

	public void setEnd(int end) {
		this._end = end;
	}

}

package boardnodes;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

public class ScribbleNodeEdit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3101127796145396154L;
	Object content;
	ScribbleNodeEditType type;
	public ScribbleNodeEdit(List<ColoredPoint> stroke) {
		type = ScribbleNodeEditType.DRAW;
		content = stroke;
	}
	public ScribbleNodeEdit(Rectangle r) {
		type = ScribbleNodeEditType.DRAG;
		content=  r;
	}
	public ScribbleNodeEdit(Color color) {
		type = ScribbleNodeEditType.BACKGROUND;
		content = color;
	}
}

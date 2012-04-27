package boardnodes;

import java.awt.Point;

public class SerializedBoardPath extends SerializedBoardElt {
    private static final long serialVersionUID = -4310196162247409269L;
    public Point _start;
    public Point _end;
    public BoardPathType _stroke; 
    public SerializedBoardPath(Point start, Point end,  BoardPathType stroke) {
        _start = start;
        _end = end;
        type = BoardEltType.PATH;
        _stroke = stroke;
    }
}

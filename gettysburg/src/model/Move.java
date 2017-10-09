package model;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgUnit;

public class Move {
    public GameCoordinate src, dest;
    public GbgUnit unit;

    private Move(GameCoordinate src, GameCoordinate dest, GbgUnit unit) {
        this.src = src;
        this.dest = dest;
        this.unit = unit;
    }

    public static Move makePlacement(GameCoordinate dest, GbgUnit unit){
        return new Move(null, dest, unit);
    }

    public static Move makeMove(GameCoordinate src, GameCoordinate dest, GbgUnit unit){
        return new Move(src, dest, unit);
    }
}

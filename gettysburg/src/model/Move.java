package model;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgUnit;

public class Move {
    public Coordinate src, dest;
    public GbgUnit unit;

    private Move(Coordinate src, Coordinate dest, GbgUnit unit) {
        this.src = src;
        this.dest = dest;
        this.unit = unit;
    }

    public static Move makePlacement(Coordinate dest, GbgUnit unit){
        return new Move(null, dest, unit);
    }

    public static Move makeMove(Coordinate src, Coordinate dest, GbgUnit unit){
        return new Move(src, dest, unit);
    }
}

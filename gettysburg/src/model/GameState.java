package model;

import gettysburg.common.GbgGameStep;

import java.util.Collection;
import java.util.function.BiFunction;

public class GameState {
    public Board board;
    public GbgGameStep step;
    //should be state validators
    public Collection<BiFunction<GameState, Move, Boolean>> moveValidators;

    public GameState(Board board, GbgGameStep step) {
        this.board = board;
        this.step = step;
    }

    public GameState(Board board, GbgGameStep step,
                     Collection<BiFunction<GameState, Move, Boolean>> moveValidators) {
        this.board = board;
        this.step = step;
        this.moveValidators = moveValidators;
    }
}

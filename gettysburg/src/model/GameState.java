package model;

import engine.GameEngine;
import gettysburg.common.GbgGameStep;

import java.util.Collection;
import java.util.function.BiFunction;

public class GameState {
    public Board board;
    public GbgGameStep step;
    //should be state validators
    public Collection<BiFunction<GameState, Move, Boolean>> moveValidators;

    private GameState(Board board, GbgGameStep step) {
        this.board = board;
        this.step = step;
    }

    private GameState(Board board, GbgGameStep step,
                     Collection<BiFunction<GameState, Move, Boolean>> moveValidators) {
        this.board = board;
        this.step = step;
        this.moveValidators = moveValidators;
    }

    public static GameState makeState(Board board, GbgGameStep step){
        return new GameState(board, step);
    }

    public static GameState makeState(GameEngine game){
        return new GameState(
                game.getBoard(), game.getCurrentStep());
    }


    public static GameState makeState(Board board, GbgGameStep step,
                                      Collection<BiFunction<GameState, Move, Boolean>> moveValidators){
        return new GameState(board, step);
    }
}

package engine;

import gettysburg.common.*;
import model.Board;
import model.GameCoordinate;

import java.util.List;

public class TestGameEngine extends GameEngine implements TestGbgGame {

    @Override
    public void putUnitAt(GbgUnit unit, int x, int y, Direction facing) {
        unit.setFacing(facing);
        board.placeUnit(GameCoordinate.makeCoordinate(x, y), unit);
    }

    @Override
    public void clearBoard() {
        board.clear();
    }

    public Board getBoard(){
        return board;
    }

    @Override
    public void setGameTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public void setGameStep(GbgGameStep step) {
        this.step = step;
    }

    @Override
    public void setBattleResult(BattleDescriptor battle, BattleResult result) {

    }

    @Override
    public void setBattleResults(List<BattleResult> results) {

    }
}

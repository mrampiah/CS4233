package engine;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgGame;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import model.Board;
import model.GameState;
import model.Move;
import model.Unit;
import util.BattleOrder;
import validators.PlacementValidator;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class GameEngine implements GbgGame{
    private Board board;
    private GbgGameStep step;
    private int turn;

    public GameEngine(){
        board = new Board();

        getReinforcements();
        //place pieces before turn 1
        turn = 1;
        loop();
    }

    private void loop(){
    }

    private void getReinforcements(){
        Map<GbgUnit, Coordinate> unions = BattleOrder.getUnionBattleOrder()
                .stream().filter(init -> init.turn == turn)
                .collect(Collectors.toMap(init -> init.unit, init -> init.where));


        Map<GbgUnit, Coordinate> confeds = BattleOrder.getConfederateBattleOrder()
                .stream().filter(init -> init.turn == turn)
                .collect(Collectors.toMap(init -> init.unit, init -> init.where));

        for(GbgUnit unit : unions.keySet()){
            placeUnit(unions.get(unit), unit);
        }

        for(GbgUnit unit : confeds.keySet()){
            placeUnit(confeds.get(unit), unit);
        }
    }

    @Override
    public int getTurnNumber(){
        return turn;
    }

    public void placeUnit(Coordinate dest, GbgUnit unit){
        //test no unit present
        //load placement validators
        List<BiFunction<GameState, Move, Boolean>> moveValidators = new LinkedList<>();
        moveValidators.addAll(Arrays.asList(
                PlacementValidator.noSource));
        //todo: add location validator

        boolean valid = true;
        GameState state = new GameState(board, step, moveValidators);
        Move move = Move.makePlacement(dest, unit);

        for(BiFunction<GameState, Move, Boolean> validator : moveValidators){
            if(!validator.apply(state, move))
                valid = false;
        }

        if(valid)
            board.placeUnit(dest, unit);
    }

    @Override
    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        return board.getUnitsAt(where);
    }
}

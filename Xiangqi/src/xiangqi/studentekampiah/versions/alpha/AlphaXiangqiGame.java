package xiangqi.studentekampiah.versions.alpha;

import xiangqi.common.*;
import xiangqi.studentekampiah.versions.TestCoordinate;

/**
 * Created by mrampiah on 2/9/17.
 */
public class AlphaXiangqiGame implements XiangqiGame {
    private int turn;
    private XiangqiCoordinate source =  TestCoordinate.makeCoordinate(1, 1);
    private XiangqiCoordinate destination =  TestCoordinate.makeCoordinate(1, 2);
    private String moveMessage;

    public AlphaXiangqiGame() {
        turn = 0;
    }

    @Override
    public MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        if(turn++ == 0) {
            if( testValidMove(source, destination)){
                setMoveMessage(MoveResult.OK.toString());
                return MoveResult.OK;
            }else {
                setMoveMessage(MoveResult.ILLEGAL.toString());
                return MoveResult.ILLEGAL;
            }
        }else {
            setMoveMessage(MoveResult.RED_WINS.toString());
            return MoveResult.RED_WINS;
        }
    }

    private void setMoveMessage(String message){
        moveMessage = String.format("Previous move result: %s\n", message);
    }

    @Override
    public String getMoveMessage() {
        return moveMessage;
    }

    private boolean testValidMove(XiangqiCoordinate source, XiangqiCoordinate destination){
        return this.source.equals(source) && this.destination.equals(destination);
    }

    @Override
    public XiangqiPiece getPieceAt(XiangqiCoordinate where, XiangqiColor aspect) {
        return new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.NONE;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.NONE;
            }
        };
    }

    @Override
    public void initialize(Object... args) {
    }
}

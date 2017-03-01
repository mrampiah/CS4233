package xiangqi.studentekampiah;

import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;
import xiangqi.studentekampiah.versions.TestCoordinate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by mrampiah on 2/9/17.
 */
public class AlphaGameTests {

    private XiangqiGame game;

    @Before
    public void setup(){
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.ALPHA_XQ);
    }

    @Test
    public void testGameFactoryMakesAlpha(){
        assertNotNull(game);
    }

    @Test
    public void testValidFirstMoveByRed(){
        assertEquals(MoveResult.OK, game.makeMove(makeCoordinate(1, 1),
                makeCoordinate(1, 2)));
    }

    @Test
    public void testBlackMakesValidMove(){
        game.makeMove(TestCoordinate.makeCoordinate(1, 1),
            TestCoordinate.makeCoordinate(1, 2));
        assertEquals(MoveResult.RED_WINS, game.makeMove(makeCoordinate(1, 1),
                makeCoordinate(1, 2)));
    }

    @Test
    public void tryToMoveToInvalidLocation(){
        assertEquals(MoveResult.ILLEGAL, game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1)));
        assertTrue(game.getMoveMessage().length() > 0);
    }

    @Test
    public void tryToMoveFromInvalidLocation(){
        assertEquals(MoveResult.ILLEGAL, game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 2)));
        assertTrue(game.getMoveMessage().length() > 0);
    }

    @Test
    public void getPieceAtReturnsNone(){
        XiangqiPiece piece = game.getPieceAt(makeCoordinate(1, 1), XiangqiColor.RED);
        assertEquals(XiangqiPieceType.NONE, piece.getPieceType());
        assertEquals(XiangqiColor.NONE, piece.getColor());
    }

    private TestCoordinate makeCoordinate(int rank, int file){
        return TestCoordinate.makeCoordinate(rank, file);
    }
}

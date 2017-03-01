package xiangqi.studentekampiah;

import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;
import xiangqi.studentekampiah.versions.TestCoordinate;
import xiangqi.studentekampiah.versions.beta.BetaXiangqiGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mrampiah on 2/10/17.
 */
public class BetaGameTests {
    private XiangqiGame game;
    private XiangqiCoordinate[] chariots;
    private XiangqiCoordinate[] advisors;
    private XiangqiCoordinate general;
    private XiangqiCoordinate soldier;

    @Before
    public void setup(){
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.BETA_XQ);

        chariots = new XiangqiCoordinate[2];
        chariots[0] = makeCoordinate(1, 1);
        chariots[1] = makeCoordinate(1, 5);

        advisors = new XiangqiCoordinate[2];
        advisors[0] = makeCoordinate(1, 2);
        advisors[1] = makeCoordinate(1, 4);

        general = makeCoordinate(1, 3);
        soldier = makeCoordinate(2, 3);
    }

    @Test
    public void testGameFactoryMakesBeta(){
        assertNotNull(game);
    }

    @Test
    public void testGetPieceAt(){
        XiangqiCoordinate general = makeCoordinate(1, 3);

        //same baseCoordinate should be interpreted right for either aspect
        assertEquals(XiangqiPieceType.GENERAL, game.getPieceAt(general, XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.GENERAL, game.getPieceAt(general, XiangqiColor.RED).getPieceType());
    }

    @Test
    public void testCorrectStartLocation(){
        //should be at same location given aspect ration

        //possible Chariot locations
        List<XiangqiCoordinate> chariot = new ArrayList<>();
        chariot.add(makeCoordinate(1, 1));
        chariot.add(makeCoordinate(1, 5));

        //possible advisor locations
        List<XiangqiCoordinate> advisor = new ArrayList<>();
        advisor.add(makeCoordinate(1, 2));
        advisor.add(makeCoordinate(1, 4));

        XiangqiCoordinate general = makeCoordinate(1, 3);
        XiangqiCoordinate soldier = makeCoordinate(2, 3);

        //check red locations
        assertEquals(XiangqiPieceType.CHARIOT, game.getPieceAt(chariot.get(0), XiangqiColor.RED).getPieceType());
        assertEquals(XiangqiPieceType.CHARIOT, game.getPieceAt(chariot.get(1), XiangqiColor.RED).getPieceType());
        assertEquals(XiangqiPieceType.ADVISOR, game.getPieceAt(advisor.get(0), XiangqiColor.RED).getPieceType());
        assertEquals(XiangqiPieceType.ADVISOR, game.getPieceAt(advisor.get(1), XiangqiColor.RED).getPieceType());
        assertEquals(XiangqiPieceType.GENERAL, game.getPieceAt(general, XiangqiColor.RED).getPieceType());
        assertEquals(XiangqiPieceType.SOLDIER, game.getPieceAt(soldier, XiangqiColor.RED).getPieceType());

        //check black locations
        assertEquals(XiangqiPieceType.CHARIOT, game.getPieceAt(chariot.get(0), XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.CHARIOT, game.getPieceAt(chariot.get(1), XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.ADVISOR, game.getPieceAt(advisor.get(0), XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.ADVISOR, game.getPieceAt(advisor.get(1), XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.GENERAL, game.getPieceAt(general, XiangqiColor.BLACK).getPieceType());
        assertEquals(XiangqiPieceType.SOLDIER, game.getPieceAt(soldier, XiangqiColor.BLACK).getPieceType());
    }

    //region test moves

    @Test
    public void testHorizontalMove(){
        //one or more steps to the right
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 1), makeCoordinate(1, 2)));
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 1), makeCoordinate(1, 4)));

        //different rank
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(2, 1), makeCoordinate(2, 2)));
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(2, 1), makeCoordinate(2, 4)));

        //one or more steps to the left
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 3), makeCoordinate(1, 1)));
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 5), makeCoordinate(1, 4)));


        //different rank
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(4, 3), makeCoordinate(4, 1)));
        assertTrue(BetaXiangqiGame.isHorizontalMove(makeCoordinate(4, 5), makeCoordinate(4, 4)));
    }

    @Test
    public void testVerticalMove(){
        //one or more steps up
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(1, 1), makeCoordinate(2, 1)));
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(1, 1), makeCoordinate(3, 1)));

        //different file
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(2, 2), makeCoordinate(3, 2)));
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(2, 2), makeCoordinate(5, 2)));

        //one or more steps down
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(3, 3), makeCoordinate(2, 3)));
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(3, 3), makeCoordinate(1, 3)));


        //different file
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(4, 5), makeCoordinate(3, 5)));
        assertTrue(BetaXiangqiGame.isVerticalMove(makeCoordinate(4, 5), makeCoordinate(2, 5)));
    }

    @Test
    public void testDiagonalMoves(){
        //proving they're not so start off with them being true
        boolean horizontal, vertical;

        //top-left
        horizontal = BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 2), makeCoordinate(2, 1));
        vertical = BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 2), makeCoordinate(2, 1));
        assertFalse(horizontal || vertical);

        //top-right
        horizontal = BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 1), makeCoordinate(2, 2));
        vertical = BetaXiangqiGame.isHorizontalMove(makeCoordinate(1, 1), makeCoordinate(2, 2));
        assertFalse(horizontal || vertical);

        //bottom-left
        horizontal = BetaXiangqiGame.isHorizontalMove(makeCoordinate(3, 4), makeCoordinate(2, 3));
        vertical = BetaXiangqiGame.isHorizontalMove(makeCoordinate(3, 4), makeCoordinate(2, 3));
        assertFalse(horizontal || vertical);

        //bottom-right
        horizontal = BetaXiangqiGame.isHorizontalMove(makeCoordinate(4, 2), makeCoordinate(3, 1));
        vertical = BetaXiangqiGame.isHorizontalMove(makeCoordinate(4, 2), makeCoordinate(3, 1));
        assertFalse(horizontal || vertical);
    }

    @Test
    public void testUpDownMove(){
        assertTrue(BetaXiangqiGame.isUpMove(makeCoordinate(1, 3), makeCoordinate(3, 3)));
        assertFalse(BetaXiangqiGame.isUpMove(makeCoordinate(2, 3), makeCoordinate(1, 3)));
    }

    @Test
    public void testLeftRightMove(){
        assertTrue(BetaXiangqiGame.isLeftMove(makeCoordinate(1, 3), makeCoordinate(1, 2)));
        assertFalse(BetaXiangqiGame.isLeftMove(makeCoordinate(1, 3), makeCoordinate(1, 5)));
    }

    @Test
    public void testLegalVerticalChariotMove(){
        //from initial setup a move to rank 4 is allowed
        //move to rank 5 captures a piece; still a valid move

        assertEquals(MoveResult.OK, game.makeMove(chariots[0], makeCoordinate(4, 1)));
        assertEquals(MoveResult.OK, game.makeMove(chariots[0], makeCoordinate(5, 1)));

        assertEquals(MoveResult.OK, game.makeMove(chariots[1], makeCoordinate(4, 5)));
        assertEquals(MoveResult.OK, game.makeMove(chariots[1], makeCoordinate(5, 5)));
    }

    @Test
    public void testLegalHorizontalChariotMove(){
        //needs some work from initial setup


    }

    @Test
    public void testIllegalVerticatalChariotMove(){
        //need to move some pieces first
    }

    @Test
    public void testIllegalHorizontalChariotMove(){
        //try to move sideways = fail
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[0], makeCoordinate(1, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[1], makeCoordinate(1, 4)));

        //diagonal move fails
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[0], makeCoordinate(2, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[1], makeCoordinate(2, 4)));
    }

    @Test
    public void testLegalGeneralMove(){
        //need to free up some space first
    }

    @Test
    public void testIllegalGeneralMove(){
        //unreachable tiles
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, makeCoordinate(1, 5)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, makeCoordinate(1, 1)));

        //piece blocking
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, soldier));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, advisors[0]));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, advisors[1]));

        //diagonal moves fail
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, makeCoordinate(2, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(general, makeCoordinate(2, 4)));
    }

    @Test
    public void testLegalSoldierMove(){
        //can go forward one step
        assertEquals(MoveResult.OK, game.makeMove(soldier, makeCoordinate(3, 3)));
    }

    @Test
    public void testIllegalSoldierMove(){
        //cant go backward
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, makeCoordinate(1, 3)));

        //cant make diagonal moves
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, makeCoordinate(3, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, makeCoordinate(3, 4)));

        //cant make sideways moves
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, makeCoordinate(2, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, makeCoordinate(2, 4)));

        //blocking piece
        assertEquals(MoveResult.ILLEGAL, game.makeMove(soldier, general));

        //TODO: check forward blocking piece
    }

    @Test
    public void testLegalAdvisorMove(){
        assertEquals(MoveResult.OK, game.makeMove(advisors[0], makeCoordinate(2, 1)));
        assertEquals(MoveResult.OK, game.makeMove(advisors[1], makeCoordinate(2, 5)));
    }

    @Test
    public void testIllegalAdvisorMove(){
        //not diagonal
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[0], makeCoordinate(2, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[1], makeCoordinate(2, 4)));
        //todo: check "valid" horizontal move

        //blocking piece
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[0], general));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[1], general));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[0], soldier));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(advisors[1], soldier));
    }

    @Test
    public void testOutOfBoundsMove(){
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[0], makeCoordinate(0, 2)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[0], makeCoordinate(1, 0)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[1], makeCoordinate(3, 6)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[1], makeCoordinate(0, 4)));
        assertEquals(MoveResult.ILLEGAL, game.makeMove(chariots[1], makeCoordinate(7, 4)));
    }

    @Test
    public void testIllegalMoveMessage(){
        MoveResult result = game.makeMove(chariots[0], makeCoordinate(0, 2));
        assertTrue(game.getMoveMessage().length() > 0);
        assertTrue(game.getMoveMessage().contains("ILLEGAL"));
    }

    @Test
    public void testLegalMoveMessage(){
        MoveResult result =  game.makeMove(advisors[0], makeCoordinate(2, 1));
        assertTrue(game.getMoveMessage().length() > 0);
        assertTrue(game.getMoveMessage().contains("OK"));
    }

    @Test
    public void testTurnChange(){
        XiangqiColor currentTurn = ((BetaXiangqiGame) game).getCurrentTurn();
        //change on legal move
        game.makeMove(advisors[0], makeCoordinate(2, 1));
        assertNotEquals(currentTurn, ((BetaXiangqiGame) game).getCurrentTurn());

        //remain the same on illegal move
        game.makeMove(chariots[0], makeCoordinate(0, 2));
        assertNotEquals(currentTurn, ((BetaXiangqiGame) game).getCurrentTurn());
    }

    //endregion

    private TestCoordinate makeCoordinate(int rank, int file){
        return  TestCoordinate.makeCoordinate(rank, file);
    }
}

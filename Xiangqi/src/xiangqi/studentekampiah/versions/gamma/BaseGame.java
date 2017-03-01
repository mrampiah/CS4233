package xiangqi.studentekampiah.versions.gamma;

import xiangqi.common.*;

/**
 * Created by mrampiah on 3/1/17.
 */
public class BaseGame implements XiangqiGame {
    @Override
    public MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        return null;
    }

    @Override
    public String getMoveMessage() {
        return null;
    }

    @Override
    public XiangqiPiece getPieceAt(XiangqiCoordinate where, XiangqiColor aspect) {
        return null;
    }
}

package xiangqi.studentekampiah.versions.beta;

import xiangqi.common.XiangqiColor;
import xiangqi.common.XiangqiPiece;
import xiangqi.common.XiangqiPieceType;

/**
 * Created by mrampiah on 2/9/17.
 */
public class RedPiece implements XiangqiPiece {
    @Override
    public XiangqiColor getColor() {
        return XiangqiColor.RED;
    }

    @Override
    public XiangqiPieceType getPieceType() {
        return XiangqiPieceType.NONE;
    }
}

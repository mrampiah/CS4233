package xiangqi.studentekampiah.versions.beta;

import xiangqi.common.*;
import xiangqi.studentekampiah.versions.TestCoordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrampiah on 2/9/17.
 */
public class BetaXiangqiGame implements XiangqiGame {
    private static final int BOARD_LENGTH = 5;
    private List<List<XiangqiPiece>> board;

    //red pieces
    private XiangqiPiece redGeneral;
    private XiangqiPiece redSoldier;
    private XiangqiPiece[] redAdvisors;
    private XiangqiPiece[] redChariots;

    //black pieces
    private XiangqiPiece blackGeneral;
    private XiangqiPiece blackSoldier;
    private XiangqiPiece[] blackAdvisors;
    private XiangqiPiece[] blackChariots;

    private String moveMessage;
    private XiangqiColor currentTurn;
    private XiangqiCoordinate blackGeneralLocation, redGeneralLocation;
    private int redMoves, blackMoves;

    private BetaXiangqiGame() {
        board = new ArrayList<>();

        //initialize board to none pieces
        for (int i = 0; i < BOARD_LENGTH; i++) {
            board.add(i, new ArrayList<>());
            for (int j = 0; j < BOARD_LENGTH; j++) {
                board.get(i).add(j, new XiangqiPiece() {
                    @Override
                    public XiangqiColor getColor() {
                        return XiangqiColor.NONE;
                    }

                    @Override
                    public XiangqiPieceType getPieceType() {
                        return XiangqiPieceType.NONE;
                    }
                });
            }
        }
        setup();
        currentTurn = XiangqiColor.RED; //start with red
        redMoves = blackMoves = 0;
    }

    public static BetaXiangqiGame makeBetaXiangqiGame() {
        return new BetaXiangqiGame();
    }

    private void setup() {
        createPieces();

        //place red pieces
        board.get(0).set(0, redChariots[0]);
        board.get(0).set(4, redChariots[1]);
        board.get(0).set(1, redAdvisors[0]);
        board.get(0).set(3, redAdvisors[0]);
        board.get(0).set(2, redGeneral);
        board.get(1).set(2, redSoldier);
        redGeneralLocation = makeCoordinate(1, 3);

        //place black pieces
        board.get(4).set(0, blackChariots[0]);
        board.get(4).set(4, blackChariots[1]);
        board.get(4).set(1, blackAdvisors[0]);
        board.get(4).set(3, blackAdvisors[0]);
        board.get(4).set(2, blackGeneral);
        board.get(3).set(2, blackSoldier);
        blackGeneralLocation = makeCoordinate(5, 3);
    }

    private void createPieces() {
        //create red pieces
        redGeneral = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.GENERAL;
            }
        };
        redSoldier = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.SOLDIER;
            }
        };

        redAdvisors = new XiangqiPiece[2];
        redAdvisors[0] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.ADVISOR;
            }
        };
        redAdvisors[1] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.ADVISOR;
            }
        };

        redChariots = new XiangqiPiece[2];
        redChariots[0] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.CHARIOT;
            }
        };
        redChariots[1] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.RED;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.CHARIOT;
            }
        };

        //create black pieces
        blackGeneral = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.GENERAL;
            }
        };
        blackSoldier = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.SOLDIER;
            }
        };

        blackAdvisors = new XiangqiPiece[2];
        blackAdvisors[0] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.ADVISOR;
            }
        };
        blackAdvisors[1] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.ADVISOR;
            }
        };

        blackChariots = new XiangqiPiece[2];
        blackChariots[0] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.CHARIOT;
            }
        };
        blackChariots[1] = new XiangqiPiece() {
            @Override
            public XiangqiColor getColor() {
                return XiangqiColor.BLACK;
            }

            @Override
            public XiangqiPieceType getPieceType() {
                return XiangqiPieceType.CHARIOT;
            }
        };
    }

    public XiangqiColor getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        XiangqiPiece piece = getPieceAt(source, currentTurn);
        boolean pass = false;
        MoveResult result = MoveResult.ILLEGAL;
        switch (piece.getPieceType()) {
            case GENERAL:
                pass = isValidGeneralMove(source, destination);
                break;
            case ADVISOR:
                pass = isValidAdvisorMove(source, destination);
                break;
            case CHARIOT:
                pass = isValidChariotMove(source, destination);
                break;
            case SOLDIER:
                pass = isValidSoldierMove(source, destination);
                break;
            case NONE:
                result = MoveResult.ILLEGAL;
        }

        if (pass) {
            if (isMoveOutOfBounds(source, destination) || hasPieceInWay(piece, source, destination))
                return MoveResult.ILLEGAL;

            //either OK or someone wins
            result = MoveResult.OK;
            if(piece.getPieceType() == XiangqiPieceType.GENERAL){
                if(piece.getColor() == XiangqiColor.BLACK)
                    blackGeneralLocation = destination;
                else
                    redGeneralLocation = destination;
            }

        }
        if(result == MoveResult.OK) { //change the current turn if move is ok
            if(currentTurn == XiangqiColor.RED) {
                redMoves++;
                currentTurn = XiangqiColor.BLACK;
            } else {
                blackMoves++;
                currentTurn = XiangqiColor.RED;
            }

        }
        //update move message
        moveMessage = String.format("Move result: %s", result.toString());

        return gameStatus(result);
    }

    private boolean hasPieceInWay(XiangqiPiece piece, XiangqiCoordinate source, XiangqiCoordinate destination) {
        switch (piece.getPieceType()) {
            case GENERAL: //only need to check if there's a piece at the destination:

            case SOLDIER: //same for general

            case ADVISOR: //same for general & soldier
                return !getPieceAt(destination, piece.getColor()).getPieceType().equals(XiangqiPieceType.NONE);
            case CHARIOT: //make sure it's not jumping over any pieces in the process
                //check horizontal move
                if (isHorizontalMove(source, destination)) {
                    //check if left/right move
                    int start, end = 0;
                    if (isLeftMove(source, destination)) {
                        start = destination.getFile();
                        end = source.getFile();
                    } else {
                        start = source.getFile();
                        end = destination.getFile();
                    }
                    for (int i = start; i < end; i++) {
                        if (!getPieceAt(makeCoordinate(source.getRank(), i), currentTurn)
                                .getPieceType().equals(XiangqiPieceType.NONE))
                            return true;
                    }
                } else {//vertical move
                    //check if up/down move
                    int start, end = 0;
                    if (isUpMove(source, destination)) {
                        start = source.getFile();
                        end = destination.getFile();
                    } else {
                        start = destination.getFile();
                        end = source.getFile();
                    }
                    for (int i = start; i < end; i++) {
                        if (!getPieceAt(makeCoordinate(source.getFile(), i), currentTurn)
                                .getPieceType().equals(XiangqiPieceType.NONE))
                            return true;
                    }
                }
                //if it gets to this point it's a valid chariot move
                return false;
        }
        return true;
    }

    private TestCoordinate makeCoordinate(int rank, int file) {
        return TestCoordinate.makeCoordinate(rank, file);
    }

    @Override
    public String getMoveMessage() {
        return moveMessage;
    }

    @Override
    public XiangqiPiece getPieceAt(XiangqiCoordinate where, XiangqiColor aspect) {
        if (aspect == XiangqiColor.RED)
            return board.get(where.getRank() - 1).get(where.getFile() - 1);
        else
            return board.get(BOARD_LENGTH - where.getRank()).get(BOARD_LENGTH - where.getFile());
    }

    private boolean isValidGeneralMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        //delimit the only valid positions: [12, 14]
        int validRank = 1;
        int validFileLowerBound = 2;
        int validFileUpperBound = 4;

        //check if the piece moves one step
        boolean oneStep = Math.abs(source.getFile() - destination.getFile()) == 1 ||
                Math.abs(source.getRank() - destination.getRank()) == 1;

        //ensure the source is within bounds
        boolean validSource = source.getRank() == validRank &&
                (validFileLowerBound <= source.getFile() || source.getFile() <= validFileUpperBound);

        //ensure the destination is within bounds
        boolean validDestination = destination.getRank() == validRank &&
                (validFileLowerBound <= destination.getFile() || destination.getFile() <= validFileUpperBound);

        return validSource && validDestination && oneStep;
    }

    private boolean isValidChariotMove(XiangqiCoordinate source, XiangqiCoordinate destination) {

        return isHorizontalMove(source, destination) || isVerticalMove(source, destination);
    }

    private boolean isValidAdvisorMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        return !(isVerticalMove(source, destination) || isHorizontalMove(source, destination));
    }

    private boolean isValidSoldierMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        boolean oneStep = destination.getRank() - source.getRank() == 1; //check if it moves one step forward
        return oneStep && isVerticalMove(source, destination) && !isHorizontalMove(source, destination);
    }

    private MoveResult gameStatus(MoveResult status){
        if(status == MoveResult.ILLEGAL)
            return MoveResult.ILLEGAL;
        if(blackMoves == 10)
            return MoveResult.DRAW;
        if(visibleGenerals()){
            if(currentTurn == XiangqiColor.BLACK)
                return MoveResult.BLACK_WINS;
            else
                return MoveResult.RED_WINS;
        }
        return MoveResult.OK;
    }

    private boolean visibleGenerals(){
        return !hasPieceInWay(blackGeneral, blackGeneralLocation,
                makeCoordinate(redGeneralLocation.getRank(), redGeneralLocation.getFile()));
    }

    public static boolean isHorizontalMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        int rankDifference = Math.abs(source.getRank() - destination.getRank());
        int fileDifference = Math.abs(source.getFile() - destination.getFile());

        return rankDifference == 0 && fileDifference > 0;
    }

    public static boolean isVerticalMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        int rankDifference = Math.abs(source.getRank() - destination.getRank());
        int fileDifference = Math.abs(source.getFile() - destination.getFile());

        return rankDifference > 0 && fileDifference == 0;
    }

    public static boolean isLeftMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        return destination.getFile() - source.getFile() < 0;
    }

    public static boolean isUpMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        return destination.getRank() - source.getRank() > 0;
    }

    public boolean isMoveOutOfBounds(XiangqiCoordinate source, XiangqiCoordinate destination){
        int sourceFile = source.getFile();
        int sourceRank = source.getRank();
        int destinationFile = destination.getFile();
        int destinationRank = destination.getRank();

        boolean overBounds = sourceFile > BOARD_LENGTH || destinationFile > BOARD_LENGTH
                || sourceRank > BOARD_LENGTH || destinationRank > BOARD_LENGTH;

        boolean underBounds = sourceFile < 1 || sourceRank < 1 || destinationFile < 1 || destinationRank < 1;

        return overBounds || underBounds;
    }

}

package xiangqi.studentekampiah.versions;

import xiangqi.common.XiangqiCoordinate;

/**
 * Created by mrampiah on 2/9/17.
 */
public class TestCoordinate implements XiangqiCoordinate{
    private int rank, file;

    private TestCoordinate(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public static TestCoordinate makeCoordinate(int rank, int file){
        return new TestCoordinate(rank, file);
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof TestCoordinate))
            return false;
        return ((TestCoordinate) other).getFile() == file &&
                ((TestCoordinate) other).getRank() == rank;
    }
}

package xiangqi.studentekampiah;

import org.junit.Before;
import org.junit.Test;
import xiangqi.studentekampiah.versions.gamma.GammaCoordinate;
import xiangqi.studentekampiah.versions.gamma.Orientation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mrampiah on 2/10/17.
 */
public class GammaGameTests {
    GammaCoordinate baseCoordinate;

    @Before
    public void setup(){
        baseCoordinate = GammaCoordinate.makeCoordinate(1, 1);
    }

    @Test
    public void testMakeCoordinate(){
        assertNotNull(baseCoordinate);
    }

    @Test
    public void testHorizontalMove(){
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(2, 1);

        //to the left
        assertEquals(Orientation.HORIZONTAL, baseCoordinate.getOrientation(coordinate2));

        //to the right
        assertEquals(Orientation.HORIZONTAL, coordinate2.getOrientation(baseCoordinate));

        //repeat for distance greater than 1
        coordinate2 = GammaCoordinate.makeCoordinate(5, 1);

        //to the left
        assertEquals(Orientation.HORIZONTAL, baseCoordinate.getOrientation(coordinate2));

        //to the right
        assertEquals(Orientation.HORIZONTAL, coordinate2.getOrientation(baseCoordinate));

    }

    @Test
    public void testVerticalMove(){
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(1, 2);

        //up
        assertEquals(Orientation.VERTICAL, baseCoordinate.getOrientation(coordinate2));

        //down
        assertEquals(Orientation.VERTICAL, coordinate2.getOrientation(baseCoordinate));

        //repeat for distance greater than 1
        coordinate2 = GammaCoordinate.makeCoordinate(1, 5);

        //up
        assertEquals(Orientation.VERTICAL, baseCoordinate.getOrientation(coordinate2));

        //down
        assertEquals(Orientation.VERTICAL, coordinate2.getOrientation(baseCoordinate));

    }

    @Test
    public void testDiagonalMove(){
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(2, 2);

        //up
        assertEquals(Orientation.DIAGONAL, baseCoordinate.getOrientation(coordinate2));

        //down
        assertEquals(Orientation.DIAGONAL, coordinate2.getOrientation(baseCoordinate));

        //repeat for distance greater than 1
        coordinate2 = GammaCoordinate.makeCoordinate(5, 5);

        //up
        assertEquals(Orientation.DIAGONAL, baseCoordinate.getOrientation(coordinate2));

        //down
        assertEquals(Orientation.DIAGONAL, coordinate2.getOrientation(baseCoordinate));

    }

    @Test
    public void testVerticalGetDistance(){
        //0
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(1, 1);
        assertEquals(0, baseCoordinate.distanceTo(coordinate2));

        //1
        coordinate2 = GammaCoordinate.makeCoordinate(1, 2);
        assertEquals(1, baseCoordinate.distanceTo(coordinate2));

        // >1
        coordinate2 = GammaCoordinate.makeCoordinate(1, 5);
        assertEquals(4, baseCoordinate.distanceTo(coordinate2));
    }

    @Test
    public void testHorizontalGetDistance(){
        //0
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(1, 1);
        assertEquals(0, baseCoordinate.distanceTo(coordinate2));

        //1
        coordinate2 = GammaCoordinate.makeCoordinate(2, 1);
        assertEquals(1, baseCoordinate.distanceTo(coordinate2));

        // >1
        coordinate2 = GammaCoordinate.makeCoordinate(4, 1);
        assertEquals(3, baseCoordinate.distanceTo(coordinate2));
    }

    @Test
    public void testDiagonalGetDistance(){
        //0
        GammaCoordinate coordinate2 = GammaCoordinate.makeCoordinate(1, 1);
        assertEquals(0, baseCoordinate.distanceTo(coordinate2));

        //1
        coordinate2 = GammaCoordinate.makeCoordinate(2, 2);
        assertEquals(1, baseCoordinate.distanceTo(coordinate2));

        // >1
        coordinate2 = GammaCoordinate.makeCoordinate(6, 6);
        assertEquals(5, baseCoordinate.distanceTo(coordinate2));
    }

}
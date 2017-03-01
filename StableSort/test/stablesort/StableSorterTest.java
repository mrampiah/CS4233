/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package stablesort;

import org.junit.Before;
import org.junit.Test;
import util.Sortable;
import util.SortableFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for StableSorter.
 * @version Sep 26, 2016
 */
public class StableSorterTest
{
	private SortableFactory<Integer> iFactory = null;
	private StableSorter sorter = null;
	
	@Before
	public void setup()
	{
		iFactory = new SortableFactory<Integer>();
		sorter = new StableSorter();
	}
	
	@Test	// 1
	public void sortEmptyArray()
	{
		Sortable<Integer>[] result = sorter.stableSort(new Sortable[0] );
		assertEquals(0, result.length);
	}

	@Test //2
	public void sortSingleElement(){
		Integer[] result = sorter.stableSort(new Integer[] {1});
		assertEquals(1, result.length);
		assertEquals(1, (int) result[0]);
	}


    @Test //3
    public void sortTwoElementsInOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {1, 2});
        assertEquals(2, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(2, (int) result[1]);
    }


    @Test //4
    public void sortTwoElementsOutOfOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {2, 1});
        assertEquals(2, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(2, (int) result[1]);
    }


    @Test //5
    public void sortThreeElementsInOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {1, 2, 3});
        assertEquals(3, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(2, (int) result[1]);
        assertEquals(3, (int) result[2]);
    }

    @Test //6
    public void sortThreeElementsOutOfOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {3, 1, 2});
        assertEquals(3, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(2, (int) result[1]);
        assertEquals(3, (int) result[2]);
    }

    @Test //7
    public void sortMultipleElementsInOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        runAsserts(result);
    }

    @Test //8
    public void sortMultipleElementsOutOfOrder(){
        Integer[] result = sorter.stableSort(new Integer[] {8, 4, 6, 5, 2, 3, 7, 9, 1, 0});
        runAsserts(result);
    }

    private void runAsserts(Integer[] result){
        assertEquals(10, result.length);
        assertEquals(0, (int) result[0]);
        assertEquals(1, (int) result[1]);
        assertEquals(2, (int) result[2]);
        assertEquals(3, (int) result[3]);
        assertEquals(4, (int) result[4]);
        assertEquals(5, (int) result[5]);
        assertEquals(6, (int) result[6]);
        assertEquals(7, (int) result[7]);
        assertEquals(8, (int) result[8]);
        assertEquals(9, (int) result[9]);
    }

    @Test //9
    public void sortFourElementsWithDuplicates(){
        Integer[] result = sorter.stableSort(new Integer[] {1, 2, 3, 2});
        assertEquals(4, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(2, (int) result[1]);
        assertEquals(2, (int) result[2]);
        assertEquals(3, (int) result[3]);
    }

    @Test //10
    public void sortObjectsWithDuplicates(){
        Item item1 = new Item(1, "red");
        Item item2 = new Item(2, "red");
        Item item3 = new Item(1, "black");

        Item[] result = sorter.stableSort(new Item[]{item1, item2, item3});
        assertEquals(3, result.length);
        assertEquals(item1, result[0]);
        assertEquals(item3, result[1]);
        assertEquals(item2, result[2]);
    }

    @Test //11
    public void sortObjectsWithDuplicates2(){
        Item[] items = new Item[] {
                new Item(1, "red"),
                new Item(2, "red"),
                new Item(3, "black"),
                new Item(2, "black"),
                new Item(2, "red")};

        Item[] result = sorter.stableSort(items.clone());
        assertEquals(5, result.length);
        assertEquals(items[0], result[0]);
        assertEquals(items[1], result[1]);
        assertEquals(items[3], result[2]);
        assertEquals(items[4], result[3]);
        assertEquals(items[2], result[4]);
    }
}

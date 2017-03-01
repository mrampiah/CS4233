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

/**
 * This class performs stable sorts on an array of objects.
 * @version Sep 26, 2016
 */
public class StableSorter implements StableSort
{
	
	/*
	 * @see stablesort.StableSort#stableSort(T)
	 */
	@Override
	public <T extends Comparable<T>> T[] stableSort(T ... items)
	{
	    return bubbleSort(items);
	}

	private <T extends Comparable<T>> T[] bubbleSort(T... items){
        boolean inOrder;
        do{
            inOrder = true;
            for(int i = 0; i < items.length - 1; i++){
                if(items[i].compareTo(items[i + 1]) > 0) {
                    inOrder = false;
                    T temp = items[i + 1];
                    items[i + 1] = items[i];
                    items[i] = temp;
                }
            }
        }while(!inOrder);

        return items;
    }
}

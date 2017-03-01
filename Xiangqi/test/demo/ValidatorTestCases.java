package demo;

import org.junit.Test;
import xiangqi.studentekampiah.versions.gamma.demo.Coordinate;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static xiangqi.studentekampiah.versions.gamma.demo.Coordinate.makeCoordinate;
import static xiangqi.studentekampiah.versions.gamma.demo.ValidatorFactory.PieceType.K;
import static xiangqi.studentekampiah.versions.gamma.demo.ValidatorFactory.PieceType.R;
import static xiangqi.studentekampiah.versions.gamma.demo.ValidatorFactory.makeValidators;

public class ValidatorTestCases
{
	
	@Test
	public void validRookMove()
	{
		final List<BiPredicate<Coordinate, Coordinate>> rookValidators =
				makeValidators(R);
		for (BiPredicate<Coordinate, Coordinate> p : rookValidators) {
			assertTrue(p.test(makeCoordinate(1, 1), makeCoordinate(1, 5)));
		}
	}

	@Test
	public void invalidKingMove()
	{
		final List<BiPredicate<Coordinate, Coordinate>> kingValidators = 
				makeValidators(K);
		boolean result = true;
		Iterator<BiPredicate<Coordinate, Coordinate>> iter = kingValidators.iterator();
		while (result && iter.hasNext()) {
			result = iter.next().test(makeCoordinate(1, 4), makeCoordinate(1, 7));
		}
		assertFalse(result);
	}
}

package coberturawifi.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import coberturawifi.model.BitsChromosome;
import coberturawifi.model.Coordinates;

public class ChromosomeTest {

	private static final short ONE_ACCESS_POINT = 1;
	private static final short THREE_ACCESS_POINTS = 3;

	private static final short FIRST_ACCESS_POINT = 0;
	private static final short SECOND_ACCESS_POINT = 1;
	private static final short THIRD_ACCESS_POINT = 2;

	@Test
	public void singleAccessPointDataRepresentationTest() {
		final Coordinates coordinates = new Coordinates(543, 30000);

		final BitsChromosome chromosome = new BitsChromosome(ONE_ACCESS_POINT);
		chromosome.setCoordinatesFor(coordinates, FIRST_ACCESS_POINT);

		assertEquals(coordinates, chromosome.getCoordinatesFor(FIRST_ACCESS_POINT));

		final List<Coordinates> coordinatesList = chromosome.getCoordinateList();
		assertEquals(ONE_ACCESS_POINT, coordinatesList.size());
		assertEquals(coordinates, coordinatesList.get(0));
	}

	@Test
	public void threeAccessPointDataRepresentationTest() {
		final Coordinates firstCoordinates = new Coordinates(100, 0);
		final Coordinates secondCoordinates = new Coordinates(0, 8547);
		final Coordinates thirdCoordinates = new Coordinates(25478, 1110);

		final BitsChromosome chromosome = new BitsChromosome(THREE_ACCESS_POINTS);
		chromosome.setCoordinatesFor(firstCoordinates, FIRST_ACCESS_POINT);
		chromosome.setCoordinatesFor(secondCoordinates, SECOND_ACCESS_POINT);
		chromosome.setCoordinatesFor(thirdCoordinates, THIRD_ACCESS_POINT);

		assertEquals(firstCoordinates, chromosome.getCoordinatesFor(FIRST_ACCESS_POINT));
		assertEquals(secondCoordinates, chromosome.getCoordinatesFor(SECOND_ACCESS_POINT));
		assertEquals(thirdCoordinates, chromosome.getCoordinatesFor(THIRD_ACCESS_POINT));

		final List<Coordinates> coordinatesList = chromosome.getCoordinateList();
		assertEquals(THREE_ACCESS_POINTS, coordinatesList.size());
		assertEquals(firstCoordinates, coordinatesList.get(0));
		assertEquals(secondCoordinates, coordinatesList.get(1));
		assertEquals(thirdCoordinates, coordinatesList.get(2));

		final int actualBits = chromosome.getBits().size();
		final int expectedBits = chromosome.getMaxBitsCount();
		assertEquals(expectedBits, actualBits);
	}

	@Test
	public void getMaxBitsCountTest() {
		{
			final BitsChromosome chromosome = new BitsChromosome(ONE_ACCESS_POINT);
			final short bitsCount = BitsChromosome.CHROMOSOME_ATTRIBUTE_COUNT * BitsChromosome.CHROMOSOME_ATTRIBUTE_LENGTH
					* ONE_ACCESS_POINT;
			assertEquals(bitsCount, chromosome.getMaxBitsCount());
			assertEquals(bitsCount, BitsChromosome.getMaxBitsCount(ONE_ACCESS_POINT));
		}
		{
			final BitsChromosome chromosome = new BitsChromosome(THREE_ACCESS_POINTS);
			final short bitsCount = BitsChromosome.CHROMOSOME_ATTRIBUTE_COUNT * BitsChromosome.CHROMOSOME_ATTRIBUTE_LENGTH
					* THREE_ACCESS_POINTS;
			assertEquals(bitsCount, chromosome.getMaxBitsCount());
			assertEquals(bitsCount, BitsChromosome.getMaxBitsCount(THREE_ACCESS_POINTS));
		}
	}

}

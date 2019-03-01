package coberturawifi.solution.bitsrepresentation;

import org.junit.Test;

import coberturawifi.model.Coordinates;
import coberturawifi.solution.bitsrepresentation.BitsChromosome;

public class CrossingHalfByHalfStrategyTest {

	@Test
	public void crossingTest() {

		final BitsChromosome father = new BitsChromosome((short) 3);
		father.setCoordinatesFor(new Coordinates(10, 10), (short) 0);
		father.setCoordinatesFor(new Coordinates(33, 33), (short) 1);
		father.setCoordinatesFor(new Coordinates(50, 50), (short) 2);

		System.out.println("Father ------------");
		System.out.println(father.getCoordinateList());
		System.out.println(father.getBits());

		final BitsChromosome mother = new BitsChromosome((short) 3);
		mother.setCoordinatesFor(new Coordinates(20, 20), (short) 0);
		mother.setCoordinatesFor(new Coordinates(200, 200), (short) 1);
		mother.setCoordinatesFor(new Coordinates(400, 400), (short) 2);

		System.out.println("Mother ------------");
		System.out.println(mother.getCoordinateList());
		System.out.println(mother.getBits());

		final BitsCrossingHalfByHalfStrategy strategy = BitsCrossingHalfByHalfStrategy.getInstance();

		final BitsChromosome firstBorn = strategy.cross(father, mother);

		System.out.println("Firstborn ------------");
		System.out.println(firstBorn.getCoordinateList());
		System.out.println(firstBorn.getBits());

		final BitsChromosome youngest = strategy.cross(mother, father);

		System.out.println("Youngest ------------");
		System.out.println(youngest.getCoordinateList());
		System.out.println(youngest.getBits());
	}

}

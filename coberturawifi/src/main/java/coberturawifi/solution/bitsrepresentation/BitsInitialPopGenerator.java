package coberturawifi.solution.bitsrepresentation;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.BitsChromosome;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Coordinates;
import coberturawifi.solution.InitialPopulationGenerator;
import coberturawifi.util.Randomizer;

public class BitsInitialPopGenerator implements InitialPopulationGenerator {

	private static BitsInitialPopGenerator instance;

	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;

	private BitsInitialPopGenerator() {
		this.populationSize = Configs.getInstance().getShort(Configs.POPULATION_SIZE);
	}

	public static BitsInitialPopGenerator getInstance() {
		if (instance == null) {
			instance = new BitsInitialPopGenerator();
		}
		return instance;
	}

	public List<BitsChromosome> generatePopulation(final Blueprint plant, final short accessPointCount) {

		final List<BitsChromosome> population = new ArrayList<>(populationSize);
		for (short i = 0; i < populationSize; i++) {
			final BitsChromosome chromosome = generateChromosome(plant, accessPointCount);
			population.add(chromosome);
		}
		return population;
	}

	private BitsChromosome generateChromosome(final Blueprint plant, final short accessPointCount) {
		final BitsChromosome chromosome = new BitsChromosome(accessPointCount);
		for (short apIndex = 0; apIndex < accessPointCount; apIndex++) {
			final int x = randomizer.nextInt(plant.widthInPixels);
			final int y = randomizer.nextInt(plant.heightInPixels);

			final Coordinates coords = new Coordinates(x, y);

			chromosome.setCoordinatesFor(coords, apIndex);
		}
		return chromosome;
	}

}

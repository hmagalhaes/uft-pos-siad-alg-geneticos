package coberturawifi.solution.realrepresentation;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Coordinates;
import coberturawifi.model.IntsChromosome;
import coberturawifi.solution.InitialPopulationGenerator;
import coberturawifi.util.Randomizer;

public class IntsInitialPopGenerator implements InitialPopulationGenerator {

	private static IntsInitialPopGenerator instance;

	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;

	private IntsInitialPopGenerator() {
		this.populationSize = Configs.getInstance().getShort(Configs.POPULATION_SIZE);
	}

	public static IntsInitialPopGenerator getInstance() {
		if (instance == null) {
			instance = new IntsInitialPopGenerator();
		}
		return instance;
	}

	public List<IntsChromosome> generatePopulation(final Blueprint plant, final short accessPointCount) {

		final List<IntsChromosome> population = new ArrayList<>(populationSize);
		for (short i = 0; i < populationSize; i++) {
			final IntsChromosome chromosome = generateChromosome(plant, accessPointCount);
			population.add(chromosome);
		}
		return population;
	}

	private IntsChromosome generateChromosome(final Blueprint plant, final short accessPointCount) {
		final IntsChromosome chromosome = new IntsChromosome(accessPointCount);
		for (short apIndex = 0; apIndex < accessPointCount; apIndex++) {
			final int x = randomizer.nextInt(plant.widthInPixels);
			final int y = randomizer.nextInt(plant.heightInPixels);

			final Coordinates coords = new Coordinates(x, y);

			chromosome.setCoordinatesFor(coords, apIndex);
		}
		return chromosome;
	}

}

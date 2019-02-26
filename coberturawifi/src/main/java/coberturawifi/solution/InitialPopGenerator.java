package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Coordinates;
import coberturawifi.util.Randomizer;

class InitialPopGenerator {

	private static InitialPopGenerator instance;

	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;

	private InitialPopGenerator() {
		this.populationSize = Configs.getInstance().getShort(Configs.POPULATION_SIZE);
	}

	public static InitialPopGenerator getInstance() {
		if (instance == null) {
			instance = new InitialPopGenerator();
		}
		return instance;
	}

	public List<Chromosome> generatePopulation(final Blueprint plant, final short accessPointCount) {

		final List<Chromosome> population = new ArrayList<>(populationSize);
		for (short i = 0; i < populationSize; i++) {
			final Chromosome chromosome = generateChromosome(plant, accessPointCount);
			population.add(chromosome);
		}
		return population;
	}

	private Chromosome generateChromosome(final Blueprint plant, final short accessPointCount) {
		final Chromosome chromosome = new Chromosome(accessPointCount);
		for (short apIndex = 0; apIndex < accessPointCount; apIndex++) {
			final int x = randomizer.nextInt(plant.widthInPixels);
			final int y = randomizer.nextInt(plant.heightInPixels);

			final Coordinates coords = new Coordinates(x, y);

			chromosome.setCoordinatesFor(coords, apIndex);
		}
		return chromosome;
	}

}

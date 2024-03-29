package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Coordinates;
import coberturawifi.util.Randomizer;

public class CrossingAgent {

	private static CrossingAgent instance;

	private final short crossedPopulationSize;
	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;

	private CrossingAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		{
			final float crossedRatio = configs.getFloat(Configs.POPULATION_RATIO_CROSSED);
			final short crossedCount = (short) Math.round(populationSize * crossedRatio);
			this.crossedPopulationSize = (short) Math.max(1, crossedCount);
		}
		System.out.println("RealCrossingAgent => crossedPopSize: " + crossedPopulationSize);
	}

	public static CrossingAgent getInstance() {
		if (instance == null) {
			instance = new CrossingAgent();
		}
		return instance;
	}

	public short getCrossedPopulationSize() {
		return crossedPopulationSize;
	}

	public List<Chromosome> crossPopulation(final List<Chromosome> population) {
		final List<Chromosome> crossedPopulation = new ArrayList<>(crossedPopulationSize);
		while (crossedPopulation.size() < crossedPopulationSize) {
			final int fatherIndex = randomizer.nextInt(populationSize);
			final int motherIndex = randomizer.nextInt(populationSize);

			final Chromosome father = (Chromosome) population.get(fatherIndex);
			final Chromosome mother = (Chromosome) population.get(motherIndex);

			final Chromosome firstBorn = cross(father, mother);
			crossedPopulation.add(firstBorn);

			if (crossedPopulation.size() >= crossedPopulationSize) {
				break;
			}

			final Chromosome youngest = cross(mother, father);
			crossedPopulation.add(youngest);
		}
		return crossedPopulation;
	}

	private Chromosome cross(final Chromosome father, final Chromosome mother) {
		final List<Coordinates> fatherCoords = father.getCoordinateList();
		final List<Coordinates> motherCoords = mother.getCoordinateList();

		final short chromosomeSize = (short) fatherCoords.size();
		final short fatherHalfSize = (short) (chromosomeSize / 2);
		final short motherHalfSize = (short) (chromosomeSize - fatherHalfSize);
		final short motherHalfIndex = (short) (chromosomeSize - motherHalfSize);

		final List<Coordinates> fatherHalf = fatherCoords.subList(0, fatherHalfSize);
		final List<Coordinates> motherHalf = motherCoords.subList(motherHalfIndex, chromosomeSize);

		final List<Coordinates> childCoords = new ArrayList<>(chromosomeSize);
		childCoords.addAll(fatherHalf);
		childCoords.addAll(motherHalf);

		// TODO move it to unity test
		if (childCoords.size() != chromosomeSize) {
			throw new IllegalStateException("Chromosome created with invalid size => " + childCoords.size());
		}

		return mother.withCoordinates(childCoords);
	}

}

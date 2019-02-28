package coberturawifi.solution.realrepresentation;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Coordinates;
import coberturawifi.model.RealChromosome;
import coberturawifi.solution.MutationAgent;
import coberturawifi.util.Randomizer;

public class RealMutationAgent extends MutationAgent {

	private static RealMutationAgent instance;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final float mutationRatio;
	private final short mutantPopulationSize;

	private RealMutationAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		this.mutationRatio = configs.getFloat(Configs.MUTATION_BITS_RATIO);
		{
			final float mutantRatio = configs.getFloat(Configs.POPULATION_RATIO_MUTANT);
			final short mutantCount = (short) Math.round(this.populationSize * mutantRatio);
			this.mutantPopulationSize = (short) Math.max(1, mutantCount);
		}
		System.out.println(
				"MutationAgent => mutationRatio: " + mutationRatio + ", mutantPopSize: " + mutantPopulationSize);
	}

	public static RealMutationAgent getInstance() {
		if (instance == null) {
			instance = new RealMutationAgent();
		}
		return instance;
	}

	public short getMutantPopulationSize() {
		return mutantPopulationSize;
	}

	public List<? extends Chromosome> mutatePopulation(final List<? extends Chromosome> population,
			final Blueprint blueprint) {

		final short mutationPoints = calcMutationItems(population);
		System.out.println("Mutation points (coords): " + mutationPoints);

		final List<RealChromosome> mutatedPop = new ArrayList<>(mutantPopulationSize);
		for (int i = 0; i < mutantPopulationSize; i++) {
			final int mutantIndex = randomizer.nextInt(populationSize);
			final RealChromosome healty = (RealChromosome) population.get(mutantIndex);
			final RealChromosome mutant = mutateChromosome(healty, mutationPoints, blueprint);

			mutatedPop.add(mutant);
		}
		return mutatedPop;
	}

	private RealChromosome mutateChromosome(final RealChromosome healthy, final short mutationBits,
			final Blueprint blueprint) {

		final List<Coordinates> coordsList = healthy.getCoordinateList();
		for (short mutationCount = 0; mutationCount < mutationBits; mutationCount++) {
			final short coordIndex = randomizer.nextShort(healthy.getMaxItemCount());
			final Coordinates healthyCoords = healthy.getCoordinateList().get(coordIndex);
			final Coordinates mutantCoords = generateCoord(healthyCoords, blueprint);

			coordsList.set(coordIndex, mutantCoords);
		}
		return healthy.withCoordinates(coordsList);
	}

	private Coordinates generateCoord(final Coordinates coords, final Blueprint blueprint) {

		final short xOrY = randomizer.nextShort((short) 2);

		if (xOrY <= 0) {
			final int x = randomizer.nextInt(blueprint.widthInPixels);
			return coords.withX(x);
		}

		final int y = randomizer.nextInt(blueprint.heightInPixels);
		return coords.withY(y);
	}

	private short calcMutationItems(final List<? extends Chromosome> population) {
		final RealChromosome anyChromosome = (RealChromosome) population.get(0);
		final short totalItems = ((RealChromosome) anyChromosome).getMaxItemCount();

		final short mutantItems = (short) Math.round(totalItems * mutationRatio);
		return (short) Math.max(1, mutantItems);
	}

}

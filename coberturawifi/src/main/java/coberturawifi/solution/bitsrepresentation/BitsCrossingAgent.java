package coberturawifi.solution.bitsrepresentation;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.BitsChromosome;
import coberturawifi.solution.CrossingAgent;
import coberturawifi.util.Randomizer;

public class BitsCrossingAgent implements CrossingAgent {

	private static BitsCrossingAgent instance;

	public final short crossedPopulationSize;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final BitsCrossingStrategy crossingStrategy;

	private BitsCrossingAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		{
			final float crossedRatio = configs.getFloat(Configs.POPULATION_RATIO_CROSSED);
			final short crossedCount = (short) Math.round(populationSize * crossedRatio);
			this.crossedPopulationSize = (short) Math.max(1, crossedCount);
		}
		{
			final String strategy = configs.getString(Configs.STRATEGY_CROSSING);
			if (BitsCrossingStrategyTwoByTwo.STRATEGY_NAME.equals(strategy)) {
				this.crossingStrategy = BitsCrossingStrategyTwoByTwo.getInstance();
			} else if (BitsCrossingHalfByHalfStrategy.STRATEGY_NAME.equals(strategy)) {
				this.crossingStrategy = BitsCrossingHalfByHalfStrategy.getInstance();
			} else {
				throw new IllegalArgumentException("Bad crossing strategy => " + strategy);
			}
		}
		System.out.println("CrossingAgent => crossedPopSize: " + crossedPopulationSize);
	}

	public static BitsCrossingAgent getInstance() {
		if (instance == null) {
			instance = new BitsCrossingAgent();
		}
		return instance;
	}

	public List<BitsChromosome> crossPopulation(final List<BitsChromosome> population) {
		final List<BitsChromosome> crossedPopulation = new ArrayList<>(crossedPopulationSize);
		while (crossedPopulation.size() < crossedPopulationSize) {
			final int fatherIndex = randomizer.nextInt(populationSize);
			final int motherIndex = randomizer.nextInt(populationSize);

			final BitsChromosome father = population.get(fatherIndex);
			final BitsChromosome mother = population.get(motherIndex);

			final BitsChromosome firstBorn = crossingStrategy.cross(father, mother);
			crossedPopulation.add(firstBorn);

			if (crossedPopulation.size() >= crossedPopulationSize) {
				break;
			}

			final BitsChromosome youngest = crossingStrategy.cross(mother, father);
			crossedPopulation.add(youngest);
		}
		return crossedPopulation;
	}

}

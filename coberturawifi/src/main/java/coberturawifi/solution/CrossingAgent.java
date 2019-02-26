package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Chromosome;
import coberturawifi.util.Randomizer;

class CrossingAgent {

	public static final String STRATEGY_NAME = "chunks";

	private static CrossingAgent instance;

	public final short crossedPopulationSize;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final CrossingStrategy crossingStrategy;

	private CrossingAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		{
			final float crossedRatio = configs.getFloat(Configs.POPULATION_RATIO_CROSSED);
			final short crossedCount = (short) Math.round(populationSize * crossedRatio);
			this.crossedPopulationSize = (short) Math.max(1, crossedCount);
		}
		{
			final String strategy = configs.getString(Configs.STRATEGY_CROSSING);
			if (CrossingStrategyTwoByTwo.STRATEGY_NAME.equals(strategy)) {
				this.crossingStrategy = CrossingStrategyTwoByTwo.getInstance();
			} else if (CrossingHalfByHalfStrategy.STRATEGY_NAME.equals(strategy)) {
				this.crossingStrategy = CrossingHalfByHalfStrategy.getInstance();
			} else {
				throw new IllegalArgumentException("Bad crossing strategy => " + strategy);
			}
		}
		System.out.println("CrossingAgent => crossedPopSize: " + crossedPopulationSize);
	}

	public static CrossingAgent getInstance() {
		if (instance == null) {
			instance = new CrossingAgent();
		}
		return instance;
	}

	public List<Chromosome> crossPopulation(final List<Chromosome> population) {
		final List<Chromosome> crossedPopulation = new ArrayList<>(crossedPopulationSize);
		while (crossedPopulation.size() < crossedPopulationSize) {
			final int fatherIndex = randomizer.nextInt(populationSize);
			final int motherIndex = randomizer.nextInt(populationSize);

			final Chromosome father = population.get(fatherIndex);
			final Chromosome mother = population.get(motherIndex);

			final Chromosome firstBorn = crossingStrategy.cross(father, mother);
			crossedPopulation.add(firstBorn);

			if (crossedPopulation.size() >= crossedPopulationSize) {
				break;
			}

			final Chromosome youngest = crossingStrategy.cross(mother, father);
			crossedPopulation.add(youngest);
		}
		return crossedPopulation;
	}

}

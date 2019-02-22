package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.util.Randomizer;

class CrossingAgent {

	public static final String STRATEGY_NAME = "chunks";

	private static CrossingAgent instance;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final short crossedPopulationSize;
	private final CrossingStrategy crossingStrategy;

	private CrossingAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		this.crossedPopulationSize = (short) (populationSize - EliteAgent.getInstance().elitePopulationSize
				- MutationAgent.getInstance().mutantPopulationSize);
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
	}

	public static CrossingAgent getInstance() {
		if (instance == null) {
			instance = new CrossingAgent();
		}
		return instance;
	}

	public List<Chromosome> crossPopulation(final List<Chromosome> population) {
		final List<Chromosome> crossedPopulation = new ArrayList<>(crossedPopulationSize);
		for (int i = 0; i < crossedPopulationSize; i++) {
			final int fatherIndex = randomizer.nextInt(populationSize);
			final int motherIndex = randomizer.nextInt(populationSize);

			final Chromosome father = population.get(fatherIndex);
			final Chromosome mother = population.get(motherIndex);

			final Chromosome firstBorn = crossingStrategy.cross(father, mother);
			final Chromosome youngest = crossingStrategy.cross(mother, father);

			crossedPopulation.add(firstBorn);
			crossedPopulation.add(youngest);
		}
		return crossedPopulation;
	}

}

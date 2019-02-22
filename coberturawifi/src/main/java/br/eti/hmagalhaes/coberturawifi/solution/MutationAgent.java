package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.util.BitList;
import br.eti.hmagalhaes.coberturawifi.util.Randomizer;

class MutationAgent {

	private static MutationAgent instance;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final float mutationRatio;

	final short mutantPopulationSize;

	private MutationAgent() {
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

	public static MutationAgent getInstance() {
		if (instance == null) {
			instance = new MutationAgent();
		}
		return instance;
	}

	public List<Chromosome> mutatePopulation(final List<Chromosome> population) {
		final short mutationBits = calcMutationBits(population);

		final List<Chromosome> mutatedPop = new ArrayList<>(population);
		for (int i = 0; i < mutantPopulationSize; i++) {
			final int mutantIndex = randomizer.nextInt(populationSize);
			final Chromosome healty = population.get(mutantIndex);
			final Chromosome mutant = mutateChromosome(healty, mutationBits);

			mutatedPop.set(mutantIndex, mutant);
		}
		return mutatedPop;
	}

	private Chromosome mutateChromosome(final Chromosome healthy, final short mutationBits) {

		final BitList bits = healthy.getBits();
		for (short mutationCount = 0; mutationCount < mutationBits; mutationCount++) {
			final short bitIndex = randomizer.nextShort(healthy.getBitsCount());
			final boolean newBit = !bits.get(bitIndex);

			bits.set(bitIndex, newBit);
		}
		return healthy.withBits(bits);
	}

	private short calcMutationBits(List<Chromosome> population) {
		final Chromosome anyChromosome = population.get(0);
		final short totalBits = anyChromosome.getBitsCount();

		final short mutantBits = (short) Math.round(totalBits * mutationRatio);
		return (short) Math.max(1, mutantBits);
	}

}

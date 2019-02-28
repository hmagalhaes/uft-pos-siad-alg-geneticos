package coberturawifi.solution.bitsrepresentation;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.solution.MutationAgent;
import coberturawifi.util.BitList;
import coberturawifi.util.Randomizer;

public class BitsMutationAgent extends MutationAgent {

	private static BitsMutationAgent instance;

	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();
	private final short populationSize;
	private final float mutationRatio;
	private final short mutantPopulationSize;

	private BitsMutationAgent() {
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

	public static BitsMutationAgent getInstance() {
		if (instance == null) {
			instance = new BitsMutationAgent();
		}
		return instance;
	}

	public short getMutantPopulationSize() {
		return mutantPopulationSize;
	}

	public List<Chromosome> mutatePopulation(final List<Chromosome> population, final Blueprint blueprin) {

		final short mutationBits = calcMutationBits(population);

		final List<Chromosome> mutatedPop = new ArrayList<>(mutantPopulationSize);
		for (int i = 0; i < mutantPopulationSize; i++) {
			final int mutantIndex = randomizer.nextInt(populationSize);
			final BitsChromosome healty = (BitsChromosome) population.get(mutantIndex);
			final BitsChromosome mutant = mutateChromosome(healty, mutationBits);

			mutatedPop.add(mutant);
		}
		return mutatedPop;
	}

	private BitsChromosome mutateChromosome(final BitsChromosome healthy, final short mutationBits) {

		final BitList bits = healthy.getBits();
		for (short mutationCount = 0; mutationCount < mutationBits; mutationCount++) {
			final short bitIndex = randomizer.nextShort(healthy.getMaxBitsCount());
			final boolean newBit = !bits.get(bitIndex);

			bits.set(bitIndex, newBit);
		}
		return healthy.withBits(bits);
	}

	private short calcMutationBits(final List<? extends Chromosome> population) {
		final BitsChromosome anyChromosome = (BitsChromosome) population.get(0);
		final short totalBits = ((BitsChromosome) anyChromosome).getMaxBitsCount();

		final short mutantBits = (short) Math.round(totalBits * mutationRatio);
		return (short) Math.max(1, mutantBits);
	}

}

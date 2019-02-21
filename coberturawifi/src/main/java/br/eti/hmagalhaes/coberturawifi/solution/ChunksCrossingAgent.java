package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.util.BitList;

class ChunksCrossingAgent implements CrossingAgent {

	public static final String STRATEGY_NAME = "chunks";

	private static final byte CROSSED_BIT_CHUNK_SIZE = 2;

	private static ChunksCrossingAgent instance;

	private final Configs configs = Configs.getInstance();
	private final short populationSize;
	private final short crossedPopulationSize;

	private ChunksCrossingAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		this.crossedPopulationSize = (short) (populationSize - EliteAgent.getInstance().elitePopulationSize
				- MutationAgent.getInstance().mutantPopulationSize);
	}

	public static ChunksCrossingAgent getInstance() {
		if (instance == null) {
			instance = new ChunksCrossingAgent();
		}
		return instance;
	}

	@Override
	public List<Chromosome> crossPopulation(final List<Chromosome> population) {
		final Random random = new Random();

		final List<Chromosome> crossedPopulation = new ArrayList<>(crossedPopulationSize);
		for (int i = 0; i < crossedPopulationSize; i++) {
			final int fatherIndex = random.nextInt(populationSize);
			final int motherIndex = random.nextInt(populationSize);

			final Chromosome father = population.get(fatherIndex);
			final Chromosome mother = population.get(motherIndex);

			final Chromosome firstBorn = cross(father, mother);
			final Chromosome youngest = cross(mother, father);

			crossedPopulation.add(firstBorn);
			crossedPopulation.add(youngest);
		}
		return crossedPopulation;
	}

	private Chromosome cross(final Chromosome father, final Chromosome mother) {
		final BitList fatherBits = father.getBits();
		final BitList motherBits = mother.getBits();
		final short chromosomeSize = (short) fatherBits.size();
		final BitList childBits = BitList.allocate(chromosomeSize);

		short chunkStart = 0;
		while (chunkStart < chromosomeSize) {
			{
				// copies father chunk

				final short chunkEnd = calculateChunkEnd(chunkStart, chromosomeSize);
				if (chunkStart >= chunkEnd) {
					break;
				}

				final BitList chunk = fatherBits.getBitsInterval(chunkStart, chunkEnd);
				childBits.set(chunk, chunkStart);

				chunkStart = chunkEnd;
			}
			{
				// copies mother chunk

				final short chunkEnd = calculateChunkEnd(chunkStart, chromosomeSize);
				if (chunkStart >= chunkEnd) {
					break;
				}

				final BitList chunk = motherBits.getBitsInterval(chunkStart, chunkEnd);
				childBits.set(chunk, chunkStart);

				chunkStart = chunkEnd;
			}
		}

		return mother.withBits(childBits);
	}

	private short calculateChunkEnd(final short chunkStart, final short chromosomeSize) {
		short chunkEnd = (short) (chunkStart + CROSSED_BIT_CHUNK_SIZE);
		return chunkEnd <= chromosomeSize ? chunkEnd : chromosomeSize;
	}

}

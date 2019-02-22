package br.eti.hmagalhaes.coberturawifi.solution;

import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.util.BitList;

class CrossingStrategyTwoByTwo implements CrossingStrategy {

	public static final String STRATEGY_NAME = "2/2";

	private static final byte CROSSED_BIT_CHUNK_SIZE = 2;

	private static CrossingStrategyTwoByTwo instance;

	public static CrossingStrategyTwoByTwo getInstance() {
		if (instance == null) {
			instance = new CrossingStrategyTwoByTwo();
		}
		return instance;
	}
	
	@Override
	public Chromosome cross(final Chromosome father, final Chromosome mother) {
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

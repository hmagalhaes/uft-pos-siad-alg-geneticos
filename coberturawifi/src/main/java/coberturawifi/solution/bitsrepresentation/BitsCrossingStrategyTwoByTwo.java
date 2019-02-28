package coberturawifi.solution.bitsrepresentation;

import coberturawifi.model.BitsChromosome;
import coberturawifi.util.BitList;

class BitsCrossingStrategyTwoByTwo implements BitsCrossingStrategy {

	public static final String STRATEGY_NAME = "2/2";

	private static final byte CROSSED_BIT_CHUNK_SIZE = 2;

	private static BitsCrossingStrategyTwoByTwo instance;

	public static BitsCrossingStrategyTwoByTwo getInstance() {
		if (instance == null) {
			instance = new BitsCrossingStrategyTwoByTwo();
		}
		return instance;
	}

	@Override
	public BitsChromosome cross(final BitsChromosome father, final BitsChromosome mother) {
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
				childBits.set(chunkStart, chunk);

				chunkStart = chunkEnd;
			}
			{
				// copies mother chunk

				final short chunkEnd = calculateChunkEnd(chunkStart, chromosomeSize);
				if (chunkStart >= chunkEnd) {
					break;
				}

				final BitList chunk = motherBits.getBitsInterval(chunkStart, chunkEnd);
				childBits.set(chunkStart, chunk);

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

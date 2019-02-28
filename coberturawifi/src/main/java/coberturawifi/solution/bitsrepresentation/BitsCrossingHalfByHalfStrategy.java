package coberturawifi.solution.bitsrepresentation;

import coberturawifi.model.BitsChromosome;
import coberturawifi.util.BitList;

class BitsCrossingHalfByHalfStrategy implements BitsCrossingStrategy {

	public static final String STRATEGY_NAME = "half/half";

	private static BitsCrossingHalfByHalfStrategy instance;

	public static BitsCrossingHalfByHalfStrategy getInstance() {
		if (instance == null) {
			instance = new BitsCrossingHalfByHalfStrategy();
		}
		return instance;
	}

	@Override
	public BitsChromosome cross(final BitsChromosome father, final BitsChromosome mother) {
		final BitList fatherBits = father.getBits();
		final BitList motherBits = mother.getBits();

		final short chromosomeSize = (short) father.getBits().size();
		final short chromosomeHalfSize = (short) (chromosomeSize / 2);

		final int motherHalfIndex = chromosomeSize - chromosomeHalfSize;

		final BitList fatherHalf = fatherBits.getBitsInterval(0, chromosomeHalfSize);
		final BitList motherHalf = motherBits.getBitsInterval(motherHalfIndex, chromosomeSize);

		final BitList childBits = BitList.allocate(chromosomeSize);
		childBits.set(0, fatherHalf);
		childBits.set(motherHalfIndex, motherHalf);

		return mother.withBits(childBits);
	}

}

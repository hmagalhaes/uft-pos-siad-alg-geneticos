package br.eti.hmagalhaes.coberturawifi.solution;

import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.util.BitList;

public class CrossingHalfByHalfStrategy implements CrossingStrategy {

	public static final String STRATEGY_NAME = "half/half";

	private static CrossingHalfByHalfStrategy instance;

	public static CrossingHalfByHalfStrategy getInstance() {
		if (instance == null) {
			instance = new CrossingHalfByHalfStrategy();
		}
		return instance;
	}

	@Override
	public Chromosome cross(final Chromosome father, final Chromosome mother) {
		final BitList fatherBits = father.getBits();
		final BitList motherBits = mother.getBits();

		final short chromosomeSize = (short) father.getBits().size();
		final short chromosomeHalfSize = (short) (chromosomeSize / 2);

		final int motherHalfIndex = chromosomeSize - chromosomeHalfSize;

		final BitList fatherHalf = fatherBits.getBitsInterval(0, chromosomeHalfSize);
		final BitList motherHalf = motherBits.getBitsInterval(motherHalfIndex, chromosomeSize);

		final BitList childBits = BitList.allocate(chromosomeSize);
		childBits.set(fatherHalf, 0);
		childBits.set(motherHalf, motherHalfIndex);

		return mother.withBits(childBits);
	}

}

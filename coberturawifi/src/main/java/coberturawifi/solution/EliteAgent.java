package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import coberturawifi.Configs;
import coberturawifi.model.Chromosome;
import coberturawifi.model.GeneticSolution;

public class EliteAgent {

	private static EliteAgent instance;

	private final Configs configs = Configs.getInstance();

	public final short elitePopulationSize;

	private EliteAgent() {
		final short populationSize = configs.getShort(Configs.POPULATION_SIZE);
		{
			final float eliteRatio = configs.getFloat(Configs.POPULATION_RATIO_ELITE);
			final short eliteCount = (short) Math.round(populationSize * eliteRatio);
			this.elitePopulationSize = (short) Math.max(1, eliteCount);
		}
		System.out.println("EliteAgent => elitePopulationSize: " + elitePopulationSize);
	}

	public static EliteAgent getInstance() {
		if (instance == null) {
			instance = new EliteAgent();
		}
		return instance;
	}

	public <T extends Chromosome> List<T> findPopulationElite(final List<GeneticSolution<T>> solutionList) {

		final List<GeneticSolution<T>> sortedSolutions = solutionList.stream()
				.sorted(GeneticSolution.getBestFitnessComparator()).collect(Collectors.toList());

//		System.out.println("Generation elite:");
		final List<T> eliteList = new ArrayList<>(elitePopulationSize);
		for (int i = 0; i < elitePopulationSize; i++) {
			final GeneticSolution<T> solution = sortedSolutions.get(i);
			final T elite = solution.chromosome;

			eliteList.add(elite);
//			System.out.println("--- " + solution);
		}
		return eliteList;
	}

}

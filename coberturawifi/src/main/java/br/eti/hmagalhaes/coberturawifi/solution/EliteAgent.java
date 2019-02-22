package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.model.GeneticSolution;

class EliteAgent {

	private static EliteAgent instance;

	private final Configs configs = Configs.getInstance();

	final short elitePopulationSize;

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

	public List<Chromosome> findPopulationElite(final List<GeneticSolution> solutionList) {
		final List<GeneticSolution> sortedSolutions = solutionList.stream()
				.sorted(GeneticSolution.getBestFitnessComparator()).collect(Collectors.toList());

		System.out.println("Generation elite:");
		final List<Chromosome> eliteList = new ArrayList<>(elitePopulationSize);
		for (int i = 0; i < elitePopulationSize; i++) {
			GeneticSolution solution = sortedSolutions.get(i);
			Chromosome elite = solution.chromosome;

			eliteList.add(elite);
			System.out.println("--- " + solution);
		}
		return eliteList;
	}

}

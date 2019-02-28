package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import coberturawifi.model.Chromosome;
import coberturawifi.model.GeneticSolution;

public class BestSolutionsHolder {

	private final List<GeneticSolution<? extends Chromosome>> bestList;
	private final short solutionsLimit;

	public BestSolutionsHolder(short solutionsLimit) {
		this.solutionsLimit = solutionsLimit;
		this.bestList = new ArrayList<>(solutionsLimit);
	}

	public void checkForBetter(final List<GeneticSolution<? extends Chromosome>> candidateList) {

		final List<GeneticSolution<? extends Chromosome>> allSolutions = new ArrayList<>(
				bestList.size() + candidateList.size());
		allSolutions.addAll(bestList);
		allSolutions.addAll(candidateList);

		final List<GeneticSolution<? extends Chromosome>> sortedSolutions = allSolutions.stream()
				.sorted(GeneticSolution.getBestFitnessComparator()).collect(Collectors.toList());

		bestList.clear();

//		System.out.println("Melhores soluções globais:");
		for (short i = 0; bestList.size() < solutionsLimit && i < sortedSolutions.size(); i++) {
			final GeneticSolution<? extends Chromosome> candidate = sortedSolutions.get(i);
			if (isNewSolution(candidate)) {
				bestList.add(candidate);
//				System.out.println("--- " + candidate.fitness);
			}
		}
	}

	public List<GeneticSolution<? extends Chromosome>> getBestSolutions() {
		return new ArrayList<>(bestList);
	}

	private boolean isNewSolution(final GeneticSolution<? extends Chromosome> candidate) {
		for (GeneticSolution<? extends Chromosome> best : bestList) {
			if (best.equals(candidate)) {
				return false;
			}
		}
		return true;
	}

	public double currentBestFitness() {
		if (bestList.isEmpty()) {
			return 0;
		}
		return bestList.stream().map(solution -> solution.fitness)
				.collect(Collectors.maxBy((fitness1, fitness2) -> Double.compare(fitness1, fitness2))).get();
	}

}

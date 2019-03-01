package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import coberturawifi.Configs;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Solution;
import coberturawifi.util.Randomizer;

class SelectionAgent {

	private static SelectionAgent instance;

	public final short selectedPopulationSize;

	private final short populationSize;
	private final Configs configs = Configs.getInstance();
	private final Randomizer randomizer = Randomizer.getInstance();

	private SelectionAgent() {
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);
		this.selectedPopulationSize = (short) (populationSize - EliteAgent.getInstance().elitePopulationSize
				- MutationAgent.getInstance().getMutantPopulationSize()
				- CrossingAgent.getInstance().getCrossedPopulationSize());
		System.out.println("SelectionAgent => selectedPopSize: " + this.selectedPopulationSize);
	}

	public static SelectionAgent getInstance() {
		if (instance == null) {
			instance = new SelectionAgent();
		}
		return instance;
	}

	public List<Chromosome> select(final List<Solution> solutionList) {

		final List<Chromosome> selectedPopulation = new ArrayList<>(selectedPopulationSize);
		for (short i = 0; i < selectedPopulationSize; i++) {
			final Chromosome chromosome = rollTheRoulette(solutionList);
			selectedPopulation.add(chromosome);
		}
		return selectedPopulation;
	}

	private Chromosome rollTheRoulette(final List<Solution> solutionList) {
		final double totalFitness = solutionList.stream()
				.collect(Collectors.summingDouble(solution -> solution.fitness));
		final double rolled = randomizer.nextDouble(totalFitness);

		double accumulated = 0;
		for (Solution solution : solutionList) {
			accumulated += solution.fitness;
			if (accumulated >= rolled) {
				return solution.chromosome;
			}
		}

		final int lastIndex = solutionList.size() - 1;
		return solutionList.get(lastIndex).chromosome;
	}

}
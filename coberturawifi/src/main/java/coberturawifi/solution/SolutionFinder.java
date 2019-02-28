package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.GeneticSolution;
import coberturawifi.model.Layout;

public class SolutionFinder {

	private static final String BITS_TYPE = "bits";
	private static final String INTS_REPRESENTATION = "ints";

	private static SolutionFinder instance;

	private final short generationCount;
	private final short populationSize;
	private final short resultSolutionCount;

	private final Configs configs = Configs.getInstance();
	private final MutationAgent mutationAgent = MutationAgent.getInstance();
	private final EliteAgent eliteAgent = EliteAgent.getInstance();
	private final CrossingAgent crossingAgent = CrossingAgent.getInstance();
	private final InitialPopulationGenerator initialPopGenerator = InitialPopulationGenerator.getInstance();
	private final SelectionAgent selectionAgent = SelectionAgent.getInstance();
	private final FitnessAgent fitnessAgent = FitnessAgent.getInstance();

	private SolutionFinder() {
		this.generationCount = configs.getShort(Configs.GENERATION_COUNT);
		this.resultSolutionCount = configs.getShort(Configs.RESULT_SOLUTION_COUNT);
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);

		System.out.println("BitsSolutionFinder started => generationCount: " + generationCount + ", populationSize: "
				+ populationSize + ", resultSolutionCount: " + resultSolutionCount);
	}

	public static SolutionFinder getInstance() {
		if (instance == null) {
			instance = new SolutionFinder();
		}
		return instance;
	}

	public List<Layout> findBestMatch(final Blueprint blueprint, final short accessPointCount,
			final int accessPointRadiusInPixels) {

		final BestSolutionsHolder bestSolutionsHolder = new BestSolutionsHolder(resultSolutionCount);

		List<? extends Chromosome> population = initialPopGenerator.generatePopulation(blueprint, accessPointCount);
		int generation = 0;
		while (true) {
			final List<GeneticSolution<? extends Chromosome>> solutionList = fitnessAgent.calculateFitness(blueprint,
					population, accessPointRadiusInPixels);
			System.out.printf("Buscando => geração: %d / %d, melhorLocal: %f, mediaLocal: %f, melhorGlobal: %f\n",
					generation + 1, this.generationCount, getBestFitness(solutionList), getAverageFitness(solutionList),
					bestSolutionsHolder.currentBestFitness());

			System.out.println("Melhores da geração:");
			solutionList.stream().sorted(GeneticSolution.getBestFitnessComparator()).limit(resultSolutionCount)
					.map(solution -> solution.fitness).forEach(System.out::println);

			bestSolutionsHolder.checkForBetter(solutionList);

			if (stopConditionReached(generation)) {
				break;
			}

			population = createNewGeneration(population, solutionList);
			generation++;
		}

		System.out.println("Global best:");
		final List<GeneticSolution<? extends Chromosome>> globalBestSolutionList = bestSolutionsHolder
				.getBestSolutions();
		final int totalRequiredTiles = blueprint.requiredTileList.size();

		final List<Layout> layoutList = new ArrayList<>(globalBestSolutionList.size());
		for (GeneticSolution<? extends Chromosome> solution : globalBestSolutionList) {
			System.out.println("--- coverability: " + solution.coverability(totalRequiredTiles) + ", requiredTiles: "
					+ totalRequiredTiles + ", solution: " + solution);

			final Layout layout = Layout.of(solution, accessPointRadiusInPixels);
			layoutList.add(layout);
		}
		return layoutList;
	}

	private float getBestFitness(List<GeneticSolution<? extends Chromosome>> solutionList) {
		return solutionList.stream().map(solution -> solution.fitness)
				.collect(Collectors.maxBy((fitness1, fitness2) -> Double.compare(fitness1, fitness2))).get()
				.floatValue();
	}

	private float getAverageFitness(List<GeneticSolution<? extends Chromosome>> solutionList) {
		return solutionList.stream().collect(Collectors.averagingDouble(solution -> solution.fitness)).floatValue();
	}

	private List<? extends Chromosome> createNewGeneration(final List<? extends Chromosome> population,
			final List<GeneticSolution<? extends Chromosome>> solutionList) {

		final List<? extends Chromosome> eliteList = eliteAgent.findPopulationElite(solutionList);
		final List<? extends Chromosome> crossedList = crossingAgent.crossPopulation(population);
		final List<? extends Chromosome> mutantList = mutationAgent.mutatePopulation(population);
		final List<? extends Chromosome> selectedList = selectionAgent.select(solutionList);

//		System.out.println("elites: " + eliteList.size() + ", crossed: " + crossedList.size() + ", mutant: "
//				+ mutantList.size() + ", selected: " + selectedList.size());

		final List<Chromosome> newPopulation = new ArrayList<>(populationSize);
		newPopulation.addAll(eliteList);
		newPopulation.addAll(crossedList);
		newPopulation.addAll(mutantList);
		newPopulation.addAll(selectedList);

		if (newPopulation.size() != populationSize) {
			throw new IllegalStateException(
					"Tamanho população inválido => pop: " + newPopulation.size() + ", expected: " + populationSize);
		}

		return newPopulation;
	}

	private boolean stopConditionReached(final int generation) {
		return generation >= (this.generationCount - 1);
	}

}

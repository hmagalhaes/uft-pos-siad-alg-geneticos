package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Solution;

public class SolutionFinder {

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

	public List<Solution> findBestMatch(final Blueprint blueprint) {

		final short accessPointRadiusInMeters = Configs.getInstance().getShort(Configs.ACCESS_POINT_RADIUS_IN_METERS);
		final int accessPointRadiusInPixels = accessPointRadiusInMeters * blueprint.pixelsForMeter;
		System.out.println(
				"RadioPontoAcesso => " + accessPointRadiusInMeters + "m => " + accessPointRadiusInPixels + "px");

		final BestSolutionsHolder bestSolutionsHolder = new BestSolutionsHolder(resultSolutionCount);
		final short accessPointCount = Configs.getInstance().getShort(Configs.ACCESS_POINT_QUANTITY);

		List<Chromosome> population = initialPopGenerator.generatePopulation(blueprint, accessPointCount);
		int generation = 0;
		while (true) {
			final List<Solution> solutionList = fitnessAgent.calculateFitness(blueprint, population,
					accessPointRadiusInPixels);
			System.out.printf("Buscando => geração: %d / %d, melhorLocal: %f, mediaLocal: %f, melhorGlobal: %f\n",
					generation + 1, this.generationCount, getBestFitness(solutionList), getAverageFitness(solutionList),
					bestSolutionsHolder.currentBestFitness());

			System.out.println("Melhores da geração:");
			solutionList.stream().sorted(Solution.getBestFitnessComparator()).limit(resultSolutionCount)
					.map(solution -> solution.fitness).forEach(System.out::println);

			bestSolutionsHolder.checkForBetter(solutionList);

			if (stopConditionReached(generation)) {
				break;
			}

			population = createNewGeneration(population, solutionList, blueprint);
			generation++;
		}

		System.out.println("Global best:");
		final List<Solution> globalBestSolutionList = bestSolutionsHolder.getBestSolutions();
		final int totalRequiredTiles = blueprint.requiredTileList.size();

		for (Solution solution : globalBestSolutionList) {
			System.out.println("--- coverability: " + solution.coverability(totalRequiredTiles) + ", requiredTiles: "
					+ totalRequiredTiles + ", solution: " + solution);
		}

		return globalBestSolutionList;
	}

	private float getBestFitness(List<Solution> solutionList) {
		return solutionList.stream().map(solution -> solution.fitness)
				.collect(Collectors.maxBy((fitness1, fitness2) -> Double.compare(fitness1, fitness2))).get()
				.floatValue();
	}

	private float getAverageFitness(List<Solution> solutionList) {
		return solutionList.stream().collect(Collectors.averagingDouble(solution -> solution.fitness)).floatValue();
	}

	private List<Chromosome> createNewGeneration(final List<Chromosome> population, final List<Solution> solutionList,
			final Blueprint blueprint) {

		final List<Chromosome> eliteList = eliteAgent.findPopulationElite(solutionList);
		final List<Chromosome> crossedList = crossingAgent.crossPopulation(population);
		final List<Chromosome> mutantList = mutationAgent.mutatePopulation(population, blueprint);
		final List<Chromosome> selectedList = selectionAgent.select(solutionList);

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

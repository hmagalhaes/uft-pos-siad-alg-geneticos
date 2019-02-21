package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.model.Coordinates;
import br.eti.hmagalhaes.coberturawifi.model.GeneticSolution;
import br.eti.hmagalhaes.coberturawifi.model.Layout;
import br.eti.hmagalhaes.coberturawifi.model.Tile;

public class SolutionFinder {

	private static SolutionFinder instance;

	private final short generationCount;
	private final short populationSize;
	private final short resultSolutionCount;

	private final Configs configs = Configs.getInstance();
	private final MutationAgent mutationAgent = MutationAgent.getInstance();
	private final EliteAgent eliteAgent = EliteAgent.getInstance();
	private final CrossingAgent crossingAgent;

	private SolutionFinder() {
		{
			final String strategy = configs.getString(Configs.STRATEGY_CROSSING);
			if (ChunksCrossingAgent.STRATEGY_NAME.equals(strategy)) {
				this.crossingAgent = ChunksCrossingAgent.getInstance();
			} else {
				throw new IllegalArgumentException("Bad crossing strategy => " + strategy);
			}
		}

		this.generationCount = configs.getShort(Configs.GENERATION_COUNT);
		this.resultSolutionCount = configs.getShort(Configs.RESULT_SOLUTION_COUNT);
		this.populationSize = configs.getShort(Configs.POPULATION_SIZE);

		System.out.println("Solution Finder started => generationCount: " + generationCount + ", populationSize: "
				+ populationSize + ", resultSolutionCount: " + resultSolutionCount);
	}

	public static SolutionFinder getInstance() {
		if (instance == null) {
			instance = new SolutionFinder();
		}
		return instance;
	}

	public List<Layout> findBestMatch(final Blueprint plant, final short accessPointCount,
			final int accessPointRadiusInPixels) {

		List<GeneticSolution> globalBestSolutionList = Collections.emptyList();
		List<Chromosome> population = generateRandomPopulation(plant, accessPointCount);
		int generation = 0;

		while (true) {
			System.out.printf("Buscando melhor solução => geração: %d / %d\n", generation, this.generationCount);

			final List<GeneticSolution> solutionList = calculateFitness(plant, population);

			globalBestSolutionList = getBestGlobalSolutions(globalBestSolutionList, solutionList);

			if (stopConditionReached(generation)) {
				break;
			}

			population = createNewGeneration(population, solutionList);
			generation++;
		}

		final List<Layout> layoutList = new ArrayList<>(globalBestSolutionList.size());
		for (GeneticSolution geneticSolution : globalBestSolutionList) {
			final Layout layout = Layout.of(geneticSolution, accessPointRadiusInPixels);
			layoutList.add(layout);
		}
		return layoutList;
	}

	private List<Chromosome> createNewGeneration(final List<Chromosome> population,
			final List<GeneticSolution> solutionList) {

		final List<Chromosome> eliteList = eliteAgent.findPopulationElite(solutionList);
		final List<Chromosome> crossedList = crossingAgent.crossPopulation(population);
		final List<Chromosome> mutantList = mutationAgent.mutatePopulation(population);

		final List<Chromosome> newPopulation = new ArrayList<>(populationSize);
		newPopulation.addAll(eliteList);
		newPopulation.addAll(crossedList);
		newPopulation.addAll(mutantList);
		return newPopulation;
	}

	private List<GeneticSolution> getBestGlobalSolutions(final List<GeneticSolution> list1,
			final List<GeneticSolution> list2) {

		final List<GeneticSolution> allSolutions = new ArrayList<>(list1.size() + list2.size());
		allSolutions.addAll(list1);
		allSolutions.addAll(list2);

		final List<GeneticSolution> sortedSolutions = allSolutions.stream()
				.sorted(GeneticSolution.getBestFitnessComparator()).collect(Collectors.toList());

		System.out.println("Melhores soluções globais");
		final List<GeneticSolution> bestSolutions = new ArrayList<>(resultSolutionCount);
		for (int i = 0; i < resultSolutionCount; i++) {
			GeneticSolution solution = sortedSolutions.get(i);
			bestSolutions.add(solution);
			System.out.println("--- " + solution.fitness);
		}
		return bestSolutions;
	}

	private List<GeneticSolution> calculateFitness(final Blueprint plant, final List<Chromosome> population) {
		final List<GeneticSolution> solutionList = new ArrayList<>(population.size());
		for (Chromosome chromosome : population) {
			final GeneticSolution solution = calculateFitness(plant, chromosome);
			solutionList.add(solution);
		}
		return solutionList;
	}

	private GeneticSolution calculateFitness(final Blueprint blueprint, final Chromosome chromosome) {
		int tilesHit = 0;
		for (Tile tile : blueprint.requiredTileList) {
			for (Coordinates coords : chromosome.getCoordinateList()) {
				if (tile.rect.collidesWith(coords)) {
					tilesHit++;
					break;
				}
			}
		}

		final double fitness = ((double) tilesHit) / ((double) blueprint.requiredTileList.size());
//		System.out.println("fitness: " + fitness + ", tileshit: " + tilesHit + ", requiredTiles: "
//				+ blueprint.requiredTileList.size());

		return new GeneticSolution(chromosome, fitness);
	}

	private boolean stopConditionReached(final int generation) {
		return generation >= (this.generationCount - 1);
	}

	private List<Chromosome> generateRandomPopulation(final Blueprint plant, final short accessPointCount) {

		final Random random = new Random();

		final List<Chromosome> population = new ArrayList<>(populationSize);
		for (short i = 0; i < populationSize; i++) {
			final Chromosome chromosome = generateChromosome(plant, accessPointCount, random);
			population.add(chromosome);
		}
		return population;
	}

	private Chromosome generateChromosome(final Blueprint plant, final short accessPointCount, final Random random) {
		final Chromosome chromosome = new Chromosome(accessPointCount);
		for (short apIndex = 0; apIndex < accessPointCount; apIndex++) {
			final int x = random.nextInt(plant.widthInPixels);
			final int y = random.nextInt(plant.heightInPixels);

			chromosome.setCoordinatesIn(x, y, apIndex);
		}
		return chromosome;
	}

}

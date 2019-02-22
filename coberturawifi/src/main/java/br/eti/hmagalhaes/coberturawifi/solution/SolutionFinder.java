package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.model.Coordinates;
import br.eti.hmagalhaes.coberturawifi.model.GeneticSolution;
import br.eti.hmagalhaes.coberturawifi.model.Layout;
import br.eti.hmagalhaes.coberturawifi.model.Rect;
import br.eti.hmagalhaes.coberturawifi.model.Tile;

public class SolutionFinder {

	private static SolutionFinder instance;

	private final short generationCount;
	private final short populationSize;
	private final short resultSolutionCount;

	private final Configs configs = Configs.getInstance();
	private final MutationAgent mutationAgent = MutationAgent.getInstance();
	private final EliteAgent eliteAgent = EliteAgent.getInstance();
	private final CrossingAgent crossingAgent = CrossingAgent.getInstance();
	private final InitialPopGenerator initialPopGenerator = InitialPopGenerator.getInstance();

	private SolutionFinder() {
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

		final BestSolutionsHolder bestSolutionsHolder = new BestSolutionsHolder(resultSolutionCount);
		List<Chromosome> population = initialPopGenerator.generatePopulation(plant, accessPointCount);
		int generation = 0;

		while (true) {
			System.out.printf("Buscando melhor solução => geração: %d / %d\n", generation + 1, this.generationCount);

			final List<GeneticSolution> solutionList = calculateFitness(plant, population, accessPointRadiusInPixels);

			System.out.println("Melhores da geração:");
			solutionList.stream().sorted(GeneticSolution.getBestFitnessComparator()).limit(resultSolutionCount)
					.forEach(System.out::println);
//			System.out.println("Solutions: \n" + solutionList);

			bestSolutionsHolder.checkForBetter(solutionList);

			if (stopConditionReached(generation)) {
				break;
			}

			population = createNewGeneration(population, solutionList);
			generation++;
		}

		final List<GeneticSolution> globalBestSolutionList = bestSolutionsHolder.getBestSolutions();

		System.out.println("Resultado >> bestfitness: " + bestFitness + ", bestTilesHit: " + bestTilesHit);

		System.out.println("Global best:");
		final List<Layout> layoutList = new ArrayList<>(globalBestSolutionList.size());
		for (GeneticSolution solution : globalBestSolutionList) {
			System.out.println("--- " + solution);

			final Layout layout = Layout.of(solution, accessPointRadiusInPixels);
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

	private List<GeneticSolution> calculateFitness(final Blueprint plant, final List<Chromosome> population,
			final int accessPointRadiusInPixels) {

		final List<GeneticSolution> solutionList = new ArrayList<>(population.size());
		for (Chromosome chromosome : population) {
			final GeneticSolution solution = calculateFitness(plant, chromosome, accessPointRadiusInPixels);
			solutionList.add(solution);
		}
		return solutionList;
	}

	private static double bestFitness = 0;
	private static int bestTilesHit = 0;

	private GeneticSolution calculateFitness(final Blueprint blueprint, final Chromosome chromosome,
			final int accessPointRadiusInPixels) {

		int tilesHit = 0;
		for (Tile tile : blueprint.requiredTileList) {
			for (Coordinates coords : chromosome.getCoordinateList()) {
				if (collides(tile.rect, coords, accessPointRadiusInPixels)) {
					tilesHit++;
					break;
				}
			}
		}
		
		// TODO necessário reduzir a pontuação quando há sobreposição de cobertura
		
		final double fitness = ((double) tilesHit) / ((double) blueprint.requiredTileList.size());
//		System.out.println("fitness: " + fitness + ", tileshit: " + tilesHit + ", requiredTiles: "
//				+ blueprint.requiredTileList.size());

		bestFitness = Math.max(bestFitness, fitness);
		bestTilesHit = Math.max(bestTilesHit, tilesHit);

		return new GeneticSolution(chromosome, fitness);
	}

	private boolean collides(Rect rect, Coordinates circleCenter, int circleRadius) {
//		if (circleCenter.isWithin(rect)) {
//			return true;
//		}

		// https://yal.cc/rectangle-circle-intersection-test/

		final int rectWidth = rect.width();
		final int rectHeight = rect.height();

		final int nearestX = Math.max(rect.x1, Math.min(circleCenter.x, rect.x1 + rectWidth));
		final int nearestY = Math.max(rect.y1, Math.min(circleCenter.y, rect.y1 + rectHeight));

		final int deltaX = circleCenter.x - nearestX;
		final int deltaY = circleCenter.y - nearestY;

		return (deltaX * deltaX + deltaY * deltaY) < (circleRadius * circleRadius);

//
//		final int xDistance = Math.abs(rect.xCenter() - circleCenter.x);
//		final int yDistance = Math.abs(rect.yCenter() - circleCenter.y);
//
//		final int rectHalfWidth = rect.width() / 2;
//		final int rectHalfHeight = rect.height() / 2;
//
//		// Centros estão além da mínima distância para colisão
//		{
//			final int maximumXDistance = rectHalfWidth + circleRadius;
//			if (xDistance >= maximumXDistance) {
//				return false;
//			}
//			final int maximumYDistance = rectHalfHeight + circleRadius;
//			if (yDistance >= maximumYDistance) {
//				return false;
//			}
//		}

		// Colisão com o raio de cobertura
		// https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection

	}

	private boolean stopConditionReached(final int generation) {
		return generation >= (this.generationCount - 1);
	}

}

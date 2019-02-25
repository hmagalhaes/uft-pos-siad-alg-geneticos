package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Chromosome;
import br.eti.hmagalhaes.coberturawifi.model.Coordinates;
import br.eti.hmagalhaes.coberturawifi.model.GeneticSolution;
import br.eti.hmagalhaes.coberturawifi.model.Rect;
import br.eti.hmagalhaes.coberturawifi.model.Tile;

class FitnessAgent {

	private static FitnessAgent instance;

	private FitnessAgent() {

	}

	static FitnessAgent getInstance() {
		if (instance == null) {
			instance = new FitnessAgent();
		}
		return instance;
	}

	List<GeneticSolution> calculateFitness(final Blueprint plant, final List<Chromosome> population,
			final int accessPointRadiusInPixels) {

		final List<GeneticSolution> solutionList = new ArrayList<>(population.size());
		for (Chromosome chromosome : population) {
			final GeneticSolution solution = calculateFitness(plant, chromosome, accessPointRadiusInPixels);
			solutionList.add(solution);
		}
		return solutionList;
	}

	private GeneticSolution calculateFitness(final Blueprint blueprint, final Chromosome chromosome,
			final int accessPointRadiusInPixels) {

		final List<Tile> coveredTileList = new ArrayList<>();

		int tilesHit = 0;
		for (Tile tile : blueprint.requiredTileList) {
			for (Coordinates coords : chromosome.getCoordinateList()) {
				if (collides(tile.rect, coords, accessPointRadiusInPixels)) {
					tilesHit++;
					coveredTileList.add(tile);
					break;
				}
			}
		}

		final double fitness = ((double) tilesHit) / ((double) blueprint.requiredTileList.size());
//		System.out.println("fitness: " + fitness + ", tileshit: " + tilesHit + ", requiredTiles: "
//				+ blueprint.requiredTileList.size());

		return new GeneticSolution(chromosome, fitness, coveredTileList);
	}

	private boolean collides(Rect rect, Coordinates circleCenter, int circleRadius) {

//		  Abordagem #1
//		  https://yal.cc/rectangle-circle-intersection-test/

		final int nearestX = Math.max(rect.x1, Math.min(circleCenter.x, rect.x2));
		final int nearestY = Math.max(rect.y1, Math.min(circleCenter.y, rect.y2));

		final int deltaX = circleCenter.x - nearestX;
		final int deltaY = circleCenter.y - nearestY;

		return ((deltaX * deltaX) + (deltaY * deltaY)) < (circleRadius * circleRadius);

//		// Abordagem #2
//		// https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
//		final int xDistance = Math.abs(circleCenter.x - rect.xCenter());
//		final int yDistance = Math.abs(circleCenter.y - rect.yCenter());
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
//
//		// Estão muito próximos de forma que a colisão é garantida
//		if ((xDistance <= rectHalfWidth) && (yDistance <= rectHalfHeight)) {
//			return true;
//		}
//
//		// Interseção com a quina do retângulo
//		{
//			final int xPow = ((int) Math.pow(xDistance - rectHalfWidth, 2));
//			final int yPow = ((int) Math.pow(yDistance - rectHalfHeight, 2));
//			final int cornerDistance = xPow + yPow;
//
//			return cornerDistance <= (Math.pow(circleRadius, 2));
//		}
	}

}

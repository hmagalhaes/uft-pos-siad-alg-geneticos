package coberturawifi.solution;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.Coordinates;
import coberturawifi.model.GeneticSolution;
import coberturawifi.model.Rect;

public class FitnessAgent {

	private static FitnessAgent instance;

	private FitnessAgent() {

	}

	public static FitnessAgent getInstance() {
		if (instance == null) {
			instance = new FitnessAgent();
		}
		return instance;
	}

	public List<GeneticSolution> calculateFitness(final Blueprint blueprint,
			final List<? extends Chromosome> population, final int accessPointRadiusInPixels) {

		final List<GeneticSolution> solutionList = new ArrayList<>(population.size());
		for (Chromosome chromosome : population) {
			final GeneticSolution solution = calculateFitness(blueprint, chromosome, accessPointRadiusInPixels);
			solutionList.add(solution);
		}
		return solutionList;
	}

	private GeneticSolution calculateFitness(final Blueprint blueprint, final Chromosome chromosome,
			final int accessPointRadiusInPixels) {

		final List<Rect> coveredTileList = new ArrayList<>();
		for (Rect tile : blueprint.requiredTileList) {
			if (isHit(tile, chromosome, accessPointRadiusInPixels)) {
				coveredTileList.add(tile);
			}
		}

		final int tilesHit = coveredTileList.size();
		final double fitness = ((double) tilesHit) / ((double) blueprint.requiredTileList.size());
//		System.out.println("fitness: " + fitness + ", tileshit: " + tilesHit + ", requiredTiles: "
//				+ blueprint.requiredTileList.size());

		return new GeneticSolution(chromosome, fitness, coveredTileList);
	}

	private <T extends Chromosome> boolean isHit(final Rect rect, final T chromosome,
			final int accessPointRadiusInPixels) {

		for (Coordinates coords : chromosome.getCoordinateList()) {
			if (collides(rect, coords, accessPointRadiusInPixels)) {
				return true;
			}
		}
		return false;
	}

	private boolean collides(Rect rect, Coordinates circleCenter, int circleRadius) {
//		// Abordagem #1
//		// https://yal.cc/rectangle-circle-intersection-test/
//		final int nearestX = Math.max(rect.x1, Math.min(circleCenter.x, rect.x2));
//		final int nearestY = Math.max(rect.y1, Math.min(circleCenter.y, rect.y2));
//
//		final int deltaX = circleCenter.x - nearestX;
//		final int deltaY = circleCenter.y - nearestY;
//
//		return ((deltaX * deltaX) + (deltaY * deltaY)) < (circleRadius * circleRadius);

		// Abordagem #2
		// http://jeffreythompson.org/collision-detection/circle-rect.php

		// which edge is closest?
		final int nearestX;
		if (circleCenter.x < rect.x1) {
			nearestX = rect.x1;
		} else if (circleCenter.x > rect.x2) {
			nearestX = rect.x2;
		} else {
			nearestX = circleCenter.x;
		}

		final int nearestY;
		if (circleCenter.y < rect.y1) {
			nearestY = rect.y1;
		} else if (circleCenter.y > rect.y2) {
			nearestY = rect.y2;
		} else {
			nearestY = circleCenter.y;
		}

		// get distance from closest edges
		final int distX = circleCenter.x - nearestX;
		final int distY = circleCenter.y - nearestY;
		final int distance = (int) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

		return distance <= circleRadius;
	}

}

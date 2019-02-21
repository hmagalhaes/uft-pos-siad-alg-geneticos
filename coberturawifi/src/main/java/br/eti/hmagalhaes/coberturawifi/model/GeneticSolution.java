package br.eti.hmagalhaes.coberturawifi.model;

import java.util.Comparator;

public class GeneticSolution implements Comparable<GeneticSolution> {

	public final Chromosome chromosome;
	public final double fitness;

	public GeneticSolution(Chromosome chromosome, double fitness) {
		this.chromosome = chromosome;
		this.fitness = fitness;
	}

	@Override
	public int compareTo(GeneticSolution other) {
		return Double.compare(fitness, other.fitness);
	}

	public GeneticSolution withChromosome(Chromosome newChromosome) {
		return new GeneticSolution(newChromosome, this.fitness);
	}

	public static Comparator<GeneticSolution> getBestFitnessComparator() {
		return (solution1, solution2) -> Double.compare(solution2.fitness, solution1.fitness);
	}

}

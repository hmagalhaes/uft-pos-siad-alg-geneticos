package coberturawifi.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solution implements Comparable<Solution> {

	public final Chromosome chromosome;
	public final double fitness;
	public final List<Rect> coveredTileList;

	public Solution(Chromosome chromosome, double fitness, List<Rect> coveredTileList) {
		this.chromosome = chromosome;
		this.fitness = fitness;
		this.coveredTileList = coveredTileList == null ? Collections.emptyList()
				: Collections.unmodifiableList(coveredTileList);
	}

	@Override
	public int compareTo(Solution other) {
		return Double.compare(fitness, other.fitness);
	}

	public Solution withChromosome(Chromosome newChromosome) {
		return new Solution(newChromosome, this.fitness, this.coveredTileList);
	}

	public float coverability(final int totalTilesCount) {
		return (float) coveredTileList.size() / (float) totalTilesCount;
	}

	@Override
	public String toString() {
		return "GeneticSolution [chromosome=" + chromosome + ", fitness=" + fitness + ", coveredTiles="
				+ coveredTileList.size() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fitness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solution other = (Solution) obj;
		if (Double.doubleToLongBits(fitness) != Double.doubleToLongBits(other.fitness))
			return false;
		if (chromosome == null) {
			if (other.chromosome != null)
				return false;
		} else if (!chromosome.equals(other.chromosome))
			return false;
		return true;
	}

	public static Comparator<Solution> getBestFitnessComparator() {
		return (solution1, solution2) -> Double.compare(solution2.fitness, solution1.fitness);
	}

}

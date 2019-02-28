package coberturawifi.model;

import java.util.List;

public class BitsGeneticSolution extends GeneticSolution<BitsChromosome> {

	public BitsGeneticSolution(BitsChromosome chromosome, double fitness, List<Tile> coveredTileList) {
		super(chromosome, fitness, coveredTileList);
	}

//	public BitsGeneticSolution withChromosome(BitsChromosome newChromosome) {
//		return new BitsGeneticSolution(newChromosome, this.fitness, this.coveredTileList);
//	}

}

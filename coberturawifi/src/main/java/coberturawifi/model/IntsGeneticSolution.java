package coberturawifi.model;

import java.util.List;

public class IntsGeneticSolution extends GeneticSolution<RealChromosome> {

	public IntsGeneticSolution(RealChromosome chromosome, double fitness, List<Tile> coveredTileList) {
		super(chromosome, fitness, coveredTileList);
	}

//	public BitsGeneticSolution withChromosome(BitsChromosome newChromosome) {
//		return new BitsGeneticSolution(newChromosome, this.fitness, this.coveredTileList);
//	}

}

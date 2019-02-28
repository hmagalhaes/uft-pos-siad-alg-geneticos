package coberturawifi.solution;

import java.util.List;

import coberturawifi.model.BitsChromosome;

public interface CrossingAgent {

	List<BitsChromosome> crossPopulation(List<BitsChromosome> population);

}

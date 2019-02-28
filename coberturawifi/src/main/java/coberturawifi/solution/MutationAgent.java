package coberturawifi.solution;

import java.util.List;

import coberturawifi.model.Chromosome;

public interface MutationAgent {

	List<? extends Chromosome> mutatePopulation(final List<? extends Chromosome> population);
}

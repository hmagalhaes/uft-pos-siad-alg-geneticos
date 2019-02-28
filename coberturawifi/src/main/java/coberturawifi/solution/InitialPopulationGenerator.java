package coberturawifi.solution;

import java.util.List;

import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;

public interface InitialPopulationGenerator {

	List<? extends Chromosome> generatePopulation(final Blueprint plant, final short accessPointCount);

}

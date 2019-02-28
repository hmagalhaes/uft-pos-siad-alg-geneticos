package coberturawifi.solution;

import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.RepresentationType;
import coberturawifi.solution.bitsrepresentation.BitsMutationAgent;
import coberturawifi.solution.realrepresentation.RealMutationAgent;

public abstract class MutationAgent {

	private static MutationAgent instance;

	public static MutationAgent getInstance() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	private static MutationAgent createInstance() {
		final RepresentationType type = RepresentationType.fromConfigs(Configs.getInstance());
		switch (type) {
		case BITS:
			return BitsMutationAgent.getInstance();
		case REAL:
			return RealMutationAgent.getInstance();
		default:
			throw new IllegalArgumentException("Bad type => " + type);
		}
	}

	public abstract List<? extends Chromosome> mutatePopulation(final List<? extends Chromosome> population,
			final Blueprint blueprint);

	public abstract short getMutantPopulationSize();
}

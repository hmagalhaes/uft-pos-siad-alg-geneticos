package coberturawifi.solution;

import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Chromosome;
import coberturawifi.model.RepresentationType;
import coberturawifi.solution.bitsrepresentation.BitsCrossingAgent;
import coberturawifi.solution.realrepresentation.RealCrossingAgent;

public abstract class CrossingAgent {

	private static CrossingAgent instance;

	public static CrossingAgent getInstance() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	private static CrossingAgent createInstance() {
		final RepresentationType type = RepresentationType.fromConfigs(Configs.getInstance());
		switch (type) {
		case BITS:
			return BitsCrossingAgent.getInstance();
		case REAL:
			return RealCrossingAgent.getInstance();
		default:
			throw new IllegalArgumentException("Bad type => " + type);
		}
	}

	public abstract List<? extends Chromosome> crossPopulation(List<? extends Chromosome> population);

	public abstract short getCrossedPopulationSize();

}

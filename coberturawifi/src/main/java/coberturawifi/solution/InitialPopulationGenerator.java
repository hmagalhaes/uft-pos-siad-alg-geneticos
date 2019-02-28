package coberturawifi.solution;

import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Chromosome;
import coberturawifi.model.RepresentationType;
import coberturawifi.solution.bitsrepresentation.BitsInitialPopGenerator;
import coberturawifi.solution.realrepresentation.RealInitialPopGenerator;

public abstract class InitialPopulationGenerator {

	private static InitialPopulationGenerator instance;

	public static InitialPopulationGenerator getInstance() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	private static InitialPopulationGenerator createInstance() {
		final RepresentationType type = RepresentationType.fromConfigs(Configs.getInstance());
		switch (type) {
		case BITS:
			return BitsInitialPopGenerator.getInstance();
		case REAL:
			return RealInitialPopGenerator.getInstance();
		default:
			throw new IllegalArgumentException("Bad type => " + type);
		}
	}

	public abstract List<Chromosome> generatePopulation(final Blueprint plant, final short accessPointCount);

}

package coberturawifi;

import java.io.IOException;
import java.util.List;

import coberturawifi.input.BlueprintReader;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Solution;
import coberturawifi.output.SolutionWriter;
import coberturawifi.solution.SolutionFinder;

public class CoberturaApplication {

	public static void main(final String... args) throws IOException {
		new CoberturaApplication().run(args);
	}

	public void run(final String... args) throws IOException {
		if (args.length > 0) {
			System.setProperty(Configs.CONFIG_FILE_ENV_ENTRY, args[0]);
		}

		final long blueprintStart = System.currentTimeMillis();
		final Blueprint blueprint = BlueprintReader.getInstance().readPlant();
		System.out.printf("Planta lida em %dms\n", System.currentTimeMillis() - blueprintStart);

		final long searchStart = System.currentTimeMillis();
		final List<Solution> solutionList = SolutionFinder.getInstance().findBestMatch(blueprint);
		System.out.printf("Soluções encontradas em %dms\n", System.currentTimeMillis() - searchStart);

		SolutionWriter.getInstance().printSolutions(blueprint, solutionList);
	}

}

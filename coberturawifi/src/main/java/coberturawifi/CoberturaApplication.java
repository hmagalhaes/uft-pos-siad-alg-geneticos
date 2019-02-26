package coberturawifi;

import java.io.IOException;
import java.util.List;

import coberturawifi.input.BlueprintReader;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Layout;
import coberturawifi.output.SolutionWriter;
import coberturawifi.solution.SolutionFinder;

public class CoberturaApplication {

	public static void main(final String... args) throws IOException {
		new CoberturaApplication().run(args);
	}

	public void run(final String... args) throws IOException {
		if (args.length < 5) {
			System.out.println("O programa deve receber os seguintes parâmetros de execução:"
					+ "\n\t<caminho bitmap planta> <largura planta em metros> <altura planta em metros> <qtd. pontos acesso> <raio ponto acesso em metros>"
					+ "Ex:\n\tjava CoberturaApplication \"planta.bmp\" 30 100");
			return;
		}

		final String blueprintFile = args[0];
		final String outputFolder = args[1];
		final int plantWidthInMeters = Integer.parseInt(args[2]);
		final int plantHeightInMeters = Integer.parseInt(args[3]);
		final short accessPointCount = Short.parseShort(args[4]);
		final short accessPointRadiusInMeters = Short.parseShort(args[5]);

		System.out.println("Parâmetros:\nplanta:" + blueprintFile + ", diretorioSaida: " + outputFolder + ", largura: "
				+ plantWidthInMeters + "m, altura: " + plantHeightInMeters + "m, pontosAcesso: " + accessPointCount
				+ ", raioPontoAcesso: " + accessPointRadiusInMeters + "m");

		final long blueprintStart = System.currentTimeMillis();
		final Blueprint blueprint = BlueprintReader.getInstance().readPlant(blueprintFile, plantWidthInMeters,
				plantHeightInMeters);
		System.out.printf("Planta lida em %dms\n", System.currentTimeMillis() - blueprintStart);

		final int accessPointRadiusInPixels = accessPointRadiusInMeters * blueprint.pixelsForMeter;
		System.out.println("RadioPontoAcesso: " + accessPointRadiusInPixels + "px");

		final long searchStart = System.currentTimeMillis();
		final List<Layout> layoutList = SolutionFinder.getInstance().findBestMatch(blueprint, accessPointCount,
				accessPointRadiusInPixels);
		System.out.printf("Soluções encontradas em %dms\n", System.currentTimeMillis() - searchStart);

		SolutionWriter.getInstance().printSolutions(blueprintFile, layoutList, outputFolder);

		SolutionWriter.getInstance().printGridAllTiles(blueprintFile, blueprint, outputFolder);
		SolutionWriter.getInstance().printGridSelectedTiles(blueprintFile, blueprint, outputFolder);

	}

}

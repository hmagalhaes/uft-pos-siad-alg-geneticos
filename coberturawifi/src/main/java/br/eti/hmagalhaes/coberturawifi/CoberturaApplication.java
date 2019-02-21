package br.eti.hmagalhaes.coberturawifi;

import java.io.IOException;
import java.util.List;

import br.eti.hmagalhaes.coberturawifi.input.BlueprintReader;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Layout;
import br.eti.hmagalhaes.coberturawifi.output.SolutionWriter;
import br.eti.hmagalhaes.coberturawifi.solution.SolutionFinder;

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
		final int plantWidthInMeters = Integer.parseInt(args[1]);
		final int plantHeightInMeters = Integer.parseInt(args[2]);
		final short accessPointCount = Short.parseShort(args[3]);
		final short accessPointRadiusInMeters = Short.parseShort(args[4]);

		System.out.println("Parâmetros:\nplanta:" + blueprintFile + ", largura: " + plantWidthInMeters + "m, altura: "
				+ plantHeightInMeters + "m, pontosAcesso: " + accessPointCount + ", raioPontoAcesso: "
				+ accessPointRadiusInMeters + "m");

		final long plantStart = System.currentTimeMillis();
		final Blueprint blueprint = BlueprintReader.getInstance().readPlant(blueprintFile, plantWidthInMeters,
				plantHeightInMeters);
		System.out.printf("Planta lida em %dms\n", System.currentTimeMillis() - plantStart);

		final int accessPointRadiusInPixels = accessPointRadiusInMeters * blueprint.pixelsForMeter;
		final long searchStart = System.currentTimeMillis();
		final List<Layout> layoutList = SolutionFinder.getInstance().findBestMatch(blueprint, accessPointCount,
				accessPointRadiusInPixels);
		System.out.printf("Soluções encontradas em %dms\n", System.currentTimeMillis() - searchStart);

		final long outputStart = System.currentTimeMillis();
		SolutionWriter.getInstance().printSolutions(blueprintFile, layoutList);
		System.out.printf("Soluções salvas em %dms\n", System.currentTimeMillis() - outputStart);

		final long gridStart = System.currentTimeMillis();
		SolutionWriter.getInstance().printGridAllTiles(blueprintFile, blueprint);
		SolutionWriter.getInstance().printGridSelectedTiles(blueprintFile, blueprint);
		System.out.printf("Grids salvos em %dms\n", System.currentTimeMillis() - gridStart);
	}

}

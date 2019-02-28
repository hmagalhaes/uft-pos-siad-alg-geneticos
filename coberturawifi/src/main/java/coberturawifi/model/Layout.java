package coberturawifi.model;

import java.util.ArrayList;
import java.util.List;

public class Layout {

	public final List<AccessPoint> accessPointList;
	public final List<Tile> coveredTileList;

	public Layout(List<AccessPoint> accessPointList, List<Tile> coveredTileList) {
		this.accessPointList = accessPointList;
		this.coveredTileList = coveredTileList;
	}

	public static Layout of(final GeneticSolution geneticSolution, final int rangeRadiusInPixels) {
		final List<Coordinates> coordsList = geneticSolution.chromosome.getCoordinateList();

		final List<AccessPoint> apList = new ArrayList<>(coordsList.size());
		for (Coordinates coords : coordsList) {
			AccessPoint ap = new AccessPoint(coords.x, coords.y, rangeRadiusInPixels);
			apList.add(ap);
		}

		// TODO remover lista de tiles criada para debug
		return new Layout(apList, geneticSolution.coveredTileList);
	}

	@Override
	public String toString() {
		return accessPointList.toString();
	}

}

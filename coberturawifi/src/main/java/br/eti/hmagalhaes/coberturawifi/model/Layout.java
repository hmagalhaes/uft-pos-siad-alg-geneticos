package br.eti.hmagalhaes.coberturawifi.model;

import java.util.ArrayList;
import java.util.List;

public class Layout {

	public final List<AccessPoint> accessPointList;

	public Layout(List<AccessPoint> accessPointList) {
		this.accessPointList = accessPointList;
	}

	public static Layout of(final GeneticSolution geneticSolution, final int rangeRadiusInPixels) {
		final List<Coordinates> coordsList = geneticSolution.chromosome.getCoordinateList();

		final List<AccessPoint> apList = new ArrayList<>(coordsList.size());
		for (Coordinates coords : coordsList) {
			AccessPoint ap = new AccessPoint(coords.x, coords.y, rangeRadiusInPixels);
			apList.add(ap);
		}

		return new Layout(apList);
	}
	
	@Override
	public String toString() {
		return accessPointList.toString();
	}

}

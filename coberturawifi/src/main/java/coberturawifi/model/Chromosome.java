package coberturawifi.model;

import java.util.List;

public abstract class Chromosome {

	public static final byte CHROMOSOME_ATTRIBUTE_COUNT = 2;

	public final short accessPointCount;

	public Chromosome(short accessPointCount) {
		this.accessPointCount = accessPointCount;
	}

	public abstract void setCoordinatesFor(final Coordinates coordinates, short accessPointIndex);

	public abstract List<Coordinates> getCoordinateList();

	public abstract Coordinates getCoordinatesFor(short accessPointIndex);

}

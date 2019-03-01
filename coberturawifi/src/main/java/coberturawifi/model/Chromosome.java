package coberturawifi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chromosome {

	public static final byte CHROMOSOME_ATTRIBUTE_COUNT = 2;

	public final short accessPointCount;

	private final List<Coordinates> coordinatesList;

	public Chromosome(short accessPointCount, List<Coordinates> coordinatesList) {
		this.accessPointCount = accessPointCount;
		this.coordinatesList = fixCoordsList(coordinatesList);
	}

	public Chromosome(short accessPointCount) {
		this(accessPointCount, Collections.emptyList());
	}

	private List<Coordinates> fixCoordsList(final List<Coordinates> coordinatesList) {
		final List<Coordinates> fixedList = new ArrayList<>(coordinatesList);
		while (fixedList.size() < getMaxItemCount()) {
			fixedList.add(null);
		}
		return fixedList;
	}

	public static short getMaxItemCount(short accessPointCount) {
//		return (short) (accessPointCount * CHROMOSOME_ATTRIBUTE_COUNT);
		return accessPointCount;
	}

	public short getMaxItemCount() {
		return Chromosome.getMaxItemCount(this.accessPointCount);
	}

	public void setCoordinatesFor(final Coordinates coordinates, short accessPointIndex) {
		coordinatesList.set(accessPointIndex, coordinates);
	}

	public List<Coordinates> getCoordinateList() {
		final List<Coordinates> coordsList = new ArrayList<>(accessPointCount);
		for (short i = 0; i < accessPointCount; i++) {
			final Coordinates coords = getCoordinatesFor(i);
			coordsList.add(coords);
		}
		return coordsList;
	}

	public Coordinates getCoordinatesFor(short accessPointIndex) {
		return coordinatesList.get(accessPointIndex);
	}

	public List<Coordinates> getCoordinates() {
		return Collections.unmodifiableList(coordinatesList);
	}

	public Chromosome withCoordinates(final List<Coordinates> newCoordinates) {
		return new Chromosome(accessPointCount, newCoordinates);
	}

	@Override
	public String toString() {
		return "Chromosome [ " + getCoordinateList().toString() + " ]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessPointCount;
		result = prime * result + ((coordinatesList == null) ? 0 : coordinatesList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		if (accessPointCount != other.accessPointCount)
			return false;
		if (coordinatesList == null) {
			if (other.coordinatesList != null)
				return false;
		} else if (!coordinatesList.equals(other.coordinatesList))
			return false;
		return true;
	}

}

package coberturawifi.solution.realrepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coberturawifi.model.Chromosome;
import coberturawifi.model.Coordinates;

public class RealChromosome extends Chromosome {

	private final List<Coordinates> coordinatesList;

	public RealChromosome(short accessPointCount, List<Coordinates> coordinatesList) {
		super(accessPointCount);
		this.coordinatesList = fixCoordsList(coordinatesList);
	}

	public RealChromosome(short accessPointCount) {
		super(accessPointCount);
		this.coordinatesList = fixCoordsList(Collections.emptyList());
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
		return RealChromosome.getMaxItemCount(this.accessPointCount);
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

	public RealChromosome withCoordinates(final List<Coordinates> newCoordinates) {
		return new RealChromosome(accessPointCount, newCoordinates);
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
		RealChromosome other = (RealChromosome) obj;
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

package coberturawifi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntsChromosome extends Chromosome {

	private final List<Integer> intList;

	public IntsChromosome(short accessPointCount, List<Integer> intList) {
		super(accessPointCount);
		this.intList = new ArrayList<>(intList);
	}

	public IntsChromosome(short accessPointCount) {
		super(accessPointCount);
		this.intList = new ArrayList<>(getMaxIntCount());
	}

	public static short getMaxIntCount(short accessPointCount) {
		return (short) (accessPointCount * CHROMOSOME_ATTRIBUTE_COUNT);
	}

	public short getMaxIntCount() {
		return IntsChromosome.getMaxIntCount(this.accessPointCount);
	}

	public void setCoordinatesFor(final Coordinates coordinates, short accessPointIndex) {
		final int xIndex = accessPointIndex * CHROMOSOME_ATTRIBUTE_COUNT;
		final int yIndex = xIndex + 1;

		intList.set(xIndex, coordinates.x);
		intList.set(yIndex, coordinates.y);
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
		final int xIndex = accessPointIndex * CHROMOSOME_ATTRIBUTE_COUNT;
		final int yIndex = xIndex + 1;

		final int x = intList.get(xIndex);
		final int y = intList.get(yIndex);

		return new Coordinates(x, y);
	}

	public List<Integer> getInts() {
		return Collections.unmodifiableList(intList);
	}

	public IntsChromosome withBits(final List<Integer> newIntList) {
		return new IntsChromosome(accessPointCount, newIntList);
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
		result = prime * result + ((intList == null) ? 0 : intList.hashCode());
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
		IntsChromosome other = (IntsChromosome) obj;
		if (accessPointCount != other.accessPointCount)
			return false;
		if (intList == null) {
			if (other.intList != null)
				return false;
		} else if (!intList.equals(other.intList))
			return false;
		return true;
	}

}

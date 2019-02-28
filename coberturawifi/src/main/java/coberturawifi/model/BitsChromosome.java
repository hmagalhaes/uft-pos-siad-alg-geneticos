package coberturawifi.model;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.util.BitList;

public class BitsChromosome extends Chromosome {

	public static final byte CHROMOSOME_ATTRIBUTE_LENGTH = Integer.SIZE;

	private final BitList bitList;

	public BitsChromosome(short accessPointCount, BitList bitList) {
		super(accessPointCount);
		this.bitList = bitList.clone();
	}

	public BitsChromosome(short accessPointCount) {
		super(accessPointCount);
		this.bitList = BitList.allocate(getMaxBitsCount());
	}

	public static short getMaxBitsCount(short accessPointCount) {
		return (short) (accessPointCount * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_COUNT);
	}

	public short getMaxBitsCount() {
		return BitsChromosome.getMaxBitsCount(this.accessPointCount);
	}

	public void setCoordinatesFor(final Coordinates coordinates, short accessPointIndex) {
		final BitList xBitList = BitList.of(coordinates.x);
		final BitList yBitList = BitList.of(coordinates.y);

		final int xIndex = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_COUNT;
		final int yIndex = xIndex + CHROMOSOME_ATTRIBUTE_LENGTH;

		this.bitList.set(xIndex, xBitList);
		this.bitList.set(yIndex, yBitList);
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
		final int xStart = accessPointIndex * CHROMOSOME_ATTRIBUTE_COUNT * CHROMOSOME_ATTRIBUTE_LENGTH;
		final int xEnd = xStart + CHROMOSOME_ATTRIBUTE_LENGTH;
		final int yStart = xStart + CHROMOSOME_ATTRIBUTE_LENGTH;
		final int yEnd = yStart + CHROMOSOME_ATTRIBUTE_LENGTH;

		final int x = bitList.getBitsInterval(xStart, xEnd).toInt();
		final int y = bitList.getBitsInterval(yStart, yEnd).toInt();

		return new Coordinates(x, y);
	}

	public BitList getBits() {
		return bitList.clone();
	}

	public BitsChromosome withBits(final BitList newBits) {
		return new BitsChromosome(accessPointCount, newBits);
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
		result = prime * result + ((bitList == null) ? 0 : bitList.hashCode());
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
		BitsChromosome other = (BitsChromosome) obj;
		if (accessPointCount != other.accessPointCount)
			return false;
		if (bitList == null) {
			if (other.bitList != null)
				return false;
		} else if (!bitList.equals(other.bitList))
			return false;
		return true;
	}

}

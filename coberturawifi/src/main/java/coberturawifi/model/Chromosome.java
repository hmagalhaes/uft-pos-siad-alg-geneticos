package coberturawifi.model;

import java.util.ArrayList;
import java.util.List;

import coberturawifi.util.BitList;

public class Chromosome {

	public static final byte CHROMOSOME_ATTRIBUTE_COUNT = 2;
	public static final byte CHROMOSOME_ATTRIBUTE_LENGTH = Integer.SIZE;

	private final BitList bitList;

	public final short accessPointCount;

	public Chromosome(short accessPointCount, BitList bitList) {
		this.accessPointCount = accessPointCount;
		this.bitList = bitList.clone();
	}

	public Chromosome(short accessPointCount) {
		this.accessPointCount = accessPointCount;
		this.bitList = BitList.allocate(getBitsCount());
	}

	public static short getBitsCount(short accessPointCount) {
		return (short) (accessPointCount * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_COUNT);
	}

	public short getBitsCount() {
		return Chromosome.getBitsCount(this.accessPointCount);
	}

	public void setCoordinatesFor(final Coordinates coordinates, short accessPointIndex) {
		final BitList xBitList = BitList.of(coordinates.x);
		final BitList yBitList = BitList.of(coordinates.y);

		final int xIndex = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_COUNT;
		final int yIndex = xIndex + CHROMOSOME_ATTRIBUTE_LENGTH;
		System.out.println("xIndex: " + xIndex + ", yIndex: " + yIndex);

		this.bitList.set(xBitList, xIndex);
		this.bitList.set(yBitList, yIndex);
		

		if (this.bitList.size() > getBitsCount()) {
			throw new RuntimeException("Illegal bits counts => actual: " + bitList.size() + ", expected: "
					+ getBitsCount() + ", apIndex: " + accessPointIndex + ", xBitList: " + xBitList.size()
					+ ", yBitList: " + yBitList.size() + ", xIndex: " + xIndex + ", yIndex: " + yIndex);
		}

		final Coordinates savedCoordinates = getCoordinatesFor(accessPointIndex);
		if (!savedCoordinates.equals(coordinates)) {
			throw new RuntimeException("Unable to save coordinates properly => expected: " + coordinates + ", actual: "
					+ savedCoordinates);
		}
	}

	public List<Coordinates> getCoordinateList() {
		final List<Coordinates> coordsList = new ArrayList<>();
		for (short i = 0; i < accessPointCount; i++) {
			final Coordinates coords = getCoordinatesFor(i);
			coordsList.add(coords);
		}
		return coordsList;
	}

	public Coordinates getCoordinatesFor(short accessPointIndex) {
		final int x = getX(accessPointIndex);
		final int y = getY(accessPointIndex);

		return new Coordinates(x, y);
	}

	private int getX(short accessPointIndex) {
		final int start = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_LENGTH;
		final int end = start + CHROMOSOME_ATTRIBUTE_LENGTH;
		final BitList bitListPiece = bitList.getBitsInterval(start, end);

		return bitListPiece.toInt();
	}

	private int getY(short accessPointIndex) {
		final int start = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_LENGTH
				+ CHROMOSOME_ATTRIBUTE_LENGTH;
		final int end = start + CHROMOSOME_ATTRIBUTE_LENGTH;
		final BitList bitListPiece = bitList.getBitsInterval(start, end);

		return bitListPiece.toInt();
	}

	public BitList getBits() {
		return bitList.clone();
	}

	public Chromosome withBits(final BitList newBits) {
		return new Chromosome(accessPointCount, newBits);
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
		Chromosome other = (Chromosome) obj;
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

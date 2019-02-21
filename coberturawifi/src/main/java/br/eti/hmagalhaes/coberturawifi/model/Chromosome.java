package br.eti.hmagalhaes.coberturawifi.model;

import java.util.ArrayList;
import java.util.List;

import br.eti.hmagalhaes.coberturawifi.util.BitList;

public class Chromosome {

	public static final byte CHROMOSOME_ATTRIBUTE_COUNT = 2;
	public static final byte CHROMOSOME_ATTRIBUTE_LENGTH = Integer.SIZE;

	private final BitList bitList;

	public final short accessPointCount;

	public Chromosome(short accessPointCount, BitList bitList) {
		this.accessPointCount = accessPointCount;
		this.bitList = bitList;
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

	public void setCoordinatesIn(int x, int y, short accessPointIndex) {
		final BitList xBitList = BitList.of(x);
		final BitList yBitList = BitList.of(y);

		final int xIndex = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_LENGTH;
		final int yIndex = xIndex + CHROMOSOME_ATTRIBUTE_LENGTH;

		this.bitList.set(xBitList, xIndex);
		this.bitList.set(yBitList, yIndex);
	}

	public List<Coordinates> getCoordinateList() {
		final List<Coordinates> coordsList = new ArrayList<>();
		for (short i = 0; i < accessPointCount; i++) {
			final Coordinates coords = getCoordinate(i);
			coordsList.add(coords);
		}
		return coordsList;
	}

	public Coordinates getCoordinate(short accessPointIndex) {
		final int x = getX(accessPointIndex);
		final int y = getY(accessPointIndex);

		return new Coordinates(x, y);
	}

	public int getX(short accessPointIndex) {
		final int start = accessPointIndex * CHROMOSOME_ATTRIBUTE_LENGTH * CHROMOSOME_ATTRIBUTE_LENGTH;
		final int end = start + CHROMOSOME_ATTRIBUTE_LENGTH;
		final BitList bitListPiece = bitList.getBitsInterval(start, end);

		return bitListPiece.toInt();
	}

	public int getY(short accessPointIndex) {
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

}

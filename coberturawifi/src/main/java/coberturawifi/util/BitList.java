package coberturawifi.util;

import java.util.BitSet;

public class BitList implements Cloneable {

	private final BitSet bitSet;
	private int size = 0;

	private BitList(BitSet bitSet, int size) {
		this.bitSet = bitSet;
		this.size = size;
	}

	private BitList() {
		this.bitSet = new BitSet();
	}

	private BitList(int bitsToAllocate) {
		this.bitSet = new BitSet(bitsToAllocate);
	}

	public static BitList allocate(int bitsToAllocate) {
		return new BitList(bitsToAllocate);
	}

	public static BitList of(int value) {
		final BitList bitList = new BitList();

		int bitSetIndex = 0;
		while (value != 0) {
			if (value % 2 != 0) {
				// se for ímpar o primeiro bit é 1 e por isso deve ser ativo no bitset
				bitList.set(bitSetIndex, true);
			} else {
				// sendo par o primeiro bit é 0
				bitList.set(bitSetIndex, false);
			}

			++bitSetIndex;

			// Move os bits do valor pra direita, descartando o primeiro bit e colocando o
			// segundo em seu lugar
			value = value >>> 1;
		}

		bitList.size = Integer.SIZE;
		return bitList;
	}

	public int size() {
		return size;
	}

	public BitList set(final int index, final boolean value) {
		if (size < (index + 1)) {
			size = index + 1;
		}
		bitSet.set(index, value);
		return this;
	}

	public void set(final int index, final BitList sourceBitList) {
		int sourceIndex = 0;
		int targetIndex = index;

		while (sourceIndex < sourceBitList.size()) {
			final boolean sourceBit = sourceBitList.get(sourceIndex);
			this.bitSet.set(targetIndex, sourceBit);

			sourceIndex++;
			targetIndex++;
		}

		final int requiredSize = index + sourceBitList.size;
		this.size = Math.max(this.size, requiredSize);
	}

//	public BitList append(final BitList otherBitList) {
//		for (int otherIndex = 0; otherIndex < otherBitList.size(); otherIndex++) {
//			final boolean otherBit = otherBitList.get(otherIndex);
//			bitSet.set(size++, otherBit);
//		}
//		return this;
//	}

	public int toInt() {
		int value = 0;
		for (int i = 0; i < bitSet.length(); ++i) {
			// se o bit estiver ativo, gera um inteiro cujos bits são todos 0 exceto aquele
			// que equivale ao bit lido no bitset
			if (bitSet.get(i)) {
				value += 1 << i;
			}
		}
		return value;
	}

	public boolean get(final int index) {
		return bitSet.get(index);
	}

	/**
	 * @param start Inclusivo
	 * @param end   Não inclusivo
	 */
	public BitList getBitsInterval(int start, int end) {
		final BitSet bitSetInterval = bitSet.get(start, end);
		final int size = end - start;

		return new BitList(bitSetInterval, size);
	}

	@Override
	public BitList clone() {
		final BitSet clonedBits = bitSet.get(0, size);
		return new BitList(clonedBits, size);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitSet == null) ? 0 : bitSet.hashCode());
		result = prime * result + size;
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
		BitList other = (BitList) obj;
		if (size != other.size)
			return false;
		if (bitSet == null) {
			if (other.bitSet != null)
				return false;
		} else if (!bitSet.equals(other.bitSet))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("BitList [ size: ").append(size).append(", bits: ");
		for (int i = 0; i < size; i++) {
			if (bitSet.get(i)) {
				sb.append("1, ");
			} else {
				sb.append("0, ");
			}
		}
		return sb.append("]").toString();
	}

}

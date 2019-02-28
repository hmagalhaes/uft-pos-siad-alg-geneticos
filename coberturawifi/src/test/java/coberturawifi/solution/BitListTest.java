package coberturawifi.solution;

import org.junit.Test;
import static org.junit.Assert.*;

import coberturawifi.util.BitList;

public class BitListTest {

	@Test
	public void createFromIntTest() {
		// 23 (b 10) => 10111 (b 2)

		final BitList bitList = BitList.of(23);
		assertEquals(Integer.SIZE, bitList.size());

		assertTrue(bitList.get(0));
		assertTrue(bitList.get(1));
		assertTrue(bitList.get(2));
		assertFalse(bitList.get(3));
		assertTrue(bitList.get(4));

		assertEquals(23, bitList.toInt());
	}

	@Test
	public void createByAllocateTest() {
		final BitList bitList = BitList.allocate(Integer.SIZE * 3);
		assertEquals(0, bitList.size());
	}

	@Test
	public void setSingleValueTest() {
		final BitList bitList = BitList.allocate(Integer.SIZE);

		assertEquals(0, bitList.size());

		bitList.set(0, true);
		bitList.set(1, true);
		bitList.set(2, true);
		bitList.set(3, false);
		bitList.set(4, true);

		assertEquals(5, bitList.size());

		assertTrue(bitList.get(0));
		assertTrue(bitList.get(1));
		assertTrue(bitList.get(2));
		assertFalse(bitList.get(3));
		assertTrue(bitList.get(4));

		assertEquals(23, bitList.toInt());
	}

	@Test
	public void setManyValuesTest() {
		final BitList firstSourceBitList = BitList.of(23);
		final BitList secondSourceBitList = BitList.of(5678);

		final BitList targetBitList = BitList.allocate(Integer.SIZE * 2);
		assertEquals(0, targetBitList.size());

		targetBitList.set(0, firstSourceBitList);

		assertEquals(firstSourceBitList.size(), targetBitList.size());
		assertEquals(firstSourceBitList.toInt(), targetBitList.toInt());

		for (int i = 0; i < firstSourceBitList.size(); i++) {
			final boolean expected = firstSourceBitList.get(i);
			final boolean actual = targetBitList.get(i);

			assertEquals("Bit index: " + i, expected, actual);
		}

		targetBitList.set(1, firstSourceBitList);

		assertEquals(firstSourceBitList.size() + 1, targetBitList.size());

		for (int i = 0; i < firstSourceBitList.size(); i++) {
			final boolean expected = firstSourceBitList.get(i);
			final boolean actual = targetBitList.get(i + 1);

			assertEquals("Bit index: " + (i + 1), expected, actual);
		}

		targetBitList.set(0, firstSourceBitList);
		targetBitList.set(Integer.SIZE, secondSourceBitList);

		assertEquals(Integer.SIZE * 2, targetBitList.size());

		for (int i = 0; i < firstSourceBitList.size(); i++) {
			final boolean expected = firstSourceBitList.get(i);
			final boolean actual = targetBitList.get(i);

			assertEquals("Bit index: " + i, expected, actual);
		}

		for (int i = 0; i < secondSourceBitList.size(); i++) {
			final boolean expected = secondSourceBitList.get(i);
			final boolean actual = targetBitList.get(i + Integer.SIZE);

			assertEquals("Bit index: " + (i + Integer.SIZE), expected, actual);
		}
	}

}

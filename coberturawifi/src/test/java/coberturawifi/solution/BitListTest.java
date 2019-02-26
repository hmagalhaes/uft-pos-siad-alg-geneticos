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
		assertEquals(23, bitList.toInt());
		assertTrue(bitList.get(0));
		assertTrue(bitList.get(1));
		assertTrue(bitList.get(2));
		assertFalse(bitList.get(3));
		assertTrue(bitList.get(4));
	}

	@Test
	public void createByAllocateTest() {
		final BitList bitList = BitList.allocate(Integer.SIZE);
		assertEquals(0, bitList.size());
	}

	@Test
	public void setSingleValueTest() {
		final BitList bitList = BitList.allocate(Integer.SIZE);

		assertEquals(0, bitList.size());

		bitList.set(0, true);
		bitList.set(1, false);
		bitList.set(2, true);
		bitList.set(3, true);

		assertEquals(4, bitList.size());

		assertTrue(bitList.get(0));
		assertFalse(bitList.get(1));
		assertTrue(bitList.get(2));
		assertTrue(bitList.get(3));
	}

	@Test
	public void setManyValuesTest() {
		final BitList sourceBitList = BitList.of(23);
		assertEquals(Integer.SIZE, sourceBitList.size());
		
		final BitList targetBitList = BitList.allocate(Integer.SIZE * 2);
		
		assertEquals(0, targetBitList.size());

		targetBitList.set(sourceBitList, 0);
		
		assertEquals(Integer.SIZE, targetBitList.size());

		assertEquals(23, targetBitList.toInt());
		
		// 23 (b 10) => 10111 (b 2)
		assertTrue(targetBitList.get(0));
		assertTrue(targetBitList.get(1));
		assertTrue(targetBitList.get(2));
		assertFalse(targetBitList.get(3));
		assertTrue(targetBitList.get(4));
		
		targetBitList.set(sourceBitList, 1);
		
		assertEquals(Integer.SIZE + 1, targetBitList.size());
		
		// 47 (b10) => 101111 (b2)
		assertTrue(targetBitList.get(0));
		assertTrue(targetBitList.get(1));
		assertTrue(targetBitList.get(2));
		assertTrue(targetBitList.get(3));
		assertFalse(targetBitList.get(4));
		assertTrue(targetBitList.get(5));
	}

}

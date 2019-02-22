package br.eti.hmagalhaes.coberturawifi.util;

import java.util.Random;

public class Randomizer {

	private static Randomizer instance;

	private final Random random;

	{
		random = new Random();
	}

	public static Randomizer getInstance() {
		if (instance == null) {
			instance = new Randomizer();
		}
		return instance;
	}

	public int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public short nextShort(short bound) {
		return (short) random.nextInt(bound);
	}

}

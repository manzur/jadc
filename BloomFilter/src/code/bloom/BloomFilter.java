package code.bloom;

import java.util.BitSet;

// Given:
// p - false positive probability
// k - number of hash functions

// Changes:
// m - total number of bits
public class BloomFilter<E> {

	/* false positive probability */
	public static final double p = 0.1;

	/** "hash functions" count */
	public static final int k = 4;

	/* expected number of elements in the set */
	private int n;

	/* number of bits in the bitset(Bloom filter) */
	private int m;

	private BitSet bitset;

	/**
	 * returns the indexes those're result of applying hash functions i.e
	 * positions where to set the bit(to 1).
	 */
	int[] getBitIndexes(int hash) {

		int region1 = hash & 255;
		int region2 = (hash >> 8) & 255;
		int region3 = (hash >> 16) & 255;
		int region4 = (hash >> 24) & 255;

		int regionSize = m >> 2;

		int index1 = (region1 % regionSize) + 0 * regionSize;

		int value = region2 + (region1 / regionSize);

		int index2 = (value % regionSize) + 1 * regionSize;

		value = region3 + value / regionSize;

		int index3 = (value % regionSize) + 1 * regionSize;

		value = region4 + value / regionSize;

		int index4 = (value % regionSize) + 3 * regionSize;

		return new int[] { index4, index3, index2, index1 };

	}

	public BloomFilter(int n) {
		this.n = n;
		this.m = calcm();
		this.bitset = new BitSet(m);
	}

	public void add(E element) {
		int[] indexes = getBitIndexes(element.hashCode());

		for (int i = 0; i < indexes.length; i++) {
			bitset.set(indexes[i]);
		}
	}

	public boolean contains(E element) {
		int[] indexes = getBitIndexes(element.hashCode());

		boolean result = true;
		for (int i = 0; i < indexes.length; i++) {
			result = result && bitset.get(indexes[i]);
		}

		return result;
	}

	int calcm() {
		int result = (int) (-n * Math.log(p) * (Math.log(2) * Math.log(2)));

		while (result % 4 == 0) {
			result = result + 1;
		}

		return result;
	}

	// Integer[] getBitIndexes(int hash) {
	// if (hash == 0) {
	// return new Integer[] { 0, 1, 3, 4 };
	// }
	//
	// final Set<Integer> result = new HashSet<Integer>();
	//
	// int step = hash % 29 + 1;
	//
	// int index = 0;
	// int setCount = 0;
	//
	// while (result.size() != k) {
	//
	// Iterator<Integer> iterator = bitIterator(hash, Integer.SIZE - 1);
	//
	// while (result.size() != k && iterator.hasNext()) {
	// Integer next = iterator.next();
	//
	// setCount = setCount + next;
	//
	// if (setCount == step) {
	// result.add(index);
	// setCount = 0;
	// }
	//
	// index = (index + 1) % m;
	// }
	//
	// }
	//
	// return result.toArray(new Integer[0]);
	// }

}

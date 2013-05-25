package code.bloom;

import java.util.Iterator;

public class Utils {
	/**
	 * returns iterator that traverses the int as a bit array from left to
	 * right. i.e from most significant bit to less.
	 */
	public static Iterator<Integer> bitIterator(final int hash,
			final int startBit) {
		return new Iterator<Integer>() {
			int index = startBit;

			@Override
			public boolean hasNext() {
				return index >= 0;
			}

			@Override
			public Integer next() {
				Integer result = (hash >> index) & 1;
				index = index - 1;

				return result;
			}

			/* unsupported operations */
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}

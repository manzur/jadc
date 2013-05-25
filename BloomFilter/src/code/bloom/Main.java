package code.bloom;

import java.text.MessageFormat;

public class Main {
	public static void main(String[] args) {

		BloomFilter<Integer> filter = new BloomFilter<Integer>(100);

		addAndtest(filter, 11);

		test(filter, 13);

		addAndtest(filter, 13);

		test(filter, 0);
		addAndtest(filter, 0);

		addAndtest(filter, 8);

		addAndtest(filter, 89);

		addAndtest(filter, 2);

		int containsTrue = 0;
		for (int i = 0; i < 100; i++) {
			containsTrue += test(filter, Integer.valueOf(i)) ? 1 : 0;
		}

		System.out.println("false positive count -> " + (containsTrue - 6));

	}

	private static <V> void addAndtest(BloomFilter<V> filter, V value) {
		filter.add(value);
		test(filter, value);
	}

	private static <V> boolean test(BloomFilter<V> filter, V value) {
		boolean result = filter.contains(value);

		System.out
				.println(MessageFormat.format("get({0}) -> ", value) + result);

		return result;
	}
}

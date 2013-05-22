package code.skiplist;

public class Main {
	public static void main(String[] args) {
		System.out.println("main");

		test1();
	}

	private static void test1() {
		SkipList<Integer, String> map = new SkipList<Integer, String>();
		// HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		map.put(4, "d");
		map.put(5, "e");
		map.put(6, "f");
		map.put(7, "g");
		map.put(8, "h");
		map.put(9, "i");
		map.put(10, "j");
		map.put(11, "k");
		map.put(12, null);

		Searcher searcher = map.seacher();
		//
		// System.out.println("searching for " + 1 + " and result is "
		// + searcher.search(1));

		System.out.println("searching for " + 2 + " and result is "
				+ searcher.search(2));

		System.out.println("searching for " + 1 + " and result is "
				+ searcher.search(1));

		System.out.println("searching for " + 3 + " and result is "
				+ searcher.search(3));

		System.out.println("searching for " + 6 + " and result is "
				+ searcher.search(6));

		System.out.println("searching for " + 8 + " and result is "
				+ searcher.search(8));

		System.out.println("searching for " + 12 + " and result is "
				+ searcher.search(12));
	}

}

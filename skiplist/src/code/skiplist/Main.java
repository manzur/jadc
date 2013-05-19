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

		for (int i = 12; i > 0; i--) {
			map.remove(i);
		}
		System.out.println(map.toString());
		map.put(12, "a");
		System.out.println(map.toString());

		System.out.println("=" + map.containsValue("a"));
	}

}

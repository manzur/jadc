package code.skiplist;

public class Main {
	public static void main(String[] args) {
		System.out.println("main");

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

		System.out.println(map.toDebugString());

		System.out.println("values");
		for (String v : map.values()) {
			System.out.println(v);
		}

		System.out.println("map size " + map.size());
		for (int i = 12; i > 1; i--) {
			System.out.println("map remove " + i + " " + map.remove(i));
		}

		System.out.println(map.toDebugString());

		System.out.println("map remove " + 1 + " " + map.remove(1));
		System.out.println("map get " + 1 + " " + map.get(1));

		System.out.println("map get " + 1 + " " + map.get(1));

		for (int i = 12; i > 0; i--) {
			System.out.println("map get " + i + " " + map.get(i));
		}

		System.out.println("map size " + map.size());

		map.remove(22);
		System.out.println("values ");
		for (String v : map.values()) {
			System.out.println(v);
		}
	}
}

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;

public class Trains {

	static class Edge {
		final City destination;
		final int duration;

		Edge(City destination, int duration) {
			this.destination = destination;
			this.duration = duration;
		}
	}

	static class City implements Comparable<City> {
		final String name;
		final int time;

		final LinkedList<Edge> trains;

		City(String name, int time) {
			this.name = name;
			this.time = time;
			this.trains = new LinkedList<Edge>();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof City) {
				City other = (City) obj;
				return time == other.time && name == other.name;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return name.hashCode() * 37 + time;
		}

		@Override
		public int compareTo(City arg0) {
			if (!(arg0 instanceof City)) {
				throw new IllegalArgumentException();
			}

			return Integer.compare(time, arg0.time);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int testCases = sc.nextInt();

		for (int tCase = 1; tCase <= testCases; tCase++) {
			int citiesCount = sc.nextInt();

			HashMap<String, LinkedList<City>> cityToTrains = new HashMap<String, LinkedList<City>>(
					citiesCount, 1);

			for (int i = 0; i < citiesCount; i++) {
				String city = sc.next();
				cityToTrains.put(city, new LinkedList<City>());
			}

			int trainsCount = sc.nextInt();
			for (int i = 1; i <= trainsCount; i++) {
				int stationsCount = sc.nextInt();

				City previous = null;
				for (int j = 1; j <= stationsCount; j++) {
					int time = timeToInt(sc.next());
					String name = sc.next();

					City city = new City(name, time);
					if (previous != null) {
						int duration = time - previous.time;
						Edge edge = new Edge(city, duration);
						previous.trains.add(edge);
					}

					previous = city;

					LinkedList<City> list = cityToTrains.get(city.name);
					list.add(city);
					cityToTrains.put(city.name, list);
				}
			}

			/* read source-destination */
			int time = timeToInt(sc.next());
			String source = sc.next();
			String dest = sc.next();

			/* build edges from trains from the same city (transshipment) */
			for (Entry<String, LinkedList<City>> entry : cityToTrains
					.entrySet()) {

				if (entry.getKey().equals(source)) {
					continue;
				}

				LinkedList<City> cities = entry.getValue();

				for (int i = 0; i < cities.size() - 1; i++) {
					for (int j = i + 1; j < cities.size(); j++) {
						City city1 = cities.get(i);
						City city2 = cities.get(j);

						switch (city1.compareTo(city2)) {
						case -1:
							city1.trains.add(new Edge(city2, 0));
							break;

						case 0:
							city1.trains.add(new Edge(city2, 0));
							city2.trains.add(new Edge(city1, 0));
							break;

						case 1:
							city2.trains.add(new Edge(city1, 0));
							break;
						}
					}
				}
			}

			City minSource = null;
			City minDest = null;

			for (City city : cityToTrains.get(source)) {
				if (city.time < time) {
					continue;
				}

				City result = findShortestPath(cityToTrains, city, dest);

				if (result == null) {
					continue;
				}

				if (minDest == null || result.time < minDest.time
						|| (result.time == minDest.time && city.time > minSource.time)) {
					minSource = city;
					minDest = result;
				}
			}

			System.out.println(String.format("Scenario %d", tCase));
			if (minDest == null) {
				System.out.println("No connection");

			} else {
				String sSourceTime = intToTime(minSource.time);
				String sDestTime = intToTime(minDest.time);

				System.out.println(String.format("Departure %s %s",
						sSourceTime, source));
				System.out.println(String.format("Arrival   %s %s", sDestTime,
						dest));
			}
		}

		sc.close();
	}

	private static City findShortestPath(
			HashMap<String, LinkedList<City>> cityToTrains, City city,
			String destination) {

		HashSet<City> inTree = new HashSet<City>();

		HashMap<City, Integer> distance = new HashMap<City, Integer>();
		for (LinkedList<City> list : cityToTrains.values()) {
			for (City c : list) {
				distance.put(c, Integer.MAX_VALUE);
			}
		}

		City c = city;
		distance.put(c, 0);

		while (!inTree.contains(c)) {
			inTree.add(c);

			for (Edge train : c.trains) {
				final City dest = train.destination;
				if (!inTree.contains(dest)
						&& distance.get(dest) > distance.get(c)
								+ train.duration) {
					distance.put(dest, distance.get(c) + train.duration);
				}
			}

			Integer min = Integer.MAX_VALUE;
			for (Entry<City, Integer> d : distance.entrySet()) {
				City current = d.getKey();

				if (!inTree.contains(current) && min > d.getValue()) {
					c = current;
				}
			}
		}

		int minTime = Integer.MAX_VALUE;
		City result = null;
		for (Entry<City, Integer> d : distance.entrySet()) {
			City current = d.getKey();
			if (current.name.equals(destination)) {
				if (minTime > d.getValue()) {
					result = current;
					minTime = d.getValue();
				}
			}
		}

		return result;
	}

	static int timeToInt(String s) {
		int h1 = (s.charAt(0) - '0') * 10;
		int h2 = (s.charAt(1) - '0') * 1;

		int m1 = (s.charAt(2) - '0') * 10;
		int m2 = (s.charAt(3) - '0') * 1;

		int result = (h1 + h2) * 60 + (m1 + m2);
		return result;
	}

	static String intToTime(int i) {
		int h = i / 60;
		int h1 = h / 10;
		int h2 = h % 10;

		int m = i % 60;
		int m1 = m / 10;
		int m2 = m % 10;

		String result = String.format("%d%d%d%d", h1, h2, m1, m2);
		return result;
	}
}

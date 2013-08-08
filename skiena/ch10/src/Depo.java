import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Depo {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		String s = reader.readLine();
		int testCases = Integer.parseInt(s);

		reader.readLine();

		while (testCases-- > 0) {
			s = reader.readLine();
			String[] v = s.split(" ");

			int f = Integer.parseInt(v[0]);
			int n = Integer.parseInt(v[1]);

			Set<Integer> depos = new HashSet<Integer>(f, 1);

			for (int i = 1; i <= f; i++) {
				s = reader.readLine();
				int k = Integer.parseInt(s);
				depos.add(k);
			}

			int[][] adj = new int[n + 1][n + 1];
			for (int i = 0; i <= n; i++) {
				for (int j = 0; j <= n; j++) {
					adj[i][j] = Integer.MAX_VALUE;
				}
			}

			while ((s = reader.readLine()).length() > 0) {
				v = s.split(" ");
				Integer source = Integer.parseInt(v[0]);
				Integer dest = Integer.parseInt(v[1]);
				Integer dist = Integer.parseInt(v[2]);

				adj[source][dest] = adj[dest][source] = dist;
			}

			/* 1. preparation */
			int[] depoDist = null;
			for (Integer depo : depos) {
				int[] distance = findShortestDistance(depo, adj);

				if (depoDist == null) {
					depoDist = distance;

				} else {
					merge(depoDist, distance);
				}
			}

			int minDepo = Integer.MAX_VALUE;
			int min = calcSum(depoDist);

			/* 2. try to setup new station */
			for (int i = 1; i <= n; i++) {
				int[] restDistance = findShortestDistance(i, adj);
				
				if(depoDist == null){
					depoDist = restDistance;
				} else{
					merge(restDistance, depoDist);
				}
				
				int sum = calcSum(restDistance);

				// System.out.println("at " + i + " sum is " + sum);
				/* 3. compare with existing solution */
				if (sum < min || (sum == min && i < minDepo)) {
					min = sum;
					minDepo = i;
				}
			}

			System.out.println(minDepo);
			System.out.println();
		}

		reader.close();
	}

	private static int[] findShortestDistance(int depo, int[][] adj) {
		int[] result = new int[adj.length];

		Arrays.fill(result, Integer.MAX_VALUE);
		result[depo] = 0;

		boolean[] inTree = new boolean[adj.length];
		int v = depo;

		while (!inTree[v]) {
			inTree[v] = true;
			for (int i = 1; i < adj.length; i++) {
				if (!inTree[i] && adj[v][i] != Integer.MAX_VALUE
						&& adj[v][i] + result[v] < result[i]) {
					result[i] = adj[v][i] + result[v];
				}
			}

			int min = Integer.MAX_VALUE;
			for (int i = 1; i < result.length; i++) {
				if (!inTree[i] && min > result[i]) {
					v = i;
					min = result[i];
				}
			}
		}

		return result;
	}

	private static int calcSum(int[] depoDist) {
		if (depoDist == null) {
			return Integer.MAX_VALUE;
		}

		int result = 0;

		for (int i = 1; i < depoDist.length; i++) {
			result += depoDist[i];
		}

		return result;
	}

	private static void merge(int[] depoDist, int[] distance) {
		
		for (int i = 0; i < depoDist.length; i++) {
			int min = Math.min(depoDist[i], distance[i]);
			depoDist[i] = min;
		}
	}

	private static void depoInit(int n, Set<Integer> depos, int[] depoDist) {
		depoDist[0] = Integer.MAX_VALUE;
		for (int i = 1; i <= n; i++) {
			if (depos.contains(i)) {
				depoDist[i] = 0;

			} else {
				depoDist[i] = Integer.MAX_VALUE / 10;
			}
		}
	}

	private static int calcDistance(int n, Set<Integer> depos, int[][] adj,
			int[] depoDist) {

		// floyd algorithm
		for (int k = 1; k <= n; k++) {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					int oldDist = adj[i][j];
					int newDist = adj[i][k] + adj[k][j];

					if (newDist < oldDist) {
						adj[i][j] = newDist;

						if (depos.contains(i) && depoDist[j] > newDist) {
							depoDist[j] = newDist;

						} else if (depos.contains(j) && depoDist[i] > newDist) {
							depoDist[i] = newDist;
						}
					}
				}
			}
		}

		int sum = 0;
		for (int i = 1; i <= n; i++) {
			sum += depoDist[i];
		}

		return sum;
	}
}

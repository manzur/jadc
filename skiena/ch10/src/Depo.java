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
					adj[i][j] = Integer.MAX_VALUE / 10;
				}
			}

			while ((s = reader.readLine()).length() > 0) {
				v = s.split(" ");
				Integer source = Integer.parseInt(v[0]);
				Integer dest = Integer.parseInt(v[1]);
				Integer dist = Integer.parseInt(v[2]);

				adj[source][dest] = adj[dest][source] = dist;
			}

			int[] depoDist = new int[n + 1];

			int minDepo = -1;
			int min = Integer.MAX_VALUE;

			for (int i = 1; i <= n; i++) {
				depoInit(n, depos, depoDist);

				if (!depos.contains(i)) {
					depos.add(i);

					int result = calcDistance(n, depos, adj, depoDist);
					System.out.println("Depo " + i + " dist: " + result);
					if (result < min) {
						min = result;
						minDepo = i;
					}

					depos.remove(i);
				}
			}

			System.out.println(minDepo);
			System.out.println();
		}

		reader.close();
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

import java.util.Scanner;


public class Necklace {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int testCases = sc.nextInt();
		
		final int COLOR_COUNT = 51;
		for(int tCase = 1; tCase <= testCases; tCase++){
			
			int[][] adj = new int[COLOR_COUNT][COLOR_COUNT];
			
			int beadsCount = sc.nextInt();
			int color = 0;
			
			for(int i = 1; i <= beadsCount; i++){
				int a = sc.nextInt();
				int b = sc.nextInt();
				
				adj[a][b]++;
				adj[b][a]++;
				
				if(color == 0){
					color = a;
				}
			}
			
			boolean exists = existsEulerianCycle(adj);
			
			System.out.println(String.format("Case #%d", tCase));

			if(!exists){
				System.out.println("some beads may be lost");
			} else{
				printEulerianCycle(adj, color);
			}
			
			if(tCase < testCases){
				System.out.println();
			}
			
		}
		sc.close();
	}

	private static void printEulerianCycle(int[][] adj, int color) {
		while(adj[color][color] >  0){
			System.out.println(String.format("%d %d", color, color));

			// dec by two
			adj[color][color]--;
			adj[color][color]--;
		}
		
		for(int i = 1; i < adj.length; i++){
			if(adj[color][i] > 0){
				System.out.println(String.format("%d %d", color, i));
				adj[color][i]--;
				adj[i][color]--;
				
				printEulerianCycle(adj, i);
			}
		}
	}

	private static boolean existsEulerianCycle(int[][] adj) {
		// Eulerian cycle exists in undirected graph if all vertices have degree even
		for(int[] row : adj){
			int sum = 0;
			for(int col : row){
				sum += col;
			}
			
			if(sum % 2 == 1){
				return false;
			}

		}
		
		return true;
	}
}

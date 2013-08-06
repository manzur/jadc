import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Collars {
	
	static class Pair{
		int x;
		int y;
		
		Pair(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int testCases = sc.nextInt();
		Set<Integer>[] verticesByColor = new HashSet[51];

		for (int i = 0; i < verticesByColor.length; i++) {
			verticesByColor[i] = new HashSet<Integer>();
		}

		while (testCases-- > 0) {
			for (int i = 0; i < verticesByColor.length; i++) {
				verticesByColor[i].clear();
			}

			int N = sc.nextInt();
			Pair[] collars = new Pair[N + 1];
			for(int i = 0; i < N; i++){
				int color1 = sc.nextInt();
				int color2 = sc.nextInt();
				
				Pair pair = new Pair(color1, color2);
				collars[i] = pair;
			}
			
			LinkedList[] solutions = new LinkedList[N * 2 + 1];
			for(int i = 1; i <= N; i++){
				solutions[toIndex(i, N)] = new LinkedList<Integer>();
				solutions[toIndex(i, N)].add(i);
				
				solutions[toIndex(-i, N)] = new LinkedList<Integer>();
				solutions[toIndex(-i, N)].add(-i);
			}
			
			for(int k = 2; k <= N; k++){
				for(int i = 1; i <= N; i++){
					// for -i
					LinkedList<Pair> negList = solutions[toIndex(-i, N)];
					Pair pair = negList.getLast();
					int color = pair.y;
					Set<Integer> compatibleColors = verticesByColor[color];
					for(Integer c : compatibleColors){
						
					}
					
					// for i
					LinkedList posList = solutions[toIndex(-i, N)];
				}
			}

		}
		sc.close();
	}

	private static int toIndex(int i, int n) {
		if(i < 0){
			return n + i * -1;
		}
		
		return i;
	}
}

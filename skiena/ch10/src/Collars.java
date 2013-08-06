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
		
		@Override
		public int hashCode() {
			return x << 16 + y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pair){
				Pair other = (Pair) obj;
				return x == other.x && y == other.y;
			}
			
			return false;
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
			for(int i = 1; i <= N; i++){
				int color1 = sc.nextInt();
				int color2 = sc.nextInt();
				
				Pair pair = new Pair(color1, color2);
				collars[i] = pair;

				verticesByColor[color1].add(i);
				verticesByColor[color2].add(-i);
			}
			
			LinkedList<LinkedList<Integer>> solutions = new LinkedList<LinkedList<Integer>>();
			for(int i = 1; i <= N; i++){
				LinkedList<Integer> list1 = new LinkedList<Integer>();
				LinkedList<Integer> list2 = new LinkedList<Integer>();
				
				list1.add(i);
				list2.add(-i);
				
				solutions.add(list1);
				solutions.add(list2);
			}
			
			for(int k = 2; k <= N; k++){
				for(int i = 1; i <= N; i++){
					LinkedList<LinkedList<Integer>> newSolutions = new LinkedList<LinkedList<Integer>>();
					
					for(LinkedList<Integer> list : solutions){
						Integer last = list.getLast();
						Pair lastPair = getElem(collars, last);
						int color1 = lastPair.y;
						
						if(verticesByColor[color1].contains(i)){
							LinkedList<Integer> newList = (LinkedList<Integer>) list.clone();
							newList.add(i);
							newSolutions.add(newList);
						}
						
						if(verticesByColor[color1].contains(-i)){
							LinkedList<Integer> newList = (LinkedList<Integer>) list.clone();
							newList.add(i);
							newSolutions.add(newList);
						}
					}
				}
			}

		}
		sc.close();
	}

	private static Pair getElem(Pair[] collars, Integer last) {
		if(last < 0){
			Pair old = collars[-last];
			Pair result = new Pair(old.x, old.y);
			return result;
		}
		
		return collars[last];
	}

	private static int toIndex(int i, int n) {
		if(i < 0){
			return n + i * -1;
		}
		
		return i;
	}
}

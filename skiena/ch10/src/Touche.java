import java.util.Arrays;
import java.util.Scanner;

public class Touche {
	
	static class Point{
		double x;
		double y;
		
		Point(double _x, double _y){
			x = _x;
			y = _y;
		}
		
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int testCases = sc.nextInt();
		
		while(testCases-- > 0){
			int toucheCount = sc.nextInt();
			Point[] touchePositions = new Point[toucheCount];

			for(int toucheN = 0; toucheN < touchePositions.length; toucheN++){
				double x = sc.nextDouble();
				double y = sc.nextDouble();

				touchePositions[toucheN] = new Point(x, y);
			}
			
			double result = minMstDistance(touchePositions);
			System.out.println(String.format("%.2f", result));
			
			if(testCases > 0){
				System.out.println();
			}
		}
			
		sc.close();
	}
	
	private static double minMstDistance(Point[] touchePositions) {
		
		int start = 0;
		
		boolean[] inTree = new boolean[touchePositions.length];

		double[] distance = new double[touchePositions.length];
		Arrays.fill(distance, Double.MAX_VALUE);

		int[] parents = new int[inTree.length];
		Arrays.fill(parents, -1);

		int v = start;

		/* 1. calculate all shortest paths from start */
		while(!inTree[v]){
			inTree[v] = true;
			
			for(int i = 0; i < touchePositions.length; i++){
				if(v == i) continue;
				
				double distanceV2I = distance(touchePositions[v], touchePositions[i]);
				
				if(distance[i] > distanceV2I && !inTree[i]){
					distance[i] = distanceV2I;
					parents[i] = v;
				}
			}
			
			double min = Double.MAX_VALUE;
			for(int i = 0; i < distance.length; i++){
				if(distance[i] < min && !inTree[i]){
					min = distance[i];
					v = i;
				}
			}
		}
		
		/* 2. calculate all shortest paths from start */
		double result = 0.0;
		for(int i = 0; i < parents.length; i++){
			if(parents[i] != -1){
				Point v1 = touchePositions[parents[i]];
				Point v2 = touchePositions[i];
				
				result += distance(v1, v2);
			}
		}

		return result;
	}

	private static double distance(Point point1, Point point2){
		double result = 0.0f;
		result = Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
		
		return result;
	}
}

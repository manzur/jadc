import java.util.Scanner;

import sun.security.provider.certpath.AdjacencyList;

public class Touche {
	
	static class Point{
		float x;
		float y;
		
		Point(float _x, float _y){
			x = _x;
			y = _y;
		}
		
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int testCases = sc.nextInt();
		
		for(int caseN = 0; caseN < testCases; caseN++){
			int toucheCount = sc.nextInt();
			Point[] touchePositions = new Point[toucheCount];

			for(int toucheN = 0; toucheN < touchePositions.length; toucheN++){
				float x = sc.nextFloat();
				float y = sc.nextFloat();

				touchePositions[toucheN] = new Point(x, y);
			}
			
			float result = shortestCycle(touchePositions);
			System.out.println(String.format("%.2f", result));
		}
			
		sc.close();
	}
	
	private static float shortestCycle(Point[] touchePositions) {
		float[] distance = new float[touchePositions.length];
		
		int start = 0;
		
		for(int i = 1; i < distance.length; i++){
			distance[i] = distance(touchePositions[start], touchePositions[i]);
		}
		
		boolean[] inTree = new boolean[touchePositions.length];
		
		int v = start;

		/* 1. calculate all shortest paths from start */
		while(!inTree[v]){
			inTree[v] = true;
			
			for(int i = 0; i < touchePositions.length; i++){
				if(v == i) continue;
				
				float distanceV2I = distance(touchePositions[v], touchePositions[i]);
				
				if(distance[i] > distance[v] + distanceV2I){
					distance[i] = distance[v] + distanceV2I;
				}
			}
			
			float min = Float.MAX_VALUE;
			for(int i = 0; i < distance.length; i++){
				if(distance[i] < min){
					min = distance[i];
					v = i;
				}
			}
		}
		
		/* 2. calculate all shortest paths from start */
		float min = Float.MAX_VALUE;
		for(int i = 0; i < touchePositions.length; i++){
			if(start == i) continue;
			
			float dist = distance(touchePositions[start], touchePositions[i]) + distance[i];
					
			if(min > dist){
				min = dist;
			}
		}
		
		return min;
	}

	private static float distance(Point point1, Point point2){
		float result = 0.0f;
		result = (point1.x - point2.x) * (point1.x - point2.x) + (point1.x - point2.x) * (point1.x - point2.x);
		
		return result;
	}
}

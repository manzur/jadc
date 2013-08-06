import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Guider {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int testCase = 1;
		int nCities;
		
		while((nCities = sc.nextInt()) != 0){
			ArrayList<String> cities = new ArrayList<String>(nCities);
			
			for(int i = 0; i < nCities; i++){
				cities.add(sc.next());
			}
			
			int nEdges = sc.nextInt();
			boolean[][] edges = new boolean[nCities][nCities];
			
			for(int i = 0; i < nEdges; i++){
				String c1 = sc.next();
				String c2 = sc.next();
				
				int index1 = cities.indexOf(c1);
				int index2 = cities.indexOf(c2);
				edges[index1][index2] = edges[index2][index1] = true;
			}
			
			List<String> articles = findArticles(cities, edges);
			Collections.sort(articles);
			
			System.out.println(String.format("City map #%d: %d camera(s) found", testCase, articles.size()));
			for(String c : articles){
				System.out.println(c);
			}

			System.out.println();
			
			testCase = testCase + 1;
		}
		
		sc.close();
	}

	private static List<String> findArticles(ArrayList<String> cities,
			boolean[][] edges) {
		
		LinkedList<String> result = new LinkedList<String>();
		
		/* components size */
		int compCount = 0;
		Set<String> visiteds = new HashSet<String>(cities.size(), 1);
		for(int i = 0; i < cities.size(); i++){
			String city = cities.get(i);
	
			if(!visiteds.contains(city)){
				dfs(i, cities, edges, visiteds, "-1");
				compCount = compCount + 1;
			}
		}
		
		for(int i = 0; i < cities.size(); i++){
			String forbidden = cities.get(i);
			
			visiteds = new HashSet<String>(cities.size(), 1);
			
			int cc = 0;
			for(int j = 0; j < cities.size(); j++){
				if(i == j) continue;

				String city = cities.get(j);
				if(!visiteds.contains(city)){
					visiteds.add(city);
					dfs(j, cities, edges, visiteds, forbidden);
				
					cc = cc + 1;
				}
			}

			
			if(cc > compCount){
				result.add(forbidden);
			}
		}
		
		return result;
	}

	private static void dfs(int start, ArrayList<String> cities, boolean[][] edges, Set<String> visiteds, String forbidden) {
		for(int i = 0; i < cities.size(); i++){
			
			String city = cities.get(i);
			
			if(edges[start][i] && !forbidden.equals(cities.get(i)) && !visiteds.contains(city)){
				visiteds.add(city);
				dfs(i, cities, edges, visiteds, forbidden);
			}
		}
		
	}
}

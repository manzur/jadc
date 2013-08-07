package union;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class RankUnionFindInteger implements IUnionFind<Integer> {

	private final int[] parent;
	private final int[] rank;
	
	public RankUnionFindInteger(int N) {
		rank = new int[N + 1];
		parent = new int[N + 1];
	}
	
	@Override
	public void makeSet(Integer element) {
		parent[element] = element;
		rank[element] = 0;
	}

	@Override
	public void union(Integer first, Integer second) {
		Integer rootFirst = find(first);
		Integer rootSecond = find(second);
		
		if(rootFirst == null || rootSecond == null){
			throw new IllegalArgumentException();
		}
		
		if(rank[rootFirst] == rank[rootSecond]){
			parent[rootSecond] = rootFirst;
			rank[rootFirst] = rank[rootFirst] + 1;
			
		} else if(rank[rootFirst] < rank[rootSecond]){
			parent[rootFirst] = rootSecond;
			
		} else{
			parent[rootSecond] = rootFirst;
		}
	}

	@Override
	public Integer find(Integer x) {
		Integer p = parent[x];
		if(p == x){
			return p;
		}
		
		return find(parent[x]); 
	}
	
	@Override
	public String toString() {
		HashMap<Integer, LinkedList<Integer>> map = new HashMap<>();

		for(int i = 1; i < parent.length; i++){
			int p;
			
			if((p = parent[i]) != 0){
				LinkedList<Integer> list = map.get(p);
				if(list == null){
					list = new LinkedList<Integer>();
				}
				
				list.add(i);
				map.put(p, list);
			}
		}
		
		StringBuilder builder = new StringBuilder();
		for(Entry<Integer, LinkedList<Integer>> entry : map.entrySet()){
			Integer setNumber = entry.getKey();
			
			builder.append("Set #" + setNumber + "\n");
			
			for(Integer v : entry.getValue()){
				builder.append(v + " ");
			}
			
			builder.append("\n");
		}
		
		return builder.toString();
	}

}

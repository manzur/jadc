package union;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/* Disjoint-set for numbers from 1 to N */
public class UnionFindInteger implements IUnionFind<Integer>{
	
	private int[] elementToSetIndex;
	private LinkedList<Integer> indexToSetIndex = new LinkedList<Integer>();
	
	private Map<Integer, Integer> setRepresentatives = new HashMap<Integer, Integer>();
	
	private int setCounter = 1;
	
	private final int N;

	public UnionFindInteger(int N){
		this.N = N;
		elementToSetIndex = new int[N + 1];
		indexToSetIndex.add(null);
	}
			

	@Override
	public void makeSet(Integer element) {
		int setNumber = setCounter++;
		indexToSetIndex.add(setNumber);
		elementToSetIndex[element] = indexToSetIndex.size() - 1;
		setRepresentatives.put(setNumber, element);
	}

	@Override
	public void union(Integer first, Integer second) {
		int setIndex1 = elementToSetIndex[first];
		int setIndex2 = elementToSetIndex[second];
		
		int setNumber1 = indexToSetIndex.get(setIndex1);
		int setNumber2 = indexToSetIndex.get(setIndex2);
		
		indexToSetIndex.set(setIndex2, setNumber1);
		setRepresentatives.remove(setNumber2);
	}

	@Override
	public Integer find(Integer x) {
		int setIndex1 = elementToSetIndex[x];
		Integer setNumber1 = indexToSetIndex.get(setIndex1);
		if(setNumber1 == null){
			throw new IllegalArgumentException("no such element");
		}
		
		return setRepresentatives.get(setNumber1);
	}
	
	@Override
	public String toString() {
		HashMap<Integer, LinkedList<Integer>> map = new HashMap<>();
		for(int i = 1; i <= N; i++){
			int setIndex = elementToSetIndex[i];
			Integer setNumber = indexToSetIndex.get(setIndex);
					
			if(setNumber != null){
				LinkedList<Integer> list = map.get(setNumber);
				if(list == null){
					list = new LinkedList<>();
				}
				
				list.add(i);
				map.put(setNumber, list);
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

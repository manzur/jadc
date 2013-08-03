package union;
import java.util.Collections;
import java.util.LinkedList;

public class UnionFindList<T> implements IUnionFind<T> {
	
	private LinkedList<LinkedList<T>> sets = new LinkedList<>();

	@Override
	public void makeSet(T element){
		LinkedList newSet = new LinkedList<>();
		newSet.add(element);
		sets.add(newSet);
	}
	
	@Override
	public void union(T first, T second){
		int index1 = -1;
		int index2 = -1;
		for(int i = 0; i < sets.size(); i++){
			if(index1 != -1 && index2 != -1) break;
			for(T t : sets.get(i)){
				
				if(t.equals(first)){
					index1 = i; 
				
				}else if(t.equals(second)){
					index2 = i; 
				}
			}
			
		}
		
		if(index1 == -1 || index2 == -1){
			throw new IllegalArgumentException();
		}
		
		LinkedList<T> f = sets.get(index1);
		LinkedList<T> s = sets.get(index2);
		
		for(T t : s){
			f.add(t);
		}

		sets.remove(index2);
	}
	
	@Override
	public T find(T element){
		for(LinkedList<T> set: sets){
			if(set.contains(element)){
				return set.getFirst();
			}
		}
		
		return null; 
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		for(int i = 1; i <= sets.size(); i++){
			builder.append("Set #" + i + "\n");
			
			for(T t : sets.get(i - 1)){
				builder.append(t + " ");
			}
			
			builder.append("\n");
		}
		
		return builder.toString();
	}

}
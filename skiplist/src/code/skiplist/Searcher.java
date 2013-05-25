package code.skiplist;

public abstract class Searcher<K extends Comparable<K>, V> {
	public abstract V search(K k);
}

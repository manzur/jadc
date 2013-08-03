package union;

public interface IUnionFind<T> {
	void makeSet(T element);
	void union(T first, T second);
	T find(T x);
}

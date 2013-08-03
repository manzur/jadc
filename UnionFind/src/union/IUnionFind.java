package union;

public interface IUnionFind {
	IUnionFind union(IUnionFind other);
	int find(int x);
}

package union;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestUnionFindList {

	@Test
	public void test() {
		UnionFindList<Integer> unionFind = new UnionFindList<>();
		
		unionFind.makeSet(1);
		unionFind.makeSet(2);
		unionFind.makeSet(3);
		
		System.out.println(unionFind.toString());
		
		System.out.println("element: " + unionFind.find(1));
		System.out.println("element: " + unionFind.find(2));
		System.out.println("element: " + unionFind.find(3));
		
		unionFind.union(1, 2);
		
		System.out.println(unionFind.toString());
		
		System.out.println("element: " + unionFind.find(1));
		System.out.println("element: " + unionFind.find(2));
		System.out.println("element: " + unionFind.find(3));
		
		unionFind.union(1, 3);
		
		System.out.println(unionFind.toString());
	}

}

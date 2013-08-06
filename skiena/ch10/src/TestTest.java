import java.util.Scanner;

import javax.swing.text.html.parser.TagElement;


public class TestTest {
	
	int defaultV = getFofo();
	
	int getFofo(){
		System.out.println("get fofo");
		return 111;
	}
	
	{
		System.out.println("block" + defaultV);
	}
	
	static {
		System.out.println("static block");
	}
	
	public TestTest() {
		System.out.println("const" + defaultV);
	}
	
	public static void main(String[] args) {
		new TestTest();
//		Scanner sc = new Scanner(System.in);
//		System.out.println(sc.next());
//		sc.close();
	}
}

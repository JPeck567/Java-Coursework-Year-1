
public class Tester extends Test {

	public static void main() {
		
	}
	
	public void printer() {
		super.printer();
		System.out.println("aaaaaaaaaaaaaaa");
	}
	
	public static void main(String[] args) {
		Test t = new Tester();
		t.printer();
	}

}

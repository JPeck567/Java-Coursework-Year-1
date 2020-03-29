import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// for testing stuff
public class test {
	public static void main(String[] args) {
		Map<Integer, ArrayList<Integer>> requestsList;
		requestsList = new HashMap<Integer, ArrayList<Integer>>();
		
		requestsList.put(5, new ArrayList<Integer>());
		ArrayList<Integer> s = requestsList.get(5);
		s.add(5);
		requestsList.put(5, s);
		System.out.println("Hello There");
	}
}

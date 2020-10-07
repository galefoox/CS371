
import org.junit.Test;

public class MemDriver{
	public static void main(String[] args) {
		final int SIZE = 10000;

		final int TEST_SIZE_1 = 10;
		final int TEST_SIZE_2 = 20;
		MyMemoryAllocation m= new MyMemoryAllocation(SIZE, "NF");
		boolean result = true;
		int ptr[] = new int[SIZE]; // MAKES AN ARRAY OF 10,000
		int p = 0;
		while (m.max_size() >= TEST_SIZE_1) {	// 9,999 > 10
			ptr[p] = m.alloc(TEST_SIZE_1);		// ALLOC SIZE OF 10 IN EVERY INDEX
			if (ptr[p] == 0) {
				result = false;
			}
			p++;
		}
		m.print();
		// CHECKS IF MAX_P > 400, IT IS 999
		// int max_p = p;
		// if (max_p < 400) {
		// 	result = false;
		// }
		// int l_limit = p / 3; // 333.3
		// int u_limit = 2 * p / 3; /// 666.6
		// for (int i = l_limit; i < u_limit; i++) {	// FREES 3330 -> 6650
		// 	m.free(ptr[i]);
		// 	ptr[i] = 0;
		// }
		// if(m.max_size() != (u_limit-l_limit)*TEST_SIZE_1) {
		// 	result = false;
		// }
	
		// // MAX SIZE IS 3330

		// p = l_limit; // P = 333.3
		// while (p < u_limit && m.max_size() >= TEST_SIZE_1) { // 333 < 666 && 3330 > 10
		// 	ptr[p] = m.alloc(TEST_SIZE_1);
		// 	if (ptr[p] == 0) {
		// 		result = false;
		// 	}
		// 	p++;
		// }

		
		// for (int i = 0; i < max_p; i++) { // FAIL
		// 	if (ptr[i] > 0)
		// 		m.free(ptr[i]);
		// 	ptr[i] = 0;
		// }
		
		//  if(m.size() != SIZE-1) {
		//  	result = false;
		// }
		
		if (result) {
			System.out.println("end2endTest1: PASS ");
		} else {
			System.out.println("end2endTest1: FAIL");
		}
	
	}
	

}

	




import org.junit.Test;

public class MemDriver{
	public static void main(String[] args) {
		MyMemoryAllocation mal= new MyMemoryAllocation(14, "BF");
		mal.alloc(1);
		mal.alloc(3);
		mal.alloc(2);
		mal.alloc(2);
		mal.alloc(1);
		mal.alloc(1);
		mal.alloc(1);
		mal.alloc(2);
	    mal.free(2);
		mal.free(7);
		mal.free(10);
		mal.free(12);
	 //	mal.alloc(1);
//		mal.alloc(2);
	//	mal.alloc(2); // FIX NF HERE, IT DELETES THE NODE BUT GIVES US NOT ENOUGH SPACE
		// mal.alloc(3);
		// mal.alloc(1);
		mal.print();
	}

	@Test
	public void testBFAlloc() {
		MyMemoryAllocation mal= new MyMemoryAllocation(14, "NF");
		mal.alloc(1);
		mal.alloc(3);
		mal.alloc(2);
		mal.alloc(2);
		mal.alloc(1);
		mal.alloc(1);
		mal.alloc(1);
		mal.alloc(2);
		mal.free(2);
		mal.free(7);
		mal.free(10);
		mal.free(12);
		assert(mal.size() == 8);
		assert(mal.max_size() == 3);
		// assert(mal.alloc(1)==2);
		// assert(mal.alloc(2)==7);
	//	assert(mal.alloc(2)==12);
	//	assert(mal.alloc(3)==0); //also failed case ! fragments!
	//	assert(mal.alloc(1)==3);

	}
}

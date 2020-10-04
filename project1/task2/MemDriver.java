
public class MemDriver{
	public static void main(String[] args) {
		MyMemoryAllocation trial = new MyMemoryAllocation(14, "FF");


		trial.alloc(6); //13-6 == 7
		trial.alloc(1); //7 - 1 == 6
		trial.alloc(2); //13 - 9 = 4 total
		trial.alloc(7);
		trial.alloc(2);
		trial.print();
	
		
	}
		
	}

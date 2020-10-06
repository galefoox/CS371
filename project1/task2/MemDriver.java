
public class MemDriver{
	public static void main(String[] args) {
		MyMemoryAllocation trial = new MyMemoryAllocation(15, "NF");


		trial.alloc(6); // 1

		trial.alloc(1); // 7

		trial.alloc(2); // 8

		trial.alloc(2); // 10
		//trial.alloc(2); // 17
		// trial.alloc(1); // 19
		// trial.alloc(5); // 20
		// trial.alloc(6); ra// 25
		// trial.alloc(7); // 31
		// trial.alloc(8); // 38
		trial.free(7);
	//	trial.free(1);
		trial.free(8);
		trial.alloc(1);
	//	trial.alloc(4);
		trial.alloc(1);
	//	trial.free(10);
	

		trial.print();



	}
}

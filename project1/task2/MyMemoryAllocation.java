import java.util.Iterator;

public class MyMemoryAllocation extends MemoryAllocation {
	

	private String algo;
	private MyLinkedList usedList;
	private MyLinkedList freeList;
	private int totalSizeAvail;
	private int totalUsedSize;
	

	public MyMemoryAllocation(int mem_size, String algorithm) {
		super(mem_size, algorithm);
		usedList = new MyLinkedList();
		freeList = new MyLinkedList();
		freeList.firstBlock(mem_size - 1);
		totalSizeAvail = mem_size - 1;
		algo = algorithm;
		//initialize linked list here (all data initialized here)!!!
	}
	

	@Override
	public int alloc(int size) {
		// TODO Auto-generated method stub

		// MAKE SURE TO INCLUDE THAT THE FREE OFFSET CANT BE BIGGER THAN THE MEM SIZE
		
		
		int offset = 0;

		if(algo.contentEquals("FF"))
		{
			offset = FirstFit(size);
		
		}




		return offset;
		
		
		// else if(algorithm.equals("NF"))
		// {
		// 	return NextFit(size); //FINISSHHH LATERRRRR
		// }
			
			
			
}
	
	public int FirstFit(int size)
	{
		if (freeList == null || size > totalSizeAvail )
		{
			System.out.println("Not enough available space");
			return 0;
		}
		
		else
		{
			 int offset;
			// int usedListOff;
			// int usedListSize;
			offset = freeList.getThatOffsetInitial();
			usedList.insert(size, offset);//Insert Node into used list
			// usedListOff = usedList.getThatOffsetRemaining();
			// usedListSize = usedList.getThatSizeRemaining();
			// freeList.setFirstFreeNode(usedListOff + size, totalSizeAvail - usedListSize);
			freeList.

			usedList.sortIt();

			
			//System.out.println("initial size: " + totalSizeAvail);
			
			totalSizeAvail = size();
			
			System.out.println("TotalSizeAvail: " + totalSizeAvail);
			
			return offset;
		}
	}
	
	// public int NextFit(int size)
	// {
	// 	int offset = FirstFit(size);
	// 	return offset;
		
	// 	//FINISHHH LATERRRRRR
	// }

		
	

	@Override
	public void free(int addr) {

	
		// TODO Auto-generated method stub
		/* if FF, */
		freeList.insertMayCompact(usedList.getUsedListMatch(addr));
		usedList.delete(addr);
		
		size();
		

		//insert to freeList
		//delete from usedList
	}

	@Override
	public int size() { // Keeps track of the total memory we're using
		
	

		Iterator iterator = usedList.iterator();
		
		while(iterator.hasNext())
		{
			totalUsedSize = usedList.getThatSizeRemaining();
			iterator.next();
		}
		
		totalSizeAvail -= totalUsedSize;
		
		return totalSizeAvail;
	}

	@Override
	public int max_size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void print() {

		Iterator iterator = usedList.iterator();
		Iterator iterator2 = freeList.iterator();


		while(iterator.hasNext())
		{
			
			System.out.println(iterator.next());

		}
		System.out.println("FreeList");
		while(iterator2.hasNext())
		{
			
			System.out.println(iterator2.next());

		}
		
	}

	/*
	 * Allocates memory with defined size. 
         * If the memory is evailable the function returns pointer (offset) of the begining of allocated memory. 
         * Otherwise it returns 0.
	 */
	
	//MyLinkedList list = new MyLinkedList();
	
	
}

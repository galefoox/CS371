import java.util.Iterator;

public class MyMemoryAllocation extends MemoryAllocation {
	

	private String algorithm;
	private MyLinkedList usedList;
	private MyLinkedList freeList;
	private int totalSizeAvail;

	public MyMemoryAllocation(int mem_size, String algorithm) {
		super(mem_size, algorithm);
		usedList = new MyLinkedList();
		freeList = new MyLinkedList();
		freeList.firstBlock(mem_size - 1);
		totalSizeAvail = mem_size - 1;
		//initialize linked list here (all data initialized here)!!!
	}
	

	@Override
	public int alloc(int size) {
		// TODO Auto-generated method stub

		// MAKE SURE TO INCLUDE THAT THE FREE OFFSET CANT BE BIGGER THAN THE MEM SIZE
		if(algorithm.equals("FF"))
		{
			
			return FirstFit(size);
		}
		
		else if(algorithm.equals("NF"))
		{
			return NextFit(size); //FINISSHHH LATERRRRR
		}
			
			
			
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
			int usedListOff;
			int usedListSize;
			offset = freeList.getThatOffsetInitial();
			usedList.insert(size, offset);//Insert Node into used list
			usedListOff = usedList.getThatOffsetRemaining();
			usedListSize = usedList.getThatSizeRemaining();
			freeList.setFirstFreeNode(usedListOff + size, usedListSize);
			usedList.sortIt();
			
			//System.out.println("initial size: " + totalSizeAvail);
			
			totalSizeAvail = size();
			
			System.out.println("TotalSizeAvail: " + totalSizeAvail);
			
			return offset;
		}
	}
	
	public int NextFit(int size)
	{
		int offset = FirstFit(size);
		return offset;
		
		//FINISHHH LATERRRRRR
	}

		
	

	@Override
	public void free(int addr) {


		// TODO Auto-generated method stub
		/* if FF, */
	}

	@Override
	public int size() { // Keeps track of the total memory we're using
		
		int totalUsedSize = 0;
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



		while(iterator.hasNext())
		{
			
			System.out.println(iterator.next());

		}
		
	}

	/*
	 * Allocates memory with defined size. 
         * If the memory is evailable the function returns pointer (offset) of the begining of allocated memory. 
         * Otherwise it returns 0.
	 */
	
	//MyLinkedList list = new MyLinkedList();
	
	
}

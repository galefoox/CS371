
import java.util.Iterator;



public class MyMemoryAllocation extends MemoryAllocation {
	

	private String algo;
	private MyLinkedList usedList;
	private MyLinkedList freeList;
	private int totalSizeAvail;
	private int maxSize;
	
	

	public MyMemoryAllocation(int mem_size, String algorithm) {
		super(mem_size, algorithm);
		usedList = new MyLinkedList();
		freeList = new MyLinkedList();
		freeList.firstBlock(mem_size - 1);
		totalSizeAvail = mem_size - 1;
		algo = algorithm;
		maxSize = totalSizeAvail;
		//initialize linked list here (all data initialized here)!!!
	}
	

	@Override
	public int alloc(int size) {
		// TODO Auto-generated method stub

		// MAKE SURE TO INCLUDE THAT THE FREE OFFSET CANT BE BIGGER THAN THE MEM SIZE
		int offset = 0;
	
		maxSize = max_size();
		if (freeList == null || size > maxSize )
		{
			System.out.println("Not enough available space");
			return 0;
		}

		else if(algo.contentEquals("FF"))
		{
			offset = FirstFit(size);
		
		}
		else if (algo.contentEquals("BF"))
		{
			offset = BestFit(size);
		}
		else
		{
			offset = NextFit(size);
		}
		
		totalSizeAvail = size();
		return offset;
			
}
	public int BestFit(int size)
	{

			int offset;	

			offset = freeList.getThatOffsetInitial();
			usedList.insert(size, offset);//Insert Node into used list
			freeList.splitMayDelete(size, algo);


			usedList.sortIt();
			freeList.sortItSize();
		
			return offset;

	}
	
	public int FirstFit(int size)
	{

			 int offset;

			offset = freeList.getThatOffsetInitial();
			usedList.insert(size, offset);//Insert Node into used list
			freeList.splitMayDelete(size, algo);


			usedList.sortIt();

			
			return offset;
		
	}
	
	public int NextFit(int size)
	{
			int offset;
			
			if (usedList.isEmpty())
			{
				offset = freeList.getThatOffsetInitial();
				usedList.insert(size, offset);
				freeList.splitMayDelete(size, algo);
				
			}
			
			else
			{
					offset = freeList.thatNF();
					freeList.splitMayDelete(size, algo);
					usedList.insert(size, offset);
		
			}
			usedList.sortIt();
			
			
			return offset;
	}



		
	

	@Override
	public void free(int addr) {

	
		// TODO Auto-generated method stub
		/* if FF, */
		// check if the free addy is valid

		if (usedList.checkUsedOffset(addr) != addr)
		{
			System.err.println("No such offset");
		}
		//insert free addy
		else if (algo.contentEquals("FF") || algo.contentEquals("NF"))
		{
			freeList.insertMayCompact(usedList.getUsedListMatch(addr));
			usedList.delete(addr);
			
			totalSizeAvail = size();
		}
		else
		{
			freeList.insertMayCompact(usedList.getUsedListMatch(addr));
			usedList.delete(addr);
			

			freeList.sortItSize();
		}
		maxSize = max_size();
		totalSizeAvail = size();
		

		//insert to freeList
		//delete from usedList
	}

	@Override
	public int size() { // Keeps track of the total memory we're using
		

		totalSizeAvail = freeList.getThatSizeRemaining();
	//	System.out.println("TotalSizeAvail: " + totalSizeAvail);

		
		return totalSizeAvail;
	}

	@Override
	public int max_size() {
		// Iterator iterator = freeList.iterator();
		
		int biggestSize = 0;
		biggestSize = freeList.getMaxSize();
		// int biggestSize = 0;
		// while (iterator.hasNext())
		// {
		// 	if (biggestSize < freeList.getThatSizeInitial())
		// 	{
		// 		biggestSize = freeList.getThatSizeInitial();
		// 	}
		// 	iterator.next();
			
		// }
		// System.out.println("Max size: " + biggestSize);
		return biggestSize;
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
		System.out.print("Max Size: " + maxSize + " ");
		System.out.println("Total Size: " +totalSizeAvail);

	}
	
}

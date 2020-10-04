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
			offset = freeList.getThatOffset(freeList.getHead());
			usedList.insert(size, offset);//Insert Node into used list
			usedListOff = usedList.getThatOffset(usedList.getTail());
			usedListSize = usedList.getThatSize(usedList.getTail());
			freeList.setFreeNode(usedListOff + size, usedListSize);
			usedList.sortIt();
			totalSizeAvail = size();
			return offset;
			//keep the offset
			//update the size
			//Update size and offset of freelist
		}

		//SortedInsert()
		/* so basically she will pass some number into alloc and then we will take
		 * that value and put it into block and then pass block into insert for the 
		 * linked list
		 * 
		 * put if else statement
		 * if: free linked list/max size > size (in alloc) then you insert block 
		 * else: no space avail
		 * 
		 * make new block 
		 * put size into block 
		 * put offset into block
		 * call function insert
		 */
	}

	@Override
	public void free(int addr) {


		// TODO Auto-generated method stub
		/* if FF, */
	}

	@Override
	public int size() { // Keeps track of the total memory we're using
		// TODO Auto-generated method stub
		int usedSize = 0;
		Iterator iterator = usedList.iterator();
		if (iterator.hasNext() == false)
		{
			usedSize = usedList.getThatSize(usedList.getHead());
			totalSizeAvail = totalSizeAvail - usedSize;
		}
		else
		{
			while (iterator.hasNext())
			{
				usedSize = usedSize + usedList.getThatSize(usedList.getTail());
				iterator.next();

			}
		totalSizeAvail = totalSizeAvail - usedSize;
		}
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
			iterator.traverse();
			iterator.next();

		}
		
	}

	/*
	 * Allocates memory with defined size. 
         * If the memory is evailable the function returns pointer (offset) of the begining of allocated memory. 
         * Otherwise it returns 0.
	 */
	
	//MyLinkedList list = new MyLinkedList();
	
	
}

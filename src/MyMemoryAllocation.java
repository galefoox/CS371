
import java.util.Iterator;

//import javax.lang.model.util.ElementScanner14;




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

			freeList.splitMayDelete(size, algo);
			offset = freeList.getThatOffset();
			usedList.insert(size, offset);//Insert Node into used list
			


			usedList.sortIt();
			freeList.sortItSize();
		
			return offset;

	}
	
	public int FirstFit(int size)
	{

			 int offset;

			//Insert Node into used list
			freeList.splitMayDelete(size, algo);
			offset = freeList.getThatOffset();
			usedList.insert(size, offset);


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
					freeList.splitMayDelete(size, algo);
					offset = freeList.getThatOffset();
					usedList.insert(size, offset);
					
			}
			usedList.sortIt();
			
			
			return offset;
	}



		
	

	@Override
	public void free(int addr) {


		
		Iterator<MyLinkedList.Block> iterator = usedList.iterator();
		MyLinkedList.Block temp = iterator.next();
		int usedOffset = 0;

		if (temp.offset == addr)
		{
			usedOffset = temp.offset;
		}
		else
		{
			while (iterator.hasNext())
			{
				temp = iterator.next();
				if (temp.offset == addr)
				{
					usedOffset = temp.offset;
					break;
				}
			}
		}

		if (usedOffset != addr)
		{
			System.err.println("No such offset");
		}
		else if (algo.contentEquals("BF"))
		{
			freeList.insertMayCompact(temp);
			usedList.delete(addr);
			freeList.sortItSize();
		}
		else
		{
			freeList.insertMayCompact(temp);
			usedList.delete(addr);
		}
		maxSize = max_size();
		totalSizeAvail = size();

		//insert to freeList
		//delete from usedList
	}

	@Override
    public int size() { // Keeps track of the total memory we're using //sum of avail parts of mem


        Iterator<MyLinkedList.Block> iterator = freeList.iterator();

        MyLinkedList.Block firstBlock = iterator.next(); 
        int availSize;

        if(firstBlock == null)
        {
            availSize = 0;
        }
        else
        {
            availSize = firstBlock.size;
        }

        while(iterator.hasNext())
        {
            MyLinkedList.Block next = iterator.next();
            availSize += next.size;
        }

        return availSize;



//        totalSizeAvail = freeList.getThatSizeRemaining();
//    //    System.out.println("TotalSizeAvail: " + totalSizeAvail);
//
//
//        return totalSizeAvail;
    }


@Override
public int max_size() {


	 Iterator<MyLinkedList.Block> iterator = freeList.iterator();
	 MyLinkedList.Block maxSizeBlock = iterator.next(); 

	 int maxSize;

	 if(maxSizeBlock == null)
		{
			maxSize = 0;
		}
		else
		{
			maxSize = maxSizeBlock.size;
		}


	 while(iterator.hasNext())
	 {
		 MyLinkedList.Block temp = iterator.next(); 

		 if(temp.size > maxSizeBlock.size)
		 {
			 maxSize = temp.size;
		 }


	 }

	 return maxSize;
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
		System.out.print("Max Size: " + max_size() + " ");
		System.out.println("Total Size: " + size());

	}
	
}

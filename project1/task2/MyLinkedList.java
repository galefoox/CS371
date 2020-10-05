import java.util.Iterator;
import java.util.Comparator;


class MyLinkedList implements Iterable{	
//make private and use getters and setters 
	 private Node head; 
	 private Node tail;
	
			//data used inside of Node
			private class Block {
				private int size;
				private int offset; 
				
				public Block(int size, int offset) { //constructor 
					this.size = size;
					this.offset = offset;
				}
				
				public Block(int size) { //constructor 
					this.size = size;
				}
				
				public String toString()
				{
					
					return "Offset: " + offset + " Size: " + size; 
				}
				public int getSize()
				{
					return size;
				}



				
				
			}
			
			private class Node {
				private Block data; // has offset and size
				private Node next; //points to next node
				
				public Node(Block data, Node next) //constructor
				{
					this.data = data;
					this.next = next;
				}
				
				public Node(Block data) //constructor
				{
					this.data = data;
				}
				
				public String toString()
				{
					
					return "Offset: " + data.offset + " Size: " + data.size; 
				}

				
				
			}

	
	 
	
	//CHANGE BACK TO ONE T
	public MyLinkedList() //constructor 
	{
		head = null;
		tail = null;
	}

	
	// return Head 
	public Node getHead() 
	{ 
		return head; 
	} 
			
	// return Tail 
	public Node getTail() 
	{ 
		return tail; 
	}
	
	public void firstBlock(int num) 
	{
		Block tempBlock = new Block(num, 1); //13
		head = new Node(tempBlock);
		tail = head;

	}
	
	public int getThatOffsetRemaining()
	{
		return tail.data.offset;
		
	}
	
	public int getThatOffsetInitial()
	{
		return head.data.offset;
		
	}
	
	
	public int getThatSizeInitial ()//Node nodeOff)
	{
		//return nodeOff.data.size;
		return head.data.size;
	}
	
	public int getThatSizeRemaining()
	{
		return tail.data.size;
	}
	
	public void setFirstFreeNode(int theOff, int theSize)
	{
		Node offNode = head;
		offNode.data.offset = theOff;
		offNode.data.size = theSize;

		// We are changing the offset and the size 
		// base off of what we put into the parameters

	}

	public Block getUsedListMatch(int addr)
	{
		Node usedListTemp = head;
		Block dataSub = usedListTemp.data;
		while (usedListTemp != null)
		{
			if (usedListTemp.data.offset == addr)
			{
				dataSub = usedListTemp.data;
				break;
			}
			else
			{
				usedListTemp = usedListTemp.next;
			}

		}
		return dataSub;

	}

	
//	public void traverse(Node tempr)
//	{
//		tempr = tempr.next;
//		System.out.println(tempr);
//	}
	

		
	
	
	public void insert(int num, int off){
		

		Block newData = new Block(num); //create new block
		newData.offset = off; // set offset
	    Node newNode = new Node(newData); //now create the new node

		if (head == null)
		{
			head = newNode;
			tail = newNode;
		}
		else
		{
			tail.next = newNode;
			tail = tail.next;
		}
		sortIt();
}
	
	public void sortIt()
	{

		
		Node index = head;
		Node current = head;
		Block temp;
		while (index != null)
		{

			while (current.next != null)
			{
				if (current.data.offset > current.next.data.offset)
				{
					temp = current.data;
					current.data = current.next.data;
					current.next.data = temp;
						
				}
				current = current.next;
			
			}
			// if (index.data.size == 0)
			// {
			// 	delete(index.data.offset);
			// }

			index = index.next;
			
		}

	}
	public void splitMayDelete(Block num)
	{
		Iterator freeListTemp = new iterator();
		Comparator compare = new Comparator<Block>();
	

		while (freeListTemp.hasNext())
		{
			if (compare.compare(num, freeListTemp.next()) <= 0)
			{
	
			}

		}
	}

	public void delete(int addy)
	{
		Node traverse = head;
		Node temp = head;
	

		if (head.data.offset == addy)
		{
			head = head.next;
		}
		else
		{
			while (temp != null)
			{
			
				if (traverse.next.data.offset == addy)	// If it equals the offset then delete
				{
					System.out.println(traverse.data.offset);
					traverse.next = traverse.next.next;
				}
	
				else
				{
					
					traverse = traverse.next;
				}
				temp = temp.next;
			
	
			}
		}
	}

	public void insertMayCompact(Block temp)
	{
		
		// int size = 0;
		

		// while (temp != null)
		// {
		// 	if (temp.data.offset == free)
		// 	{
		// 		size = temp.data.size;
				insert(temp.size, temp.offset);
			//	mayMerge(temp);
		// 		break;
		// 	}
		// 	temp = temp.next;
		// }
		sortIt();
	}
	public void mayMerge(Node node)
	{
		if (node.data.offset + node.data.size == node.next.data.offset)
		{
			node.data.size = node.data.size + node.next.data.size;
			node.next = node.next.next;
		}
	}


	public class ByOffset implements Comparator<Block>
	{
		@Override
		public int compare(Block lhs, Block rhs)
		{
			return Integer.compare(lhs.offset, rhs.offset);
		}
	}
		



	@Override
	public Iterator<Block> iterator() {
		return new Iterator<Block>(){

			Node current = head;
			Block data;
			@Override
			public boolean hasNext() {
				return current!= null;
			}

			@Override 
			public Block next() {
				if(hasNext()) {
	
					data = current.data;
					current = current.next;
					
					
					return data;
				}

				return null;
			}
			


	};

}


}

				

		
		













		
		//have a linked list ? maybe ? yeah?
		//from there go to and start implementing functions
		//test files - calls mymemalloc mylinkedlist
		//so in alloc function - should be implementing linkedlist
		//alloc(int size)
		//free(address) which i think is offset - offset vs address? maybe offset and address = same
		/* so insert may consolidate i wrote in my notes - "when you free a block and need to
		 * insert node, you need to merge them with address - i think this is referring to when say a block on
		 * the right is free and then you free the block next to it, we want to merge them and make one big block
		 * 
		 * also - she said when you do insert, you are not allowed to delete the two nodes and add 1 thats like a combo of both
		 * 
		 * her class byOffset is just a sorter
		 */

	
	

		
		
		
		
		
	

	



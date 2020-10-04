import java.util.Iterator;



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
		
}
	
	public void sortIt()
	{

		
		Node index = head;
		Node current = head;
		Block temp;
		while (index.next != null)
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
			index = index.next;
			
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
			
//			public Node traverse()
//			{
//				current = head;
//				current = head.next;
//				
//				return current;
//			}


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

	
	

		
		
		
		
		
	

	



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
				
				//setters
				public void setSize(int size)
				{
					this.size = size;
				}
				
				public void setOffset(int offset)
				{
					this.offset = offset;
				}
				
				//getters
				public int getSize() 
				{
					return size;
				}
				
				public int getOffset()
				{
					return offset;
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
				
				
				public void setNext(Node current, Node next)
				{
					current.next = next;
				
				}
				
				public Node getNextNode()
				{
					return next;
				}
				
				public Block getBlock()
				{
					return data;
				}
				public void setBlock(Block data)
				{
					this.data = data;
				}

				
				//set block?
				
				
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
		Block tempBlock = new Block(num, 1);
		head = new Node(tempBlock);

	}
	public int getThatOffset(Node freeOff)
	{
		return freeOff.getBlock().getOffset();
		
	}
	public void setFreeNode(int theOff, int theSize)
	{
		Node offNode = head;
		offNode.getBlock().setOffset(theOff);
		offNode.getBlock().setSize(theSize);

		// We are changing the offset and the size 
		// base off of what we put into the parameters

	}
	public int getThatSize (Node nodeOff)
	{
		return nodeOff.getBlock().getSize();
	}

		
	
	
	public void insert(int num, int off){
		

		Block newData = new Block(num); //create new block
		newData.setOffset(off); // set offset
	    Node newNode = new Node(newData); //now create the new node

		if (head == null)
		{
			head = newNode;
			tail = newNode;
		}
		else
		{
			tail.setNext(tail, newNode);
			tail = tail.getNextNode();
		}
		
}
	
	public Node sortIt()
	{

		
		Node index = head;
		Node current = head;
		Block temp;
		while (index.getNextNode() != null)
		{
			while (current.getNextNode() != null)
			{
				if (current.getBlock().getOffset() > current.getNextNode().getBlock().getOffset())
				{
					temp = current.getBlock();
					current.setBlock(current.getNextNode().getBlock());
					current.getNextNode().setBlock(temp);
						
				}
				current = current.getNextNode();
			//	current.setNext(current, current.getNextNode());	WHY NO WORK
			}
			index = index.getNextNode();
			
		}


		return head;
	}
		



	@Override
	public Iterator<Block> iterator() {
		return new Iterator<Block>(){

			Node current = head;

			@Override
			public boolean hasNext() {
				return current!= null;
			}

			@Override 
			public Block next() {
				if(hasNext()) {

					Block data = current.getBlock();
					
					System.out.print(current.data.offset + " ");
					System.out.println(current.data.size);
					current = current.getNextNode();
					
					return data;
				}

				return null;
			}

			// public String toString() {

			// 	String block_offset = "Offset: " + current.getBlock().getOffset();

			// 	return block_offset;
			// }



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

	
	

		
		
		
		
		
	

	



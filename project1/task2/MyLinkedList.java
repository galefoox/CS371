import java.util.Iterator;
//import java.util.Comparator;

class MyLinkedList implements Iterable {
	// make private and use getters and setters
	private Node head;
	private Node tail;
	private Node nfNode;
	private int thatOffset;

	// data used inside of Node
	public class Block {
		public int size;
		public int offset;

		public Block(int size, int offset) { // constructor
			this.size = size;
			this.offset = offset;
		}

		public Block(int size) { // constructor
			this.size = size;
		}

		public String toString() {

			return "Offset: " + offset + " Size: " + size;
		}

		public int getSize() {
			return size;
		}

	}

	private class Node {
		private Block data; // has offset and size
		private Node next; // points to next node

		public Node(Block data, Node next) // constructor
		{
			this.data = data;
			this.next = next;
		}

		public Node(Block data) // constructor
		{
			this.data = data;
		}

		public String toString() {

			return "Offset: " + data.offset + " Size: " + data.size;
		}

	}

	public boolean isEmpty() {
		return head == null;
	}

	// return Head
	public Node thatNext() {
		return head.next;
	}

	// return Tail
	public Node getTail() {
		return tail;
	}

	public void firstBlock(int num) {
		Block tempBlock = new Block(num, 1); // 13
		head = new Node(tempBlock);
		tail = head;

	}

	public int getThatOffsetRemaining() {
		return tail.data.offset;

	}

	public int getThatOffset() {
		return thatOffset;

	}

	public int getThatSizeInitial()// Node nodeOff)
	{
		// return nodeOff.data.size;
		return head.data.size;
	}

	public int getThatSizeRemaining() {
		Node usedHead = head;
		int sumOfUsedSize = 0;
		while (usedHead != null) {
			sumOfUsedSize = sumOfUsedSize + usedHead.data.size;
			usedHead = usedHead.next;
		}
		return sumOfUsedSize;
	}

	public void setFirstFreeNode(int theOff, int theSize) {
		Node offNode = head;
		offNode.data.offset = theOff;
		offNode.data.size = theSize;

		// We are changing the offset and the size
		// base off of what we put into the parameters

	}

	// Checks for any of the usedList that matches the address
	public Block getUsedListMatch(int addr) {
		Node usedListTemp = head;
		Block dataSub = usedListTemp.data;
		while (usedListTemp != null) {
			if (usedListTemp.data.offset == addr) {
				dataSub = usedListTemp.data;
				break;
			} else {
				usedListTemp = usedListTemp.next;
			}

		}
		return dataSub;

	}

	public int checkUsedOffset(int addr) {
		Node usedListHead = head;
		int usedOffset = 0;
		while (usedListHead != null) {
			if (usedListHead.data.offset == addr) {
				usedOffset = usedListHead.data.offset;
			}
			usedListHead = usedListHead.next;
		}
		return usedOffset;
	}

	public void removeEmptyBlock() {
		Node temp = head;
		while (temp != null) {
			if (temp.data.size == 0) {
				delete(temp.data.offset);
			}
			temp = temp.next;
		}
	}

	public int thatNF() {
		if (nfNode.next == null) {
			nfNode = head;
			return nfNode.data.offset;
		} else {
			return nfNode.next.data.offset;
		}

	}

	public int getThatOffsetInitial() {
		return head.data.offset;
	}

	public void insert(int num, int off) {

		Block newData = new Block(num); // create new block
		newData.offset = off; // set offset
		Node newNode = new Node(newData); // now create the new node
		Node temp = head;

		if (temp == null) {
			head = newNode;
			tail = newNode;
		} else {
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = newNode;
			tail = temp.next;

		}

	}

	public int getMaxSize() {
		Node temp = head;

		int max = head.data.size;
		while (temp.next != null) {
			if (max < temp.next.data.size) {
				max = temp.next.data.size;

			}

			temp = temp.next;
		}
		return max;
	}

	public void sortIt() {
		// Credits:
		// https://www.javatpoint.com/program-to-sort-the-elements-of-the-singly-linked-list
		// Node current will point to head
		Node current = head, index = null;
		Block temp;

		while (current != null) {
			// Node index will point to node next to current
			index = current.next;

			while (index != null) {
				// If current node's data is greater than index's node data, swap the data
				// between them
				if (current.data.offset > current.next.data.offset) {
					temp = current.data;
					current.data = index.data;
					index.data = temp;
				}
				index = index.next;
			}
			current = current.next;
		}
	}

	public void sortUsedList() {
		// Credits:
		// https://www.javatpoint.com/program-to-sort-the-elements-of-the-singly-linked-list
		// Node current will point to head
		Node current = head, index = null;
		Block temp;

		while (current != null) {
			// Node index will point to node next to current
			index = current.next;

			while (index != null) {
				// If current node's data is greater than index's node data, swap the data
				// between them
				if (current.data.offset > current.next.data.offset) {
					temp = current.data;
					current.data = index.data;
					index.data = temp;
					sortUsedList();
				}
				index = index.next;
			}
			current = current.next;
		}
	}

	public void sortItSize() {

		Node index = head;
		Node current = head;
		Block temp;
		while (index != null) {

			while (current.next != null) {
				if (current.data.size > current.next.data.size) {
					temp = current.data;
					current.data = current.next.data;
					current.next.data = temp;
					sortItSize();
				}

				current = current.next;

			}

			index = index.next;

		}

	}

	public void splitMayDelete(int size, String algo) {
		Node temp = head;

		// CHeck if FreeList has enough size
		if (algo.contentEquals("NF")) {
			temp = nfNode; // SETS WHERE WE LEFT OFF AT
			if (temp == null || temp.next == null) {
				temp = head;
			}

		}
		while (temp != null) {

			if (temp.data.size >= size) {
				thatOffset = temp.data.offset;
				nfNode = temp.next;
				temp.data.size = temp.data.size - size;
				temp.data.offset = temp.data.offset + size;
				break;
			}

			temp = temp.next;

		}
		removeEmptyBlock();
	}

	public void delete(int addy) {
		Node traverse = head;
		Node temp = head;

		if (head.data.offset == addy) {
			head = head.next;
		} else {
			while (temp != null) {

				if (traverse.next.data.offset == addy) // If it equals the offset then delete
				{

					traverse.next = traverse.next.next;
				}

				else {

					traverse = traverse.next;
				}

				temp = temp.next;

			}
		}
	}

	public void insertMayCompact(Block temp) {
		/*
		 * my idea is this - hold a temp=sum of the firstNodesOffse + firstNodesSize and
		 * compare this temp to the offset of every other node in the list if the this
		 * temp=sum matches, then we compact
		 * 
		 * call itself so that it can return to the beginning each time to check
		 * 
		 */

		insert(temp.size, temp.offset);
		mayMerge();

	}

	public void mayMerge() {
		Node currentNode = head;
		Node temp = currentNode;
		while (currentNode != null) {

			while (temp != null) {
				// If the added offset + size = ANY of the offset values in the freeList, then
				// combine
				// MUST FIX THIS
				if (currentNode.data.offset + currentNode.data.size == temp.data.offset) {
					currentNode.data.size = currentNode.data.size + temp.data.size;
					delete(temp.data.offset);
					mayMerge();
				} else if (temp.data.offset + temp.data.size == currentNode.data.offset) {
					temp.data.size = temp.data.size + currentNode.data.size;
					delete(currentNode.data.offset);
					mayMerge();
				}
				temp = temp.next;
			}
			temp = currentNode.next;
			currentNode = currentNode.next;
		}

	}

	@Override
	public Iterator<Block> iterator() {
		return new Iterator<Block>() {

			Node current = head;
			Block data;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public Block next() {
				if (hasNext()) {

					data = current.data;
					current = current.next;

					return data;
				}

				return null;
			}

		};

	}

}

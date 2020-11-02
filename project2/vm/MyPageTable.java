import java.util.*;
import java.util.LinkedList;
import java.util.Objects;

public class MyPageTable {

    class PageTableEntry {
        int vpn;
        int pfn;
        boolean dirtyBit;

        public PageTableEntry(int vpn, int pfn, boolean dirtyBit) {
            this.vpn = vpn;
            this.pfn = pfn;
            this.dirtyBit = dirtyBit;
        }

        public void setPFN(int pfn) {
            this.pfn = pfn;
        }

        public int getPFN() {
            return pfn;
        }

        public void setVPN(int vpn) {
            this.vpn = vpn;
        }

        public int getVPN() {
            return vpn;
        }

        public String toString() {

            return "VPN " + vpn + " PFN " + pfn + " DirtyBit " + dirtyBit;
        }

        public void setAll(int vpn, int pfn, boolean dirtyBit) {
            this.vpn = vpn;
            this.pfn = pfn;
            this.dirtyBit = dirtyBit;
        }

    }

    private static int INITIAL_SIZE = 10;
    // Correct way of
    LinkedList<PageTableEntry> temp[] = new LinkedList[INITIAL_SIZE];

    public MyPageTable() {

        for (int i = 0; i < INITIAL_SIZE; i++) {

            temp[i] = new LinkedList<PageTableEntry>();
            for (int j = 0; j < 4; j++) {
                PageTableEntry boop = new PageTableEntry(j, j, true);
                temp[i].add(boop);

            }
        }

    }

    public int containsVPN(int address) {

        int index = address % INITIAL_SIZE;
        int vpn = address / INITIAL_SIZE;

        // Check for the matching Index

        // int theSpot = temp[index]..indexOf(vpn); // Gets the index of where the
        // correct VPN is
        // PageTableEntry tempBoop; // Makes a temporary Node
        // tempBoop = temp[index].get(theSpot); // Returns the node at the correct index
        // to tempBoop
        // return tempBoop.getPFN();
        // ITERATES THROUGH THE WHOLE LIST
        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempBoop = temp[index].getFirst(); // Sets the head to traverse

        iter = temp[index].listIterator(); // Sets iter to the temp to iterate
        int count = 0;
        while (iter.hasNext()) {
            tempBoop = temp[index].get(count);
            if (tempBoop.getVPN() == vpn) { // If VPN matches then find the PFN
                return tempBoop.getPFN(); // Return PFN
            } else {
                count++;
                iter.next(); // Traverse if not
            }
        }
        return 100;

    }

    public void printAll() {
        System.out.println(temp.length); // Length of the array
        System.out.println(temp[1].size()); // # of nodes inside each index

        // ITERATES THROUGH THE WHOLE LIST
        // ListIterator<PageTableEntry> iter = null;
        // for (int i = 0; i < table.length; i++) {
        // }
        // iter = entries.listIterator();
        // while (iter.hasNext()) {
        // System.out.println(iter.next());
        // }

        System.out.println("Table Entry 1: " + temp[1].toString());
        System.out.println("Table Entry 8: " + temp[8].toString());

    }

}

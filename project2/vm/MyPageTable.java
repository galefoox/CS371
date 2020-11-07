import java.util.*;
import java.util.LinkedList;
import java.util.Objects;

public class MyPageTable {

    private static int INITIAL_SIZE = 256;
    // Correct way of
    LinkedList<PageTableEntry> temp[] = new LinkedList[INITIAL_SIZE];
    LinkedList<PageTableEntry> dirtyBits = new LinkedList<PageTableEntry>();
    int index;

    static class PageTableEntry {
        int vpn;
        int pfn;
        boolean dirtyBit;

        public PageTableEntry(int vpn, int pfn, boolean dirtyBit) {
            this.vpn = vpn;
            this.pfn = pfn;
            this.dirtyBit = dirtyBit;
        }

        public int getVpn() {
            return vpn;
        }

        public int getPfn() {
            return pfn;
        }

    }

    public MyPageTable() {

        for (int i = 0; i < INITIAL_SIZE; i++) {

            temp[i] = new LinkedList<PageTableEntry>();

        }
    }

    public void addEntry(int vpn, PageTableEntry entry) {
        index = vpn % INITIAL_SIZE;
        temp[index].add(entry);
        dirtyBits.add(entry);

    }

    public int containsVPN(int vpn) {

        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempBoop; // Sets the head to traverse
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator(); // Sets iter to the temp to iterate
        while (iter.hasNext()) {
            tempBoop = temp[index].get(count);
            if (tempBoop.vpn == vpn) { // If VPN matches then find the PFN
                return tempBoop.pfn; // Return PFN
            } else {
                count++;
                iter.next(); // Traverse if not
            }
        }
        return -1;

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

    protected LinkedList<PageTableEntry> returnDirtyList() {
        return dirtyBits;
    }

    protected void resetDirtyList() {
        dirtyBits.clear();
    }

}

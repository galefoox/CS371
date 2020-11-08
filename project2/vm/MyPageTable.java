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

    }

    public void addDirtyEntry(PageTableEntry entry) {
        dirtyBits.add(entry);
    }

    public void removeEntry(PageTableEntry entry) {
        dirtyBits.remove(entry);
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

    public int getVPN(int PFN, int vpn) {
        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempboop1;
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator();
        while (iter.hasNext()) {
            tempboop1 = temp[index].get(count);
            if (tempboop1.pfn == PFN) {
                return tempboop1.vpn;
            } else {
                count++;
                iter.next();
            }
        }
        return -1;
    }

    protected LinkedList<PageTableEntry> returnDirtyList() {
        return dirtyBits;
    }

    protected void resetDirtyList() {
        dirtyBits.clear();
    }

    protected void resetDirtyBits(int vpn) {
        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempBoop3; // Sets the head to traverse
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator(); // Sets iter to the temp to iterate
        while (iter.hasNext()) {
            tempBoop3 = temp[index].get(count);
            if (tempBoop3.vpn == vpn) { // If VPN matches then find the PFN
                tempBoop3.dirtyBit = false;
                iter.next(); // Return PFN
            } else {
                count++;
                iter.next(); // Traverse if not
            }
        }
    }

}

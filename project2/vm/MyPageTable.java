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

    public void removeHead(int vpn) {
        index = vpn % INITIAL_SIZE;
        temp[index].remove();
    }

    public int matchingPFN(int vpn) {

        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempEntry; // Sets the head to traverse
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator(); // Sets iter to the temp to iterate
        while (iter.hasNext()) {
            tempEntry = temp[index].get(count);
            if (tempEntry.vpn == vpn) { // If VPN matches then find the PFN
                return tempEntry.pfn; // Return PFN
            } else {
                count++;
                iter.next(); // Traverse if not
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
        PageTableEntry tempEntry; // Sets the head to traverse
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator(); // Sets iter to the temp to iterate
        while (iter.hasNext()) {
            tempEntry = temp[index].get(count);
            if (tempEntry.vpn == vpn) { // If VPN matches then find the PFN
                tempEntry.dirtyBit = false;
                iter.next(); // Return PFN
            } else {
                count++;
                iter.next(); // Traverse if not
            }
        }
    }

    public boolean checkForVPN(int vpn) {
        ListIterator<PageTableEntry> iter = null;
        PageTableEntry tempEntry;
        int count = 0;
        index = vpn % INITIAL_SIZE;
        iter = temp[index].listIterator();
        while (iter.hasNext()) {
            tempEntry = temp[index].get(count);
            if (tempEntry.vpn == vpn) {
                return true;
            } else {
                count++;
                iter.next();
            }
        }
        return false;
    }

}

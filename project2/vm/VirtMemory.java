import Storage.PhyMemory;
import java.util.*;

public class VirtMemory extends Memory {
    // INSIDE HERE IS WHERE WE WILL EXTRACT VPN AND THEN PASS VPN INTO MYPAGETABLE

    int virtSize;

    static class EvictionStatus {
        int evictPFN;
        boolean dirtyBit;

        public EvictionStatus(int evictPFN, boolean dirtyBit) {
            this.evictPFN = evictPFN;
            this.dirtyBit = dirtyBit;
        }
    }

    public VirtMemory() {

        virtSize = 1024 * 64;
    }

    public VirtMemory(int specifiedSize) {
        this.virtSize = specifiedSize;

    }

    PhyMemory theRam = new PhyMemory();
    private int getThatPFN;
    private int pAddy;
    private int maxFrames;
    MyPageTable hashTable = new MyPageTable();
    EvictionStatus evictTemp;
    int writeBackCounter = 0;

    protected PhyMemory getPhyMemory() {
        theRam = super.ram;
        return theRam;
    }

    @Override
    public void write(int address, byte value) {

        int index = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault
        maxFrames = theRam.num_frames();
        if (address >= maxFrames) {
            System.err.print("Out of Bounds");
        }

        else {

            Policy policyPFN = new Policy(maxFrames);
            evictTemp = policyPFN.advise();

            // PUT INSIDE PTE
            MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                    evictTemp.dirtyBit);
            hashTable.addEntry(index, oof);

            // load from PhyMemory
            // call Policy to get PFN
            pAddy = evictTemp.evictPFN * 64 + index;
            theRam.load(vpn, pAddy); // blockNum = VPN, startAddr = PFN * 64 or table_Size
            if (writeBackCounter == 32) {
                write_back();
            }
            theRam.write(pAddy, value); // Write to PhyMemory
            theRam.store(vpn, pAddy); // Store to disk
            writeBackCounter++;

        }

    }

    @Override
    public byte read(int virtAddy) {

        int index = virtAddy % 64; // HashCode
        int vpn = virtAddy / 64;
        int hashTablePFN = -1;

        if (virtAddy > virtSize) {

            System.err.println("Out of Bounds");
        }

        else {

            hashTablePFN = hashTable.containsVPN(vpn, index);

            if (hashTablePFN == -1) {

                System.err.println("Page Fault"); // Cache Miss

                // Find a PFN to use
                maxFrames = theRam.num_frames();
                Policy policyPFN = new Policy(maxFrames);
                evictTemp = policyPFN.advise();

                // Put inside PTE
                // MyPageTable.PageTableEntry tableEntry = new MyPageTable.PageTableEntry(vpn,
                // evictTemp.evictPFN,
                // evictTemp.dirtyBit);
                // hashTable.addEntry(index, tableEntry);

                // Reload PhyMemory and adjust
                if (evictTemp.evictPFN > 255 || evictTemp.evictPFN < 0) {

                    System.err.println("No such PFN");

                }
                pAddy = getThatPFN * 64 + index;
                theRam.load(vpn, pAddy);
                hashTablePFN = hashTable.containsVPN(vpn, index);
                return theRam.read(hashTablePFN);

            }

        }

        return 0;
    }

    @Override
    void write_back() {
        int writeBackPFN;
        int writeBackVPN;
        LinkedList<MyPageTable.PageTableEntry> tempDirtyList = new LinkedList<MyPageTable.PageTableEntry>();
        tempDirtyList = hashTable.returnDirtyList();

        ListIterator<MyPageTable.PageTableEntry> iter = null;
        iter = tempDirtyList.listIterator();
        int countIndex = 0;
        while (iter.hasNext()) {
            writeBackVPN = tempDirtyList.get(countIndex).getVpn();
            writeBackPFN = tempDirtyList.get(countIndex).getPFN();

        }

    }

}
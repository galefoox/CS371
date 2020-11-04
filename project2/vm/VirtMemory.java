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
    Policy policyPFN = new Policy(theRam.num_frames());
    private int getThatPFN;
    private int pAddy;
    MyPageTable hashTable = new MyPageTable();
    EvictionStatus evictTemp;
    int writeBackCounter = 0;

    protected PhyMemory getPhyMemory() {
        theRam = super.ram;
        return theRam;
    }

    @Override
    public void write(int address, byte value) {

        int offset = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault
        // maxFrames = theRam.num_frames();
        if (address >= theRam.num_frames()) {
            System.err.print("Out of Bounds");
        }

        else {

            evictTemp = policyPFN.advise();

            // PUT INSIDE PTE
            MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                    evictTemp.dirtyBit);
            hashTable.addEntry(vpn, oof);

            // load from PhyMemory
            // call Policy to get PFN
            pAddy = evictTemp.evictPFN * 64 + offset;
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

        int offset = virtAddy % 64; // HashCode
        int vpn = virtAddy / 64;
        int hashTablePFN = -1;

        if (virtAddy > virtSize) {

            System.err.println("Out of Bounds");
        }

        else {

            hashTablePFN = hashTable.containsVPN(vpn);

            if (hashTablePFN == -1) {

                System.err.println("Page Fault"); // Cache Miss

                // Find a PFN to use

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
                pAddy = getThatPFN * 64 + offset;
                theRam.load(vpn, pAddy);
                hashTablePFN = hashTable.containsVPN(vpn);
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

        // MAKE SURE WHEN WE CALL THIS WE RESET WRITEBACKCOUNTER = 0 AND WE NEED TO MAKE
        // A METHOD INSIDE OF PAGE TABLE TO CLEAR OUT DIRTYBIT LIST

        ListIterator<MyPageTable.PageTableEntry> iter = null;
        iter = tempDirtyList.listIterator();
        int countIndex = 0;
        while (iter.hasNext()) {
            writeBackVPN = tempDirtyList.get(countIndex).getVpn();
            writeBackPFN = tempDirtyList.get(countIndex).getPFN();

        }
        writeBackCounter = 0;
        hashTable.resetDirtyList();

    }

}
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
    MyPageTable hashTable = new MyPageTable();
    private int physAddr;
    private int startAddr;
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
            physAddr = evictTemp.evictPFN * 64 + offset;
            startAddr = evictTemp.evictPFN * 64; // no offset

            if (writeBackCounter == 32) {
                write_back();
            }

            theRam.load(vpn, startAddr); // blockNum = VPN, startAddr = PFN * 64 or table_Size
            theRam.write(physAddr, value); // Write to PhyMemory
            theRam.store(vpn, startAddr); // Store to disk
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
                    // NOTE: FIX THIS IF STATEMENT BC IDK IF PFN ...
                    System.err.println("No such PFN");

                }
                // physAddr = getThatPFN * 64 + offset;
                startAddr = evictTemp.evictPFN * 64;
                theRam.load(vpn, startAddr);
                hashTablePFN = hashTable.containsVPN(vpn);
                return theRam.read(hashTablePFN);

            }

        }

        return -1;
    }

    @Override
    void write_back() {
        int writeBackPFN;
        int writeBackVPN;
        LinkedList<MyPageTable.PageTableEntry> tempDirtyList = new LinkedList<MyPageTable.PageTableEntry>();
        tempDirtyList = hashTable.returnDirtyList();
        int countIndex = 0;
        startAddr = tempDirtyList.get(countIndex).getPfn() * 64;

        // MAKE SURE WHEN WE CALL THIS WE RESET WRITEBACKCOUNTER = 0 AND WE NEED TO MAKE
        // A METHOD INSIDE OF PAGE TABLE TO CLEAR OUT DIRTYBIT LIST

        ListIterator<MyPageTable.PageTableEntry> iter = null;
        iter = tempDirtyList.listIterator();

        theRam.load(tempDirtyList.get(countIndex).getVpn(), startAddr); // blockNum = VPN, startAddr = PFN * 64 or
                                                                        // table_Size
        while (iter.hasNext()) {
            // writeBackVPN = tempDirtyList.get(countIndex).getVpn();
            // writeBackPFN = tempDirtyList.get(countIndex).getPFN();

            // Store to disk
            // ram.read? get byte?
            // ram.write?

        }
        theRam.store(tempDirtyList.get(countIndex).getVpn(), startAddr);
        writeBackCounter = 0;
        hashTable.resetDirtyList();

    }

}
import Storage.PhyMemory;
import java.util.*;

public class VirtMemory extends Memory {
    // INSIDE HERE IS WHERE WE WILL EXTRACT VPN AND THEN PASS VPN INTO MYPAGETABLE

    int virtSize;

    static class EvictionStatus {
        int evictPFN;
        boolean evictStatus;

        public EvictionStatus(int evictPFN, boolean evictStatus) {
            this.evictPFN = evictPFN;
            this.evictStatus = evictStatus;
        }
    }

    public VirtMemory() {

        virtSize = 1024 * 64;
        super.ram = theRam;
    }

    public VirtMemory(int specifiedSize) {
        this.virtSize = specifiedSize;

    }

    PhyMemory theRam = new PhyMemory();
    Policy policyPFN = new Policy(theRam.num_frames());
    MyPageTable hashTable = new MyPageTable();
    LinkedList<MyPageTable.PageTableEntry> tempDirtyList = new LinkedList<MyPageTable.PageTableEntry>();
    private int physAddr;
    private int startAddr;
    EvictionStatus evictTemp;
    int writeBackCounter = 0;
    int offset;
    int blockNum = -1;
    int currentBlock;
    int blockAddy;
    int matchingVPN;
    LinkedList<MyPageTable.PageTableEntry> dirtyList = new LinkedList<MyPageTable.PageTableEntry>();

    @Override
    public void write(int address, byte value) {

        offset = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault

        if (address >= 64 * 1024) {
            System.err.print("Out of Bounds");
        }

        else {
            evictTemp = policyPFN.advise(vpn);
            MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN, true);
            physAddr = evictTemp.evictPFN * 64 + offset;
            if (evictTemp.evictStatus == true) { // This is for eviction
                startAddr = policyPFN.getCurrentPFN() * 64;
                matchingVPN = hashTable.getVPN(evictTemp.evictPFN, vpn);
                theRam.store(matchingVPN, startAddr);
                hashTable.removeEntry(oof);
            }

            // PUT INSIDE PTE
            hashTable.addEntry(vpn, oof);
            hashTable.addDirtyEntry(oof);

            if (vpn == blockNum) { // Write if it's to the same block

                theRam.write(physAddr, value);

            } else { // Load another block and write to that block
                theRam.load(vpn, startAddr);

                theRam.write(physAddr, value);
            }

            // table_Size
            // Write to PhyMemory
            blockNum = vpn;
            writeBackCounter++;
            if (writeBackCounter == 32) {
                write_back();
            }

        }

    }

    @Override
    public byte read(int virtAddy) {

        int offset = virtAddy % 64; // HashCode
        int vpn = virtAddy / 64;
        int hashTablePFN;

        if (virtAddy > virtSize) {

            System.err.println("Out of Bounds");
        }

        else {

            hashTablePFN = hashTable.containsVPN(vpn);
            startAddr = evictTemp.evictPFN * 64;
            if (hashTablePFN == -1) {

                System.err.println("Page Fault"); // Cache Miss

                // Find a PFN to use

                evictTemp = policyPFN.advise(vpn);
                MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN, true);
                if (evictTemp.evictStatus == true) { // This is for eviction
                    startAddr = policyPFN.getCurrentPFN() * 64;
                    matchingVPN = hashTable.getVPN(evictTemp.evictPFN, vpn);
                    theRam.store(matchingVPN, startAddr);
                    hashTable.removeEntry(oof);// remove from dirtyList
                }
                hashTable.addEntry(vpn, oof);
                hashTablePFN = hashTable.containsVPN(vpn);
                startAddr = evictTemp.evictPFN * 64;
                theRam.load(vpn, startAddr);
                physAddr = hashTablePFN * 64 + offset;

                // // startAddr = policyPFN.getCurrentPFN() * 64; // Get the Previous PFN
                // theRam.load(vpn, startAddr);
                // physAddr = evictTemp.evictPFN * 64 + offset;
                // startAddr = evictTemp.evictPFN * 64; // no offset

                return theRam.read(physAddr);

            } else {
                startAddr = hashTablePFN * 64;
                theRam.load(vpn, startAddr);
                physAddr = hashTablePFN * 64 + offset;
                return theRam.read(physAddr);
            }

        }

        return -1;
    }

    @Override
    void write_back() {

        int writeBackVPN;
        tempDirtyList = hashTable.returnDirtyList();
        int countIndex = 0;

        if (tempDirtyList.size() != 0) {

            startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
            ListIterator<MyPageTable.PageTableEntry> iter = null;
            iter = tempDirtyList.listIterator();

            currentBlock = tempDirtyList.get(countIndex).getVpn();

            while (iter.hasNext()) {

                writeBackVPN = tempDirtyList.get(countIndex).getVpn();

                if (writeBackVPN != currentBlock) {

                    theRam.store(currentBlock, startAddr);
                    startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
                    currentBlock = writeBackVPN;
                    hashTable.resetDirtyBits(writeBackVPN); // resets all the dirtyBits to false

                }
                countIndex++;
                iter.next();

            }

            theRam.store(currentBlock, startAddr);
            writeBackCounter = 0;
            hashTable.resetDirtyList();
        }

    }

}
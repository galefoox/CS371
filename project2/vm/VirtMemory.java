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

    @Override
    public void write(int address, byte value) {

        offset = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault

        if (address >= virtSize) {
            System.err.print("Out of Bounds");
        }

        else {
            // When use policy advise it will return PFN
            evictTemp = policyPFN.advise(vpn);
            MyPageTable.PageTableEntry entry = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN, true);
            physAddr = evictTemp.evictPFN * 64 + offset;
            if (evictTemp.evictStatus == true) { // This is for eviction from Page Table - Correlates to what is inside
                                                 // PHYMEMORY
                if (blockNum != vpn) {
                    hashTable.removeHead(vpn);

                }

            }

            // Adds to dirtylist for write back
            hashTable.addDirtyEntry(entry);
            if (vpn == blockNum) { // Write if it's to the same block

                theRam.write(physAddr, value);

            } else { // Load another block and write to that block
                startAddr = policyPFN.getCurrentPFN() * 64;
                theRam.load(vpn, startAddr);
                hashTable.addEntry(vpn, entry); // Adds entry to page table
                theRam.write(physAddr, value);
            }

            writeBackCounter++;
            // Keeps count of writes to write back 32 at a time - Fail.Safe
            if (writeBackCounter == 32) {
                write_back();
            }
            blockNum = vpn;
        }

    }

    @Override
    public byte read(int virtAddr) {

        offset = virtAddr % 64; // HashCode
        int vpn = virtAddr / 64;
        int hashTablePFN = hashTable.matchingPFN(vpn);

        if (virtAddr > virtSize) {

            System.err.println("Out of Bounds");
        }

        else {
            // Not in Page Table
            if (!hashTable.checkForVPN(vpn)) {

                System.err.println("Page Fault");

                evictTemp = policyPFN.advise(vpn);
                MyPageTable.PageTableEntry entry = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN, true);
                if (evictTemp.evictStatus == true) { // This is for eviction
                    hashTable.removeHead(vpn);
                    hashTable.addEntry(vpn, entry);
                    if (hashTable.checkForVPN(vpn)) {
                        hashTablePFN = hashTable.matchingPFN(vpn);
                        startAddr = hashTablePFN * 64;
                        theRam.load(vpn, startAddr);
                    }

                    physAddr = hashTablePFN * 64 + offset;

                    return theRam.read(physAddr);

                }

                // If in Page Table
            } else {
                physAddr = hashTablePFN * 64 + offset;
                return theRam.read(physAddr);
            }
        }

        return -1;

    }

    @Override
    void write_back() {

        int writeBackVPN;
        int countIndex = 0;
        tempDirtyList = hashTable.returnDirtyList();

        if (tempDirtyList.size() != 0) {

            startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
            ListIterator<MyPageTable.PageTableEntry> iter = null;
            iter = tempDirtyList.listIterator();

            currentBlock = tempDirtyList.get(countIndex).getVpn();

            // Iterate through Dirty List
            while (iter.hasNext()) {

                writeBackVPN = tempDirtyList.get(countIndex).getVpn();

                // If in different blocks - store, reset dirty bits
                if (writeBackVPN != currentBlock) {

                    theRam.store(currentBlock, startAddr);
                    startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
                    currentBlock = writeBackVPN;

                }
                hashTable.resetDirtyBits(writeBackVPN); // resets all the dirtyBits to false
                countIndex++;
                iter.next();

            }
            // If in same block, store at once - reset counter|Dirty List
            theRam.store(currentBlock, startAddr);
            writeBackCounter = 0;
            hashTable.resetDirtyList();
        }

    }

}
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

        virtSize = 256 * 64;
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
    LinkedList<MyPageTable.PageTableEntry> dirtyList = new LinkedList<MyPageTable.PageTableEntry>();

    @Override
    public void write(int address, byte value) {

        offset = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault

        if (address >= theRam.num_frames() * 64) {
            System.err.print("Out of Bounds");
        }

        else {
            evictTemp = policyPFN.advise(vpn);

            // PUT INSIDE PTE
            MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                    evictTemp.dirtyBit);
            hashTable.addEntry(vpn, oof);

            // load from PhyMemory
            // call Policy to get PFN
            physAddr = evictTemp.evictPFN * 64 + offset;

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

        else { // Has to be an else if statement that will check if the VPN is within that
               // block, else load another block

            hashTablePFN = hashTable.containsVPN(vpn);

            if (hashTablePFN == -1) {

                System.err.println("Page Fault"); // Cache Miss

                // Find a PFN to use

                evictTemp = policyPFN.advise(vpn);

                // Reload PhyMemory and adjust
                if (evictTemp.evictPFN > 255 || evictTemp.evictPFN < 0) {
                    // NOTE: FIX THIS IF STATEMENT BC IDK IF PFN ...
                    System.err.println("Out of Range");

                }

                startAddr = policyPFN.getCurrentPFN() * 64;
                theRam.load(vpn, startAddr);
                physAddr = evictTemp.evictPFN * 64 + offset;
                startAddr = evictTemp.evictPFN * 64; // no offset
                Byte val = theRam.read(physAddr);
                theRam.write(physAddr, val);
                MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                        evictTemp.dirtyBit);
                hashTable.addEntry(vpn, oof);

                // hashTablePFN = hashTable.containsVPN(vpn) * 64 + offset;
                return theRam.read(physAddr);

            } else {
                startAddr = hashTablePFN * 64;
                theRam.load(vpn, startAddr);
                return theRam.read(virtAddy);
            }

        }

        return -1;
    }

    @Override
    void write_back() {
        // int writeBackPFN;
        int writeBackVPN;
        tempDirtyList = hashTable.returnDirtyList();
        int countIndex = 0;

        if (tempDirtyList.size() != 0) {

            startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
            ListIterator<MyPageTable.PageTableEntry> iter = null;
            iter = tempDirtyList.listIterator();

            currentBlock = tempDirtyList.get(countIndex).getPfn();

            while (iter.hasNext()) {

                // writeBackPFN = tempDirtyList.get(countIndex).getPfn();
                writeBackVPN = tempDirtyList.get(countIndex).getVpn();

                if (writeBackVPN != currentBlock) {

                    theRam.store(currentBlock, startAddr);
                    startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
                    currentBlock = writeBackVPN;

                }
                countIndex++;
                iter.next();

            }

            theRam.store(currentBlock, startAddr);
            writeBackCounter = 0;
            hashTable.resetDirtyList();
        }
        // Remember to reset the DirtyBits in the list

    }

}
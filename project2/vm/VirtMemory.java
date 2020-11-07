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
        super.ram = theRam;
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
    int offset;
    int blockNum;
    int currentBlock;
    int blockAddy;

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
            // startAddr = evictTemp.evictPFN * 64; // no offset

            // theRam.load(vpn, startAddr); // blockNum = VPN, startAddr = PFN * 64 or
            // table_Size
            theRam.write(physAddr, value); // Write to PhyMemory
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
                    System.err.println("No such PFN");

                }
                // physAddr = getThatPFN * 64 + offset;
                startAddr = policyPFN.getCurrentPFN() * 64;
                theRam.load(vpn, startAddr);
                physAddr = evictTemp.evictPFN * 64 + offset;
                startAddr = evictTemp.evictPFN * 64; // no offset
                Byte val = theRam.read(physAddr);
                theRam.write(physAddr, val);
                MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                        evictTemp.dirtyBit);
                hashTable.addEntry(vpn, oof);

                hashTablePFN = hashTable.containsVPN(vpn) * 64 + offset;
                return theRam.read(hashTablePFN);

            }
            hashTablePFN = hashTable.containsVPN(vpn) * 64 + offset;
            return theRam.read(hashTablePFN);

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

        if (tempDirtyList.size() != 0) {

            // MAKE SURE WHEN WE CALL THIS WE RESET WRITEBACKCOUNTER = 0 AND WE NEED TO MAKE
            // A METHOD INSIDE OF PAGE TABLE TO CLEAR OUT DIRTYBIT LIST
            startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
            ListIterator<MyPageTable.PageTableEntry> iter = null;
            iter = tempDirtyList.listIterator();

            theRam.load(tempDirtyList.get(countIndex).getVpn(), startAddr); // blockNum =
            // VPN, startAddr = PFN * 64 or
            currentBlock = tempDirtyList.get(countIndex).getPfn(); // table_Size

            while (iter.hasNext()) {

                writeBackPFN = tempDirtyList.get(countIndex).getPfn();
                writeBackVPN = tempDirtyList.get(countIndex).getVpn();
                // offset = (writeBackVPN * 64) % 64;
                // physAddr = writeBackPFN * 64 + offset;
                // } else

                if (writeBackVPN != currentBlock) {

                    theRam.store(currentBlock, startAddr);
                    startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
                    theRam.load(writeBackVPN, startAddr);
                    // Byte temp = theRam.read(physAddr);
                    // theRam.write(physAddr, temp);
                    // currentBlock++; // Uhh double check if it's in the correct block
                    currentBlock = writeBackVPN;
                    // iter.next();

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
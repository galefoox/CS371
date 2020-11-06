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

    @Override
    public void write(int address, byte value) {

        offset = address % 64; // HashCode
        int vpn = address / 64;

        // Consider pagefault
        int phyFrames = theRam.num_frames();
        // maxFrames = theRam.num_frames();
        if (vpn >= theRam.num_frames() * 64) {
            System.err.print("Out of Bounds");
        }

        else {

            /*
             * 20201104 TEST 3/4 THERE NEEDS TO BE AN IF ELSE STATEMENT TO DETERMINE WHICH
             * BLOCK IT IS IN AND DEPENDING ON WHICH BLOCK IT IS IT, WE WILL LOAD THAT BLOCK
             * AND DO THE WRITES ELSE, STICK WITH THE SAME BLOCK AND WRITE THERE
             * 
             * FIGURE OUT THE MATH
             * 
             * THE ONLY ISSUE WOULD PROBABLY BE THE # OF WRITE COUNTS AND READ COUNTS FOR
             * TEST 3 WALK THROUGH AND DOUBLE CHECK TO SEE IF THE LISTS ARE ACTUALLY BEING
             * PUT INTO IT CORRECTLY
             * 
             * IF THE NEXT ADDRESS ISN'T WITHIN THIS CURRENT BLOCK, THEN LOAD AGAIN AND
             * WRITE THEN STORE
             * 
             * IF IT IS WITHIN THE SAME BLOCK, YOU DON'T HAVE TO LOAD AND STORE SO MANY
             * TIMES
             * 
             */

            evictTemp = policyPFN.advise();

            // PUT INSIDE PTE
            MyPageTable.PageTableEntry oof = new MyPageTable.PageTableEntry(vpn, evictTemp.evictPFN,
                    evictTemp.dirtyBit);
            hashTable.addEntry(vpn, oof);

            // load from PhyMemory
            // call Policy to get PFN
            physAddr = evictTemp.evictPFN * 64 + offset;
            startAddr = evictTemp.evictPFN * 64; // no offset

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
        int hashTablePFN = -1;

        if (virtAddy > virtSize) {

            System.err.println("Out of Bounds");
        }

        else { // Has to be an else if statement that will check if the VPN is within that
               // block, else load another block

            hashTablePFN = hashTable.containsVPN(vpn);

            if (hashTablePFN == -1) {

                System.err.println("Page Fault"); // Cache Miss

                // Find a PFN to use

                evictTemp = policyPFN.advise();

                // Reload PhyMemory and adjust
                if (evictTemp.evictPFN > 255 || evictTemp.evictPFN < 0) {
                    // NOTE: FIX THIS IF STATEMENT BC IDK IF PFN ...
                    System.err.println("No such PFN");

                }
                // physAddr = getThatPFN * 64 + offset;
                startAddr = policyPFN.getCurrentPFN() * 64 + offset;
                theRam.load(vpn, startAddr);

                hashTablePFN = hashTable.containsVPN(vpn) * 64 + offset;
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
        if (tempDirtyList.size() != 0) {

            // MAKE SURE WHEN WE CALL THIS WE RESET WRITEBACKCOUNTER = 0 AND WE NEED TO MAKE
            // A METHOD INSIDE OF PAGE TABLE TO CLEAR OUT DIRTYBIT LIST
            // startAddr = tempDirtyList.get(countIndex).getPfn() * 64;
            ListIterator<MyPageTable.PageTableEntry> iter = null;
            iter = tempDirtyList.listIterator();

            // theRam.load(tempDirtyList.get(countIndex).getVpn(), startAddr); // blockNum =
            // VPN, startAddr = PFN * 64 or
            // currentBlock = 0; // table_Size
            while (iter.hasNext()) {

                writeBackPFN = tempDirtyList.get(countIndex).getPfn();
                writeBackVPN = tempDirtyList.get(countIndex).getVpn();
                offset = (writeBackVPN * 64) % 64;
                physAddr = writeBackPFN * 64 + offset;
                // Byte temp = theRam.read(physAddr);
                // theRam.write(physAddr, temp);
                // countIndex++;

                if (physAddr > startAddr && physAddr > startAddr + 63) {
                    startAddr = tempDirtyList.get(countIndex).getPfn() * 64; // + 64? Because we're going to look
                                                                             // at the next block which is 64 byte
                                                                             // // after the first
                    theRam.store(currentBlock, startAddr);
                    // currentBlock++; // Uhh double check if it's in the correct block
                    startAddr += 64;

                }

                iter.next();
                // Store to disk
                // ram.read? get byte?
                // ram.write?

            }
            // Depending on the VPN, it will determine which block, so for example
            // VPN = 5, then it is in block 0-64 or block 0 because it goes from
            // VPN = 0 - 63
            // theRam.store(currentBlock, startAddr);
            writeBackCounter = 0;
            hashTable.resetDirtyList();
        }
        // Remember to reset the DirtyBits in the list

    }

}
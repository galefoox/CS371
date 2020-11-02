import Storage.PhyMemory;

public class VirtMemory {
    // INSIDE HERE IS WHERE WE WILL EXTRACT VPN AND THEN PASS VPN INTO MYPAGETABLE

    PhyMemory ram = new PhyMemory();
    private int getThatPFN;
    private boolean getThatDirty;

    protected PhyMemory getPhyMemory() {

        return ram;
    }

    public void write(int address, byte value) {
        // Get the Hash of the address
        int index = address % 64;
        int vpn = address / 64;
        // Consider pagefault
        int maxFrames = ram.num_frames();
        Policy policyPFN = new Policy(maxFrames);
        getThatPFN = policyPFN.getPFN();
        getThatDirty = policyPFN.getDirty();
        // put inside PTE with getDirty and getPFN
        // load from PhyMemory
        // call Policy to get PFN
        ram.load(blockNum, startAddr); // blockNum = VPN, startAddr = PFN * 64 or table_Size
        // call dirtyBit
        // Consult policy class to get a PFN and store it into DISK/PageTable

        // read && write = to Cache
        // store && load = to Disk
        // block is 64 bytes page is 64 bytes 1:1 mapping

        // evict PageTable

    }

public byte read(int virtAddy)
{
    // Check exception for out of range address
        //return error code
    
    // if there's a page fault then write()
        //else ram.read(phyAddy)
    
        if contains()
        {
            return ram.read(pAddr);
        }
    

    return byte phyAddy;
}

    // HASH CODE HERE TO PASS IT INTO MYPAGETABLE
    // MYPAGETABLE SHOULDN'T KNOW WHAT PAGE_sIZE IS

    // HARD CODE PAGE SIZE IN HERE
}
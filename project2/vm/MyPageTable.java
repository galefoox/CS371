import java.util.LinkedList;
import java.util.Objects;

public class MyPageTable {

    class PageTableEntry {
        int vpn;
        int pfn;
        boolean dirtyBit;

        public void setPFN(int pfn) {
            this.pfn = pfn;
        }

        public int getPFN() {
            return pfn;
        }

        public void setVPN(int vpn) {
            this.vpn = vpn;
        }

        public int getVPN() {
            return vpn;
        }

    }

    public void setAll(int vpn, int pfn, boolean dirtyBit) {
        MyPageTable.setPFN(pfn);
        this.vpn = vpn;
        this.pfn = pfn;
        this.dirtyBit = dirtyBit;
    }

    private static int INITIAL_SIZE = 256;
    Object[] table = new Object[INITIAL_SIZE];
    LinkedList<PageTableEntry> entries = new LinkedList<>();

    public MyPageTable() {

        for (int i = 0; i < INITIAL_SIZE; i++) {
            table[i] = entries;
        }
    }

    public int containsVPN(int address) {

        int index = address % INITIAL_SIZE;
        int vpn = address / INITIAL_SIZE;

        entries.add(My(5, 5, true));

        return 1;
    }

}

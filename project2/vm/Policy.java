import Storage.PhyMemory;
import java.util.Queue;
import java.util.LinkedList;

public class Policy {

    Queue<Integer> queue = new LinkedList<>();

    int holdPFN;
    int maxFrames;
    boolean dirty = true;
    int policyVpn = -1;

    public Policy(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    protected VirtMemory.EvictionStatus advise(int blockNum) {

        if (policyVpn == blockNum) {
            holdPFN = blockNum;
        } else {
            if (queue.size() != maxFrames) { // It will never be more because we will remove in else
                holdPFN = queue.size();
                queue.add(queue.size());
                // holdPFN = queue.peek();

            } else {

                holdPFN = queue.remove(); // so this evicts but now we need to add to front
                dirty = false;
                queue.add(holdPFN);
            }

        }
        policyVpn = blockNum;

        return new VirtMemory.EvictionStatus(holdPFN, dirty);

    }

    protected int getCurrentPFN() {
        return holdPFN;
    }
}
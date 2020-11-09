import Storage.PhyMemory;
import java.util.Queue;
import java.util.LinkedList;

public class Policy {

    Queue<Integer> queue = new LinkedList<>();

    int holdPFN;
    int maxFrames;
    boolean evictStatus = false;
    int policyVpn = -1;
    int currentPFN;

    public Policy(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    protected VirtMemory.EvictionStatus advise(int blockNum) {

        if (policyVpn == blockNum) {
            holdPFN = currentPFN;
        } else {
            if (queue.size() < maxFrames) { // It will never be more because we will remove in else
                holdPFN = queue.size();
                queue.add(queue.size());
                currentPFN = holdPFN;
            } else {

                holdPFN = queue.remove(); // we get the head
                evictStatus = true;
                queue.add(holdPFN); // add back to tail
                currentPFN = holdPFN;
            }

        }
        policyVpn = blockNum;

        return new VirtMemory.EvictionStatus(holdPFN, evictStatus);

    }

    protected int getCurrentPFN() {
        return currentPFN;
    }

}
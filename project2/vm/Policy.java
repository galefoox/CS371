import Storage.PhyMemory;
import java.util.Queue;
import java.util.LinkedList;

public class Policy {

    Queue<Integer> queue = new LinkedList<>();

    int holdPFN;
    int maxFrames;
    boolean dirty = false;

    public Policy(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    protected VirtMemory.EvictionStatus advise() {

        if (queue.size() != maxFrames) { // It will never be more because we will remove in else

            queue.add(queue.size());
            holdPFN = queue.peek();
        } else {

            holdPFN = queue.remove(); // so this evicts but now we need to add to front
            dirty = true;
            queue.add(holdPFN);
        }

        return new VirtMemory.EvictionStatus(holdPFN, dirty);

    }
}
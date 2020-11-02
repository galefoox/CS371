import Storage.PhyMemory;
import java.util.Queue;
import java.util.LinkedList;

public class Policy {

    Queue<Integer> queue = new LinkedList<>();

    int holdPFN;
    boolean dirty = false;

    public Policy(int maxFrames) {

        if (queue.size() != maxFrames) { // It will never be more because we will remove in else

            queue.add(queue.size());
            holdPFN = queue.peek();
        } else {
            holdPFN = queue.remove();
            dirty = true;

        }

    }

    public int getPFN() {

        return holdPFN;
    }

    public boolean getDirty() {
        return dirty;
    }
}
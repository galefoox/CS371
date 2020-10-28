
public class MemDriver {
    public static void main(String[] args) {
        final int SIZE = 10000;

        final int TEST_SIZE_1 = 10;
        final int TEST_SIZE_2 = 20;

        MyMemoryAllocation m = new MyMemoryAllocation(SIZE, "NF");
        boolean result = true;
        int ptr[] = new int[SIZE];
        int p = 0;
        while (m.max_size() >= TEST_SIZE_1) {
            ptr[p] = m.alloc(TEST_SIZE_1);
            if (ptr[p] == 0) {
                result = false;
            }
            p++;
        }
        int max_p = p;
        if (max_p < 400) {
            result = false;
        }
        int l_limit = p / 3;
        int u_limit = 2 * p / 3;
        for (int i = l_limit; i < u_limit; i++) {
            m.free(ptr[i]);
            ptr[i] = 0;
        }
        if (m.max_size() != (u_limit - l_limit) * TEST_SIZE_1) {
            result = false;
        }

        p = l_limit;
        while (p < u_limit && m.max_size() >= TEST_SIZE_1) {
            ptr[p] = m.alloc(TEST_SIZE_1);
            System.out.println("ptr[p]: " + ptr[p] + " Test Size: " + TEST_SIZE_1 + "MaxSize: " + m.max_size());
            if (ptr[p] == 0) {
                result = false;
            }
            p++;
        }
        for (int i = 0; i < max_p; i++) {
            if (ptr[i] > 0)
                m.free(ptr[i]);
            ptr[i] = 0;
            System.out.println("ptr[i]: " + ptr[i] + " Test Size: " + max_p + " MaxSize: " + m.size());
        }
        if (m.size() != SIZE - 1) {
            result = false;
            if (result)

            {
                System.out.println("end2endTest1: PASS ");
            } else {
                System.out.println("end2endTest1: FAIL");
            }
            assert (result == true);
        }

        // MyMemoryAllocation mal = new MyMemoryAllocation(14, "NF");
        // mal.alloc(1);
        // mal.alloc(3);
        // mal.alloc(2);
        // mal.alloc(2);
        // mal.alloc(1);
        // mal.alloc(1);
        // mal.alloc(1);
        // mal.alloc(2);
        // mal.free(2);
        // mal.free(7);
        // mal.free(10);
        // mal.free(12);
        // mal.alloc(1);
        // mal.alloc(2);
        // mal.print();
    }
}
import java.util.LinkedList;
import java.util.*;

public class MainTest {
    public static void main(String[] args) {

        MyPageTable table = new MyPageTable();

        table.printAll();
        System.out.println(table.containsVPN(10));

    }

}

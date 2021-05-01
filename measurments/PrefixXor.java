import java.util.Random;

public class PrefixXor {
    public static void main(String[] args) {
        int n;
        AVLTree tree;
        double avgTime;
        for (int i = 1; i <= 5; i++) {
            n = 500*i;
            tree = createTree(n);
            System.out.println("Number of keys: " + n);
            avgTime = measurePrefixXor(tree, n, false);
            System.out.println("prefixXor (all keys): " + avgTime + "ns");
            avgTime = measurePrefixXor(tree, 100, false);
            System.out.println("prefixXor (first 100 keys): " + avgTime + "ns");
            avgTime = measurePrefixXor(tree, n, true);
            System.out.println("succPrefixXor (all keys): " + avgTime + "ns");
            avgTime = measurePrefixXor(tree, 100, true);
            System.out.println("succPrefixXor (first 100 keys): " + avgTime + "ns");
        }
    }

    public static AVLTree createTree(int n) {
        AVLTree tree = new AVLTree();
        Random random = new Random();
        int nextInt;
        boolean nextBoolean;
        int i = 0;
        while (i < n) {
            nextInt = random.nextInt();
            if (nextInt >= 1 && tree.search(nextInt) == null) {
                nextBoolean = random.nextBoolean();
                tree.insert(nextInt, nextBoolean);
                i += 1;
            }
        }
        return tree;
    }

    public static double measurePrefixXor(AVLTree tree, int n, boolean isSucc) {
        long[] times = new long[n];
        long startTime;
        long endTime;
        int key;
        AVLTree.AVLNode node = tree.getMin();
        for (int i = 0; i < n; i++) {
            key = node.getKey();
            if (isSucc) {
                startTime = System.nanoTime();
                tree.succPrefixXor(key);
                endTime = System.nanoTime();
            }
            else {
                startTime = System.nanoTime();
                tree.prefixXor(key);
                endTime = System.nanoTime();
            }
            times[i] = endTime - startTime;
            node = tree.successor(node);
        }
        return average(times);
    }

    public static double average(long[] arr) {
        double avg = 0;
        if (!(arr.length == 0)) {
            for (long val : arr) {
                avg += val;
            }
            avg = ((double)avg)/arr.length;
        }
        return avg;
    }
}

import java.util.Arrays;

public class AVLTreeTest {
    public static void main(String[] args) {
        int[] keys = {1};
        boolean[] infos = {true};
        testInsertUnique(keys, infos, keys);

    }

    private static boolean isLegal(AVLTree tree, int size, int min, int max, boolean isEmpty, int root) {
        return tree.getRootVirtual().getKey() == root && tree.getMax().getKey() == max && tree.getMin().getKey() == min && tree.getSize() == size && tree.empty() == isEmpty;
    }

    private static int getAVLCriminal(AVLTree tree) {
        AVLTree.AVLNode node = tree.getMin();
        while (node != null && node.isRealNode()) {
            if (Math.abs(node.getBF()) >= 2) {
                return node.getKey();
            }
            node = tree.successor(node);
        }
        return -1;
    }

    private static int getMin(int[] arr) {
        int min = arr[0];
        for (int i: arr) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int i: arr) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private static AVLTree testInsertUnique(int[] keys, boolean[] infos, int roots[]) {
        AVLTree tree = new AVLTree();
        int size=0;
        boolean isEmpty = true;
        int min = -1;
        int max = -1;
        int root = -1;
        if (!isLegal(tree, size, min, max, isEmpty, root)) {
            System.out.println("not Legal when empty");
            return null;
        }
        if (getAVLCriminal(tree) != -1) {
            System.out.println("Unbalanced when empty");
            return null;
        }
        int n = keys.length;
        isEmpty = false;
        for (int i = 0; i < n; i++) {
            tree.insert(keys[i], infos[i]);
            size++;
            int criminal;
            min = getMin(Arrays.copyOf(keys,i+1));
            max = getMax(Arrays.copyOf(keys, i+1));
            root = roots[i];
            if (!isLegal(tree, size, min, max, isEmpty, root)) {
                System.out.println("not Legal when inserting " + keys[i] + " the " + i + "th key.");
                return null;
            }
            criminal = getAVLCriminal(tree);
            if (criminal != -1) {
                System.out.println("Unbalanced when inserting " + keys[i] + " the " + i + "th key. criminal is: " + criminal);
                return null;
            }
        }
        return tree;
    }

}

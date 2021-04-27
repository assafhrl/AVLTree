public class AVLTreeTest {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(1, true);
        tree.insert(2, true);
        tree.insert(3, false);
        System.out.println(":-)");
    }
    private static boolean isLegal(AVLTree tree, int size, int min, int max, boolean isEmpty, int root) {
        return tree.getRoot().getKey() == root && tree.getMax().getKey() == max && tree.getMin().getKey() == min && tree.getSize() == size && tree.empty() == isEmpty;
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

}

public class AVLTreeTest {
    public static void main(String[] args) {

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

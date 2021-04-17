/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {

    private AVLNode root;
    private AVLNode minNode;
    private AVLNode maxNode;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree(){
        this.root = new AVLNode();
        this.minNode = new AVLNode();
        this.maxNode = new AVLNode();
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return !this.getRoot().isRealNode();
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        return null;  // to be replaced by student code
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
        return 42;    // to be replaced by student code
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        return this.minNode.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        return this.maxNode.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.getRoot().getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.getNode(this.root);
    }

    public AVLNode getMin() {
        return this.getNode(this.minNode);
    }

    public AVLNode getMax() {
        return this.getNode(this.maxNode);
    }

    private AVLNode getNode(AVLNode node) {
        if (node.isRealNode()) {
            return node;
        }
        return null;
    }

    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     *
     */
    public boolean prefixXor(int k){
        return false;
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node) {
        return null;
    }

    public AVLNode predecessor(AVLNode node) {
        return null;
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k){
        AVLNode node = this.getMin();
        int trueCounter = 0;
        while (node.getKey() <= k) {
            trueCounter += node.getValue().booleanValue() ? 1 : 0;
            node = this.successor(node);
        }
        return (trueCounter % 2) == 1;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {

        private int key;
        private Boolean value;
        private AVLNode parent;
        private AVLNode left;
        private AVLNode right;
        private int size;
        private int height;
        private int booleanValueSum;

        public AVLNode(int key, Boolean value) {
            this.setKey(key);
            this.setValue(value);
            this.setLeft(new AVLNode(this));
            this.setRight(new AVLNode(this));
            this.setParent(null);
            this.updateFields();
        }

        public AVLNode(int key, Boolean value, AVLNode parent) {
            this(key, value);
            this.setParent(parent);
        }

        public AVLNode() {
            this.setKey(-1);
            this.setValue(null);
            this.setLeft(null);
            this.setRight(null);
            this.setParent(null);
            this.updateFields();
        }

        public AVLNode(AVLNode parent) {
            this();
            this.setParent(parent);
        }

        private void setKey(int key) {
            this.key = key;
        }

        //returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key;
        }

        private void setValue(Boolean value) {
            this.value = value;
        }

        //returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
            return this.value;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            if (this.isRealNode()) {
                this.left = node;
            }
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            return this.left;
        }

        //sets right child
        public void setRight(AVLNode node) {
            if (this.isRealNode()) {
                this.right = node;
            }
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
            return this.right;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return this.key != -1;
        }

        // sets the height of the node
        private void setHeight() {
            this.height = this.calcHeight();
        }

        private int calcHeight() {
            if (this.isRealNode()) {
                return Math.max(this.left.getHeight(), this.right.getHeight()) + 1;
            }
            return -1;
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }

        private void setSize() {
            this.size = this.calcSize();
        }

        private int calcSize() {
            if (this.isRealNode()) {
                return this.left.getSize() + this.right.getSize() + 1;
            }
            return 0;
        }

        public int getSize() {
            return this.size;
        }

        private void setBooleanValueSum() {
            this.booleanValueSum = this.calcBooleanValueSum();
        }

        private int calcBooleanValueSum() {
            if (this.isRealNode()) {
                int ret = this.left.getBooleanValueSum() + this.right.getBooleanValueSum();
                ret += this.getValue().booleanValue() ? 1 : 0;
                return ret;
            }
            return 0;
        }

        public int getBooleanValueSum() {
            return this.booleanValueSum;
        }

        public void updateFields() {
            this.setHeight();
            this.setSize();
            this.setBooleanValueSum();
        }

        public int getBF() {
            if (this.isRealNode()) {
                return this.left.getHeight() - this.right.getHeight();
            }
            return -1;
        }
    }

}



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
    private int size;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree(){
        this.initTree();
    }

    private void initTree() {
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

    private AVLNode searchNode(int k) {
        AVLNode node = this.root;
        while (node.isRealNode() && node.getKey() != k) {
            if (node.getKey() > k) {
                node = node.getLeft();
            }
            else {
                node = node.getRight();
            }
        }
        return node;
    }
    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        return this.searchNode(k).getValue();
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

    private void updateParentChild(AVLNode oldChild, AVLNode newChild) {
        AVLNode parent = oldChild.getParent();
        if (parent == null) {
            this.setRoot(newChild);
        }
        else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        }
        else {
            parent.setRight(newChild);
        }
        newChild.setParent(parent);
    }

    private void updateMinInsert(AVLNode node) {
        AVLNode currentMin = this.getMin();
        if (!currentMin.isRealNode() || node.getKey() < currentMin.getKey()) {
            this.setMin(node);
        }
    }

    private void  updateMaxInsert(AVLNode node) {
        AVLNode currentMax = this.getMax();
        if (!currentMax.isRealNode() || node.getKey() > this.getMax().getKey()) {
            this.setMax(node);
        }
    }

    private void updateMinMaxInsert(AVLNode node) {
        updateMinInsert(node);
        updateMaxInsert(node);
    }

    public int insert(int k, boolean i) {
        AVLNode node = new AVLNode(k, new Boolean(i));
        if (!insertNode(node)) {
            return -1;
        }
        int ops = this.updatePath(node);
        updateMinMaxInsert(node);
        return ops;
    }

    private boolean insertNode(AVLNode node) {
        AVLNode nodeLoc = this.searchNode(node.getKey());
        if (nodeLoc.isRealNode()) {
            return false;
        }
        updateParentChild(nodeLoc, node);
        this.incrementSize();
        nodeLoc.setParent(null);
        return true;
    }

    private int updatePath(AVLNode node) {
        int ops = 1;
        while (node != null) {
            if (rotate(node)) {
                ops += 1;
                node = node.getParent();
                node.getLeft().updateFields();
                node.getRight().updateFields();
                node.updateFields();
            } else {
                ops += node.updateHeightAndReport();
                node.updateBooleanValueSum();
            }
            node = node.getParent();
        }
        return ops;
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
        AVLNode nodeLoc = this.searchNode(k);
        if (!nodeLoc.isRealNode()) {
            return -1;
        }
        AVLNode updatePathStartNode = this.deleteNode(nodeLoc);
        this.decrementSize();
        int ops = updatePath(updatePathStartNode);
        updateMinMaxDelete(nodeLoc);
        return ops;
    }

    private void disconnectNode(AVLNode node, AVLNode parent, AVLNode child) {
        if (parent.getLeft() == node) {
            parent.setLeft(child);
        }
        else {
            parent.setRight(child);
        }
        child.setParent(parent);
        node.resetConnections();
    }

    private AVLNode deleteNodeLeaf(AVLNode node){
        AVLNode parent = node.getParent();
        if (parent == null) {
            this.initTree();
            return this.getRoot();
        }
        AVLNode child = new AVLNode();
        disconnectNode(node, parent, child);
        return child;
    }

    private AVLNode deleteNodeSingleChild(AVLNode node, AVLNode child) {
        AVLNode parent = node.getParent();
        if (parent == null) {
            this.setRoot(child);
            child.setParent(null);
            node.resetConnections();
        }
        else {
            this.disconnectNode(node, parent, child);
        }
        return child;
    }

    private AVLNode deleteNodeTwoChildren(AVLNode node){
        AVLNode successor = this.successorChild(node);
        AVLNode successorChild = this.deleteNode(successor);
        AVLNode nodeLeft = node.getLeft();
        AVLNode nodeRight = node.getRight();
        successor.setLeft(nodeLeft);
        successor.setRight(nodeRight);
        nodeLeft.setParent(successor);
        nodeRight.setParent(successor);
        AVLNode parent = node.getParent();
        if (parent == null) {
            this.setRoot(successor);
        }
        else {
            this.disconnectNode(node, parent, successor);
        }
        return successorChild;
    }

    private AVLNode deleteNode(AVLNode node) {
        AVLNode leftChild = node.getLeft();
        AVLNode rightChild = node.getRight();
        boolean leftChildReal = leftChild.isRealNode();
        boolean rightChildReal = rightChild.isRealNode();
        if (leftChildReal && rightChildReal) {
            return deleteNodeTwoChildren(node);
        }
        if (leftChildReal) {
            return deleteNodeSingleChild(node, leftChild);
        }
        if (rightChildReal) {
            return deleteNodeSingleChild(node, rightChild);
        }
        return deleteNodeLeaf(node);
    }

    private void updateMinMaxDelete(AVLNode node) {
        updateMinDelete(node);
        updateMaxDelete(node);
    }

    private void updateMinDelete(AVLNode node) {
        AVLNode currentMin = this.getMin();
        if (currentMin == node) {
            AVLNode newMin = calcMin();
            this.setMin(newMin);
        }
    }

    private void updateMaxDelete(AVLNode node) {
        AVLNode currentMax = this.getMin();
        if (currentMax == node) {
            AVLNode newMax = calcMax();
            this.setMax(newMax);
        }
    }

    private boolean rotate(AVLNode node) {
        int bf = node.getBF();
        if (bf >= -1 && bf <= 1) {
            return false;
        }
        if (bf < 0) {
           int rightBF = node.getRight().getBF();
           if (rightBF > 0) {
               this.rotateRight(node.getRight());
           }
           this.rotateLeft(node);
        } else {
            int leftBF = node.getLeft().getBF();
            if (leftBF < 0) {
                this.rotateLeft(node.getLeft());
            }
            this.rotateRight(node);
        }
        return true;
    }

    private void rotateLeft(AVLNode node) {
        AVLNode right = node.getRight();
        AVLNode rightLeft = right.getLeft();
        updateParentChild(node, right);
        node.setRight(rightLeft);
        rightLeft.setParent(node);
        right.setLeft(node);
        node.setParent(right);
    }

    private void rotateRight(AVLNode node) {
        AVLNode left = node.getLeft();
        AVLNode leftRight = left.getRight();
        updateParentChild(node, left);
        node.setLeft(leftRight);
        leftRight.setParent(node);
        left.setRight(node);
        node.setParent(left);
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        return this.getMin().getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        return this.getMax().getValue();
    }

    private AVLNode[] nodesToArray() {
        AVLNode[] arr = new AVLNode[this.getSize()];
        AVLNode node = this.getMin();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = node;
            node = this.successor(node);
        }
        return arr;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.getSize()];
        AVLNode[] nodes = this.nodesToArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nodes[i].getKey();
        }
        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.getSize()];
        AVLNode[] nodes = this.nodesToArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nodes[i].getValue();
        }
        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.root;
    }

    private void setRoot(AVLNode root) {
        this.root = root;
    }

    public AVLNode getMin() {
        return this.minNode;
    }

    private void setMin(AVLNode node) {
        this.minNode = node;
    }

    private AVLNode calcMin() {
        AVLNode newMin = this.getRoot();
        if (!this.empty()) {
            AVLNode newMinLeft = newMin.getLeft();
            while (newMinLeft.isRealNode()) {
                newMin = newMinLeft;
                newMinLeft = newMin.getLeft();
            }
        }
        return newMin;
    }

    public AVLNode getMax() {
        return this.maxNode;
    }

    private void setMax(AVLNode node) {
        this.maxNode = node;
    }

    private AVLNode calcMax() {
        AVLNode newMax = this.getRoot();
        if (!this.empty()) {
            AVLNode newMaxRight = newMax.getRight();
            while (newMaxRight.isRealNode()) {
                newMax = newMaxRight;
                newMaxRight = newMax.getRight();
            }
        }
        return newMax;
    }

    private void setSize(int size) {
        this.size = size;
    }

    private void incrementSize() {
        this.setSize(this.getSize() + 1);
    }

    private void decrementSize() {
        this.setSize(this.getSize() - 1);
    }

    public int getSize() {
        return this.size;
    }

    private int calcLeftBooleanValueSum(AVLNode node) {
        int ret = node.getLeft().getBooleanValueSum();
        ret += node.getValue().booleanValue() ? 1 : 0;
        return ret;
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
        AVLNode node = this.searchNode(k);
        int booleanValueSum = calcLeftBooleanValueSum(node);
        AVLNode next = node.getParent();
        while (next != null) {
            if (node == next.getRight()) {
                booleanValueSum += calcLeftBooleanValueSum(node);
            }
            node = next;
            next = node.getParent();
        }
        return (booleanValueSum % 2 == 1);
    }

    private AVLNode successorChild(AVLNode node) {
        node = node.getRight();
        while (node.isRealNode()) {
            node = node.getLeft();
        }
        return node.getParent();
    }

    private AVLNode successorParent(AVLNode node) {
        AVLNode next = node.getParent();
        while (next != null && next.getLeft() != node) {
            node = next;
            next = node.getParent();
        }
        return next;
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
        if (node.getRight().isRealNode()) {
            return successorChild(node);
        }
        return successorParent(node);

    }

    private AVLNode predecessorChild(AVLNode node) {
        node = node.getLeft();
        while (node.isRealNode()) {
            node = node.getRight();
        }
        return node.getParent();
    }

    private AVLNode predecessorParent(AVLNode node) {
        AVLNode next = node.getParent();
        while (next != null && next.getRight() != node) {
            node = next;
            next = node.getParent();
        }
        return next;
    }

    public AVLNode predecessor(AVLNode node) {
        if (node.getLeft().isRealNode()) {
            return predecessorChild(node);
        }
        return predecessorParent(node);
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
        private void updateHeight() {
            this.height = this.calcHeight();
        }

        private int updateHeightAndReport() {
            int newHeight = this.calcHeight();
            int oldHeight = this.getHeight();
            this.height = newHeight;
            return newHeight == oldHeight ? 0 : 1;
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

        private void updateBooleanValueSum() {
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
            this.updateHeight();
            this.updateBooleanValueSum();
        }

        public int getBF() {
            if (this.isRealNode()) {
                return this.left.getHeight() - this.right.getHeight();
            }
            return -1;
        }

        public void resetConnections() {
            this.setLeft(new AVLNode(this));
            this.setRight(new AVLNode(this));
            this.setParent(null);
        }
    }
}



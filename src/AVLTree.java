/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 */

public class AVLTree {

    private AVLNode root;
    private AVLNode minNode;
    private AVLNode maxNode;
    private int size;

    /**
     * Create an empty AVLTree object.
     */
    public AVLTree(){
        this.initTree();
    }

    /**
     * Initialize the fields of an empty AVLTree, and overrides them if not empty.
     */
    private void initTree() {
        AVLNode newRoot = new AVLNode();
        this.setRoot(newRoot);
        this.setMin(newRoot);
        this.setMax(newRoot);
        this.setSize(0);
    }

    /**
     * Checks if the AVLTree is empty.
     * @return true if tree empty else false.
     */
    public boolean empty() {
        return !this.getRootVirtual().isRealNode();
    }

    /**
     * Search for AVLNode object in the AVLTree by key.
     * @param k - key of searched node.
     * @return the searched node if exists, else a virtual node where node should exist.
     */
    private AVLNode searchNode(int k) {
        AVLNode node = this.root;
        while (node.isRealNode() && node.getKey() != k) {
            if (node.getKey() > k) {
                node = node.getLeftVirtual();
            }
            else {
                node = node.getRightVirtual();
            }
        }
        return node;
    }
    /**
     * Searches for a key in the AVLTree.
     * @param k - Key to search.
     * @return if key exists, the return value is the info of the node, else returns null.
     */
    public Boolean search(int k) {
        return this.searchNode(k).getValue();
    }

    /**
     * Checks the path from given node to root. Updates fields of nodes on the path and performs
     * rebalancing operations if needed.
     * @param node - Starting node to check path from it to root node.
     * @return - The number of rebalancing operations.
     */
    private int updatePath(AVLNode node) {
        int ops = 0;
        while (node != null) {
            if (rotate(node)) {
                ops += 1;
                node = node.getParent();
                node.getLeftVirtual().updateFields();
                node.getRightVirtual().updateFields();
            }
            node.updateFields();
            node = node.getParent();
        }
        return ops;
    }

    /**
     * Checks if the node is out of balance by calculation the balance factor, and deciding, if necessary,
     * which set of rotations should take place
     * @param node - The node to check if it's out of balance.
     * @return - true if a rotation has taken place, else false.
     */
    private boolean rotate(AVLNode node) {
        int bf = node.getBF();
        if (bf >= -1 && bf <= 1) {
            return false;
        }
        if (bf < 0) {
           int rightBF = node.getRightVirtual().getBF();
           if (rightBF > 0) {
               this.rotateRight(node.getRightVirtual());
           }
           this.rotateLeft(node);
        } else {
            int leftBF = node.getLeftVirtual().getBF();
            if (leftBF < 0) {
                this.rotateLeft(node.getLeftVirtual());
            }
            this.rotateRight(node);
        }
        return true;
    }

    /**
     * Perform a left rotation on a node.
     * @param node - The root node to perform a left rotation on.
     */
    private void rotateLeft(AVLNode node) {
        AVLNode right = node.getRightVirtual();
        AVLNode rightLeft = right.getLeftVirtual();
        updateParentChild(node, right);
        node.setRight(rightLeft);
        rightLeft.setParent(node);
        right.setLeft(node);
        node.setParent(right);
    }

    /**
     * Perform a right rotation on a node.
     * @param node - The root node to perform a right rotation on.
     */
    private void rotateRight(AVLNode node) {
        AVLNode left = node.getLeftVirtual();
        AVLNode leftRight = left.getRightVirtual();
        updateParentChild(node, left);
        node.setLeft(leftRight);
        leftRight.setParent(node);
        left.setRight(node);
        node.setParent(left);
    }

    /**
     * Change the child of a parent from an old child to a different new child.
     * @param oldChild - The current child of the AVLNode.
     * @param newChild - The soon to be new child of the AVLNode.
     */
     private void updateParentChild(AVLNode oldChild, AVLNode newChild) {
        AVLNode parent = oldChild.getParent();
        if (parent == null) {
            this.setRoot(newChild);
        }
        else if (parent.getLeftVirtual() == oldChild) {
            parent.setLeft(newChild);
        }
        else {
            parent.setRight(newChild);
        }
        newChild.setParent(parent);
    }

    /**
     * Inserts an item to the AVLTree if key doesn't exists, else does nothing. Also makes sure
     * the tree will remain valid.
     * @param k - Key to insert to tree.
     * @param i - Value to insert to tree.
     * @return - The number of nodes which require rebalancing operations. This always includes the
     * newly created node. If no node was inserted, then returns -1.
     */
    public int insert(int k, boolean i) {
        AVLNode node = new AVLNode(k, new Boolean(i));
        if (!insertNode(node)) {
            return -1;
        }
        this.incrementSize();
        int ops = this.updatePath(node) + 1;
        this.updateMinMaxInsert(node);
        return ops;
    }

    /**
     * Insert the physical AVLNode into the AVLTree if key doesn't exists.
     * @param node - The node to insert into the tree.
     * @return true if insertion has taken place, else false.
     */
    private boolean insertNode(AVLNode node) {
        AVLNode nodeLoc = this.searchNode(node.getKey());
        if (nodeLoc.isRealNode()) {
            return false;
        }
        updateParentChild(nodeLoc, node);
        nodeLoc.setParent(null);
        return true;
    }

    /**
     * Checks if there is a need to change the pointer to the minimum node while performing an
     * insertion operation. If so, then updates the pointer.
     * @param node - Node to check if should be new minimum.
     */
    private void updateMinInsert(AVLNode node) {
        AVLNode currentMin = this.getMin();
        if (!currentMin.isRealNode() || node.getKey() < currentMin.getKey()) {
            this.setMin(node);
        }
    }

    /**
     * Checks if there is a need to change the pointer to the maximum node while performing an
     * insertion operation. If so, then updates the pointer.
     * @param node - Node to check if should be new maximum.
     */
    private void  updateMaxInsert(AVLNode node) {
        AVLNode currentMax = this.getMax();
        if (!currentMax.isRealNode() || node.getKey() > this.getMax().getKey()) {
            this.setMax(node);
        }
    }

    /**
     * Checks if there is a need to change the pointer to the maximum and or minimum node while
     * performing an insertion operation. If so, then updates the pointer.
     * @param node - Node to check if should be new minimum and maximum.
     */
    private void updateMinMaxInsert(AVLNode node) {
        updateMinInsert(node);
        updateMaxInsert(node);
    }

    /**
     * Deletes an item from the AVLTree if key exists. Also makes sure the tree will remain valid.
     * @param k - The key to delete from the AVLTree.
     * @return - If key exists, then returns the number of rebalancing operations needed to perform,
     * else -1.
     */
    public int delete(int k) {
        AVLNode nodeLoc = this.searchNode(k);
        if (!nodeLoc.isRealNode()) {
            return -1;
        }
        AVLNode updatePathStartNode = this.deleteNode(nodeLoc);
        int ops = 1;
        if (!this.empty()) {
            this.decrementSize();
            ops += updatePath(updatePathStartNode);
            updateMinMaxDelete(nodeLoc);
        }
        return ops;
    }

    /**
     * Bypass the connection through a node from the parent to child nodes.
     * @param node - Node to bypass and remove from tree.
     * @param parent - node object parent
     * @param child - node object child to connect to parent
     */
    private void disconnectNode(AVLNode node, AVLNode parent, AVLNode child) {
        if (parent.getLeftVirtual() == node) {
            parent.setLeft(child);
        }
        else {
            parent.setRight(child);
        }
        child.setParent(parent);
        node.resetConnections();
    }

    /**
     * Perform a physical delete operation from the tree when the node is a leaf.
     * @param node - Leaf node to physically delete from the tree.
     * @return - node to start checking path from while performing the rebalancing operations.
     */
    private AVLNode deleteNodeLeaf(AVLNode node){
        AVLNode parent = node.getParent();
        if (parent == null) {
            this.initTree();
            return this.getRootVirtual();
        }
        AVLNode child = new AVLNode();
        disconnectNode(node, parent, child);
        return child;
    }

    /**
     * Perform a physical delete operation from the tree when the node has a single child.
     * @param node - The node to physically delete from the tree.
     * @param child - The single child of the node to bypass the connection from parent to child.
     * @return - node to start checking path from while performing the rebalancing operations.
     */
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

    /**
     * Perform a physical delete operation from the tree when the node has two children.
     * @param node - The node to physically delete from the tree.
     * @return node to start checking path from while performing the rebalancing operations.
     */
    private AVLNode deleteNodeTwoChildren(AVLNode node){
        AVLNode successor = this.successorChild(node);
        AVLNode successorChild = this.deleteNode(successor);
        AVLNode nodeLeft = node.getLeftVirtual();
        AVLNode nodeRight = node.getRightVirtual();
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

    /**
     * Perform the physical delete operation of a node from the tree by classifying the node type.
     * @param node - The node to physically delete from the tree.
     * @return - node to start checking path from while performing the rebalancing operations.
     */
    private AVLNode deleteNode(AVLNode node) {
        AVLNode leftChild = node.getLeftVirtual();
        AVLNode rightChild = node.getRightVirtual();
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

    /**
     * Checks if there is a need to change the pointer to the  minimum node while
     * performing a deletion operation. If so, then updates the pointer.
     * @param node - Deleted node to check if it were previously minimum.
     */
    private void updateMinDelete(AVLNode node) {
        AVLNode currentMin = this.getMin();
        if (currentMin == node) {
            AVLNode newMin = calcMin();
            this.setMin(newMin);
        }
    }

    /**
     * Checks if there is a need to change the pointer to the  maximum node while
     * performing a deletion operation. If so, then updates the pointer.
     * @param node - Deleted node to check if it were previously maximum.
     */
    private void updateMaxDelete(AVLNode node) {
        AVLNode currentMax = this.getMax();
        if (currentMax == node) {
            AVLNode newMax = calcMax();
            this.setMax(newMax);
        }
    }

    /**
     * Checks if there is a need to change the pointer to the maximum and or minimum node while
     * performing a deletion operation. If so, then updates the pointer.
     * @param node - Deleted node to check if it were previously minimum or maximum.
     */
    private void updateMinMaxDelete(AVLNode node) {
        updateMinDelete(node);
        updateMaxDelete(node);
    }

    /**
     * Get the sum of true appearing in the node itself plus the sum of all of its descendants to its left.
     * @param node - the which we perform the calculation for.
     * @return - the sum of true in the the sub tree that start at node and includes all its left descendants.
     */
    private int calcLeftBooleanValueSum(AVLNode node) {
        int ret = node.getLeftVirtual().getBooleanValueSum();
        ret += node.getValue().booleanValue() ? 1 : 0;
        return ret;
    }

    /**
     * Given a key in the tree, calculate the xor of the values of nodes whose keys are smaller
     * or equal to k.
     * @pre - this.search(k) != null
     * @param k - key in tree
     * @return - xor value of all k's predeceasing keys, including itself.
     */
    public boolean prefixXor(int k){
        AVLNode node = this.searchNode(k);
        int booleanValueSum = calcLeftBooleanValueSum(node);
        AVLNode next = node.getParent();
        while (next != null) {
            if (node == next.getRightVirtual()) {
                booleanValueSum += calcLeftBooleanValueSum(next);
            }
            node = next;
            next = node.getParent();
        }
        return (booleanValueSum % 2 == 1);
    }

    /**
     * Identical to prefixXor. However, the implementation of succPrefixXor is starting from the
     * minimum-key node, iteratively call successor until you reach the node of specified key.
     * @pre - this.search(k) != null
     * @param k - key in tree
     * @return - xor value of all k's predeceasing keys, including itself.
     */
    public boolean succPrefixXor(int k){
        AVLNode node = this.getMin();
        int trueCounter = 0;
        while (node != null && node.getKey() <= k) {
            trueCounter += node.getValue().booleanValue() ? 1 : 0;
            node = this.successor(node);
        }
        return (trueCounter % 2) == 1;
    }

    /**
     * Get the successor of a node in the tree, given that the node has a right child.
     * @param node - Node to get its successor.
     * @return - Successor node.
     */
    private AVLNode successorChild(AVLNode node) {
        node = node.getRightVirtual();
        while (node.isRealNode()) {
            node = node.getLeftVirtual();
        }
        return node.getParent();
    }

    /**
     * Get the successor of a node in the tree, given that the node doesn't have a right child.
     * @param node - Node to get its successor.
     * @return - Successor node if exists, else null.
     */
    private AVLNode successorParent(AVLNode node) {
        AVLNode next = node.getParent();
        while (next != null && next.getLeftVirtual() != node) {
            node = next;
            next = node.getParent();
        }
        return next;
    }

    /**
     * Given a node in the tree, return the successor of the node.
     * @param node - Node to get its successor.
     * @return - Successor node if exists, else null.
     */
    public AVLNode successor(AVLNode node) {
        if (node.getRightVirtual().isRealNode()) {
            return successorChild(node);
        }
        return successorParent(node);
    }

    /**
     * Get the predecessor of a node in the tree, given that the node has a left child.
     * @param node - Node to get its predecessor.
     * @return - Predecessor node.
     */
    private AVLNode predecessorChild(AVLNode node) {
        node = node.getLeftVirtual();
        while (node.isRealNode()) {
            node = node.getRightVirtual();
        }
        return node.getParent();
    }

    /**
     * Get the predecessor of a node in the tree, given that the node doesn't have a left child.
     * @param node - Node to get its successor.
     * @return - Predecessor node if exists, else null.
     */
    private AVLNode predecessorParent(AVLNode node) {
        AVLNode next = node.getParent();
        while (next != null && next.getRightVirtual() != node) {
            node = next;
            next = node.getParent();
        }
        return next;
    }

    /**
     * Get the predecessor of a node in the tree.
     * @param node - Node to get its successor.
     * @return - Predecessor node if exists, else null.
     */
    public AVLNode predecessor(AVLNode node) {
        if (node.getLeftVirtual().isRealNode()) {
            return predecessorChild(node);
        }
        return predecessorParent(node);
    }

    /**
     * Get the info of the item with the smallest key in the tree.
     * @return If AVLTree isn't empty then info of value with smallest key in tree else null.
     */
    public Boolean min() {
        return this.getMin().getValue();
    }

    /**
     * Get the info of the item with the largest key in the tree.
     * @return If AVLTree isn't empty then info of value with largest key in tree else null.
     */
    public Boolean max() {
        return this.getMax().getValue();
    }

    /**
     * Get a sorted array by key of all AVLNodes in the tree.
     * @return - Sorted array of AVLNodes by key in tree, or an empty array if tree is empty.
     */
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
     * Get a sorted array by key of all keys in the tree.
     * @return - Sorted array of keys by key in the tree, or an empty array if tree is empty.
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
     * Get a sorted array by key of all info in the tree.
     * @return - Sorted array of info by key in the tree, or an empty array if tree is empty.
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
     * Get the number of nodes in the tree
     * @return - Returns the number of nodes in the tree.
     */
    public int size() {
        return this.getSize();
    }

    /**
     * Get the root node of the tree
     * @return - root, including virtual node if tree is empty.
     */
    public AVLNode getRootVirtual() {
        return this.root;
    }

    /**
     * Get the root node of the tree.
     * @return - if tree in not empty then AVLNode root, else null.
     */
    public AVLNode getRoot() {
        if (this.empty()) {
            return null;
        }
        return this.getRootVirtual();
    }

    /**
     * Set the root of the tree.
     * @param root - Node to set as root
     */
    private void setRoot(AVLNode root) {
        this.root = root;
    }

    /**
     * Get the min node of the tree.
     * @return - Min node, including virtual node if tree is empty.
     */
    public AVLNode getMin() {
        return this.minNode;
    }

    /**
     * Set the minimum node of the tree
     * @param node - Node to set as min.
     */
    private void setMin(AVLNode node) {
        this.minNode = node;
    }

    /**
     * Get the minimum node of the tree by calculation.
     * @return - Min node, including virtual node if tree is empty.
     */
    private AVLNode calcMin() {
        AVLNode newMin = this.getRootVirtual();
        if (!this.empty()) {
            AVLNode newMinLeft = newMin.getLeftVirtual();
            while (newMinLeft.isRealNode()) {
                newMin = newMinLeft;
                newMinLeft = newMin.getLeftVirtual();
            }
        }
        return newMin;
    }

    /**
     * Get the max node of the tree.
     * @return - Max node, including virtual node if tree is empty.
     */
    public AVLNode getMax() {
        return this.maxNode;
    }

    /**
     * Set the maximum node of the tree
     * @param node - Node to set as max.
     */
    private void setMax(AVLNode node) {
        this.maxNode = node;
    }

    /**
     * Get the maximum node of the tree by calculation.
     * @return - Max node, including virtual node if tree is empty.
     */
    private AVLNode calcMax() {
        AVLNode newMax = this.getRootVirtual();
        if (!this.empty()) {
            AVLNode newMaxRight = newMax.getRightVirtual();
            while (newMaxRight.isRealNode()) {
                newMax = newMaxRight;
                newMaxRight = newMax.getRightVirtual();
            }
        }
        return newMax;
    }

    /**
     * Set the size of the tree.
     * @param size - New size of tree.
     */
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Increase the size of the tree by one.
     */
    private void incrementSize() {
        this.setSize(this.getSize() + 1);
    }

    /**
     * Decrease the size of the tree by one.
     */
    private void decrementSize() {
        this.setSize(this.getSize() - 1);
    }

    /**
     * Get the size of the tree
     * @return - size of tree.
     */
    public int getSize() {
        return this.size;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     */
    public class AVLNode {

        private int key;
        private Boolean value;
        private AVLNode parent;
        private AVLNode left;
        private AVLNode right;
        private int height;
        private int booleanValueSum;

        private AVLNode(int key, Boolean value) {
            this.setKey(key);
            this.setValue(value);
            this.setLeft(new AVLNode(this));
            this.setRight(new AVLNode(this));
            this.setParent(null);
            this.updateFields();
        }

        private AVLNode() {
            this.setKey(-1);
            this.setValue(null);
            this.setLeft(null);
            this.setRight(null);
            this.setParent(null);
            this.updateFields();
        }

        private AVLNode(AVLNode parent) {
            this();
            this.setParent(parent);
        }

        /**
         * Set the node's key.
         * @param key - Key to set.
         */
        private void setKey(int key) {
            this.key = key;
        }

        /**
         * Get node's key
         * @return If real node return key, else -1.
         */
        public int getKey() {
            return this.key;
        }

        /**
         * Set the node's value
         * @param value - Value to set.
         */
        private void setValue(Boolean value) {
            this.value = value;
        }

        /**
         * Get the node's value.
         * @return - If real node return value, else null.
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * Sets the left child of node, if not virtual, else does nothing.
         * @param node - Node to set as left node.
         */
        private void setLeft(AVLNode node) {
            if (this.isRealNode()) {
                this.left = node;
            }
        }

        /**
         * Get the node's left child.
         * @return - If real node then return left child including virtual, else null.
         */
        public AVLNode getLeftVirtual() {
            return this.left;
        }

        /**
         * Get the node's left child.
         * @return - If left child exists return node, else null.
         */
        public AVLNode getLeft() {
            if (this.isRealNode() && this.left.isRealNode()) {
                return this.left;
            }
            return null;
        }

        /**
         * Sets the right child of node, if not virtual, else does nothing.
         * @param node - Node to set as right node.
         */
        private void setRight(AVLNode node) {
            if (this.isRealNode()) {
                this.right = node;
            }
        }

        /**
         * Get the node's right child.
         * @return - If real node then return right child including virtual, else null.
         */
        public AVLNode getRightVirtual() {
            return this.right;
        }

        /**
         * Get the node's left child.
         * @return - If left child exists return node, else null.
         */
        public AVLNode getRight() {
            if (this.isRealNode() && this.right.isRealNode()) {
                return this.right;
            }
            return null;
        }

        /**
         * Sets the parent of node.
         * @param node - Node to set as parent node.
         */
        private void setParent(AVLNode node) {
            this.parent = node;
        }

        /**
         * Get the node's parent.
         * @return - If parent exists return node, else null.
         */
        public AVLNode getParent() {
            return this.parent;
        }

        /**
         * Checks if this is a virtual node or real node.
         * @return If real node returns true, else false.
         */
        public boolean isRealNode() {
            return this.key != -1;
        }

        /**
         * Sets the height of the node.
         * @param height - New height to set.
         */
        private void setHeight(int height) {
            this.height = height;
        }

        /**
         * Update the height of the node
         */
        private void updateHeight() {
            setHeight(this.calcHeight());
        }

        /**
         * Calculate the height of the node.
         * @return - Calculated height of the node.
         */
        private int calcHeight() {
            if (this.isRealNode()) {
                return Math.max(this.left.getHeight(), this.right.getHeight()) + 1;
            }
            return -1;
        }

        /**
         * Get the height of the node.
         * @return - Return the height of the node if real node, else -1.
         */
        public int getHeight() {
            return this.height;
        }

        /**
         * Sets the booleanValueSum of the node.
         * @param booleanValueSum - booleanValueSum to set.
         */
        private void setBooleanValueSum(int booleanValueSum) {
            this.booleanValueSum = booleanValueSum;
        }

        /**
         * Update the field booleanValueSum.
         */
        private void updateBooleanValueSum() {
            setBooleanValueSum(this.calcBooleanValueSum());
        }

        /**
         * Calculate the booleanValueSum of the node.
         * @return - Calculated booleanValueSum of the node.
         */
        private int calcBooleanValueSum() {
            if (this.isRealNode()) {
                int ret = this.left.getBooleanValueSum() + this.right.getBooleanValueSum();
                ret += this.getValue().booleanValue() ? 1 : 0;
                return ret;
            }
            return 0;
        }

        /**
         * Get the booleanValueSum of the node.
         * @return booleanValueSum of the node.
         */
        private int getBooleanValueSum() {
            return this.booleanValueSum;
        }

        /**
         * Update all the field of the node.
         */
        private void updateFields() {
            this.updateHeight();
            this.updateBooleanValueSum();
        }

        /**
         * Calculate the balance factor of the node.
         * @return BF of node.
         */
        public int getBF() {
            if (this.isRealNode()) {
                return this.left.getHeight() - this.right.getHeight();
            }
            return -1;
        }

        /**
         * Remove all the connections of the node from its parents and its children.
         */
        private void resetConnections() {
            this.setLeft(new AVLNode(this));
            this.setRight(new AVLNode(this));
            this.setParent(null);
        }
    }
}

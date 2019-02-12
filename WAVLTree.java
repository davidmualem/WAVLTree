import java.util.Arrays;

/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree. (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	private WAVLNode root;
	public final  WAVLNode EXTERNAL_NODE = new WAVLNode();

	public WAVLTree() {
		this.root = null;
	}

	public WAVLTree(WAVLNode x) {
		this.root = x;
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		if (this.root == null)
			return true;
		return false;
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree otherwise,
	 * returns null
	 */
	public String search(int k) {
		if (this.empty())
			return null;

		return searchRec(this.root, k);

	}

	private String searchRec(WAVLNode x, int k) {

		if (x == EXTERNAL_NODE)
			return null;

		if (x.getKey() == k)
			return x.getValue();

		if (k < x.getKey())
			return searchRec(x.getLeft(), k);

		return searchRec(x.getRight(), k);

	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the WAVL tree. the tree must remain
	 * valid (keep its invariants). returns the number of rebalancing operations, or
	 * 0 if no rebalancing operations were necessary. returns -1 if an item with key
	 * k already exists in the tree.
	 */
	public int insert(int k, String i) {
		if (search(k) != null) //check if item is already in tree
			return -1;
		
		WAVLNode newNode = new WAVLNode(k, i);
		
		if (this.empty()) {
			this.root = newNode;
			return 0;
		}
			
		WAVLNode insertLoc = find_insert_loc(k) ; //find where to insert item. update sizes of ancestors while doing so. 
		
		if (k < insertLoc.getKey()) // insert new Item as proper child - left or right
			insertLoc.left = newNode;
		else 
			insertLoc.right = newNode;
		
		newNode.parent = insertLoc;
		
		if (isValid(insertLoc)) //check if the current state is valid for the insertion location
			return 0;
		
		return insertRebalance(insertLoc, 0); //rebalance the tree starting from where we inserted item, return rebalancing counter

	}

	/**
	 * private WAVLNode find_insert_loc(int k) {
	 *recursively searches for the place to insert an item with key k
	 *using private WAVLNode find_insert_loc_rec(WAVLNode node, int k) 
	 *returns the actual node
	 *
	 */
	
	private WAVLNode find_insert_loc(int k) {
		return  find_insert_loc_rec(this.root, k);
	}

	private WAVLNode find_insert_loc_rec(WAVLNode node, int k) {
		
		node.subtreeSize++;
		
		if (k < node.getKey()) {
			if (node.getLeft() == EXTERNAL_NODE) 
				return node;	
			return find_insert_loc_rec(node.left, k);
		}
		
		if (node.getRight() == EXTERNAL_NODE) 
			return node;	
		return find_insert_loc_rec(node.right, k);
	
	}

	private int insertRebalance(WAVLNode node, int count ) {
		node.rank++;
		count++;
		if (this.root == node)
			return count;
		
		if (isValid(node.parent))
			return count;
		
		if (isCaseA(node.parent)) { // Check if Edges are [0,1] or [1,0]
			
			return insertRebalance(node.parent, count);
		}
		
		if (node.parent.left == node) { // Check if your'e a left child 
			if (node.rank - node.left.rank == 1) { //Rank difference between Node and father is 2 
				rotateRight(node);
				count+= 2;
				return count;
			}
			else { //Rank difference between Node and father is 2 
				doubleRotateRight(node);
				count += 5;
				return count;
			}
		}
		else {  // I'm a Right Child
			if (node.rank - node.right.rank == 1) {
				rotateLeft(node);
				count+= 2;
				return count;
			}
			else {
				doubleRotateLeft(node);
				count += 5;
				return count;
			}
		}
	}
	

	private boolean isValid(WAVLNode node) {
		int[] rankDif = checkRanks(node);
		int[] legalDif1 = {1,1};
		int[] legalDif2 = {1,2};
		int[] legalDif3 = {2,1};
		int[] legalDif4 = {2,2};
		if (Arrays.equals(rankDif, legalDif1) || Arrays.equals(rankDif, legalDif2) ||
				Arrays.equals(rankDif, legalDif3) || Arrays.equals(rankDif, legalDif4)) {
			return true;
		}
		return false;
	}

	private boolean isCaseA(WAVLNode node) {
		int[] rankDiff = this.checkRanks(node);
		int[] caseA1 = {1,0};
		int[] caseA2 = {0,1};
		if (Arrays.equals(rankDiff, caseA1) || Arrays.equals(rankDiff, caseA2))
			return true;
		return false;
		
	}

	private int[] checkRanks(WAVLNode node) {
		int[] res = new int[2];
		res[0] = node.rank - node.left.rank;
		res[1]= node.rank - node.right.rank;
		return res;
		
	}

	private void rotateLeft(WAVLNode node) {
		WAVLNode temp = node.left;
		// updating node's grandparent about his new child: node. unless node's paernt is a root
		if (node.parent == this.root) {
			this.root = node;
		}
		else if (node.parent == node.parent.parent.left) {
			node.parent.parent.left = node;
		}
		else {
			node.parent.parent.right = node;
		}
		// updating the node's parent to be his current grandparent
		node.left = node.parent;
		node.parent = node.left.parent;
		//
		node.left.parent = node;
		node.left.right = temp;
		node.left.right.parent = node.left;
		// updating subtree sizes and node's original parent's rank
		node.left.subtreeSize = 1 + node.left.left.subtreeSize + node.left.right.subtreeSize;
		node.subtreeSize = 1 + node.left.subtreeSize + node.right.subtreeSize;
		node.left.rank--;
	}

	private void rotateRight(WAVLNode node) {
		WAVLNode temp = node.right;
		if (node.parent == this.root)
			this.root = node;
		else if (node.parent == node.parent.parent.left) // node parent is a right left child
			node.parent.parent.left = node;
		else // node parent is a right child 
			node.parent.parent.right = node;
		node.right = node.parent;
		node.parent = node.right.parent;
		node.right.parent = node;
		node.right.left = temp;
		node.right.left.parent = node.right;
		node.right.subtreeSize = 1 + node.right.right.subtreeSize + node.right.left.subtreeSize;
		node.subtreeSize = 1 + node.right.subtreeSize + node.left.subtreeSize;
		node.right.rank --;
	}

	private void doubleRotateRight(WAVLNode node) {
		rotateLeft(node.right);
		rotateRight(node.parent);
		node.parent.rank ++;
		
	}

	private void doubleRotateLeft(WAVLNode node) {
		rotateRight(node.left);
		rotateLeft(node.parent);
		node.parent.rank++;
		
	}
	
	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. returns -1 if an
	 * item with key k was not found in the tree.
	 */
	public int delete(int k) {
		
		if (empty()) return -1;
		
		WAVLNode node = find_delete_loc(k, false); // find insert location, don't decrease sizes up to it (yet)
		WAVLNode target = node;
		
		if (node == null) {
			return -1;
		}
		
		if (this.size() == 1) {
			this.root = null;
			return 0;
		}
		
		if (! node.isLeaf() && ! node.isUnary()) {
			WAVLNode successor = findSuccessor(node);
			target = find_delete_loc(successor.getKey(), true); // find our target again, this time decrease all sizes up to it
			replaceNodes(node, successor); // replaces nodes 
		}
		else {
			target = find_delete_loc(k, true); // find our target again, this time decrease all sizes up to it		
		}
		
		
		
		if (target == this.root) { // target is unary and root. take care of the situation by deleting it and replacing it with its one none EXT child
			if (target.left != EXTERNAL_NODE) {
				this.root = target.left;
				this.root.parent = null;
				return 0;
			}
			else {
				this.root = target.right;
				this.root.parent = null;
				return 0;
			}
		}
		
		WAVLNode targetsParent;
		
		if (target.isUnary()) { // target is not the root, and Unary
			targetsParent  = deleteUnary(target); // replaces target with the non External leaf child,  and returns  the parent of the target replaced.
			if (isValid(targetsParent)) return 0;
			return deleteRebalance(targetsParent, 0);
			}
		
		
		else { // target is not the root and a leaf 
		targetsParent = deleteLeaf(target); // deletes the target and returns the target's parent.
		
		if (targetsParent.isLeaf()) {
			targetsParent.rank = 0;
			if (targetsParent == this.root) return 1;
			else {
				if (isValid(targetsParent.parent)) return 1;
				return deleteRebalance(targetsParent.parent, 1);
			}
			
			
		}
		else { // Target's Parent is an Unary leaf
			if (isValid(targetsParent)) {
				return 0;
			}
			else return deleteRebalance(targetsParent, 0);
		}
		}}
		
		

	private WAVLNode deleteLeaf(WAVLNode node) {
		WAVLNode targetsParent = node.parent;
		if (targetsParent.left == node) {
			targetsParent.left = EXTERNAL_NODE;
			node.parent = null;
		}
		else {
			targetsParent.right = EXTERNAL_NODE;
			node.parent = null;
		}
		return targetsParent;
	}


	private int deleteRebalance(WAVLNode node, int count) {
		int[] rankDiff = this.checkRanks(node);
		int[] caseA1 = {2,3};
		int[]  caseA2 = {3,2};
		int[] caseB1 = {2,2};
		
		if (Arrays.equals(rankDiff, caseA1) || Arrays.equals(rankDiff, caseA2)){ //Case 1
			node.rank --; //demote
			count ++;
			if (node == this.root || isValid(node.parent))
				return count; 
			return deleteRebalance(node.parent, count);
		}
		
		WAVLNode child  = node.left;
		boolean child_is_left = true;
		if ((node.rank - child.rank) != 1) {
			child = node.right; // child is updated to the correct child of node 
			child_is_left = false;
		}
		
		int[] childRankDiff = this.checkRanks(child);
		
		if (Arrays.equals(childRankDiff, caseB1)){ // Case2 
			node.rank --; 
			child.rank --;
			count += 2;
			if (node == this.root || isValid(node.parent))
				return count; 
			return deleteRebalance(node.parent, count); 
			
		}
	
		if (child_is_left) {
			if (child.rank - child.left.rank == 1 ) { //case 3 , left child 
				this.rotateRight(child);
				child.rank ++;
				count += 3;
				
				int[] arr = this.checkRanks(child.right);
				if (Arrays.equals(arr, caseB1)) {
					child.right.rank --; // demote z again
					count ++;
				}
				return count;
			}
			else { // case4, left child 
				node.rank --;
				this.doubleRotateRight(child);
				child.parent.rank ++;
				count += 7;
				return count;
					
			}
		}
		else { // child is right child
			if (child.rank - child.right.rank == 1 ) { //case 3 , right  child 
				this.rotateLeft(child);
				child.rank ++;
				count += 3;
				
				int[] arr = this.checkRanks(child.left);
				if (Arrays.equals(arr, caseB1)) {
					child.left.rank --; // demote z again
					count ++;
				}
				return count;
			}
			else { // case4, right child 
				node.rank --;
				this.doubleRotateLeft(child);
				child.parent.rank ++;
				count += 7;
				return count;
			}
			
		}
	}
		
		
		
		
		
		
	
	
	private WAVLNode deleteUnary(WAVLNode node) {
		WAVLNode target = node.parent;
		if (node == target.right) {        //node is a right child 
			if (node.right != EXTERNAL_NODE) { //node's right child is not EXT
				target.right = node.right;
				node.right.parent = target;
				node.parent = null;
				node.right = null;
				return target;
			}
			else {//node's left child is not EXT
				target.right = node.left;
				node.left.parent = target;
				node.parent = null;
				node.left = null;
				return target;
			}}
			else { // node is a left child
				if (node.right != EXTERNAL_NODE) { //node's right child is not EXT
					target.left = node.right;
					node.right.parent = target;
					node.parent = null;
					node.right = null;
					return target;
				}
				else {//node's left child is not EXT
					target.left = node.left;
					node.left.parent = target;
					node.parent = null;
					node.left = null;
					return target;
				
			}}
				
			
	}

	private void replaceNodes(WAVLNode node, WAVLNode successor) {
		int tempKey = node.key;
		String tempInfo = node.info;
		node.key = successor.key;
		node.info = successor.info;
		successor.key = tempKey;
		successor.info = tempInfo;
		
	}

	private WAVLNode findSuccessor(WAVLNode node) {
		return minSuccesor(node.right);
	
	}

	private  WAVLNode minSuccesor(WAVLNode x) {
		if (x.getLeft() == EXTERNAL_NODE)
			return x;

		return minSuccesor(x.getLeft());
	}

	private WAVLNode find_delete_loc(int k, boolean update) {
		return  find_delete_loc_rec(this.root, k, update);
	}

	private WAVLNode find_delete_loc_rec(WAVLNode node, int k, boolean update) {
	
		
		if (update) {
		node.subtreeSize--;
		}
		if (node.getKey() == k) {
			return node;
		}

		if (k < node.getKey()) {
			if (node.getLeft() == EXTERNAL_NODE) 
				return null;	
			return find_delete_loc_rec(node.left, k, update);
		}
		
		if (node.getRight() == EXTERNAL_NODE) 
			return null;	
		return find_delete_loc_rec(node.right, k, update);
	
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty
	 */
	public String min() {
		if (this.empty())
			return null;
		return minNode(this.root);

	}

	private  String minNode(WAVLNode x) {
		if (x.getLeft() == EXTERNAL_NODE)
			return x.getValue();

		return minNode(x.getLeft());
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() {
		if (this.empty())
			return null;
		return maxNode(this.root);
	}

	private  String maxNode(WAVLNode x) {
		if (x.getRight() == EXTERNAL_NODE)
			return x.getValue();

		return maxNode(x.getRight());
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() {

		return inOrderArrayKeys(this.root);
	}

	/**
	 * private int[] inOrderArrayKeys(WAVLNode x)
	 *
	 * Recursively traverses in-order of the sub-tree of WAVLNode x creates an array
	 * of the keys and returns it
	 */

	private int[] inOrderArrayKeys(WAVLNode x) {
		if (x == EXTERNAL_NODE || x == null) {
			int[] arr = new int[0];
			return arr;
		}

		else {
			int[] left = inOrderArrayKeys(x.getLeft());
			int[] item = { x.getKey() };
			int[] added = addIntArrays(left, item);
			int[] right = inOrderArrayKeys(x.getRight());
			int[] res = addIntArrays(added, right);
			return res;
		}
	}

	/**
	 * private int[] addIntArrays(int[] a, int[] b)
	 *
	 * takes 2 arrays, a and b returns a new concatenated array, a + b
	 */
	private static int[] addIntArrays(int[] a, int[] b) {
		int[] res = new int[a.length + b.length];
		for (int i = 0; i < a.length; i++)
			res[i] = a[i];

		for (int j = 0; j < b.length; j++)
			res[a.length + j] = b[j];

		return res;

	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		return inOrderArrayInfo(this.root);
	}

	/**
	 * private String[] inOrderArrayInfo(WAVLNode x)
	 *
	 * Recursively traverses in-order of the sub-tree of WAVLNode x creates an array
	 * of the values and returns it
	 */
	private  String[] inOrderArrayInfo(WAVLNode x) {
		if (x == EXTERNAL_NODE || x == null) {
			String[] arr = new String[0];
			return arr;
		}

		else {
			String[] left = inOrderArrayInfo(x.getLeft());
			String[] item = { x.getValue() };
			String[] added = addStringArrays(left, item);
			String[] right = inOrderArrayInfo(x.getRight());
			String[] res = addStringArrays(added, right);
			return res;
		}
	}

	/**
	 * privateString[] addArray(int[] a, int[] b)
	 *
	 * takes 2 arrays, a and b returns a new concatenated array, a + b
	 */
	private static String[] addStringArrays(String[] a, String[] b) {
		String[] res = new String[a.length + b.length];
		for (int i = 0; i < a.length; i++)
			res[i] = a[i];

		for (int j = 0; j < b.length; j++)
			res[a.length + j] = b[j];

		return res;

	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 */
	public int size() {
		if (this.empty())
			return 0;
		return this.root.getSubtreeSize();
	}
	

	/**
	 * public WAVLNode getRoot()
	 *
	 * Returns the root WAVL node, or null if the tree is empty
	 *
	 */
	public WAVLNode getRoot() {
		return this.root;
	}

	/**
	 * public int select(int i)
	 *
	 * Returns the value of the i'th smallest key (return -1 if tree is empty)
	 * Example 1: select(1) returns the value of the node with minimal key Example
	 * 2: select(size()) returns the value of the node with maximal key Example 3:
	 * select(2) returns the value 2nd smallest minimal node, i.e the value of the
	 * node minimal node's successor
	 *
	 */
	public String select(int i) {
		
		if (i < 0 || i > this.size())
			return null;
		if (this.empty())
			return null;
		return select_rec(this.root, i - 1);

	}

	private static String select_rec(WAVLNode x, int i) {
		int r = x.getLeft().getSubtreeSize();
		if (i == r)
			return x.getValue();
		if (i < r)
			return select_rec(x.getLeft(), i);
		return select_rec(x.getRight(), i - r - 1);
	}


	/**
	 * public class WAVLNode
	 */
	public class WAVLNode {

		private WAVLNode parent = null;
		private WAVLNode left = null;
		private WAVLNode right = null;
		private int key;
		private String info;
		private int rank = -1;
		private int subtreeSize;

		public WAVLNode() {

		}

		public boolean isUnary() {
			if (this.left == EXTERNAL_NODE && this.right != EXTERNAL_NODE) 
				return true;
			if (this.left != EXTERNAL_NODE && this.right == EXTERNAL_NODE)
				return true;
			return false;
		}

		

		public WAVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			this.left = EXTERNAL_NODE;
			this.right = EXTERNAL_NODE;
			this.rank = 0;
			this.subtreeSize = 1;
		}

		public int getKey() {
			if (this == EXTERNAL_NODE)
				return -1;
			return this.key;
		}

		public String getValue() {
			if (this == EXTERNAL_NODE)
				return null;
			return this.info;
		}

		public WAVLNode getLeft() {
			return this.left;
		}

		public WAVLNode getRight() {
			return this.right;
		}

		public boolean isInnerNode() {
			if (this == EXTERNAL_NODE)
				return false;
			return true;
		}

		public boolean isLeaf() {
			if (this.left == EXTERNAL_NODE && this.right == EXTERNAL_NODE)
				return true;
			return false;
		}

		public int getSubtreeSize() {
			return this.subtreeSize;
		}
		public int getRank() {
			return this.rank;
		}
	}
	

}
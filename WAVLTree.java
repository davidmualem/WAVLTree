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

	private int insertRebalance(WAVLNode node, int count ) {
		node.rank++;
		count++;
		if (this.root == node)
			return count;
		if (isValid(node.parent))
			return count;
		
		if (isCaseA(node.parent))
			return insertRebalance(node.parent, count);
		
		if (node.parent.left == node) {
			if (node.rank - node.left.rank == 1) {
				rotateRight(node);
				count+= 2;
				return count;
			}
			else {
				doubleRotateRight(node);
				count += 2;
				return count;
			}
		}
		else {
			if (node.rank - node.right.rank == 1) {
				rotateLeft(node);
				count+= 2;
				return count;
			}
			else {
				doubleRotateLeft(node);
				count += 2;
				return count;
			}
		}
	}
	
	private void doubleRotateLeft(WAVLNode node) {
		// TODO Auto-generated method stub
		
	}

	private void rotateLeft(WAVLNode node) {
		// TODO Auto-generated method stub
		
	}

	public void rotateRight(WAVLNode node) {
		WAVLNode temp = node.right;
		if (node.parent == this.root)
			this.root = node;
		else if (node.parent == node.parent.parent.left)
			node.parent.parent.left = node;
		else
			node.parent.parent.right = node;
		node.right = node.parent;
		node.parent = node.right.parent;
		node.right.parent = node;
		node.right.left = temp;
		node.right.subtreeSize = 1 + node.right.right.subtreeSize + node.right.left.subtreeSize;
		node.subtreeSize = 1 + node.right.subtreeSize + node.left.subtreeSize;
		node.right.rank --;
		
		
	}

	private void DoubleRotateRight(WAVLNode node) {
		// TODO Auto-generated method stub
		
	}

	private boolean isCaseA(WAVLNode node) {
		int[] rankDiff = checkRanks(node);
		int[] caseA1 = {1,0};
		int[] caseA2 = {0,1};
		if (rankDiff.equals(caseA1) || rankDiff.equals(caseA2))
			return true;
		return false;
		
	}

	private int[] checkRanks(WAVLNode node) {
		int[] res = new int[2];
		res[0] = node.rank - node.left.rank;
		res[1]= node.rank - node.right.rank;
		return res;
		
	}

	private boolean isValid(WAVLNode node) {
		int[] rankDif = checkRanks(node);
		int[] legalDif1 = {1,1};
		int[] legalDif2 = {1,2};
		int[] legalDif3 = {2,1};
		int[] legalDif4 = {2,2};
		if (rankDif.equals(legalDif1) || rankDif.equals(legalDif2) || rankDif.equals(legalDif3) || rankDif.equals(legalDif4)) {
			return true;
		}
		return false;
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

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. returns -1 if an
	 * item with key k was not found in the tree.
	 */
	public int delete(int k) {
		return 42; // to be replaced by student code
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
		if (x == EXTERNAL_NODE) {
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
		if (x == EXTERNAL_NODE) {
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
		return this.root.getSubtreeSize();
	}
	

	/**
	 * public WAVLNode getRoot()
	 *
	 * Returns the root WAVL node, or null if the tree is empty
	 *
	 */

	public void printTree() {
		
		int[] arr; 
		arr = preOrderArrayKeys(this.root);
		System.out.println(Arrays.toString(arr));

	}

	private  int[] preOrderArrayKeys(WAVLNode x) {
		if (x == EXTERNAL_NODE) {
			int[] arr = new int[0];
			return arr;
		}

		else {
			int[] item = { x.getKey() };
			int[] left = preOrderArrayKeys(x.getLeft());
			int[] added = addIntArrays(item, left);
			int[] right = preOrderArrayKeys(x.getRight());
			int[] res = addIntArrays(added, right);
			return res;
		}

	}

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
		return select_rec(this.root, i);

	}

	public static String select_rec(WAVLNode x, int i) {
		int r = x.getLeft().getSubtreeSize();
		if (i == r)
			return x.getValue();
		if (i < r)
			return select_rec(x.getLeft(), i);
		return select_rec(x.getRight(), i - r - 1);
	}

	public void dumbInsert(WAVLNode node) {
		if (this.empty()) 
			this.root = node;
		
		else {
			WAVLNode x = this.getRoot();
			while (true) {
				if (node.key > x.key) {
					x.subtreeSize ++;
					if (x.right == EXTERNAL_NODE) {
						x.right = node;
						node.parent = x;
						return;
					}
					x = x.right;
				} else {
					x.subtreeSize ++;
					if (x.left == EXTERNAL_NODE) {
						x.left = node;
						node.parent = x;
						return;
					}
					x = x.left;
				}
			}
		}
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
			if (this != EXTERNAL_NODE)
				return true;
			return false;
		}

		public boolean isLeaf() {
			if (this.left == EXTERNAL_NODE && this.right == EXTERNAL_NODE)
				return true;
			return false;
		}

		public int getSubtreeSize() {
			return this.subtreeSize;
		}
	}

}
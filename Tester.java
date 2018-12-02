import java.util.Arrays;

public class Tester {
	public static void main(String[] args) {
		WAVLTree tree = new WAVLTree();
		System.out.println("tree is empty: " + tree.empty());
		tree.dumbInsert(tree.new WAVLNode(10, "10"));
		tree.dumbInsert(tree.new WAVLNode(5, "5"));
		//tree.dumbInsert(tree.new WAVLNode(15, "15"));
		tree.dumbInsert(tree.new WAVLNode(2, "2"));
		tree.dumbInsert(tree.new WAVLNode(12, "12"));
		tree.dumbInsert(tree.new WAVLNode(6, "6"));
		//System.out.println(tree.insert(-3, "6"));
		tree.printTree();
		tree.rotateRight(tree.getRoot().getLeft());
		tree.printTree();
		
		
		
		
		
		
		
		System.out.println("Tree is empty: " + tree.empty());
		System.out.println("Tree root is: " + tree.getRoot().getValue());
		System.out.println("Tree min is: " + tree.min());
		System.out.println("Tree max is: " + tree.max());
		System.out.println("Keys To Array: " + Arrays.toString(tree.keysToArray()));
		System.out.println("Info To Array: " + Arrays.toString(tree.infoToArray()));
		System.out.println("Tree size is: " + tree.size());
	
		System.out.println("select(0) :" + tree.select(0));
		System.out.println("select(2) :" + tree.select(2));
		System.out.println("select(4) :" + tree.select(4));
		System.out.println("search for 10:" + tree.search(10));
		System.out.println("search for 7:" + tree.search(7));
		System.out.println("search for 12:" + tree.search(12));

		
	}
}

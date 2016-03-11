import java.util.Scanner;



/**
 * Tests different cases dealing with the SplayBST class
 * Shows for each statement support
 * 22 different test cases
 * @author Mike Mekker
 */
public class BSTSplayTreeTest {
	public static void main(String[] args) {
		SplayBST<String> tree = new SplayBST<String>();
		Scanner scan = new Scanner(System.in);
		boolean run = true;
		int choice = 0;
		/*tree.add("a");
		tree.add("g");
		tree.add("d");
		tree.add("t");
		tree.add("j");
		tree.add("s");
		tree.add("r");
		tree.add("k");
		tree.printTree();
		System.out.println("\n");
		for(String s : tree)
		{
			System.out.println(s);
		}*/
		while(run)
		{
			System.out.println("Enter a test case number.");
			System.out.println("0: exit, 1: tree.add left left, 2: tree.add right right, 3: tree.add left right, 4: tree.add right left,");
			System.out.println("5: tree.add right, 6: tree.add left, 7: tree.add to empty, 8: tree.search tree.root, 9: tree.search empty tree,");
			System.out.println("10: tree.search right with no subtrees, 11: tree.search right with right subtree,");
			System.out.println("12: tree.search right with left subtree, 13: tree.search right with left and right subtrees,");
			System.out.println("14: tree.search left with no subtrees, 15: tree.search left with right subtree,");
			System.out.println("16: tree.search left with left subtree, 17: tree.search left with left and right subtrees,");
			System.out.println("18: tree.search left left, 19: tree.search left right, 20: tree.search right right, 21: tree.search right left");
			System.out.println("22: tree.search for string not in tree");
			choice = scan.nextInt();
			if(choice == 0)
			{
				run = false;
			}
			else
			{
				testCase(tree, choice);
			}
		}
		scan.close();
	}
	/**
	 * This tests 22 different cases for the SplayBST class
	 * 1: tree.add left left
	 * 2: tree.add right right
	 * 3: tree.add left right
	 * 4: tree.add right left
	 * 5: tree.add right
	 * 6: tree.add left
	 * 7: tree.add to empty
	 * 8: tree.search tree.root
	 * 9: tree.search empty tree
	 * 10: tree.search right with no subtrees
	 * 11: tree.search right with right subtree
	 * 12: tree.search right with left subtree
	 * 13: tree.search right with left and right subtrees
	 * 14: tree.search left with no subtrees
	 * 15: tree.search left with right subtree
	 * 16: tree.search left with left subtree
	 * 17: tree.search left with left and right subtrees
	 * 18: tree.search left left
	 * 19: tree.search left right
	 * 20: tree.search right right
	 * 21: tree.search right left
	 * 22: tree.search for string not in tree
	 * @param tree tree to be tested
	 * @param i test case being tested
	 */
	public static void testCase(SplayBST<String> tree, int i)
    {
    	switch(i)
    	{
    	//tree.add Tests
    	case 1: //tree.adds "a" as a left left case
    		tree.root = new Node<String>("c");
    		tree.root.left = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"a\"");
			System.out.println("Should be:");
			System.out.println("|	|-------c");
			System.out.println("|-------b");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.add("a");
			tree.printTree();
			System.out.println("\n");
    		break;
    	case 2://tree.add "c" as a right right case
			tree.root = new Node<String>("a");
			tree.root.right = new Node<String>("b");
			System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"c\"");
			System.out.println("Should be:");
			System.out.println("c");
			System.out.println("|-------b");
			System.out.println("|	|-------a");
			System.out.println("Actually is:");
			tree.add("c");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 3://tree.add "b" as a left right case
    		tree.root = new Node<String>("c");
			tree.root.left = new Node<String>("a");
			System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.add("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 4: //tree.add "b" as right left case
    		tree.root = new Node<String>("a");
			tree.root.right = new Node<String>("c");
			System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.add("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 5: //tree.add "b" as right
    		tree.root = new Node<String>("a");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"b\"");
			System.out.println("Should be:");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.add("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 6: //tree.add "a" as left
    		tree.root = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"a\"");
			System.out.println("Should be:");
			System.out.println("|-------b");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.add("a");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 7: //tree.add "a" to empty
    		tree.root = null;
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.add \"a\"");
			System.out.println("Should be:");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.add("a");
			tree.printTree();
			System.out.println("\n");
			break;
			
		//tree.search Tests
    	case 8: //tree.search for tree.root
    		tree.root = null;
    		tree.add("a");
    		tree.add("c");
    		tree.add("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 9: //tree.search empty tree
    		tree.root = null;
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"a\"");
			System.out.println("Should be:");
			System.out.println("NULL");
			System.out.println("Actually is:");
			tree.search("a");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 10: //tree.search right with no subtrees
    		tree.root = null;
    		tree.add("b");
    		tree.add("a");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 11: //tree.search right with right subtree
    		tree.root = null;
    		tree.add("c");
    		tree.add("b");
    		tree.add("a");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 12: //tree.search right with left subtree
    		tree.root = new Node<String>("a");
    		tree.root.right = new Node<String>("c");
    		tree.root.right.left = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"c\"");
			System.out.println("Should be:");
			System.out.println("c");
			System.out.println("|	|-------b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("c");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 13: //tree.search right with left and right subtrees
    		tree.root = new Node<String>("a");
    		tree.root.right = new Node<String>("c");
    		tree.root.right.left = new Node<String>("b");
    		tree.root.right.right = new Node<String>("d");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"c\"");
			System.out.println("Should be:");
			System.out.println("|-------d");
			System.out.println("c");
			System.out.println("|	|-------b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("c");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 14: //tree.search left with no subtrees
    		tree.root = null;
    		tree.add("a");
    		tree.add("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"a\"");
			System.out.println("Should be:");
			System.out.println("|-------b");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.search("a");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 15: //tree.search left with right subtree
    		tree.root = new Node<String>("c");
    		tree.root.left = new Node<String>("a");
    		tree.root.left.right = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"a\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("|	|-------b");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.search("a");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 16: //tree.search left with left subtree
    		tree.root = null;
    		tree.add("a");
    		tree.add("b");
    		tree.add("c");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 17: //tree.search left with left and right subtrees
    		tree.root = new Node<String>("d");
    		tree.root.left = new Node<String>("b");
    		tree.root.left.left = new Node<String>("a");
    		tree.root.left.right = new Node<String>("c");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------d");
			System.out.println("|	|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 18: //tree.search left left
    		tree.root = null;
    		tree.add("a");
    		tree.add("b");
    		tree.add("c");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"a\"");
			System.out.println("Should be:");
			System.out.println("|	|-------c");
			System.out.println("|-------b");
			System.out.println("a");
			System.out.println("Actually is:");
			tree.search("a");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 19: //tree.search left right
    		tree.root = null;
    		tree.root = new Node<String>("c");
    		tree.root.left = new Node<String>("a");
    		tree.root.left.right = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 20: //tree.search right right
    		tree.root = null;
    		tree.add("c");
    		tree.add("b");
    		tree.add("a");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"c\"");
			System.out.println("Should be:");
			System.out.println("c");
			System.out.println("|-------b");
			System.out.println("|	|-------a");
			System.out.println("Actually is:");
			tree.search("c");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 21: //tree.search right left
    		tree.root = new Node<String>("a");
    		tree.root.right = new Node<String>("c");
    		tree.root.right.left = new Node<String>("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"b\"");
			System.out.println("Should be:");
			System.out.println("|-------c");
			System.out.println("b");
			System.out.println("|-------a");
			System.out.println("Actually is:");
			tree.search("b");
			tree.printTree();
			System.out.println("\n");
			break;
    	case 22: //tree.search string thats not in the tree
    		tree.root = null;
    		tree.add("a");
    		tree.add("c");
    		tree.add("b");
    		System.out.println("Before:");
			tree.printTree();
			System.out.println("tree.search \"d\"");
			System.out.println("Should be:");
			System.out.println("c");
			System.out.println("|-------b");
			System.out.println("|	|-------a");
			System.out.println("Actually is:");
			tree.search("d");
			tree.printTree();
			System.out.println("\n");
			break;
    	default:
    		System.out.println("No test case with that number.");
    		break;
    		
    	}
    }
	
}

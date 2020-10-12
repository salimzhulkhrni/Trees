/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salim
 */

/*

Given a binary search tree and a node in it, find the in-order successor of that node in the BST.

The successor of a node p is the node with the smallest key greater than p.val.

    2
   / \
  1  3

Example 1:


Input: root = [2,1,3], p = 1
Output: 2
Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
Example 2:

      5
     / \
    3   6
   / \
  2  4
 / 
1  

Input: root = [5,3,6,2,4,null,null,1], p = 6
Output: null
Explanation: There is no in-order successor of the current node, so the answer is null.
 

Note:

If the given node has no in-order successor in the tree, return null.
It's guaranteed that the values of the tree are unique.

*/

public class BST_Inorder_Successor {

    /**
     * @param args the command line arguments
     */
    
    static class TreeNode{
        
        TreeNode left;
        TreeNode right;
        int val;
        
        TreeNode(int val){
            
            this.val = val;
        }
               
    }
    
    /*
    
    Logic 1: Traverse inorderly and store it in an array. Get the element next to the node being searched for. That will give the inorder successor, since all the elements are stored in sorted order
             Time: O(N) | Space: O(N)
    Logic 2:
             Think about it. In a BST, elements to the left are smaller than root & elements to the right are greater than the root. So, where can the in-order successor lie? (smallest element greater than the current element)
             
             Ans: In-order successor can lie either on the right or on the left(also in the root). And what determines that? See below:
    
                i) If the node has a right-subtree, then in-order successor will be the smallest(left-most) element in the right subtree
                    Why?  because all elements in its right-subtree will be greater than the element-'p' ( whose in-order successor needs to be found). 
                          So, out of those greater elements in the right, the smallest greater element(whose in-order successor needs to be found ) will lie in the "left-most node of the right-subtree"
    
                ii) If the node does not have a right-subtree, then the in-order successor will be most-recent node(ancestor) from where we took a most-recent left-path.
                    Why? Consider the below tree
                               7
                              / \
                             4   8
                            / \   \ 
                           3   5   20
                                  /  \
                                 11  25
    
                    a) To find the inorder successor of 3 & (3 does not have a right-subtree, so the in-order successor can lie either on the left or on the root )
                       It follows the path -       7
                                                  /
                                                 4  (this is the most recent node from where we took the left-path) = in-order successor = "4"
                                                /
                                               3 
                      Implying: If it always appears on the left-side, following left-path entirely, in that case, "immediate(most-recent root) will be the in-order succesor"
    
                    b) To find the inorder successor of 5 & (5 does not have a right-subtree, so the in-order successor can lie either on the left or on the root )
                      It follows the path -    7 (this is the most recent node from where we took the left-path) = in-order successor = "7"
                                              /
                                             4 
                                              \
                                               5
                      If the path is carefully noticed, 5 appears on the right-subtree of 4. We might be tempted to apply case i) here, but thats not true, since we are not finding in-order successor for node-4 on which case i) can be applied, but trying to find in-order successor of node -5 which case ii) holds true since it node-5 has no right subtree.
                      So, from above, in-order successor will either be in the left or in the root(up).
    
                      And we cannot have 4 as in-order succesor of 5, since 4 < 5, so immeditate(most-recent) root cannot be the in-order successor like a)
    
                      So, the in-order successor of 5, ie. element greater than 5 will be on "the most-recent root from where we took a most-recent left-path".
                      In the above example, we took most-recent left path from 7, which is the in-order successor for 5
    
                      But why only the 'most-recent left-path'?
                          Because when an element appears on the left, means that element is smaller than the root(from where we took a left). So this root becomes one of the candidates for in-order successor
    
                      Implying: If it appears on the left-side & right side, following left & right path alternaely, in that case, "immediate(most-recent root) from where we took the most-recent left-path will be the in-order succesor"
                        
                      Time: O(N), Space: O(1) 
    
    
    */
    
    public TreeNode findInorder(TreeNode root, TreeNode p){
        
        if(root == null || p == null)
            return null;
        
        if(p.right != null)   // has a right sub-tree - case i)
            return findInorderWithRight(p.right, null);
        else
            return findInorderWithoutRight(root, p, null);  // does not have a right subtree - case ii)
        
    }
    
    private TreeNode findInorderWithRight(TreeNode root, TreeNode prev){
        
        // case i) - has a right subtree
        // keep moving to the left while marking prev node. Once we reach null, we know that prev-node will be the in-order successor
        
         if(root == null)
             return prev;
         
         return findInorderWithRight(root.left, root);  // prev = root in the next recursive call to track of the previous node
    
    }
    
    private TreeNode findInorderWithoutRight(TreeNode root, TreeNode p, TreeNode prev){
        
        // case ii) - does not have a right-subtree
        // it follows left & right path
        // keeps following only a) (left-path only) - immediate(most-recent) root will be the in-order successor
        // keeps following only b) (left & right path) - immediate(most-recent) root from where we took the most-recent left path will be the in-order successor
        
        if(root == null)   // we do not find the node - 'p' that we are searching for
            return null;
        
        if(p.val < root.val) //keeps following only a) (left-path only) - immediate(most-recent) root will be the in-order successor
            return findInorderWithoutRight(root.left, p, root);   // prev=root, to track the most recent root
        
        else if(p.val > root.val)
            return findInorderWithoutRight(root.right, p, prev); // keeps following only b) (left & right path) - immediate(most-recent) root from where we took the most-recent left path will be the in-order successor
                                                                 // prev keet it unchanged since it will hold the most-recent root from where the most-recent left-path was taken
        else
            return prev;  // on reaching the node, prev will hold the most-recent node adhering to either a) or b)
    }
    
    public static void main(String args[]) {
        // TODO code application logic here
        
        /*
                               7
                              / \
                             4   8
                            / \   \ 
                           3   5   20
                                  /  \
                                 11  25
        
        */
        
        
        TreeNode seven = new TreeNode(7);
        TreeNode root = seven;
        TreeNode four = new TreeNode(4);
        TreeNode three = new TreeNode(3);
        TreeNode five = new TreeNode(5);
        seven.left = four;
        four.left = three;
        four.right = five;
        TreeNode eight = new TreeNode(8);
        seven.right = eight;
        TreeNode twenty = new TreeNode(20);
        eight.right = twenty;
        twenty.left = new TreeNode(11);
        twenty.right = new TreeNode(25);
        
        BST_Inorder_Successor obj = new BST_Inorder_Successor();
        
        System.out.println("sol1: " + (obj.findInorder(root, three) == four));
        System.out.println("sol2: " + (obj.findInorder(root, five) == seven));
        
        
        
        
        
    }
}

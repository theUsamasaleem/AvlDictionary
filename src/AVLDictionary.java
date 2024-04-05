//DSA Final Project
//        Dictionary implementation Using AVL Tree
//        Name: Usama saleem
//        Fa19-BSCS-0046

//Build a system to maintain information in a dictionary.

public class AVLDictionary<E, K extends Sortable> implements Dictionary<E, K> {

    //b) Insert a new word from user with all its associated data (up to three
    //meanings).


    AVLNode<E, K> root; // the root of the AVL Tree.
    private static final int BALANCED = 2;
    private static final int MORELEFT = 1;
    private static final int MORERIGHT = 3;
    public boolean debugging = true;

    public AVLDictionary() {
        this(null); //calls the other constructor.
    }

    public AVLDictionary(AVLNode<E, K> root) {

        this.root = root;
    }

	public AVLNode<E, K> balance(AVLNode<E, K> node) {
		int balanceFactor = node.getBalance();
		if(balanceFactor > MORERIGHT) {

			AVLNode<E, K> rightNode = copyNode(node.getRight());
			if(rightNode == null) return node;
			if(rightNode.getBalance() == MORELEFT) {
				//right-left rotate
				node = rotateRIGHTLEFT(copyNode(node));
				node = rotateRIGHTRIGHT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
			else if (rightNode.getBalance() >= MORERIGHT){
				//right-right rotate
				node = rotateRIGHTRIGHT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
		}
		else if(balanceFactor < MORELEFT) {
			//need to do a left rotate
			AVLNode<E, K> leftNode = copyNode(node.getLeft());
			if(leftNode == null) return node;
			if (leftNode.getBalance() == MORERIGHT) {
				//left-right rotate
				node = rotateLEFTRIGHT(copyNode(node));
				node = rotateLEFTLEFT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
			else if(leftNode.getBalance() <= MORELEFT) {
				//left-left rotate
				node = rotateLEFTLEFT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
		}
		return copyNode(node);
	}

	public AVLNode<E, K> copyNode(AVLNode<E, K>  node) {
		//simply calls the new constructor.
	    if(node != null) {
	        return new AVLNode<E, K>(node.getKey(), node.getElement(), node.getLeft(), node.getRight(), node.getBalance());
	    }
	    else return null;
	}

    public void delete(K key) {
    	//calls the recursive delete recursive
        this.root = deleteRecursive(root, key);
    }

    public AVLNode<E, K> deleteDoubleNode(AVLNode<E, K> node) {
        if(node.getLeft() == null) {
            //at the bottom of the nodes.
            return node.getRight();
        }
        else {
            //set the left node as the right of the one we found at the bottom.
            node.setLeft(deleteDoubleNode(node.getLeft()));
        }
        return node;
    }

    public AVLNode<E, K> deleteRecursive(AVLNode<E,K> node, K key) {
        //you are at the node you want to delete
        if(key.compareTo(node.getKey()) == 0) {
            //if its a leaf
            if((node.getLeft() == null) && (node.getRight() == null)) {
                return null; 
            }
            //it is a node with one child on the right
            else if((node.getLeft() == null) && (node.getRight() != null)) {
                return node.getRight();
            }
            //it is a node with one child on the left
            else if((node.getLeft() != null) && (node.getRight() == null)) {
                return node.getLeft();
            }

            else if((node.getLeft() != null) && (node.getRight() != null)) {
                //the replacement node is the least node which is still greater than the one deleted.
                AVLNode<E, K> replacementNode = findMin(node.getRight()); 
                AVLNode<E, K> backupLeft = node.getLeft(); //save the left nodes
                //the min-value node's right is now set as all the right nodes minus itself.
                replacementNode.setRight(deleteDoubleNode(node.getRight())); 
                replacementNode.setLeft(backupLeft); //replace the left nodes as the regular left nodes
                return replacementNode; //return the new least "greater" node.
            }
        }
        //if the key is still less than the node we are at...
        else if(key.compareTo(node.getKey()) < 0) {
            if(node.getLeft() != null) {
                //keep looking but to the left of this node
                node.setLeft(copyNode(deleteRecursive(node.getLeft(), key)));
                node = balance(copyNode(node)); //rebalance tree from this node
            }
        }
        //if the key is still greater than the node we are at...
        else {
            if(node.getRight() != null) {
                //keep looking but to the right of this node
                node.setRight(copyNode(deleteRecursive(node.getRight(), key)));
                node = balance(copyNode(node)); //rebalance tree from this node
            }
        }
        return node; //return the regular node if all else fails (failsafe, most likely won't be reached).
    }

    public int depth() {
        return postorder_depth(root, 0);
    }

    public AVLNode<E, K> findMin(AVLNode<E, K> node) {
    	//continually gets the leftmost node until it can't any longer.
        while(node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    public void inorder(AVLNode<E,K> node) {
        if(node != null) {
            inorder(node.getLeft());
            System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
            inorder(node.getRight());
        }
    }

    public void insert(K key, E element) {

        if(root == null) {
            root = new AVLNode<E, K>(key, element, null, null, BALANCED);
        }

        else {
            root = copyNode(insertBelow(root, key, element));
        }
    }

    public AVLNode<E, K> insertBelow(AVLNode<E, K> node, K key, E element) {
    	if(node == null) {
    		return new AVLNode<E, K>(key, element, null, null, 2);
    	}
    	else if(key.compareTo(node.getKey()) == 0) {
    		return node; // they are equal.
    	}
    	else if(key.compareTo(node.getKey()) < 0) {
    	    if(node.getLeft() == null) {
    		    node.setLeft(new AVLNode<E, K>(key, element, null, null, 2));
    		}
    		else {

    			node.setLeft(copyNode(insertBelow(copyNode(node.getLeft()), key, element)));
    		}

    		node.setBalance(node.getBalance()-1);
    	}
    	else if(key.compareTo(node.getKey()) > 0) {
    	    if(node.getRight() == null) {

    			node.setRight(new AVLNode<E, K>(key, element, null, null, 2));
    		}
    		else {

    			node.setRight(copyNode(insertBelow(copyNode(node.getRight()), key, element)));
    		}

    		node.setBalance(node.getBalance()+1);
    	}
    	return copyNode(balance(node));
}

    int postorder_depth(AVLNode<E,K> node, int current_depth) {
        if(node != null) {
            //get the max between two sub-trees, recursively, finding the max between all the sub-trees, or the entire tree itself.
            return Math.max(postorder_depth(node.getLeft(), current_depth+1), postorder_depth(node.getRight(), current_depth+1));
        }
        else return current_depth;
    }

    public void printTree() {
        System.out.println("\nPrinting the AVL Tree below...");
        inorder(root);
    }

	public AVLNode<E, K> rotateLEFTLEFT(AVLNode<E, K> node) {

		AVLNode<E, K> newCenter = copyNode(node.getLeft());
		AVLNode<E, K> newRight = copyNode(node);

		newRight.setLeft(copyNode(newCenter.getRight()));
		newRight.setBalance(2);
		newCenter.setRight(copyNode(newRight));
		newCenter.setBalance(2);
		return newCenter;
	}

	public AVLNode<E, K> rotateLEFTRIGHT(AVLNode<E, K> node) {

		AVLNode<E, K> newTop = copyNode(node);
		AVLNode<E, K> newCenter = copyNode(node.getLeft().getRight());
		AVLNode<E, K> newBottom = copyNode(node.getLeft());
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		if(newCenter == null) return node;
		newBottom.setRight(copyNode(newCenter.getLeft()));
		newBottom.setBalance(2); //set it to be balanced.
		newCenter.setLeft(copyNode(newBottom));
		newCenter.setBalance(1); //set it to be more left
		newTop.setLeft(copyNode(newCenter));
		newTop.setBalance(0); //extra more left
		return newTop; //return the newly rotated subtree
	}

	public AVLNode<E, K> rotateRIGHTLEFT(AVLNode<E, K> node) {
		AVLNode<E, K> newTop = copyNode(node);
		AVLNode<E, K> newCenter = copyNode(node.getRight().getLeft());
		AVLNode<E, K> newBottom = copyNode(node.getRight());
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		if(newCenter == null) return node; //make sure this isn't null
		newBottom.setLeft(copyNode(newCenter.getRight()));
		newBottom.setBalance(2); //set to balanced
		newCenter.setRight(copyNode(newBottom));
		newCenter.setBalance(3); //more right
		newTop.setRight(copyNode(newCenter)); //set the new right
		newTop.setBalance(4); // extra extra right
		return newTop; //return the newly rotated subtree
	}

	public AVLNode<E, K> rotateRIGHTRIGHT(AVLNode<E, K> node) {
		//get the new nodes (as copies) to modify and rotate.
		AVLNode<E, K> newCenter = copyNode(node.getRight());
		AVLNode<E, K> newLeft = copyNode(node);

		if(newCenter == null) return node; //make sure its not null
		newLeft.setRight(copyNode(newCenter.getLeft()));
		newLeft.setBalance(2); //set to balanced
		newCenter.setLeft(copyNode(newLeft)); 
		newCenter.setBalance(2); //set to balanced
		return newCenter; //return the newly rotated subtree
	}

    public E search(K key) {
        AVLNode<E, K> nodeFound;
        nodeFound = searchNode(key);
        if(nodeFound == null) {
            return null; //not found
        }
        else return searchNode(key).getElement(); //call another helper method.
    }

    public AVLNode<E,K> searchBelow(AVLNode<E, K> node, K key) {
        if(node == null) {
            return null;
        }

        if(key.compareTo(node.getKey()) == 0) {
            return node;
        }
        // if the key we are looking for is greater than the key
        // at the node we are at, then we must go down the right child
        // to continue the search.
        else if (key.compareTo(node.getKey()) > 0) {
            return searchBelow(node.getRight(), key);
        }
        // if the key we are looking for is less than the key
        // at the node we are at, then we must go down the left child
        // to continue the search.
        else if(key.compareTo(node.getKey()) < 0) {
            return searchBelow(node.getLeft(), key);
        }
        else return null;
    }

    public AVLNode<E, K> searchNode(K key) {
        if(key == null) {
            return null; //looking for nothing
        }
        if(root == null) {
            return null; //nothing in the root, empty tree.
        }
        //if the keys are equal
        if(key.compareTo(root.getKey()) == 0) {
            return root;
        }
        //if this key is less than the root's key
        else if(key.compareTo(root.getKey()) < 0) {
            return searchBelow(root.getLeft(), key); //call the recursive search method.
        }
        //if this key is greater than the root's key
        else if(key.compareTo(root.getKey()) > 0) {
            return searchBelow(root.getRight(), key); //call the recursive search method.
        }
        else {
            //this case should never be reached.
            System.out.println("Something went wrong. ERROR #1");
            return null;
        }
    }
}


package netcracker.tree;

import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ВладПК
 */
public class TreeController implements ITreeController{
    private static TreeController instance;

    private TreeController(){}
    
    public static synchronized TreeController getInstance(){
        if(instance == null)
            instance = new TreeController();
        return instance;
    }


    @Override
    public ITreeNode create(int key, Object data) {
        if(data!=null){
            TreeNode tree = new TreeNode(key, data);
            TreePool.getInstance().put(tree);
            return tree;
        }
        return null;
    }

    @Override
    public ITreeNode addNode(ITreeNode parent, int key, Object data) {
        ITreeNode root = null;
        if(parent!=null){
            root = parent;
            if(key>parent.getKey()) {
                ITreeNode node = addNode(parent.getRight(), key, data);
                root.setRight(node);
                node.setParent(root);
            }
            else
            if(key<parent.getKey()) {
                ITreeNode node = addNode(parent.getLeft(), key, data);
                root.setLeft(node);
                node.setParent(root);
            }
            else
                parent.setData(data);
        }
        else{
            root = new TreeNode(key, data, null, null);
        }
        return root;
    }

    @Override
    public void removeNode(ITreeNode root, int key){
        if ( root == null )
            return;
        else{
            if ( key > root.getKey() ) removeNode(root.getRight(), key);
            else if ( key < root.getKey() ) removeNode(root.getLeft(), key);
            else if ( key == root.getKey() ){
                if ( root.getLeft() == null && root.getRight() == null ) {
                    if (root.getParent().getRight() == root) root.getParent().setRight(null);
                    if (root.getParent().getLeft() == root) root.getParent().setLeft(null);
                }
                else if ( root.getLeft() == null || root.getRight() == null ){
                    if (root.getLeft() == null){
                        if ( root.getParent().getRight() == root) root.getParent().setRight(root.getRight());
                        if ( root.getParent().getLeft() == root) root.getParent().setLeft(root.getRight());
                        root.getRight().setParent(root.getParent());
                    }
                    else if (root.getRight() == null){
                        if (root.getParent().getRight() == root) root.getParent().setRight(root.getLeft());
                        if (root.getParent().getLeft() == root) root.getParent().setLeft(root.getLeft());
                        root.getLeft().setParent(root.getParent());
                    }
                }
                else if ( root.getLeft() != null && root.getRight() != null ){
                    if (root.getRight().getLeft() == null){
                        root.setData(root.getRight().getData());
                        root.setKey(root.getRight().getKey());
                        root.setRight(root.getRight().getRight());
                        root.getRight().setParent(root);
                    }
                    else {
                        ITreeNode byf = minimumNode(root.getRight());
                        root.setData(byf.getData());
                        root.setKey(byf.getKey());
                        removeNode(root.getRight(), byf.getKey());
                    }
                }
            }
        }
    }

    private ITreeNode minimumNode ( ITreeNode node ){
        ITreeNode result = node;
        while (result.getLeft() != null)
            result = result.getLeft();
        return result;
    }

    @Override
    public ITreeNode regularize(ITreeNode tree) {
        return null;
    }



    @Override
    public ITreeNode addNode(ITreeNode parent, ITreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ITreeNode getTree(int key){
        return TreePool.getInstance().get(key);
    }

    //==================================================
    //Методы ниже не реализован
    //==================================================
    @Override
    public boolean removeTree(int key) {
        return TreePool.getInstance().removeTreeFromPool(key);

    }

    @Override
    public ITreeNode findNode(ITreeNode tree, int key) {
        ITreeNode root = null;
        if(tree!=null){
            if(key>tree.getKey()) {
                root = findNode(tree.getRight(), key);
            }
            else
            if(key<tree.getKey()) {
                root = findNode(tree.getLeft(), key);
            }
            else
                root = tree;
        }
        return root;
    }

    @Override
    public ITreeNode copyTo(ITreeNode sourceTree, ITreeNode destTree, ITreeNode sourceNode, ITreeNode destNode) {
        return null;
    }

    @Override
    public ITreeNode clone(ITreeNode tree) {
        return null;
    }

}

package netcracker.tree;


import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ВладПК
 */
public class TreeNode implements ITreeNode{
    private int key;
    private TreeNode parent;
    private TreeNode left;
    private TreeNode right;
    private Object data;
    
    public TreeNode(int key, Object data){
        this.key = key;
        this.data = data;
        left = null;
        right = null;
    }
    
    public TreeNode(int key, Object data, TreeNode left, TreeNode right){
        this.key = key;
        this.data = data;
        this.right = right;
        this.left = left;
    }
    
    public TreeNode(int key, Object data, TreeNode left, TreeNode right, TreeNode parent){
        this.key = key;
        this.data = data;
        this.parent = parent;
        this.right = right;
        this.left = left;
    }
    
    @Override
    public void setKey(int key) { 
        this.key = key;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public ITreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(ITreeNode parent) {
        if(parent!=null)
            this.parent = (TreeNode) parent;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data!=null)
            this.data = data;
    }

    @Override
    public TreeNode getLeft() {
        return left;
    }

    @Override
    public void setLeft(ITreeNode node) {
        //if(node!=null) // нужно для удаления
            left =  (TreeNode) node;
    }

    @Override
    public TreeNode getRight() {
        return right;
    }

    @Override
    public void setRight(ITreeNode node) {
        //if(node!=null)
            right = (TreeNode) node;
    }
    
}

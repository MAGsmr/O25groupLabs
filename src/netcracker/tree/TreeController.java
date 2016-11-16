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

    public boolean removeNode(ITreeNode parent, int key){
        return false;
    }

    @Override
    public ITreeNode regularize(ITreeNode tree) {
        return null;
    }

    @Override
    public ITreeNode addNode(ITreeNode parent, int key, Object data) {
        ITreeNode root = null;
        if(parent!=null){
            root = parent;
            if(key>parent.getKey())
                root.setRight(addNode(parent.getRight(), key, data));
            else
            if(key<parent.getKey())
                root.setLeft(addNode(parent.getLeft(), key, data));
            else
                parent.setData(data);
        }
        else{
            root = new TreeNode(key, data, null, null);  
        }
        return root;
    }

    @Override
    public ITreeNode addNode(ITreeNode parent, ITreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ITreeNode getTree(int key){
        return TreePool.getInstance().get(key);
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

    //==================================================
    //Методы ниже не реализован
    //==================================================
    @Override
    public boolean delete(int key) {
        return false;
    }

    @Override
    public ITreeNode findNode(ITreeNode tree, int key) {
        return null;
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

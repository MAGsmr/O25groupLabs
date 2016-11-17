package netcracker.tree;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ВладПК
 */
public interface ITreeNode {
    public void setKey(int key);
    public int getKey();
    public ITreeNode getParent();
    public void setParent(ITreeNode parent);
    public ITreeNode getLeft();
    public void setLeft(ITreeNode node);
    public ITreeNode getRight();
    public void setRight(ITreeNode node);
    public Object getData();
    public void setData(Object data);
            
}

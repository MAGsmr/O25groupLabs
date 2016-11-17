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
public interface ITreePool {
    public void put(ITreeNode tree);
    public boolean removeTreeFromPool(int key);
    public ITreeNode get(int key);
}

package netcracker.tree.Views;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import netcracker.tree.Nodes.ITreeNode;

import java.io.IOException;

/**
 *
 * @author ВладПК
 */
public interface ITreeView {
    public void showNode(ITreeNode tree, int key, int startOffset, int offset);
    public void hideNode(ITreeNode tree, int key, int startOffset, int offset);
    public void showTree(ITreeNode tree, int startOffset, int offset);
    public void input() throws IOException;
}

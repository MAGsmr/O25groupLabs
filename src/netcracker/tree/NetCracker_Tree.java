/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.tree;
import netcracker.tree.Views.TreeView;

import java.io.IOException;

/**
 *
 * @author ВладПК
 */
public class NetCracker_Tree {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new TreeView().input();
        //ITreeNode tree = TreeController.getInstance().create(10, "ROOT");
        //tree = null;
        //tree = TreeController.getInstance().addNode(tree, 11, "right");
        //tree = TreeController.getInstance().addNode(tree, 9, "left");
        //TreeController.getInstance().save(tree, "File.txt");
        //ITreeNode node = TreeController.getInstance().load("File.txt");
        //TreeView tr = new TreeView();
        //tr.showTree(node, 0, 2);
    }
    
}

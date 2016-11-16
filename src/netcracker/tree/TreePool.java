/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ВладПК
 */
public class TreePool implements ITreePool{
    private static TreePool instance;
    private ArrayList<ITreeNode> pool;
    
    private TreePool(){
        pool = new ArrayList<ITreeNode>();
    }
    
    public static synchronized TreePool getInstance(){
        if(instance == null)
            instance = new TreePool();
        return instance;
    }
    @Override
    public void put(ITreeNode tree) {
        if(tree!=null)
            pool.add(tree);
    }

    @Override
    public ITreeNode get(int key) {
        int i =0;
        while(i < pool.size()){
            if(pool.get(i).getKey()==key)
                return pool.get(i);
            i++;
                }
        return null;    
    }
    
}

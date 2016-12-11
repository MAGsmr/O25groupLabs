package netcracker.tree;

import java.util.Comparator;

/**
 * Created by Ник on 11.12.16.
 */
 public class keyComparator implements Comparator<ITreeNode> {
    @Override
    public int compare(ITreeNode n1, ITreeNode n2){
        if (n1.getData() == null) return Integer.MIN_VALUE;
        if (n2.getData() == null) return Integer.MAX_VALUE;

        return n1.getKey() - n2.getKey();
    }
}
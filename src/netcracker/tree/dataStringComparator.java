package netcracker.tree;

import javax.naming.NameNotFoundException;
import java.util.Comparator;


/**
 * Created by Ник on 11.12.16.
 */
public class dataStringComparator implements Comparator<ITreeNode> {

    @Override
    public int compare(ITreeNode n1, ITreeNode n2){
        if (n1.getData() == n2.getData()) return 0;
        if (n1.getData() == null) return Integer.MIN_VALUE;
        if (n2.getData() == null) return Integer.MAX_VALUE;

        return ((String)n1.getData()).compareTo((String) n2.getData());
    }
}

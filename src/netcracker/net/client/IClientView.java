package netcracker.net.client;

import netcracker.tree.ITreeView;

import java.io.IOException;

/**
 * Created by ВладПК on 17.12.2016.
 */
public interface IClientView extends ITreeView {
    public void input() throws IOException;
}

package netcracker.net.client;

import java.io.IOException;

/**
 * Created by ВладПК on 17.12.2016.
 */
public interface IClientController {
    public boolean connect(String host, int port);
    public boolean auth(String user, String pass);
    public void disconnect();
    public void send(String s);
    public String getResponse() throws IOException;
    public boolean isConnected();
}

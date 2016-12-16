package netcracker.net.client;

/**
 * Created by ВладПК on 17.12.2016.
 */
public interface IClientController {
    public boolean connect(String host, int port);
    public boolean auth(String user, String pass);
    public boolean disconnect();
}

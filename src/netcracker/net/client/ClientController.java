package netcracker.net.client;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ClientController implements IClientController {
    public ClientController(){

    }

    @Override
    public boolean connect(String host, int port) {
        return false;
    }

    @Override
    public boolean auth(String user, String pass) {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }
}

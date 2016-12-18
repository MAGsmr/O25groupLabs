package netcracker.net.server;

import netcracker.net.ServerSettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ServerConnection implements IServerConnection {
    private ServerSocket serverSocket;
    private boolean isRun;

    @Override
    public void init() throws IOException {
        serverSocket = new ServerSocket(ServerSettings.PORT);
        isRun = true;
        while(isRun){
            Socket socket = serverSocket.accept();
            new Thread(new ServerController(socket)).start();
        }
    }
}

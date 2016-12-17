package netcracker.net.client;

import netcracker.net.ISettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ClientController implements IClientController {

    private static ClientController instance;
    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private ClientController(){

    }

    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }

    @Override
    public boolean connect(String host, int port) {
        try {
            InetAddress address = InetAddress.getByName(ISettings.host);
            socket = new Socket(address, ISettings.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean auth(String user, String pass) {
        return true;
    }

    @Override
    public void disconnect() {
        if(isConnected())
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void send(String s) {
        if(isConnected()){
            out.println(s);
            out.flush();
        }
    }

    @Override
    public String getResponse() throws IOException{
        if(isConnected()){
            //System.out.println("Сообщение получено:"+in.readLine()+"|");
            return in.readLine();
        }

        return null;
    }

    @Override
    public boolean isConnected() {
        return socket!=null && socket.isConnected();
    }
}

package netcracker.net.client;

import java.io.IOException;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class Main {
    public static void main(String[] args){
        try {
            new ClientView().input();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

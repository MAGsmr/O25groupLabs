package netcracker.net;

import java.io.*;

/**
 * Created by ВладПК on 17.12.2016.
 */
public abstract class ServerSettings {
    public static final int PORT = 1051;
    public static String host = "127.0.0.1";

    public static final int SUCCESSFUL = 100;
    public static final int ERROR = 200;

    public static boolean compareCode(int c1, int c2) {
        return c1 == c2;
    }

    public static boolean compareCode(String s, int c2) {
        return getCode(s) == c2;
    }

    public static int getCode(String s){
        int code = -1;
        if(s.length()>2){
            try{
                code = Integer.parseInt(s.substring(0,3));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return code;
    }

    public static synchronized boolean auth(String login, String password) throws IOException {
        Reader fr = new FileReader(new File("regs.txt"));
        BufferedReader br = new BufferedReader(fr);

        String data = br.readLine();
        String[] componentns = data.trim().split("\\s+");
        while(data!=null && componentns.length == 2){
            if(componentns[0].equals(login) && componentns[1].equals(password)){
                br.close();
                return true;
            }
            data = br.readLine();
            if(data!=null)
                componentns = data.trim().split("\\s+");
        }
        br.close();
        return false;
    }

    public static synchronized void reg(String login, String password) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new File("regs.txt"));
        ps.println(login+" "+password);
        ps.close();
    }
}

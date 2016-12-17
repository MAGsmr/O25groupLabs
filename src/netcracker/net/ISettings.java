package netcracker.net;

/**
 * Created by ВладПК on 17.12.2016.
 */
public abstract class ISettings {
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
}

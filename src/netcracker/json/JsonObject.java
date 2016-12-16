package netcracker.json;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class JsonObject{
    private String key;
    private String value;
    public JsonObject(String key, String value){
        this.key = key;
        this.value = value;
    }

    public JsonObject(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

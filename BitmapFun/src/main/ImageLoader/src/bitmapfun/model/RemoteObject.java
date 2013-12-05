package bitmapfun.model;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim Sushkevich
 * Date: 04.12.13
 * Time: 0:03
 * To change this template use File | Settings | File Templates.
 */
public class RemoteObject {

    private long mId;
    private  String mValue;
    private String mKey;

    public RemoteObject(long id, String key, String value){
        mId = id;
        mKey = key;
        mValue = value;
    }

    public RemoteObject(String key, String value){
        mKey = key;
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public long getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}

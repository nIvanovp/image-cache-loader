package bitmapfun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import bitmapfun.application.SampleApplication;
import bitmapfun.model.RemoteObject;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim Sushkevich
 * Date: 04.12.13
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class BitmapLoader {
    private static FragmentActivity mFragmentActivity;
    private static JSONObject mJsonObject;
    private static String mRootImageLink;
    private Context mContext;
    private ImageFragment imageFragment;
    private static BitmapLoader imageLoader = null;

    public static BitmapLoader getInstance() throws Throwable {
        if(mFragmentActivity == null || mRootImageLink == null){
            throw new Throwable("before method executing please call init(String linkToJSon, FragmentActivity fragmentActivity, String rootImageLink)");
        }
        if(imageLoader == null){
            imageLoader = new BitmapLoader(mJsonObject, mFragmentActivity);
        }
        return imageLoader;
    }

    public static void init(JSONObject jsonObject, FragmentActivity fragmentActivity, String rootImageLink){
        mJsonObject = jsonObject;
        mFragmentActivity = fragmentActivity;
        mRootImageLink = rootImageLink;
    }

    private BitmapLoader(JSONObject jSonObject, FragmentActivity fragmentActivity){
        mContext = fragmentActivity.getApplicationContext();

        try {
            if(jSonObject != null){
                JSONArray jsonArray = jSonObject.names();
                String name = "";
                RemoteObject remoteObject;
                long id;
                SampleApplication.getInstance(mContext).getDateBase().openDB();
                String value = null;
                for(int currentIndex = 0; currentIndex < jsonArray.length(); currentIndex ++){
                    name = jsonArray.getString(currentIndex);
                    value = jSonObject.getString(name).endsWith(".png") ? mRootImageLink + jSonObject.getString(name) : jSonObject.getString(name);

                    Log.i("##TAG", "Value: " + value);
                    remoteObject = new RemoteObject(name, value);

                    id = SampleApplication.getInstance(mContext).getDateBase().getRemoteObjectId(remoteObject);
                    if(id == -1){
                        SampleApplication.getInstance(mContext).getDateBase().insert(remoteObject);
                    } else {
                        SampleApplication.getInstance(mContext).getDateBase().update(remoteObject, id);
                    }
//                    Images.imageThumbUrls.add(value);
                }
                SampleApplication.getInstance(mContext).getDateBase().closeDB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createImageFragment(int imageSize) {
        imageFragment = new ImageFragment(mFragmentActivity, imageSize);
    }

    public void destroy(){
        imageFragment.destroy();
    }

    public void pause(){
        imageFragment.pause();
    }

    public int getColorByKey(String key){
        String keyDataBase = SampleApplication.getInstance(mContext).getDateBase().getValueByKey(key);
        Log.i("##TAG", "KeyDataBase color:" + keyDataBase);

        int color = 0;
        try{
            color = Color.parseColor("#" + keyDataBase);
        }catch (Throwable ex){
            Log.i("##TAG", "Throwable: " + ex.getMessage());
        }
        return color;
    }

    public void resume(){
        imageFragment.resume();
    }

    public void clearCache(){
        imageFragment.clearCache();
    }

    public void loadImage(String key, View imageView){
        imageFragment.loadImage(key, imageView);
    }

    public BitmapDrawable getBitmapDrawable(String key, ImageView imageView){
        return imageFragment.getBitmapDrawable(key, imageView);
    }
}

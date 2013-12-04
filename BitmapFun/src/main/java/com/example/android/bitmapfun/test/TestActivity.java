package com.example.android.bitmapfun.test;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bitmapfun.BitmapLoader;
import com.example.android.bitmapfun.R;
import org.json.JSONObject;


/**
 * Created with IntelliJ IDEA.
 * User: Maxim Sushkevich
 * Date: 04.12.13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends FragmentActivity {

    BitmapLoader imageLoader;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        imageView = (ImageView) findViewById(R.id.imageView);
        LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://mobilecms.ctc.ru/Push/GetDynamicParameters?projectId=74079&lastupdate=2012-10-27%2020:43:09.223";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, successListener(), errorListener());
        queue.add(jsonObjectRequest);
    }

    private void initializeImageLoader(JSONObject jsonObject){
        Log.i("##TAG", "initializeImageLoader: Response: " + jsonObject.toString());
        BitmapLoader.init(jsonObject, TestActivity.this, "http://mobilecms.ctc.ru/Uploads/Images/");
        try {
            imageLoader = BitmapLoader.getInstance();
            imageLoader.createImageFragment(198);
            if(imageLoader != null){
                imageLoader.resume();
                BitmapDrawable bitmapDrawable = imageLoader.getBitmapDrawable("IOSIco152x152", imageView);
                Log.i("##TAG", "initializeImageLoader: bitmap: " + bitmapDrawable);
                imageView.setImageDrawable(bitmapDrawable);
            }else{
                Log.i("##TAG", "BitmapLoader is null!");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Log.i("##TAG", "initializeImageLoader: " + throwable.getMessage());
        }
    }

    private Response.Listener<JSONObject> successListener(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                initializeImageLoader(response);
            }
        };
    }

    private Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##TAG", "onErrorResponse: " + error.getMessage());
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(imageLoader != null){
            imageLoader.resume();
            BitmapDrawable bitmapDrawable = imageLoader.getBitmapDrawable("IOSIco152x152", imageView);
            imageView.setImageDrawable(bitmapDrawable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(imageLoader != null)
            imageLoader.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(imageLoader != null)
            imageLoader.destroy();
    }
}

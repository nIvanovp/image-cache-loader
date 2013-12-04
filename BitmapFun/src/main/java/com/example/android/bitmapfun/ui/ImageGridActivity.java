/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bitmapfun.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import com.example.android.bitmapfun.BuildConfig;
import com.example.android.bitmapfun.application.SampleApplication;
import com.example.android.bitmapfun.model.RemoteObject;
import com.example.android.bitmapfun.provider.Images;
import com.example.android.bitmapfun.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Simple FragmentActivity to hold the main {@link ImageGridFragment} and not much else.
 */
public class ImageGridActivity extends FragmentActivity {
    private static final String TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        HttpGet get = new HttpGet("http://mobilecms.ctc.ru/Push/GetDynamicParameters?projectId=74079&lastupdate=2012-10-27%2020:43:09.223");

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        String jSon = "";
        JSONObject jSonObject;
        try {
            response = client.execute(get);
            jSon = EntityUtils.toString(response.getEntity());
            long time = System.currentTimeMillis();
            Log.i("####################", jSon + " " + (System.currentTimeMillis() - time));
            jSonObject = new JSONObject(jSon);
            JSONArray jsonArray = jSonObject.names();
            String name = "";
            RemoteObject remoteObject;
            long id;
            Log.i("####################", "########################0" + " " + (System.currentTimeMillis() - time));
            SampleApplication.getInstance(getApplicationContext()).getDateBase().openDB();
            String value;
            for(int currentIndex = 0; currentIndex < jsonArray.length(); currentIndex ++){
                name = jsonArray.getString(currentIndex);
                value = "http://mobilecms.ctc.ru/Uploads/Images/" + jSonObject.getString(name);
                if(!value.endsWith(".png")) continue;
                remoteObject = new RemoteObject(name, value);

                id = SampleApplication.getInstance(getApplicationContext()).getDateBase().getRemoteObjectId(remoteObject);
                if(id == -1){
                    SampleApplication.getInstance(getApplicationContext()).getDateBase().insert(remoteObject);
                } else {
                    SampleApplication.getInstance(getApplicationContext()).getDateBase().update(remoteObject, id);
                }
                Images.imageThumbUrls.add(value);
            }
            Log.i("####################", "########################" + " " + (System.currentTimeMillis() - time));
            Log.i("####################", SampleApplication.getInstance(getApplicationContext()).getDateBase().getValueByKey("IOSIco152x152") + " " + (System.currentTimeMillis() - time));
            SampleApplication.getInstance(getApplicationContext()).getDateBase().closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }
}

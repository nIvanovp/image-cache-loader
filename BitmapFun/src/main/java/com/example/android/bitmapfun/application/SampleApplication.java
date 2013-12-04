package com.example.android.bitmapfun.application;

import android.app.Application;
import android.content.Context;
import com.example.android.bitmapfun.sql.DBConnector;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim Sushkevich
 * Date: 04.12.13
 * Time: 0:36
 * To change this template use File | Settings | File Templates.
 */
public class SampleApplication extends Application {
    private DBConnector mDateBase;
    private static SampleApplication mSampleApplication = null;

    private SampleApplication(Context context){
        mDateBase = new DBConnector(context);
    }

    public static SampleApplication getInstance(Context context){
        if(mSampleApplication == null){
            mSampleApplication = new SampleApplication(context);
        }
        return mSampleApplication;
    }

    public DBConnector getDateBase() {
        return mDateBase;
    }
}

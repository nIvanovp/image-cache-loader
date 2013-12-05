package bitmapfun;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import bitmapfun.application.SampleApplication;
import bitmapfun.util.ImageCache;
import bitmapfun.util.ImageFetcher;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim Sushkevich
 * Date: 04.12.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class ImageFragment {


    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private ImageFetcher mImageFetcher;
    private ImageView mImageView;
    FragmentActivity mFragmentActivity;
    Context mContext;

    public ImageFragment(FragmentActivity activity, int thumbnailSize){
        mFragmentActivity = activity;
        mContext = activity.getApplicationContext();
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(activity, IMAGE_CACHE_DIR);

        mImageThumbSize = 198;//activity.getResources().getDimensionPixelSize(198); //todo check thumbnailSize logic

        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher = new ImageFetcher(activity, mImageThumbSize);
//        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
//        mImageFetcher.setImageFadeIn(false);
        mImageFetcher.addImageCache(activity.getSupportFragmentManager(), cacheParams);

//        mImageFetcher.setPauseWork(false); //todo recheck this logic
    }

    public void destroy(){
        mImageFetcher.closeCache();
    }

    public void resume() {
        SampleApplication.getInstance(mFragmentActivity.getApplicationContext()).getDateBase().openDB();
        mImageFetcher.setExitTasksEarly(false);
    }

    public void pause() {
        SampleApplication.getInstance(mFragmentActivity.getApplicationContext()).getDateBase().closeDB();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    public void clearCache(){
        mImageFetcher.clearCache();
    }

    public void loadImage(String key, View imageView){
        String keyDataBase = SampleApplication.getInstance(mContext).getDateBase().getValueByKey(key);
        Log.i("##TAG", "KeyDataBase:" + keyDataBase);
        mImageFetcher.loadImage(keyDataBase, imageView);
    }

    public BitmapDrawable getBitmapDrawable(String key, ImageView imageView){
//        ImageView imageView = new RecyclingImageView(mContext);
        String keyDataBase = SampleApplication.getInstance(mContext).getDateBase().getValueByKey(key);
        Log.i("##TAG", "KeyDataBase:" + keyDataBase);
        return mImageFetcher.getBitMapDrawable(keyDataBase, imageView);
    }
}

package com.imageloader.libin.imageloadercourse.step_two;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by libin on 16/11/4.
 *
 * 单一职责原则
 * 把各个功能独立出来
 */

public class ImageLoader {
    //图片缓存
    ImageCache mImageCache = new ImageCache();
    //线程池,线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void disPlayIamge(final String url, final ImageView imageView){
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downLoadIamge(url);
                if(bitmap == null){
                    return;
                }
                if(imageView.getTag().equals(url)){
                    imageView.setImageBitmap(bitmap);
                }

                mImageCache.put(url,bitmap);
            }
        });
    }

    private Bitmap downLoadIamge(String imageUrl) {
        Bitmap bitmap =null;

        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(coon.getInputStream());
            coon.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}

//=======================================>知识点
/**
 * 单一职责原则
 * 把各个功能独立出来
 * 两个完全不一样的功能就不应该放在一个类里面,
 * 一个类应该是一组相关性很高的函数、数据的封装。
 */
package com.imageloader.libin.imageloadercourse.step_three;

import android.graphics.Bitmap;

/**
 * Created by libin on 16/11/5.
 *
 * 双缓存,获取图片时先从内存缓存中获取,如果内存中没有缓存该图片,再从SD卡获取
 * 缓存图片也是SD卡和内存中各缓存一份
 */

public class DoubleCache implements ImageCache {
    MemoryCache memoryCache  = new MemoryCache();
    DiskCache diskCache = new DiskCache();

    //先从内存缓存中获取图片,如果没有,再从SD卡获取
    public Bitmap get(String url){
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap == null){
            bitmap =diskCache.get(url);
        }
        return bitmap;
    }

    //将图片缓存到内存和SD卡中
    public void put(String url,Bitmap bitmap){
        memoryCache.put(url,bitmap);
        diskCache.put(url,bitmap);
    }
}

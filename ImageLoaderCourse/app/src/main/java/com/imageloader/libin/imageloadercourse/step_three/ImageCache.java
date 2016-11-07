package com.imageloader.libin.imageloadercourse.step_three;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by libin on 16/11/4.
 * ImageCache定义了获取和缓存两个函数,缓存的key是图片的url,值是图,然后在内存缓存,SD卡缓存,双缓存实现该接口
 */

public interface ImageCache {
    public Bitmap get(String url);
    public void put(String url,Bitmap bitmap);
}

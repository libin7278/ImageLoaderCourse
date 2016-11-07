package com.imageloader.libin.imageloadercourse.step_three;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by libin on 16/11/5.
 */

public class MemoryCache implements ImageCache {
    //图片缓存
    private LruCache<String,Bitmap> mMemoryCache;

    public MemoryCache() {
        //初始化LRU
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内存
        final int maxmemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //取四分之一的可用内存作为缓存
        final int cacheSize = maxmemory/4;

        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url,bitmap);
    }
}

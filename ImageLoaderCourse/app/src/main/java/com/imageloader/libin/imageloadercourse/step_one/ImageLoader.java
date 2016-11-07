package com.imageloader.libin.imageloadercourse.step_one;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by libin on 16/11/4.
 *
 * 最普通的图片加载工具类以及详细知识点
 */

public class ImageLoader {
    //图片缓存
    LruCache<String,Bitmap> mImageCache;
    //线程池,线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ImageLoader() {
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内存
        final int maxmemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //取四分之一的可用内存作为缓存
        final int cacheSize = maxmemory/4;

        mImageCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

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

//=========================================================》相关知识点
/**
 *1.
 * LruCache是android提供的一个缓存工具类，其算法是最近最少使用算法。
 * 它把最近使用的对象用“强引用”存储在LinkedHashMap中，并且把最近最少
 * 使用的对象在缓存值达到预设定值之前就从内存中移除。其在API12被引进，
 * 低版本可以用support包中的类
 *
 *2.
 * LinkedHashMap继承于HashMap，它使用了一个双向链表来存储Map中的Entry顺序关系，这种顺序有两种，
 * 一种是LRU顺序，一种是插入顺序，这可以由其构造函数public LinkedHashMap(int initialCapacity,
 * float loadFactor, boolean accessOrder)指定。所以，对于get、put、remove等操作，
 * LinkedHashMap除了要做HashMap做的事情，还做些调整Entry顺序链表的工作。
 * LruCache中将LinkedHashMap的顺序设置为LRU顺序来实现LRU缓存，每次调用get(也就是从内存缓存中取图片)，
 * 则将该对象移到链表的尾端。调用put插入新的对象也是存储在链表尾端，这样当内存缓存达到设定的最大值时，
 * 将链表头部的对象（近期最少用到的）移除。
 *
 * 3.
 * android.support.v4.util.LruCache在移除缓存的时候是移除最近最少访问的，符合LruCache的设计初衷。
 * android.util.LruCache在缓存满了的情况下，会把当前加入的直接移除掉，不符合LruCache的设计初衷。
 *
 * 4.
 * java.lang.Runtime.availableProcessors() 方法返回到Java虚拟机的可用的处理器数量
 *
 * 5.
 * 首先使用 newFixedThreadPool() 工厂方法创建壹個 ExecutorService ,一个ExecutorService其实就是一个线程池
 *
 * 6.
 * Runtime.getRuntime().maxMemory() 方法返回Java虚拟机将尝试使用的最大内存量 ,字节为单位
 *
 * 7.
 * protected int sizeOf(K key, V value) {
 *     return 1;  //用来计算单个对象的大小，这里默认返回1，一般需要重写该方法来计算对象的大小
 *  }
 *
 * 8.
 * getRowBytes：Since API Level 1，用于计算位图每一行所占用的内存字节数。
 * getByteCount：Since API Level 12，用于计算位图所占用的内存字节数。
 * getByteCount() = getRowBytes() * getHeight()，也就是说位图所占用的内存空间数等于位图的每一行所占用的空间数乘以位图的行数。
 *
 * 9.
 * imageView.setTag(url);
 * View中的setTag(Onbect)表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
 *
 * 10.
 * ExecutorService中submit和execute的区别
 * (1)接收的参数不一样
 * (2)submit有返回值，而execute没有
 *  用到返回值的例子，比如说我有很多个做validation的task，我希望所有的task执行完，然后每个task告诉我它的执行结果，是成功还是失败，如果是失败，原因是什么。然后我就可以把所有失败的原因综合起来发给调用者。
 *  个人觉得cancel execution这个用处不大，很少有需要去取消执行的。而最大的用处应该是第二点。
 * (3)submit方便Exception处理
 *
 *
 */
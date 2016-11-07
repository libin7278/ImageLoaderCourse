package com.imageloader.libin.imageloadercourse.step_three;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by libin on 16/11/4.
 *
 * 开闭原则
 * 里氏替换原则
 * 依赖倒置原则
 */

public class ImageLoader {
    //内存缓存
    ImageCache mImageCache = new MemoryCache();

    //线程池,线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //注入缓存实现
    public void setImageCache(ImageCache cache){
        mImageCache = cache;
    }

    public void disPlayIamge(final String url, final ImageView imageView){
        Bitmap bitmap = mImageCache.get(url);
        if(bitmap != null ){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //没有缓存交个线程池进行缓存
        submitLoadRequest(url,imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
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

//==========================================>知识点
/**
 * 开闭原则
 * 1.
 * 通过内存每次来从网络加载图片是一个问题,安卓应用内存很有限,且具有易失性,
 * 重启之后之前加载的就会消失,还要重新再下载。导致运行缓慢,消耗内存。
 * 所以接下要考虑缓存SD卡。
 * 2.
 * 软件中的对象(模块,类,函数)应该对于扩展是开放的,但对于修改是封闭的
 * 3.
 * 首先,缓存应该优先使用内存缓存,如果内存缓存没有再使用SD卡缓存,如果SD卡也没有才去网络获取。
 * 然后在内存和SD卡上都缓存一份
 * 4.
 * 整体思路每一个类单独处理一个一个事件,通过get set方法设置
 *
 * 5.
 * 为了使imageloader不臃肿,每次不必都更改imageloader,增加可扩展性,所以我们在设计的时候就应该考虑到
 * 通过扩展的方式来实现变化,而不是通过每次来更改imageLoader来实现。
 *
 * 6.
 * 通过setImageCache()方法注入不同的缓存实现,这样不仅能够使Imageloader更简单,健壮。
 * 当需要新建一个实现ImagerLoader接口的类,通过setImageCache注入到ImageLoader中。
 *
 * 7.
 * 依赖倒置关键点：
 *①高层模块不依赖于低层模块，两者都应该依赖于抽象
 *②抽象不应该依赖细节
 *③细节应该依赖抽象
 *
 * 8.
 * 里氏替换原则
 * 定义：所有引用基类的地方必须能透明的使用子类的对象
 *核心：里氏替换原则核心是抽象，抽象有依赖于继承
 *优点：①代码重用，减少创建类
 *     ②子类父类像，但是又有区别
 *     ③提高代码可扩展性
 *缺点：①继承是侵入性，只要继承就要必须拥有父类的所有属性和方法
 *     ②可能早晨子类代码冗余，灵活性降低
 *
 * 9.接口隔离原则
 * 定义：客户端不应该依赖不需要的接口，也就是类间的依赖关系应该建立在最小的接口上，
 * 接口隔离则是将非常庞大，臃肿的接口拆分成更小，更具体的接口；
 * 目的：系统解开耦合。
 * ImageCache就是接口隔离原则的运用,ImageLoader只需要知道该对象有存取的接口即可,
 * 其他的一概不管,这就使得缓存功能具体实现对ImageLoader只需要对该缓存对象有存取接口即可,
 * 这使得缓存功能具体实现对ImageLoader是隐藏的。
 */
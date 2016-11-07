package com.imageloader.libin.imageloadercourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.imageloader.libin.imageloadercourse.step_three.DoubleCache;
import com.imageloader.libin.imageloadercourse.step_three.ImageLoader;
import com.imageloader.libin.imageloadercourse.step_three.MemoryCache;


public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.setImageCache(new MemoryCache());
        imageLoader.disPlayIamge("http://gtms01.alicdn.com/tps/i1/TB1DPlJMpXXXXbBXXXXwu0bFXXX.png",imageView);
    }
}

package com.github.yangweigbh.volleyx.demo;

import android.app.Application;

import com.github.yangweigbh.volleyx.VolleyX;

/**
 * Created by yangwei on 2016/10/29.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyX.init(this);
    }
}

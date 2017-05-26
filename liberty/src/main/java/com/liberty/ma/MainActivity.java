package com.liberty.ma;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.okhttpretrofit.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            @Override
            public void run() {
                try {
                    Person xiaoming = new ApiWrapper().getAliQrcode(new Person("xiaoming", 123),MainActivity.this);
                    if (xiaoming != null) {
                        Log.e("MainActivity", xiaoming.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

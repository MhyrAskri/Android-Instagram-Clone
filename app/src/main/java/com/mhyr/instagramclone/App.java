package com.mhyr.instagramclone;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("s3vbXadvNSLdIgDea5hkJqgxBNmVWykcyICwdB8P")
                .clientKey("pkEcpexcXjyvVQOdv9HCfliZMSQa2IxwDYdzGe9O")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
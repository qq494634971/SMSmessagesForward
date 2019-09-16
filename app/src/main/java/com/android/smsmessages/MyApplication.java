package com.android.smsmessages;

import android.app.Application;

public class MyApplication extends Application {
    private String dingToken = "";

    public String getDingToken() {
        return dingToken;
    }

    public void setDingToken(String score) {
        this.dingToken = score;
    }
}
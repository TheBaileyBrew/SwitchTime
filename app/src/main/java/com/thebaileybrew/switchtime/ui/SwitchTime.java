package com.thebaileybrew.switchtime.ui;

import android.app.Application;

public class SwitchTime extends Application {
    private static SwitchTime mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static SwitchTime getContext() {
        return mContext;
    }
}

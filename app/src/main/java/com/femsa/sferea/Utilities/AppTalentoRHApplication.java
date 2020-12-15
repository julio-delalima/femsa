package com.femsa.sferea.Utilities;

import android.app.Application;

public class AppTalentoRHApplication extends Application {
    private static AppTalentoRHApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static AppTalentoRHApplication getApplication() {
        return mApplication;
    }
}

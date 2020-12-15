package com.femsa.sferea.Utilities;
import android.os.SystemClock;
import android.view.View;

 //Sacado de: https://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button

public abstract class OnSingleClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL=600;

    private long mLastClickTime;


    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;

        onSingleClick(v);
    }


}
package com.femsa.sferea.Utilities;

import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by MasterKiwi-Sferea on 20/04/2018.
 */

public class KeyboardHandlerActivity extends AppCompatActivity
{
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
    @Override
    public void onGlobalLayout() {
        // navigation bar height
        int navigationBarHeight = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // status bar height
        int statusBarHeight = 0;
        resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // display window size for the app layout
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
        int keyboardHeight = rootLayout.getRootView().getHeight() - (statusBarHeight + navigationBarHeight + rect.height());

        if (keyboardHeight <= 0) {
            onHideKeyboard();
        } else {
            onShowKeyboard(keyboardHeight);
        }
    }
};

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    protected void onShowKeyboard(int keyboardHeight) {}
    protected void onHideKeyboard() {}



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }
}
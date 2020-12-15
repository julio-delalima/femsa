package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.femsa.sferea.R;

public class GameLoadingDialogFragment extends DialogFragment {

    private boolean autoClosable = false;

    public static GameLoadingDialogFragment newInstance() {
        GameLoadingDialogFragment manager = new GameLoadingDialogFragment();
        Bundle args = new Bundle();
        manager.setArguments(args);
        return manager;
    }

    public void setAutoClosable(boolean autoClosable) {
        this.autoClosable = autoClosable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        if(autoClosable){
            CountDownTimer back = new CountDownTimer(6000,1000) {
                /**
                 * Callback fired on regular interval.
                 *
                 * @param millisUntilFinished The amount of time until finished.
                 */
                @Override
                public void onTick(long millisUntilFinished) {

                }

                /**
                 * Callback fired when the time is up.
                 */
                @Override
                public void onFinish() {
                    closeFragment();
                }
            };
            back.start();
        }
    }


    private void closeFragment(){
        this.dismiss();
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(size.x, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.game_loading_activity, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return mView;
    }
}

package com.femsa.sferea.home.inicio.componentes.dialogFragments;

import android.graphics.Point;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.femsa.sferea.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CloseAppDialog extends DialogFragment {

    public static CloseAppDialog newInstance() {
        return new CloseAppDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_close_app, container);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        Button mAcceptButton = view.findViewById(R.id.close_app_dialog_accept_button);
            mAcceptButton.setOnClickListener(v-> Objects.requireNonNull(getActivity()).finishAffinity());

        Button mCancelButton = view.findViewById(R.id.close_app_dialog_cancel_button);
            mCancelButton.setOnClickListener(v-> this.dismiss() );

        return view;
    }

}

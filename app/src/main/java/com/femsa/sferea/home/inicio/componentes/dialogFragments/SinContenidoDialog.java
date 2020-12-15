package com.femsa.sferea.home.inicio.componentes.dialogFragments;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
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
import android.widget.Button;

import com.femsa.sferea.R;

public class SinContenidoDialog extends DialogFragment {

    private View mView;

    private Button mAcceptButton;

    public SinContenidoDialog() {
        super();
    }

    public static SinContenidoDialog newInstance() {
        SinContenidoDialog frag = new SinContenidoDialog();
        return frag;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_sin_contenido, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mAcceptButton = mView.findViewById(R.id.inscribirse_si_button);
        mAcceptButton.setOnClickListener(v -> this.dismiss());
        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
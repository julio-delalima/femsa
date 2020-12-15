package com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments;

import android.graphics.Point;
import android.os.Bundle;
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

    private OnSinContenidoKOF mListener;

    public SinContenidoDialog(){
        super();
    }

    public interface OnSinContenidoKOF
    {
        void OnSinContenidoCloseDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListener(OnSinContenidoKOF listener)
    {
        mListener = listener;
    }

    public static SinContenidoDialog newInstance()
    {
        SinContenidoDialog sinc = new SinContenidoDialog();
        return sinc;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setCancelable(false);
        mView = inflater.inflate(R.layout.dialog_sin_contenido, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Button mAceptar = mView.findViewById(R.id.inscribirse_si_button);
            mAceptar.setOnClickListener(v-> {mListener.OnSinContenidoCloseDialog(); this.dismiss();});
        return mView;
    }
}

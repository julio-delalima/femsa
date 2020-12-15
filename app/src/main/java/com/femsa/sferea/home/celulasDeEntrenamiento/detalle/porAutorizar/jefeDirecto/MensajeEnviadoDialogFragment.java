package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
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

public class MensajeEnviadoDialogFragment extends DialogFragment implements View.OnClickListener {

    //Referencia que infla las vistas del layout.
    private View mView;

    //Referencia al botón que cierra el DialogFragment.
    private Button mBtnAceptar;

    //Listener que agrega funcionalidad al botón "Aceptar".
    private MensajeEnviadoDialogListener mListener;

    //Interface que define el método para cerrar el DialogFragment y la Activity cuando se presiona el botón "Aceptar".
    public interface MensajeEnviadoDialogListener{
        void onFinishMensajeEnviado();
    }

    public MensajeEnviadoDialogFragment() { }

    public static MensajeEnviadoDialogFragment newInstance() {
        Bundle args = new Bundle();
        MensajeEnviadoDialogFragment fragment = new MensajeEnviadoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mensaje_enviado_dialog, container, false);

        //Agregando la referencia al botón "Aceptar"
        mBtnAceptar = mView.findViewById(R.id.btn_fragment_mensaje_enviado_dialog_aceptar);
        mBtnAceptar.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fragment_mensaje_enviado_dialog_aceptar:
                mListener.onFinishMensajeEnviado();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        mListener = (MensajeEnviadoDialogListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    /**
     * <p>Permite redimensionar correctamente el fragment del cuadro de diálogo.</p>
     */
    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        assert window!=null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int)(size.x*0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}

package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

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
import android.widget.TextView;

import com.femsa.sferea.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PruebaTerminadaDialog extends DialogFragment {

    private OnPruebaTerminadaClick mListener;

    private boolean mExitosa;

    private static final String EXITO_KEY = "exitosa";

    private TextView mDescripcionTV, mTituloTV;

    public static PruebaTerminadaDialog newInstance(boolean exitosa) {
        PruebaTerminadaDialog prueba = new PruebaTerminadaDialog();
        Bundle args = new Bundle();
        args.putBoolean(EXITO_KEY, exitosa);
        prueba.setArguments(args);
        return prueba;
    }

    public interface OnPruebaTerminadaClick
    {
        /**
         * Método que indica a la actividad o fragment que se ha aceptado el dialogo y debe cerrase el cuadro y salir de la actividad.
         * */
        void OnPruebaTerminadaAceptar();
    }

    public void setListener(OnPruebaTerminadaClick listener)
    {
        mListener = listener;
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
        View view = inflater.inflate(R.layout.dialog_prueba_terminada, container);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        Button mAcceptButton = view.findViewById(R.id.boton_doble_aceptar);
            mAcceptButton.setOnClickListener(v-> {mListener.OnPruebaTerminadaAceptar(); this.dismiss();});

        mTituloTV = view.findViewById(R.id.prueba_titulo_interactivo);

        mDescripcionTV = view.findViewById(R.id.prueba_descripcion_interactivo);

        assert getArguments() != null;
        mExitosa = getArguments().getBoolean(EXITO_KEY);

        configurarMensaje(mExitosa);

        this.setCancelable(false);

        return view;
    }

    private void configurarMensaje(boolean exitoso)
        {
            mTituloTV.setText((exitoso) ? R.string.video_interactivo_exito_titulo : R.string.video_interactivo_fail_titulo);

            mDescripcionTV.setText((exitoso) ? R.string.video_interactivo_exito_descripcion : R.string.video_interactivo_fail_descripcion);
        }
}

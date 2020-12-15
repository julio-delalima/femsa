package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador;

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
import android.widget.TextView;

import com.femsa.sferea.R;

public class CelulasEntrenamientoDialogFragment extends DialogFragment implements View.OnClickListener {

    //Referencia que infla las vistas del layout.
    private View mView;

    //Variable en la que se guarda la descripción que se visualizará en el DialogFragment.
    private String mDescripcion;

    //Referencia al botón que cierra el DialogFragment.
    private Button mBtnAceptar;

    //Referencia al TextView donde se coloca la descripción del DialogFragment.
    private TextView mTvDescripcion;

    //Listener que agrega funcionalidad al botón "Aceptar".
    private RechazarDialogListener mListener;

    //Interface que define el método para cerrar el DialogFragment y la Activity cuando se presiona el botón "Aceptar".
    public interface RechazarDialogListener {
        void onFinishRechazar();
    }

    public CelulasEntrenamientoDialogFragment() { }

    /**
     * <p>Método creado para el caso en que se use NoDisponibleActivity.java</p>
     * @param descripcion Descripción que se visualiza en el DialogFragment.
     * @return
     */
    public static CelulasEntrenamientoDialogFragment newInstance(String descripcion) {
        Bundle args = new Bundle();
        args.putString("descripcion", descripcion);
        CelulasEntrenamientoDialogFragment fragment = new CelulasEntrenamientoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mDescripcion=getArguments().getString("descripcion");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * <p>Método que coloca la descripción en el TextView respectivo del DialogFragment.</p>
     */
    public void setDescripcion(){
        mTvDescripcion.setText(mDescripcion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rechazar_dialog, container, false);

        //Agregando la referencia y un onClickListener al botón "Aceptar".
        mBtnAceptar = mView.findViewById(R.id.btn_fragment_no_responsable_aceptar);
        mBtnAceptar.setOnClickListener(this);

        //Agregando la referencia del TextView de la descripción.
        mTvDescripcion = mView.findViewById(R.id.tv_descripcion);

        setDescripcion();
        return mView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_fragment_no_responsable_aceptar){
            mListener.onFinishRechazar();
        }
    }

    @Override
    public void onAttach(Context context) {
        mListener = (RechazarDialogListener) context;
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

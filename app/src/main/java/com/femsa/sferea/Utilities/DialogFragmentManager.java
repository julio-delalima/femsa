package com.femsa.sferea.Utilities;

import android.graphics.Point;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class DialogFragmentManager extends DialogFragment {

    private boolean mCancelable;

    private boolean mInvertirBotones;

    private boolean mBotonUnico;

    private boolean mSoloCerrar;

    private String mTitulo;

    private String mDescripcion;

    private String mBotonAceptarTexto;

    private String mBotonCancelarTexto;

    private static final String CANCELABLE_KEY = "cancelable";

    private static final String INVERTIR_BOTONES_KEY = "botonesInvertidos";

    private static final String BOTON_UNICO_KEY = "botonUnico";

    private static final String TITULO_KEY = "tituloTexto";

    private static final String DESCRIPCION_KEY = "DescripcionTexto";

    private static final String ACEPTAR_TEXTO_KEY = "aceptarTexto";

    private static final String CANCELAR_TEXTO_KEY = "cancelarTexto";

    private static final String CERRAR_KEY = "soloCerrar";

    private View mView;

    private OnDialogManagerButtonActions mListener;

    private Button mAceptarDobleBoton, mCancelarDobleBoton, mAceptarUnicoBoton;

    private TextView mTituloTextView, mDescripcionTextView;

    private ConstraintLayout mUnicoContainer, mdobleContainer;


    public interface OnDialogManagerButtonActions
        {
            void OnDialogAceptarClick();

            void OnDialogCancelarClick();
        }

    public void setListener(OnDialogManagerButtonActions listener)
        {
            mListener = listener;
        }

    public static DialogFragmentManager newInstance(boolean cancelable, boolean invertirBotones, boolean botonUnico, String titulo,
                                                    String descripcion, String botonAceptarTexto, String botonCancelarTexto, boolean soloCerrar)
    {
        DialogFragmentManager manager = new DialogFragmentManager();
        Bundle args = new Bundle();
        args.putBoolean(CANCELABLE_KEY, cancelable);
        args.putBoolean(INVERTIR_BOTONES_KEY, invertirBotones);
        args.putBoolean(BOTON_UNICO_KEY, botonUnico);
        args.putString(TITULO_KEY, titulo);
        args.putString(DESCRIPCION_KEY, descripcion);
        args.putString(ACEPTAR_TEXTO_KEY, botonAceptarTexto);
        args.putString(CANCELAR_TEXTO_KEY, botonCancelarTexto);
        args.putBoolean(CERRAR_KEY, soloCerrar);
        manager.setArguments(args);
        return manager;
    }

    /**
     * <p>Permite redimensionar correctamente el fragment del cuadro de dialogo.</p>
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.dialog_manager, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mCancelable = getArguments().getBoolean(CANCELABLE_KEY);

        mInvertirBotones = getArguments().getBoolean(INVERTIR_BOTONES_KEY);

        mBotonUnico = getArguments().getBoolean(BOTON_UNICO_KEY);

        mTitulo = getArguments().getString(TITULO_KEY);

        mDescripcion = getArguments().getString(DESCRIPCION_KEY);

        mBotonAceptarTexto = getArguments().getString(ACEPTAR_TEXTO_KEY);

        mBotonCancelarTexto = getArguments().getString(CANCELAR_TEXTO_KEY);

        mSoloCerrar = getArguments().getBoolean(CERRAR_KEY);

        bindViews();

        configureManager();

        return mView;
    }

    private void bindViews()
    {
        mAceptarDobleBoton = mView.findViewById(R.id.boton_doble_aceptar);

        mCancelarDobleBoton = mView.findViewById(R.id.boton_doble_cancelar);

        mAceptarUnicoBoton = mView.findViewById(R.id.boton_unico_aceptar);

        mTituloTextView = mView.findViewById(R.id.dialog_manager_titulo);

        mDescripcionTextView = mView.findViewById(R.id.dialog_manager_descripcion);

        mdobleContainer = mView.findViewById(R.id.boton_doble_layout);

        mUnicoContainer = mView.findViewById(R.id.boton_unico_layout);
    }

    private void configureManager()
    {
        this.setCancelable(mCancelable);

        mUnicoContainer.setVisibility((mBotonUnico) ? View.VISIBLE : View.GONE);
        mdobleContainer.setVisibility((mBotonUnico) ? View.GONE : View.VISIBLE);

        mAceptarUnicoBoton.setOnClickListener(v-> aceptarClick());

        if(mInvertirBotones)
            {
                mAceptarDobleBoton.setOnClickListener(v-> cancelarClick());
                mCancelarDobleBoton.setOnClickListener(v-> aceptarClick());
            }
        else
            {
                mAceptarDobleBoton.setOnClickListener(v-> aceptarClick());
                mCancelarDobleBoton.setOnClickListener(v-> cancelarClick());
            }

        mAceptarDobleBoton.setText((mInvertirBotones) ? mBotonCancelarTexto : mBotonAceptarTexto);

        mCancelarDobleBoton.setText((mInvertirBotones) ? mBotonAceptarTexto : mBotonCancelarTexto);

        mAceptarUnicoBoton.setText(mBotonAceptarTexto);

        mTituloTextView.setText(mTitulo);

        mDescripcionTextView.setText(mDescripcion);

    }

    private void aceptarClick()
    {
        if(!mSoloCerrar)
            {
                mListener.OnDialogAceptarClick();
            }
        this.dismiss();
    }

    private void cancelarClick()
        {
            if(!mSoloCerrar)
                {
                    mListener.OnDialogCancelarClick();
                }
            this.dismiss();
        }

}

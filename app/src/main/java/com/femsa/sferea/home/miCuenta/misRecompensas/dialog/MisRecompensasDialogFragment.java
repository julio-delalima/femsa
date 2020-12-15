package com.femsa.sferea.home.miCuenta.misRecompensas.dialog;

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

public class MisRecompensasDialogFragment extends DialogFragment implements View.OnClickListener {

    private View mView;

    private Button mBtnAceptar;

    private TextView mTvTitulo;
    private TextView mTvDescripcion;

    //Variables donde se almacenarán el título y la descripción que se visualizará en el Dialog.
    private String titulo;
    private String descripcion;

    private RecompensasListener mListener;

    public MisRecompensasDialogFragment() { }

    /**
     * <p>Método que permite asignar el título y la descripción que mostrará el DialogFragment,
     * dependiendo del resultado que se genere al intentar cambiar una recompensa.</p>
     * @param titulo Título del DialogFragment.
     * @param descripcion Descripción del DialogFragment.
     * @return Fragment de tipo MisRecompensasDialogFragment.
     */
    public static MisRecompensasDialogFragment newInstance(String titulo, String descripcion) {
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("descripcion", descripcion);
        MisRecompensasDialogFragment fragment = new MisRecompensasDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            titulo = getArguments().getString("titulo");
            descripcion = getArguments().getString("descripcion");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * <p>Método que coloca el título y la descripción en los respectivos TextView.</p>
     */
    private void setTextos(){
        mTvTitulo.setText(titulo);
        mTvDescripcion.setText(descripcion);
    }

    private void bindViews(){
        mTvTitulo = mView.findViewById(R.id.tv_fragment_mis_recompensas_dialog_title);
        mTvDescripcion = mView.findViewById(R.id.tv_fragment_mis_recompensas_dialog_description);
        mBtnAceptar = mView.findViewById(R.id.btn_fragment_mis_recompensas);
    }

    private void bindListeners(){
        mBtnAceptar.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mis_recompensas_dialog, container, false);
        bindViews();
        bindListeners();
        setTextos();
        return mView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_fragment_mis_recompensas){
            mListener.onClickAceptar();
        }
    }

    @Override
    public void onAttach(Context context) {
        mListener = (RecompensasListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

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

    public interface RecompensasListener{
        void onClickAceptar();
    }
}

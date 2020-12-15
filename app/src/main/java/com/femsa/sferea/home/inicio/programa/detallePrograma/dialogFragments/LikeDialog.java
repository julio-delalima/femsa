package com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments;

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
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;

public class LikeDialog extends DialogFragment {

    private View mView;

    private Button mAcceptButton;

    private TextView mDescripcionDialogo;


    public LikeDialog() {
        super();
    }

    public static LikeDialog newInstance(String descripcioon) {
        LikeDialog frag = new LikeDialog();
        Bundle args = new Bundle();
        args.putString(DetalleProgramaActivity.TITULO_PROGRAMA_KEY, descripcioon);
        frag.setArguments(args);
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
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.dialog_like, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mAcceptButton = mView.findViewById(R.id.inscribirse_si_button);
        mAcceptButton.setOnClickListener(v->this.dismiss());

        String tempDescrpcion = getArguments().getString(DetalleProgramaActivity.TITULO_PROGRAMA_KEY);
        mDescripcionDialogo = mView.findViewById(R.id.dialog_inscribir_descripcion_tv);
        mDescripcionDialogo.setText(tempDescrpcion);

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

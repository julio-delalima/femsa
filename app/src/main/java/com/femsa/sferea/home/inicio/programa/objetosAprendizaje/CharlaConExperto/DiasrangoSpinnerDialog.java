package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

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

import com.aigestudio.wheelpicker.WheelPicker;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.StringManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class DiasrangoSpinnerDialog extends DialogFragment {

    private Button mAcceptButton;

    private View mView;

    private WheelPicker mWheelHorasPicker;

    private ArrayList<String> horasArray, horasParseadas;

    public OnDiasSelected mListener;


    public interface OnDiasSelected {
        void onDiasSelected(String dias, String parseada);
    }

    public void setDias(ArrayList<String> dias){
        horasArray = new ArrayList<>();
        horasArray.addAll(dias);
    }

    public static DiasrangoSpinnerDialog newInstance() {
        DiasrangoSpinnerDialog frag = new DiasrangoSpinnerDialog();
        return frag;
    }
    public void setListener(OnDiasSelected listener) {
        mListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert  window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int)(size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView mText;
        mView = inflater.inflate(R.layout.fragment_diasrango_spinner_dialog,container);
        mText = mView.findViewById(R.id.hora_spinner_picker_titulo);
        mText.setText(getResources().getString(R.string.fecha_solicitada));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bindviews();
        return mView;
    }

    private void bindviews() {
        mAcceptButton = mView.findViewById(R.id.hora_spinner_picker_aceptar_button);
        mAcceptButton.setOnClickListener(v-> onAceptarClick());
        mWheelHorasPicker = mView.findViewById(R.id.hora_spinner_picker_wheel_horas);
        horasParseadas = new ArrayList<>();
        SimpleDateFormat viejo, nuevo;
        viejo = new SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault());
        nuevo = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());


        for(String fecha: horasArray){
            horasParseadas.add(StringManager.parseFecha(fecha, viejo, nuevo));
        }

        mWheelHorasPicker.setData(horasParseadas);
    }

    private void onAceptarClick() {
        mListener.onDiasSelected(horasArray.get(mWheelHorasPicker.getCurrentItemPosition()).replace("hrs.", ""),
                horasParseadas.get(mWheelHorasPicker.getCurrentItemPosition()));
        this.dismiss();
    }
}

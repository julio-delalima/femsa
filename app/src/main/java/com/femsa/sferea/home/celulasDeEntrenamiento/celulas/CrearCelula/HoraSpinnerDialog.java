package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula;

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

import com.aigestudio.wheelpicker.WheelPicker;
import com.femsa.sferea.R;

import java.util.ArrayList;


public class HoraSpinnerDialog extends DialogFragment {

    private Button mAcceptButton;

    private View mView;

    private WheelPicker mWheelHorasPicker;

    private ArrayList<String> horasArray;

    public OnHorasSelected mListener;

    public interface OnHorasSelected
        {
            void onHorasSelected(String horas);
        }

    public static HoraSpinnerDialog newInstance() {
        HoraSpinnerDialog frag = new HoraSpinnerDialog();
        return frag;
    }

    public void setListener(OnHorasSelected listener)
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_hora_spinner_picker, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bindviews();
        initHoras();
        return  mView;
    }

    private void bindviews()
        {
            mAcceptButton = mView.findViewById(R.id.hora_spinner_picker_aceptar_button);
                mAcceptButton.setOnClickListener(v-> onAceptarClick());
            mWheelHorasPicker = mView.findViewById(R.id.hora_spinner_picker_wheel_horas);
        }

    /**
     * Método para inicializar las horas dentro del spiner de horas al seleccionar la duración de un tema específico,
     * y van de 0.5H - 8H.
     * */
    private void initHoras()
    {
        horasArray = new ArrayList<>();
        for(int i = 1; i < 16; i++)
        {
            horasArray.add(getResources().getString(R.string.hrs, String.valueOf(0.5f + i/2f)));
        }
        mWheelHorasPicker.setData(horasArray);
    }

    private void onAceptarClick()
        {
            mListener.onHorasSelected(horasArray.get(mWheelHorasPicker.getCurrentItemPosition()).replace("hrs.", ""));
            this.dismiss();
            //Toast.makeText(getContext(), horasArray.get(mWheelHorasPicker.getCurrentItemPosition()) + " horas", Toast.LENGTH_SHORT).show();
        }
}

package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.sferea.R;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.Calendario.RangoFechasActivity;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class RangoFechasFragment extends DialogFragment {
    public static final String HORAST_KEY = "horas";
    public static final String TIPO_KEY = "tipo";
    private Button mBtnSeguir;
    private View mView;
    private TextView mHoraTotal, mTitulo;
    private TextView mDias;
    private float horas_totales;
    private CelulaDTO mCelulaDTO;
    private String mTipo;

    public RangoFechasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =inflater.inflate(R.layout.fragment_rango_fechas, container, false);
        horas_totales = getArguments().getFloat(HORAST_KEY);
        mTipo = getArguments().getString(TIPO_KEY);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bindViews();
        bindLissteners();
        mHoraTotal.setText(getString(R.string.horas_total, horas_totales));
        if (horas_totales <=8){
            mDias.setText(getString(R.string.dias_totales_1, String.valueOf(1)));
        }else {
            calcularDias();
        }

        mTitulo.setText(
            getResources().getString(
                R.string.label_rango_fechas,
                    mTipo.equals("Célula") ?
                    getResources().getString(R.string.Celulas_label_singular) :
                    getResources().getString(R.string.induccion_label)
                )
        );


        return mView;
    }

    private void calcularDias() {
        DecimalFormat df = new DecimalFormat("##.#");
        float dias;
        dias = horas_totales/8;
        mDias.setText(getString(R.string.dias_totales, String.valueOf(df.format(dias))));
    }

    public void setDTO(CelulaDTO celuladto)
        {
            mCelulaDTO = new CelulaDTO(celuladto);
        }

    private void bindLissteners() {

        mBtnSeguir.setOnClickListener(v -> {
            dismiss();
            Intent intent;
            intent = new Intent(getContext(), RangoFechasActivity.class);
            intent.putExtra(RangoFechasActivity.TIPO_KEY, mTipo);
            intent.putExtra("CelulaDTO", mCelulaDTO);
            startActivity(intent);
        });
    }

    private void bindViews() {
        mBtnSeguir = mView.findViewById(R.id.btn_nextrf);
        mHoraTotal = mView.findViewById(R.id.horas_totales);
        mDias = mView.findViewById(R.id.dias_totales);
        mTitulo = mView.findViewById(R.id.rango_fechas_titulo);
    }
}

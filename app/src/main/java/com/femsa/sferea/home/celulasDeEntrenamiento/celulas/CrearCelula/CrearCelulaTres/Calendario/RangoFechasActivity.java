package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.Calendario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula.ConfirmarCelulaActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class RangoFechasActivity extends AppCompatActivity {
    public static final String TIPO_KEY = "tipo";
    private EditText mFechaInicio;
    private EditText mFechaFin;
    private MaterialCalendarView Calendar;
    private CalendarDay mDate;
    private Toolbar mtoolbar;
    private ArrayList<CalendarDay> mlistado;
    private ArrayList<CalendarDay> mlistadoaux;
    private String FechaSelected;
    private Button mConfirmar;
    private CelulaDTO mCelulaDTO, mCelulaDTOEditable;
    private boolean rango = false, dia = false;
    private String mTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rango_fechas);
        mCelulaDTOEditable = (CelulaDTO) getIntent().getSerializableExtra("Editar");
        mCelulaDTO = (CelulaDTO) getIntent().getSerializableExtra("CelulaDTO");
        Intent intent = getIntent();
        mTipo = intent.getStringExtra(TIPO_KEY);
        bindViews();
        bindListeners();
    }

    private void bindListeners() {
        mtoolbar = findViewById(R.id.toolbar_rangoFecha);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Calendar.setOnDateChangedListener((widget, date1, selected) -> {
            if(!rango)
            {
                mlistadoaux = mlistado;
                mlistado.clear();
                mlistado.add(date1);
                primerDia(mlistado.get(0));
                    if(dia){
                        mFechaInicio.setText("");
                        mFechaFin.setText("");
                        dia = false;
                    }
                    else{
                        dia = true;
                    }
            }
            else{
                if(!dentrodeRango(mlistado, date1))
                {
                    mlistadoaux = mlistado;
                    mlistado.clear();
                    mlistado.add(date1);
                    primerDia(mlistado.get(0));
                    if(dia){
                        mFechaInicio.setText("");
                        mFechaFin.setText("");
                        dia = false;
                    }
                    else{
                        dia = true;
                    }
                }
                else
                {
                    mFechaInicio.setText("");
                    mFechaFin.setText("");
                    rango = false;
                }
            }
        });

        Calendar.setOnRangeSelectedListener((widget, dates) -> {
            mlistadoaux = mlistado;
            mlistado.clear();
            mlistado.addAll(dates);
            primerDia(mlistado.get(0));
            ultimoDia(mlistado.get(mlistado.size()-1));
            dia = false;
            rango = true;
        });

        if(mCelulaDTOEditable != null)
        {
            /**
             * Aqui va lo de poner la fecha en el calendario
             * */
            mCelulaDTO = mCelulaDTOEditable;
        }
    }

    private boolean dentrodeRango(ArrayList<CalendarDay> listado, CalendarDay fecha){
        for(int i = 0; i < listado.size(); i++){
            LogManager.d("Fesha", listado.get(i).toString() + " - " + fecha.toString());
            if(listado.get(i).toString().equals(fecha.toString())) {
                return true;
            }
        }
        return false;
    }

    private void primerDia(CalendarDay day){
        String dia;
        String mes;
        String anio;

        dia = Integer.toString(day.getDay());
        dia = dia.length() < 2 ? "0" + dia : dia;
        mes = cambioMes(day.getMonth());
        anio = Integer.toString(day.getYear());

        SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        FechaSelected = (StringManager.parseFecha(dia +"/"+mes + "/"+anio, fechaFormato, SharedPreferencesUtil.getInstance().getIdioma()));
        mFechaInicio.setText(FechaSelected);
        mFechaFin.setText(FechaSelected);

    }

    private void ultimoDia(CalendarDay day){
        String dia;
        String mes;
        String anio;

        dia = Integer.toString(day.getDay());
        mes = cambioMes(day.getMonth());
        anio = Integer.toString(day.getYear());
        SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        FechaSelected = (StringManager.parseFecha(dia+"/"+mes + "/"+anio, fechaFormato, SharedPreferencesUtil.getInstance().getIdioma()));
        mFechaFin.setText(FechaSelected);
    }


    private String cambioMes(int mesnum)
    {
        String result;
        switch (mesnum){
            case 1:
                result = getResources().getStringArray(R.array.short_months)[0];
                break;
            case 2:
                result = getResources().getStringArray(R.array.short_months)[1];
                break;
            case 3:
                result = getResources().getStringArray(R.array.short_months)[2];
                break;
            case 4:
                result = getResources().getStringArray(R.array.short_months)[3];
                break;
            case 5:
                result = getResources().getStringArray(R.array.short_months)[4];
                break;
            case 6:
                result = getResources().getStringArray(R.array.short_months)[5];
                break;
            case 7:
                result = getResources().getStringArray(R.array.short_months)[6];
                break;
            case 8:
                result = getResources().getStringArray(R.array.short_months)[7];
                break;
            case 9:
                result = getResources().getStringArray(R.array.short_months)[8];
                break;
            case 10:
                result = getResources().getStringArray(R.array.short_months)[9];
                break;
            case 11:
                result = getResources().getStringArray(R.array.short_months)[10];
                break;
            case 12:
                result = getResources().getStringArray(R.array.short_months)[11];
                break;
            default:
                result = Integer.toString(mesnum);
        }
        return result;
    }

    private void bindViews() {
        mFechaFin = findViewById(R.id.Edt_fechaFin);
        mFechaInicio = findViewById(R.id.Edt_fecha_inicio);
        Calendar = findViewById(R.id.calendarView);
        Calendar.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();
        mtoolbar = findViewById(R.id.toolbar_rangoFecha);
        mlistado = new ArrayList<>();
        mConfirmar = findViewById(R.id.btn_siguiente);
            mConfirmar.setOnClickListener(
                    v-> onValidarFecha()
            );
    }

    @Override
    public boolean onSupportNavigateUp() {
        mlistado = mlistadoaux;
        onBackPressed();
        return true;
    }

    private void onConfirmarFecha()
        {
            mCelulaDTO.setFechaInicio(mFechaInicio.getText().toString());
            mCelulaDTO.setFechaFin(mFechaFin.getText().toString());
            if(mCelulaDTOEditable != null)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("CelulaDTOEditable", mCelulaDTO);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            else
                {
                    Intent irAConfirmar = new Intent(this, ConfirmarCelulaActivity.class);
                    irAConfirmar.putExtra("CelulaDTO", mCelulaDTO);
                    irAConfirmar.putExtra(ConfirmarCelulaActivity.TIPO_KEY, mTipo);
                    startActivity(irAConfirmar);
                }
            }

    public void onValidarFecha()
    {
        if(!mFechaFin.getText().toString().isEmpty() && !mFechaInicio.getText().toString().isEmpty())
        {
            onConfirmarFecha();
        }
        else
        {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.sin_fecha));
        }
    }

}

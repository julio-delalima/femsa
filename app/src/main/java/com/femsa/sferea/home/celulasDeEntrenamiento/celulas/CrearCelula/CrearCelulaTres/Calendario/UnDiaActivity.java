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
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula.ConfirmarCelulaActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


public class UnDiaActivity extends AppCompatActivity {

    public static final String TIPO_KEY = "tipo";
    private Toolbar mtoolbar;
    private EditText mFechaInicio;
    private EditText mFechaFin;
    private MaterialCalendarView mCalendar;
    private CalendarDay mDate;
    private String FechaSelected;
    private Button mConfirmar;
    private CelulaDTO mCelulaDTO, mCelulaDTOEditable;
    private String mTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_dia);
        mCelulaDTOEditable = (CelulaDTO) getIntent().getSerializableExtra("Editar");
        mCelulaDTO = (CelulaDTO) getIntent().getSerializableExtra("CelulaDTO");
        Intent intent = getIntent();
        mTipo = intent.getStringExtra(TIPO_KEY);
        bindViews();
        bindListeners();

    }

    private void bindListeners() {
        mFechaInicio = findViewById(R.id.Edt_fecha_inicio);
        mFechaFin = findViewById(R.id.Edt_fechaFin);
        mCalendar = findViewById(R.id.calendarView);
        mCalendar.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();
        mCalendar.setOnDateChangedListener((widget, date1, selected) -> {
            mDate = date1;
            crearfecha(mDate);
            mFechaInicio.setText(FechaSelected);
            mFechaFin.setText(FechaSelected);
        });
        if(mCelulaDTOEditable != null)
            {
                /**
                 * Aqui va lo de poner la fecha en el calendario
                 * */
                mCelulaDTO = mCelulaDTOEditable;
            }

    }

    private void crearfecha(CalendarDay mDate) {
        String dia;
        String mes;
        String anio;

        dia = Integer.toString(mDate.getDay());
        mes = cambioMes(mDate.getMonth());
        anio = Integer.toString(mDate.getYear());

        FechaSelected = (dia+"/"+mes+"/"+anio);
    }

    private String cambioMes(int mesnum)
    {
        String result;
        switch (mesnum){
            case 1:
                result = "Ene";
                break;
            case 2:
                result = "Feb";
                break;
            case 3:
                result = "Mar";
                break;
            case 4:
                result = "Abr";
                break;
            case 5:
                result = "May";
                break;
            case 6:
                result = "Jun";
                break;
            case 7:
                result = "Jul";
                break;
            case 8:
                result = "Ago";
                break;
            case 9:
                result = "Sep";
                break;
            case 10:
                result = "Oct";
                break;
            case 11:
                result = "Nov";
                break;
            case 12:
                result = "Dic";
                break;
            default:
                result = Integer.toString(mesnum);
        }
        return result;
    }

    private void bindViews() {
        mtoolbar = findViewById(R.id.toolbar_rangoFecha);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mConfirmar = findViewById(R.id.btn_siguiente);
            mConfirmar.setOnClickListener(
                    v-> onValidarFecha()
            );
    }

    @Override
    public boolean onSupportNavigateUp() {
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
                    DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.empty_fields));
                }
        }
}

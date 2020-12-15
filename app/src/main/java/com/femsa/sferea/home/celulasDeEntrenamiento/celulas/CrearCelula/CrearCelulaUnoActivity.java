package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos.CrearCelulaDosActivity;
import com.kyleduo.switchbutton.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CrearCelulaUnoActivity extends AppCompatActivity {

    public static final String BANDERA_CELULA_KEY = "isCelula";
    //elementos de la vista
    private Toolbar mtoolbar;
    //Elementos de vista que son clickable's
    //private RadioButton mRadioBtnSi;
   // private RadioButton mRadioBtnNo;
    private Button mbuttonSiguiente;
    //Elementos editables del usuario
    private TextView mTvNumeroEmpleado;
    private TextView mTvNombreEmpleado;
    private TextView mTvPosicionSolicitante;
    private TextView mTvPaisSolicitante;
    private TextView mTvCorreo;
    private TextView mTvFecha;
    private Boolean Participar;
    private SwitchButton mSwitchSerParticipante;
    private TextView mTituloCelula;
    private String banderaCelula;
    private TextView mLabelParticipante;
    ImageView mBanderaSolicitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_uno_celula);
        Intent intent = getIntent();
        banderaCelula = intent.getStringExtra(BANDERA_CELULA_KEY);
        bindviews();
        obtenerFecha();
        obtenerDatos();
        bindLinteners();
        mTituloCelula.setText(banderaCelula.equals(getResources().getString(R.string.Celula)) ? getResources().getString(R.string.title_celulas): banderaCelula);
        mLabelParticipante.setText(this.getString(R.string.participacion, banderaCelula.toLowerCase()));
    }

    private void obtenerDatos() {
        mTvNumeroEmpleado.setText(SharedPreferencesUtil.getInstance().getNumEmpleado());
        mTvNombreEmpleado.setText(SharedPreferencesUtil.getInstance().getNombreSP());
        mTvPosicionSolicitante.setText(SharedPreferencesUtil.getInstance().getDescriptionPosition());
        mTvPaisSolicitante.setText(SharedPreferencesUtil.getInstance().getNombrePais());
        String fullPath = RequestManager.IMAGEN_CUADRADA_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBanderaSolicitante);
        mTvCorreo.setText(SharedPreferencesUtil.getInstance().getCorreo());
    }

    private void bindviews() {
        mtoolbar = findViewById(R.id.toolbar_celulas);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mbuttonSiguiente = findViewById(R.id.btn_siguiente_1);
        mTvNumeroEmpleado = findViewById(R.id.num_empleado);
        mTvNombreEmpleado = findViewById(R.id.tv_layout_datos_solicitante_nombre_solicitante);
        mTvPosicionSolicitante = findViewById(R.id.tv_layout_datos_solicitante_posicion_solicitante);
        mTvPaisSolicitante = findViewById(R.id.tv_layout_datos_solicitante_pais_solicitante);
        mTvCorreo = findViewById(R.id.tv_layout_datos_solicitante_correo_eletronico);
        //mRadioBtnSi = findViewById(R.id.rbtn_si);
        //mRadioBtnNo = findViewById(R.id.rbtn_no);
        mTvFecha = findViewById(R.id.tv_fecha_solicitud);
        mSwitchSerParticipante = findViewById(R.id.switch_seras_participante);
        mTituloCelula = findViewById(R.id.TituloCrearCelula);
        mLabelParticipante = findViewById(R.id.LabelParticipar);
            mSwitchSerParticipante.setOnCheckedChangeListener((buttonView, isChecked) -> Participar = isChecked);
        mBanderaSolicitante = findViewById(R.id.imagen_pais_solicitante_uno);
    }

    private void bindLinteners() {
        mbuttonSiguiente.setOnClickListener(
                v-> onClickAceptar()
        );
       // mRadioBtnSi.setOnClickListener(this);
        //mRadioBtnNo.setOnClickListener(this);
    }

    private void obtenerFecha() {
        Calendar fecha = Calendar.getInstance();
        String dia;
        int mesnum;
        String mes;
        String anio;
        dia = Integer.toString( fecha.get(Calendar.DAY_OF_MONTH));
        mesnum = fecha.get(Calendar.MONTH);

        mes = cambioMes(mesnum);

        anio = Integer.toString(fecha.get(Calendar.YEAR));
        SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        mTvFecha.setText(StringManager.parseFecha(dia+"/"+mes + "/"+anio, fechaFormato, SharedPreferencesUtil.getInstance().getIdioma()));

    }

    private String cambioMes(int mesnum) {
        String result;
        switch (mesnum){
            case 0:
                result = "Ene";
                break;
            case 1:
                result = "Feb";
                break;
            case 2:
                result = "Mar";
                break;
            case 3:
                result = "Abr";
                break;
            case 4:
                result = "May";
                break;
            case 5:
                result = "Jun";
                break;
            case 6:
                result = "Jul";
                break;
            case 7:
                result = "Ago";
                break;
            case 8:
                result = "Sep";
                break;
            case 9:
                result = "Oct";
                break;
            case 10:
                result = "Nov";
                break;
            case 11:
                result = "Dic";
                break;
            default:
                result = Integer.toString(mesnum);
        }
        return result;
    }

    private void onClickAceptar() {
        Intent intent = new Intent(this, CrearCelulaDosActivity.class);
        intent.putExtra(CrearCelulaDosActivity.PARTICIPAR_KEY, Participar);
        intent.putExtra(CrearCelulaDosActivity.TIPO_KEY,banderaCelula);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.ClaseMiguel;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos.CrearCelulaDosActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.Calendario.RangoFechasActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.Calendario.UnDiaActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.CrearCelulaTresActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConfirmarCelulaActivity extends AppCompatActivity implements ConfirmarParticipantesAdapter.OnConfirmarParticipantes, CelulaView,
DialogFragmentManager.OnDialogManagerButtonActions{

    public static final String TIPO_KEY = "tipo";

    private NestedScrollView mMainscroll;

    private Toolbar mToolbar;

    private TextView mFechaSolicitudTv;

    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    //Banderas
    private ImageView mBanderaPaisSolicitante, mBanderaPaisSolicitanteCelula, mBanderaPaisReceptorCelula;

    //Datos solicitante
    private TextView mNumeroEmpleadoTv, mPosicionTv, mNombreSolicitanteTv, mPaisSolicitanteTv, mCorreoSolicitantTv;

    //Participantes
    private ImageView mEditarParticipantesButtonIv;
    private RecyclerView mRecyclerParticipantes;

    // Información celula
    private TextView mTemaGeneralTv, mTemaPaisSolicitanteTv, mTemaPaisReceptorTv;
    private RecyclerView mRecyclerTemasCelula;
    private ImageView mEditarTemasButtonIv;

    //Fechas
    private ImageView mEditarFechasButtonIv;
    private TextView mFechaInicioTv, mFechaFinTv;

    //Enviar
    private Button mEnviarBtn;

    private CelulaDTO mCelulaDTO;

    private ConfirmarParticipantesAdapter mParticipantesAdapter;

    private ConfirmarTemasAdapter mTemasAdapter;

    private CelulaPresenter mPresenter;

    private String mtipo;

    private TextView mTiulo;

    private Loader loader;
    private TextView mLabeltitulo;
    private TextView mLabelInformacion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_celula);
        Intent intent = getIntent();
        mtipo = intent.getStringExtra(TIPO_KEY);
        mCelulaDTO = (CelulaDTO) getIntent().getSerializableExtra("CelulaDTO");
        bindviews();
        mTiulo.setText(mtipo);
        bindAdapters();
        fillData();
        if (mtipo.equals("Célula")){
            mLabeltitulo.setText(getResources().getString(R.string.confirmar_celula, "Célula de Entrenamiento"));
            mLabelInformacion.setText(getResources().getString(R.string.layout_informacion_celula_titulo ));
        }
        if (mtipo.equals("Inducción")){
            mLabeltitulo.setText(getResources().getString(R.string.confirmar_celula, "Inducción"));
            mLabelInformacion.setText(getResources().getString(R.string.layout_informacion_induccion_titulo ));
            mBanderaPaisSolicitanteCelula.setVisibility(View.GONE);
            mBanderaPaisReceptorCelula.setVisibility(View.GONE);
            mTemaPaisSolicitanteTv.setVisibility(View.GONE);
            mTemaPaisReceptorTv.setVisibility(View.GONE);
            findViewById(R.id.confirmar_celula_pais_solicitante_tema_titulo).setVisibility(View.GONE);
            findViewById(R.id.confirmar_celula_pais_receptor_titulo).setVisibility(View.GONE);

        }

    }

    /**
     * <p>Método para asignar a casa variable su respectivo elemento de la vista.</p>
     * */
    private void bindviews()
        {
            mToolbar = findViewById(R.id.configuracion_toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            mTiulo = findViewById(R.id.configuracion_toolbar_titulo);

            mFechaSolicitudTv = findViewById(R.id.confirmar_celula_fecha_solicitud);

            // Solicitantes
            mNumeroEmpleadoTv = findViewById(R.id.confirmar_celula_num_empleado);
            mPosicionTv  = findViewById(R.id.confirmar_celula_posicion_solicitante);
            mNombreSolicitanteTv = findViewById(R.id.confirmar_celula_nombre_solicitante_tv);
            mPaisSolicitanteTv = findViewById(R.id.confirmar_celula_pais_solicitante);
            mCorreoSolicitantTv = findViewById(R.id.confirmar_celula_correo);

            //Participante
            mEditarParticipantesButtonIv = findViewById(R.id.confirmar_celula_editar_participantes);
                mEditarParticipantesButtonIv.setOnClickListener(v-> onEditarParticipantes());
            mRecyclerParticipantes  = findViewById(R.id.recycler_confirmar_celula_participantes);

            //Celula
            mTemaGeneralTv = findViewById(R.id.confirmar_celula_tema_general);
            mTemaPaisSolicitanteTv = findViewById(R.id.confirmar_celula_pais_solicitante_tema_tv);
            mTemaPaisReceptorTv = findViewById(R.id.confirmar_celula_pais_receptor_tema_tv);
            mRecyclerTemasCelula = findViewById(R.id.confirmar_celulas_temas_rv);
            mEditarTemasButtonIv = findViewById(R.id.confirmar_celula_editar_temas);
                mEditarTemasButtonIv.setOnClickListener(v->onEditarTemas());

                        //Fechas
            mEditarFechasButtonIv = findViewById(R.id.confirmar_celula_editar_fechas);
                mEditarFechasButtonIv.setOnClickListener(v->onEditarFechas());
            mFechaInicioTv = findViewById(R.id.confirmar_celula_fecha_inicio);
            mFechaFinTv = findViewById(R.id.confirmar_celula_fecha_fin);
            mLabeltitulo = findViewById(R.id.Title_layout_confirmar);
            mLabelInformacion = findViewById(R.id.informacion_celula_titulo);

            //Enviar
            mEnviarBtn = findViewById(R.id.confirmar_celula_enviar_button);
                mEnviarBtn.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        onEnviarPressed();
                    }});

            //banderas
            mBanderaPaisSolicitante = findViewById(R.id.confirmar_celula_pais_solicitante_foto);
            mBanderaPaisSolicitanteCelula = findViewById(R.id.confirmar_celula_pais_solicitante_foto_tema);
            mBanderaPaisReceptorCelula = findViewById(R.id.confirmar_celula_pais_receptor_foto_tema);
            eligeBanderas();

            loader = Loader.newInstance();

            mMainscroll = findViewById(R.id.main_scroll_confirmar);
                mMainscroll.fullScroll(ScrollView.FOCUS_UP);
                mMainscroll.smoothScrollTo(0,0);
        }

    /**
     * <p>Método que carga las banderas en los ImageView de acuerdo al tipo de país</p>
     * */
    private void eligeBanderas()
        {
            String fullPath = mCelulaDTO.getPaisSolicitanteBandera();
            GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBanderaPaisSolicitante);
            GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBanderaPaisSolicitanteCelula);
            String fullPath2 = mCelulaDTO.getPaisReceptorBandera();
            GlideApp.with(getApplicationContext()).load(fullPath2).error(R.drawable.sin_imagen).into(mBanderaPaisReceptorCelula);
        }

    /**
     * <p>Método que inicializa los adapters de acuerdo al tipo de listado</p>
     * */
    private void bindAdapters()
        {
            mListadoPaises = new ArrayList<>();
            mPresenter = new CelulaPresenter(this, new CelulaInteractor());
            mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());

            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            RecyclerView.LayoutManager layout2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            //Participantes
            mParticipantesAdapter = new ConfirmarParticipantesAdapter(mCelulaDTO.getListadoParticipantes(), this, mListadoPaises);
            mParticipantesAdapter.setListener(this);
            mRecyclerParticipantes.setAdapter(mParticipantesAdapter);
            mRecyclerParticipantes.setLayoutManager(layout);

            //Temas
            mTemasAdapter = new ConfirmarTemasAdapter(mCelulaDTO.getListadoTemasEspecificos(), this);
            mRecyclerTemasCelula.setAdapter(mTemasAdapter);
            mRecyclerTemasCelula.setLayoutManager(layout2);
        }

    /**
     * Método que se ejecuta cuando se presiona el botón de enviar de la parte inferior
     * */
    private void onEnviarPressed()
        {
            if(SharedPreferencesUtil.getInstance().getIdioma() == StringManager.ID_IDIOMA_INGLES){
                SimpleDateFormat fechaFormato = new SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault());
                mCelulaDTO.setFechadeSolicitud(StringManager.parseFecha(mCelulaDTO.getFechadeSolicitud(), fechaFormato, StringManager.ID_IDIOMA_ESPANOL));
            }
            mPresenter.OnInteractorLlamarCelula(mCelulaDTO, (mtipo.equals("Célula") ? "2" : "1") ,SharedPreferencesUtil.getInstance().getIdEmpleado(), SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

    /**
     *<p>Método que inicia la Activity @Link{CrearCelulaDosActivity} cuando se hace click sobre el botón de
     * editar participantes.</p>
     * */
    private void onEditarParticipantes()
        {
            Intent intent = new Intent(this, CrearCelulaDosActivity.class);
            intent.putExtra("Editar", mCelulaDTO);
            intent.putExtra(CrearCelulaDosActivity.TIPO_KEY, mtipo);
            startActivityForResult(intent, 1);
        }

    /**
     *<p>Método que inicia la Activity @Link{CrearCelulaTresActivity} cuando se hace click sobre el botón de
     * editar temas.</p>
     * */
    private void onEditarTemas()
        {
            Intent intent = new Intent(this, CrearCelulaTresActivity.class);
            intent.putExtra("Editar", mCelulaDTO);
            intent.putExtra(CrearCelulaTresActivity.TIPO_KEY, mtipo);
            startActivityForResult(intent, 1);
        }

    /**
     *<p>Método que inicia la Activity for result de calendario de un día o de rango de fechas
     * dependiendo si la fecha de inicio y fecha de fn son iguales o no.</p>
     * */
    private void onEditarFechas()
        {
            Intent intent = new Intent(this,
                    (mCelulaDTO.getFechaFin().equals(mCelulaDTO.getFechaInicio()))
                            ? UnDiaActivity.class : RangoFechasActivity.class
                    );
            intent.putExtra("Editar", mCelulaDTO);
            startActivityForResult(intent, 1);
        }

    /**
     * Método que trae de vuelva un FacilitadorDTO el cual es asignado al listado de Facilitadores y asignado en el RecyclerView
     * en el elemento seleccionado.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mCelulaDTO = (CelulaDTO) data.getSerializableExtra("CelulaDTOEditable");
                bindAdapters();
                fillData();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    /**
     * Método implementado de ConfirmarParticipantesAdapter que se encarga de eliminar
     * los paricipantes de una célula verificando que quede al menos uno, no puede haber una célula sin participantes.
     * @param indiceParticipante el indice del participante a eliminar.
     * */
    @Override
    public void onEliminarParticipante(int indiceParticipante) {
        if(ClaseMiguel.funcionMiguel((mCelulaDTO.getListadoParticipantes().size() <= 1)))
            {
                DialogManager.displaySnack(getSupportFragmentManager(), StringManager.MINIMO_PARTICIPANTES);
            }
        else
            {
                mCelulaDTO.getListadoParticipantes().remove(indiceParticipante);
                mParticipantesAdapter.removeParticipante(indiceParticipante);
            }
    }

    private void fillData()
        {
            mFechaSolicitudTv.setText(mCelulaDTO.getFechadeSolicitud());

            mNumeroEmpleadoTv.setText(SharedPreferencesUtil.getInstance().getNumEmpleado());
            mPosicionTv.setText(SharedPreferencesUtil.getInstance().getDescriptionPosition());
            mNombreSolicitanteTv.setText(SharedPreferencesUtil.getInstance().getNombreSP());
            mPaisSolicitanteTv.setText(SharedPreferencesUtil.getInstance().getNombrePais());
            mCorreoSolicitantTv.setText(SharedPreferencesUtil.getInstance().getCorreo());

            mTemaGeneralTv.setText(mCelulaDTO.getTemaGeneral());
            mTemaPaisSolicitanteTv.setText(mCelulaDTO.getPaisSolicitante());
            mTemaPaisReceptorTv.setText(mCelulaDTO.getPaisReceptor());

            mFechaInicioTv.setText(mCelulaDTO.getFechaInicio());
            mFechaFinTv.setText(mCelulaDTO.getFechaFin());
            eligeBanderas();
        }

    /**
     * Se sobreescribe este método para darle una funcionalidad a la flecha de back de la toolbar.
     * */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void OnViewMostrarLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if (!loader.isAdded()) {
            loader.show(fm, "Loader");
        }
    }

    @Override
    public void OnViewOcultarLoader() {
        try
        {
            if(loader != null && loader.isAdded())
            {
                loader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnViewMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    @Override
    public void OnViewMostrarSolicitudExitosa() {
        String message = getResources().getString(R.string.solicitud_exitosa);
        if(mtipo.equals("Célula"))
        {
            mtipo = getResources().getString(R.string.Celulas_label_singular);
        }
        else
            {
                mtipo = getResources().getString(R.string.induccion_label);
            }
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, mtipo,
                message, AppTalentoRHApplication.getApplication().getString(R.string.aceptar), "", false);
        fragment.setListener(this);
        fragment.show(getSupportFragmentManager(), "Error");
    }

    @Override
    public void OnViewSesionInvalida() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {
        mParticipantesAdapter.setPaisesData(data.getmPaises().getListadoPaises());
    }

    @Override
    public void OnDialogAceptarClick() {
        this.finish();
        Intent irAHome = new Intent(this, HomeActivity.class);
        irAHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        irAHome.putExtra("Celulas", true);
        startActivity(irAHome);
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}

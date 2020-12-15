package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.InformacionDeInduccionDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ParticipanteDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.DetalleSolicitudInteractor;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.DetalleSolicitudPresenter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.DetalleSolicitudView;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.ParticipantesAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.TemaEspecificoAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador.ContactarAdministradorActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario.ConfirmarHorarioActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud.NoDisponibleActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud.NoResponsableActivity;

import java.util.ArrayList;

public class PorProgramarFacilitadorActivity extends AppCompatActivity implements View.OnClickListener,
        ParticipantesAdapter.ParticipantesListener, DetalleSolicitudView {

    //Vistas de la activity.
    private ConstraintLayout mClLayout;
    private Toolbar mToolbar;
    private Button mBtnConfirmarHorario;
    private ImageButton mIbContactarAdministrador;
    private TextView mTvNoDisponible;
    private TextView mTvNoResponsable;
    private Loader mLoader;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    //Vistas de los include que componen el layout de la activity.
    private View mIlDatosSolicitud;
    private View mIlDatosSolicitante;
    private View mIlParticipantes;
    private View mIlInformacionInduccion;
    private View mIlRangoFechas;

    //Vistas que se encuentran dentro de los include.
    private TextView mTvFechaSolicitud;
    private TextView mTvNumeroSolicitud;

    private TextView mTvNumeroEmpleado;
    private TextView mTvNombreSolicitante;
    private TextView mTvPosicionSolicitante;
    private TextView mTvPaisSolicitante;
    private TextView mTvCorreoElectronico;

    private ImageView mIvEditarParticipantes;
    private RecyclerView mRvParticipantes;

    private TextView mTvTitulo;
    private TextView mTvTemaGeneral;
    private TextView paisQueSolicita;
    private TextView mTvPaisQueSolicita;
    private TextView paisReceptor;
    private TextView mTvPaisReceptor;
    private RecyclerView mRvTemasEspecificos;

    private TextView mTvFechaInicio;
    private TextView mTvFechaFin;

    //Adaptadores para los RecyclerView.
    private ParticipantesAdapter mParticipantesAdapter;
    private TemaEspecificoAdapter mTemasAdapter;

    private ArrayList<ParticipanteDTO> mListParticipantes;
    private ArrayList<InformacionDeInduccionDTO> mListTemas;

    private int idSolicitud;
    private DetalleSolicitudPresenter mPresenter;

    private ConstraintLayout mBotonera;

    private int idReceptorGlobal = 0;

    private ArrayList<com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO> mListadoParticipantesGlobal;

    //Variable para evitar errores al tratar de hacer más de un click sobre un elemento.
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        setContentView(R.layout.activity_por_programar_facilitador);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializeAdapters();
        initializePresenter();
    }

    private void bindViews(){
        //Obteniendo las referencias de la activity.
        mClLayout = findViewById(R.id.cl_activity_por_programar_facilitador);
        mToolbar = findViewById(R.id.toolbar_activity_por_programar_facilitador);
        mBtnConfirmarHorario = findViewById(R.id.btn_activity_por_programar_facilitador_confirmar_horario);
        mIbContactarAdministrador = findViewById(R.id.ib_activity_por_programar_facilitador_contacto_administrador);
        mTvNoDisponible = findViewById(R.id.tv_activity_por_programar_facilitador_no_disponible);
        mTvNoResponsable = findViewById(R.id.tv_activity_por_programar_facilitador_no_responsable);
        mLoader = Loader.newInstance();

        //Obteniendo las referencias de los include.
        mIlDatosSolicitud = findViewById(R.id.layout_activity_por_programar_facilitador_datos_solicitud);
        mIlDatosSolicitante = findViewById(R.id.layout_activity_por_programar_facilitador_datos_solicitante);
        mIlParticipantes = findViewById(R.id.layout_activity_por_programar_facilitador_participantes);
        mIlInformacionInduccion = findViewById(R.id.layout_activity_por_programar_facilitador_informacion_induccion);
        mIlRangoFechas = findViewById(R.id.layout_activity_por_programar_facilitador_rango_fechas);

        //Obteniendo las referencias de los vistas que se encuetran en los include.
        mTvFechaSolicitud = mIlDatosSolicitud.findViewById(R.id.tv_layout_fecha_numero_solicitud_fecha);
        mTvNumeroSolicitud = mIlDatosSolicitud.findViewById(R.id.tv_layout_fecha_numero_solicitud_numero);

        mTvNumeroEmpleado = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_numero_empleado);
        mTvNombreSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_nombre_solicitante);
        mTvPosicionSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_posicion_solicitante);
        mTvPaisSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_pais_solicitante);
        mTvCorreoElectronico = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_correo_eletronico);

        mIvEditarParticipantes = mIlParticipantes.findViewById(R.id.iv_editar_participantes);
        mIvEditarParticipantes.setVisibility(View.GONE);
        mRvParticipantes = mIlParticipantes.findViewById(R.id.rv_layout_participantes_lista_participantes);

        mTvTitulo = mIlInformacionInduccion.findViewById(R.id.tv_informacion_induccion);
        mTvTemaGeneral = mIlInformacionInduccion.findViewById(R.id.tv_layout_informacion_induccion_tema_general);
        paisQueSolicita = mIlInformacionInduccion.findViewById(R.id.tv_pais_solicitante);
        mTvPaisQueSolicita = mIlInformacionInduccion.findViewById(R.id.tv_layout_informacion_induccion_pais_solicitante);
        paisReceptor = mIlInformacionInduccion.findViewById(R.id.tv_pais_receptor);
        mTvPaisReceptor = mIlInformacionInduccion.findViewById(R.id.tv_layout_informacion_induccion_pais_receptor);
        mRvTemasEspecificos = mIlInformacionInduccion.findViewById(R.id.rv_layout_informacion_induccion_temas_especificos);

        mTvFechaInicio = mIlRangoFechas.findViewById(R.id.tv_layout_rango_fechas_fecha_de);
        mTvFechaFin = mIlRangoFechas.findViewById(R.id.tv_layout_rango_fechas_fecha_al);

        mBotonera = findViewById(R.id.botonera_facilitador);
    }

    private void bindListeners(){
        mBtnConfirmarHorario.setOnClickListener(this);
        mIbContactarAdministrador.setOnClickListener(this);
        mTvNoDisponible.setOnClickListener(this);
        mTvNoResponsable.setOnClickListener(this);
    }

    /**
     *<p>Método que configura la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método que inicializa los adaptadores para los RecyclerView donde se muestran la lista
     * de Participantes y Temas Específicos.</p>
     */
    private void initializeAdapters(){
        mListParticipantes = new ArrayList<>();
        mListTemas = new ArrayList<>();
        mListadoPaises = new ArrayList<>();

        mParticipantesAdapter = new ParticipantesAdapter(this, false, false, mListadoPaises);
        mParticipantesAdapter.setListener(this);
        mRvParticipantes.setAdapter(mParticipantesAdapter);
        mRvParticipantes.setLayoutManager(new LinearLayoutManager(this));

        mTemasAdapter = new TemaEspecificoAdapter(this);
        mTemasAdapter.setEstatusPorProgramar(true);
        mTemasAdapter.setRolFacilitador(true);
        mRvTemasEspecificos.setAdapter(mTemasAdapter);
        mRvTemasEspecificos.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializePresenter(){
        mPresenter = new DetalleSolicitudPresenter(this, new DetalleSolicitudInteractor());
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
        mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * <p>Mètodo que asigna a las vistas una acción cuando sean pulsadas.</p>
     * @param view Vistas
     */
    @Override
    public void onClick(View view) {
        //Mis-clicking prevention, using threshold of 1000 ms.
        if (SystemClock.elapsedRealtime()-mLastClickTime<1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent intent = null;
        switch (view.getId()){
            case R.id.btn_activity_por_programar_facilitador_confirmar_horario:
                intent = new Intent(this, ConfirmarHorarioActivity.class);
                intent.putExtra("idSolicitud", idSolicitud);
                startActivityForResult(intent, 102);
                break;
            case R.id.ib_activity_por_programar_facilitador_contacto_administrador:
                intent = new Intent(this, ContactarAdministradorActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_activity_por_programar_facilitador_no_disponible:
                intent = new Intent(this, NoDisponibleActivity.class);
                intent.putExtra("idSolicitud", idSolicitud);
                startActivityForResult(intent, 101);
                break;
            case R.id.tv_activity_por_programar_facilitador_no_responsable:
                intent = new Intent(this, NoResponsableActivity.class);
                intent.putExtra("idSolicitud", idSolicitud);
                intent.putExtra("receptor", idReceptorGlobal);
                intent.putExtra("participante", mListadoParticipantesGlobal);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==Activity.RESULT_OK){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     *<p>No se implementa.</p>
     */
    @Override
    public void noAutorizarParticipante(ParticipanteDTO participante) { }

    /**
     *<p>No se implementa.</p>
     */
    @Override
    public void autorizarParticipante(ParticipanteDTO participante) { }

    /**
     * <p>No se implementa.</p>
     */
    @Override
    public void eliminarParticipante(ParticipanteDTO participante, int posicion) { }

    @Override
    public void onViewObtenerDetalleSolicitud(DetalleSolicitudData data) {
        DetalleSolicitudDTO detalleSolicitud = data.getDetalleSolicitud();
        String tipoSolicitud = detalleSolicitud.getTipoSolicitud();

        //DATOS DE LA SOLICITUD*********************************************************************
        mTvFechaSolicitud.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaSolicitud()));
        mTvNumeroSolicitud.setText(String.valueOf(detalleSolicitud.getIdSolicitud()));

        //DATOS DEL SOLICITANTE*********************************************************************
        mTvNumeroEmpleado.setText(String.valueOf(detalleSolicitud.getNumeroRed()));
        mTvNombreSolicitante.setText(detalleSolicitud.getNombreSolicitante());
        mTvPosicionSolicitante.setText(detalleSolicitud.getDescPosicion());
        CelulaEntrenamiento.asignarPais(mTvPaisSolicitante, detalleSolicitud.getIdPais());
        mTvCorreoElectronico.setText(detalleSolicitud.getCorreo());

        //LISTA DE PARTICIPANTES********************************************************************
        mListParticipantes.addAll(detalleSolicitud.getListaParticipantes());
        mParticipantesAdapter.addParticipantes(mListParticipantes);
        mParticipantesAdapter.notifyDataSetChanged();

        //INFORMACIÓN DE LA SOLICITUD***************************************************************
        mTvTitulo.setText(CelulaEntrenamiento.obtenerTipoSolicitud(tipoSolicitud));
        mTvTemaGeneral.setText(detalleSolicitud.getListaTemas().get(0).getNombreTema());
        if (!tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION)){
            paisQueSolicita.setVisibility(View.VISIBLE);
            mTvPaisQueSolicita.setVisibility(View.VISIBLE);
            paisReceptor.setVisibility(View.VISIBLE);
            mTvPaisReceptor.setVisibility(View.VISIBLE);
            CelulaEntrenamiento.asignarPais(mTvPaisQueSolicita, detalleSolicitud.getPaisSolicitante());
            CelulaEntrenamiento.asignarPais(mTvPaisReceptor, detalleSolicitud.getPaisReceptor());
        }
        mListTemas.addAll(detalleSolicitud.getListaTemas());
        mTemasAdapter.setTipoSolicitud(tipoSolicitud);
        mTemasAdapter.addTemasEspecificos(mListTemas.get(0).getListaTemasEspecificos());
        mTemasAdapter.notifyDataSetChanged();

        //RRANGO DE FECHAS***************************************************************************
        mTvFechaInicio.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaInicio()));
        mTvFechaFin.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaFin()));

        mClLayout.setVisibility(View.VISIBLE);
        for (int aux = 0 ; aux < detalleSolicitud.getListaTemas().get(0).getListaTemasEspecificos().size() ; aux++ ) {
            if (detalleSolicitud.getListaTemas().get(0).getListaTemasEspecificos().get(aux).isAutorizacionFacilitador() && SharedPreferencesUtil.getInstance().getIdEmpleado()== detalleSolicitud.getListaTemas().get(0).getListaTemasEspecificos().get(aux).getIdFcailitador()){
                mBotonera.setVisibility(View.GONE);
                break;
            }
        }

        idReceptorGlobal = data.getDetalleSolicitud().getPaisReceptor();
        mListadoParticipantesGlobal = new ArrayList<>();
        for(int i = 0; i < data.getDetalleSolicitud().getListaParticipantes().size(); i++){
            mListadoParticipantesGlobal.add(new com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO(
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getIdEmpleado(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getNombreParticipante(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getNumeroRed(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getDescripcionPosicion(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getArea(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getSubarea(),
                    0,
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getNombreJefeDirecto(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getPosicionJefeDirecto(),
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getCorreoJefe(),
                    "",
                    data.getDetalleSolicitud().getListaParticipantes().get(i).getIdPais()
            ));
        }
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
    }

    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mLoader.show(fragmentManager, "loader");
    }

    @Override
    public void onViewOcultarLoader() {
        if (mLoader!=null && mLoader.isAdded()){
            mLoader.dismiss();
        }
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewMostrarDetallePais(DetallePaisesResponseData data) {

    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {
        mListadoPaises.addAll(data.getmPaises().getListadoPaises());
        mParticipantesAdapter.setPaisesListado(mListadoPaises);
    }
}

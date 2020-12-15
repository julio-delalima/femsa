package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.programado;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.InformacionDeInduccionDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ParticipanteDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.TemaEspecificoDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
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
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.AutorizacionAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.ParticipantesAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.TemaEspecificoAdapter;

import java.util.ArrayList;

public class ProgramadoActivity extends AppCompatActivity implements ParticipantesAdapter.ParticipantesListener,
        DetalleSolicitudView {

    //Vistas de la Activity.
    private Toolbar mToolbar;
    private ConstraintLayout mClLayout;
    private Loader mLoader;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    //Vistas de los include que componen el layout de la Activity.
    private View mIlDatosSolicitud;
    private View mIlDatosSolicitante;
    private View mIlParticipantes;
    private View mIlInformacionInduccion;
    private View mIlRangoFechas;
    private View mIlAutorizacion;

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

    private RecyclerView mRvAutorizacion;

    //Adapters para los RecyclerView.
    private ParticipantesAdapter mParticipantesAdapter;
    private TemaEspecificoAdapter mTemasAdapter;
    private AutorizacionAdapter mAutorizacionAdapter;

    private ArrayList<ParticipanteDTO> mListParticipantes;
    private ArrayList<InformacionDeInduccionDTO> mListTemas;
    private ArrayList<Object> mListAutorizacion;

    private int idSolicitud;
    private DetalleSolicitudPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        setContentView(R.layout.activity_programado);
        bindViews();
        initializeToolbar();
        initializeAdapters();
        initializePresenter();
    }

    private void bindViews(){
        //Obteniendo las vistas de la Activity.
        mToolbar = findViewById(R.id.toolbar_activity_programado);
        mClLayout = findViewById(R.id.cl_activity_programado);
        mLoader = Loader.newInstance();

        //Obteniendo las referencias de los include.
        mIlDatosSolicitud = findViewById(R.id.layout_activity_programado_datos_solicitud);
        mIlDatosSolicitante = findViewById(R.id.layout_activity_programado_datos_solicitante);
        mIlParticipantes = findViewById(R.id.layout_activity_programado_participantes);
        mIlInformacionInduccion = findViewById(R.id.layout_activity_programado_informacion_induccion);
        mIlRangoFechas = findViewById(R.id.layout_activity_programado_rango_fechas);
        mIlAutorizacion = findViewById(R.id.layout_activity_programado_autorizacion);

        //Obteniendo las referencias de las vistas que se encuentran dentro de los include.
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

        mRvAutorizacion = mIlAutorizacion.findViewById(R.id.rv_layout_autorizacion_lista_autorizacion);
    }

    /**
     * <p>Método que configura la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método que inicializa los adaptadores para los RecyclerView  donde se muestra la lista de
     * Participantes, Temas Específicos y Autorización.</p>
     */
    private void initializeAdapters() {
        mListParticipantes = new ArrayList<>();
        mListTemas = new ArrayList<>();
        mListadoPaises = new ArrayList<>();
        mListAutorizacion = new ArrayList<>();

        mParticipantesAdapter = new ParticipantesAdapter(this, false, false, mListadoPaises);
        mParticipantesAdapter.setListener(this);
        mRvParticipantes.setAdapter(mParticipantesAdapter);
        mRvParticipantes.setLayoutManager(new LinearLayoutManager(this));

        mTemasAdapter = new TemaEspecificoAdapter(this);
        mRvTemasEspecificos.setAdapter(mTemasAdapter);
        mRvTemasEspecificos.setLayoutManager(new LinearLayoutManager(this));

        mAutorizacionAdapter = new AutorizacionAdapter(this, mListAutorizacion, mListadoPaises);
        mRvAutorizacion.setAdapter(mAutorizacionAdapter);
        mRvAutorizacion.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializePresenter(){
        mPresenter = new DetalleSolicitudPresenter(this, new DetalleSolicitudInteractor());
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
        mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * <p>No se implementa.</p>
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
        ArrayList<FechaDTO> mFechas;


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

        //RANGO DE FECHAS***************************************************************************
        mTvFechaInicio.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaInicio()));
        mTvFechaFin.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaFin()));

        //LISTA DE AUTORIZACIONES*******************************************************************

        //Agregando la lista de autorización de los jefes.
        mListAutorizacion.addAll(detalleSolicitud.getListaAutorizacion());


        //Agregando la lista de autorización de los facilitadores.
        mFechas = new ArrayList<>();
        for (int i=0; i<mListTemas.size(); i++){
            ArrayList<TemaEspecificoDTO> listaTemasEspecificos = mListTemas.get(i).getListaTemasEspecificos();
            for (int j=0; j<listaTemasEspecificos.size(); j++){
                TemaEspecificoDTO temaEspecificoDTO = listaTemasEspecificos.get(j);
                mFechas.add(temaEspecificoDTO.getmListaFechas().get(0));
                mListAutorizacion.add(new FacilitadorDTO(temaEspecificoDTO.getNombreFacilitador(),
                        temaEspecificoDTO.getDescripcionPosicion(),
                        temaEspecificoDTO.getNombreTema(),
                        temaEspecificoDTO.isAutorizacionFacilitador(),
                        temaEspecificoDTO.getFotoPerfilFacilitador(),
                        temaEspecificoDTO.getIdPais()));
            }
        }

        mAutorizacionAdapter.addListaAutorizacion(mListAutorizacion);
        mAutorizacionAdapter.notifyDataSetChanged();
        mAutorizacionAdapter.setListadoFechasFacilitador(mFechas);
        mClLayout.setVisibility(View.VISIBLE);
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
        mAutorizacionAdapter.setPaisesListado(mListadoPaises);
        mParticipantesAdapter.setPaisesListado(mListadoPaises);
        mTemasAdapter.notifyDataSetChanged();
    }
}

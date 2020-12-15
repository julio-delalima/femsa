package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.InformacionDeInduccionDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.JefeDTO;
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
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.EditarParticipantesInteractor;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.EditarParticipantesPresenter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.EditarParticipantesView;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.ParticipantesAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.TemaEspecificoAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador.ContactarAdministradorActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.AgregarParticipante.EditarParticipantesPorAutorizarActivity;

import java.util.ArrayList;

public class PorAutorizarJefeActivity extends AppCompatActivity implements View.OnClickListener,
        ParticipantesAdapter.ParticipantesListener, DetalleSolicitudView, PorAutorizarJefeView,
        EditarParticipantesView {

    //Vistas de la Activity.
    private Toolbar mToolbar;
    private Button mBtnEnviar;
    private ImageButton mIbContactoAdministrador;
    private NestedScrollView mNsvLayout;
    private Loader mLoader;

    //Referencias a los archivos include para obtener las vistas que se encuentren dentro.
    private View mIlDatosSolicitud;
    private View mIlDatosSolicitante;
    private View mIlParticipantes;
    private View mIlInformacionInduccion;
    private View mIlRangoFechas;

    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    //Vistas que se encuentran dentro de los include.
    private TextView mTvFechaSolicitud;
    private TextView mTvNumeroSolicitud;

    private TextView mTvNumeroEmpleado;
    private TextView mTvNombreSolicitante;
    private TextView paisQueSolicita;
    private TextView mTvPosicionSolicitante;
    private TextView paisReceptor;
    private TextView mTvPaisSolicitante;
    private TextView mTvCorreoElectronico;

    private ImageView mIvEditarParticipantes;
    private RecyclerView mRvParticipantes;

    private TextView mTvTitulo;
    private TextView mTvTemaGeneral;
    private TextView mTvPaisQueSolicita;
    private TextView mTvPaisReceptor;
    private RecyclerView mRvTemasEspecificos;

    private TextView mTvFechaInicio;
    private TextView mTvFechaFin;

    //Adaptadores para los RecyclerView.
    private ParticipantesAdapter mParticipantesAdapter;
    private TemaEspecificoAdapter mTemaEspecificoAdapter;

    //ArrayLists que contiene los elementos que se visualizarán en los RecyclerView.
    private ArrayList<ParticipanteDTO> mListParticipantes;
    private ArrayList<InformacionDeInduccionDTO> mListTemas;

    private int idSolicitud;
    private int idEmpleado;
    //ArrayList que contiene la lista de participantes que el autorizador acepte.
    private ArrayList<com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.ParticipanteDTO> mParticipantesAceptados;
    private DetalleSolicitudPresenter mPresenter;
    //Presenter para editar participantes
    private EditarParticipantesPresenter mPresenterEditarParticipantes;

    //Variable para evitar errores al tratar de hacer más de un click sobre los botones.
    private long mLastClickTime = 0;

    //Lista de participantes que se van a eliminar/agregar.
    private ArrayList<Integer> mListEditarParticipantes;

    private String tipoSolicitud;

    private ArrayList<JefeDTO> mListadoJefecitos;

    //Posición de un participante en la lista para eliminarlo
    private int posicion;

    private int idEmpleadoEliminado; //Variable para saber si un empleado se eliminó a si mismo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        //Id del empleado que usa la app. Para que el proceso de autorizar una solicitud funcione el id debe pertenecer a un autorizador.
        idEmpleado = SharedPreferencesUtil.getInstance().getIdEmpleado();
        setContentView(R.layout.activity_por_autorizar_jefe);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializeAdapters();
        initializePresenter();
    }

    private void bindViews(){
        mListadoPaises = new ArrayList<>();
        //Obteniendo las referencias de la activity.
        mToolbar = findViewById(R.id.activity_por_autorizar_jefe_toolbar);
        mBtnEnviar = findViewById(R.id.btn_activity_por_autorizar_jefe_enviar);
        mIbContactoAdministrador = findViewById(R.id.ib_activity_por_autorizar_jefe_contacto_administrador);
        mNsvLayout = findViewById(R.id.nsv_activity_por_autorizar_jefe);
        mLoader = Loader.newInstance();

        //Obteniendo las referencias de los include.
        mIlDatosSolicitud = findViewById(R.id.layout_activity_por_autorizar_jefe_datos_solicitud);
        mIlDatosSolicitante = findViewById(R.id.layout_activity_por_autorizar_jefe_datos_solicitante);
        mIlParticipantes = findViewById(R.id.layout_activity_por_autorizar_jefe_participantes);
        mIlInformacionInduccion = findViewById(R.id.layout_activity_por_autorizar_jefe_informacion_induccion);
        mIlRangoFechas = findViewById(R.id.layout_activity_por_autorizar_jefe_rango_fechas);

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
        mIvEditarParticipantes.setOnClickListener(v-> onAgregarParticipante());
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


    }

    private void onAgregarParticipante()
    {
        Intent sendTo = new Intent(this, EditarParticipantesPorAutorizarActivity.class);
        sendTo.putExtra("PorAutorizar", mListParticipantes);
        sendTo.putExtra("tipoSolicitud", tipoSolicitud);
        sendTo.putExtra("jefes", mListadoJefecitos);
        sendTo.putExtra("idSolicitud", idSolicitud);
        //sendTo.putExtra(CrearCelulaDosActivity.TIPO_KEY, mtipo);
        startActivityForResult(sendTo, 1);
    }

    /**
     * Método que trae de vuelva un FacilitadorDTO el cual es asignado al listado de Facilitadores y asignado en el RecyclerView
     * en el elemento seleccionado.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<ParticipanteDTO> mNuevos = new ArrayList<>();
        ArrayList<Integer> mNuevosID = new ArrayList<>();
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mNuevos = (ArrayList<ParticipanteDTO>) data.getSerializableExtra("PorAutorizar");
                //mListaEliminados = (ArrayList<ParticipanteDTO>) data.getSerializableExtra("PorAutorizarEliminados");
//                for(int i = 0; i < mListParticipantes.size(); i++)
//                    {
//                        for(int j = 0; j < mListaEliminados.size(); j++)
//                            {
//                                if(mListParticipantes.get(i).getIdEmpleado() == mListaEliminados.get(j).getIdEmpleado())
//                                    {
//                                        mListParticipantes.remove(i);
//                                        break;
//                                    }
//                            }
//                    }
                //mListParticipantes.addAll(mNuevos);

                for(int i=0; i< mNuevos.size(); i++){
                    mNuevosID.add(mNuevos.get(i).getIdEmpleado());
                }
                mListEditarParticipantes.addAll(mNuevosID);
                mPresenterEditarParticipantes.iniciarProcesoAgregarParticipantes(String.valueOf(idSolicitud), mNuevosID, true);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void bindListeners(){
        mBtnEnviar.setOnClickListener(this);
        mIbContactoAdministrador.setOnClickListener(this);
    }

    /**
     * <p>Método que inicializa los adaptadores para los RecyclerView de Participantes y Temas Específicos.</p>
     */
    private void initializeAdapters(){
        mListParticipantes = new ArrayList<>();
        mListEditarParticipantes = new ArrayList<>();
        mListTemas = new ArrayList<>();

        mParticipantesAceptados = new ArrayList<>();

        mParticipantesAdapter = new ParticipantesAdapter(this, true, true, mListadoPaises);
        mParticipantesAdapter.setListener(this);
        mRvParticipantes.setAdapter(mParticipantesAdapter);
        mRvParticipantes.setLayoutManager(new LinearLayoutManager(this));



        mTemaEspecificoAdapter = new TemaEspecificoAdapter(this);
        mRvTemasEspecificos.setAdapter(mTemaEspecificoAdapter);
        mRvTemasEspecificos.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * <p>Método que inicializa la Toolbar.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void initializePresenter(){
        mPresenterEditarParticipantes = new EditarParticipantesPresenter(this, new EditarParticipantesInteractor());

        mPresenter = new DetalleSolicitudPresenter(this, new DetalleSolicitudInteractor());
        mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
    }

    @Override
    public void onClick(View v) {
        //Mis-clicking prevention, using threshold of 1000 ms.
        if (SystemClock.elapsedRealtime()-mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.btn_activity_por_autorizar_jefe_enviar:
                PorAutorizarJefePresenter presenter = new PorAutorizarJefePresenter(this, new PorAutorizarJefeInteractor());
                presenter.iniciarProcesoObtenerAutorizacionJefe(idSolicitud, idEmpleado, mParticipantesAceptados);
                break;

            case R.id.ib_activity_por_autorizar_jefe_contacto_administrador:
                Intent intent = new Intent(this, ContactarAdministradorActivity.class);
                intent.putExtra("idSolicitud", idSolicitud);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * <p>Método que elimina a un participante del ArrayList de participantes adaptados.</p>
     * @param participante Participante en el Adapter que se va a rechazar.
     */
    @Override
    public void noAutorizarParticipante(ParticipanteDTO participante) {
        //Buscando en la lista de participantes aceptados el que coincida con el ID.
        for (int i=0; i<mParticipantesAceptados.size(); i++){
            if (mParticipantesAceptados.get(i).getIdParticipante()==participante.getIdEmpleado()){
                mParticipantesAceptados.remove(i);
            }
        }
    }

    /**
     * <p>Método que agrega los participantes que hayan sido aceptados.</p>
     * @param participante Participante en el Adapter que se va a aceptar.
     */
    @Override
    public void autorizarParticipante(ParticipanteDTO participante) {
        mParticipantesAceptados.add(new com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.ParticipanteDTO(participante.getIdEmpleado()));
    }

    /**
     * <p>Método que llama al presentador para eliminar a un participante.</p>
     * @param participante Información del participante que se va a eliminar.
     */
    @Override
    public void eliminarParticipante(ParticipanteDTO participante, int posicion) {
        this.posicion = posicion;
        idEmpleadoEliminado = participante.getIdEmpleado();
        mListEditarParticipantes.add(participante.getIdEmpleado());
        mPresenterEditarParticipantes.iniciarProcesoEliminarParticipantes(String.valueOf(idSolicitud),
                mListEditarParticipantes, false);
    }

    /**
     * <p>Respuesta de la petición cuando se pide la información de una solicitud.</p>
     * @param data Información de la solicitud.
     */
    @Override
    public void onViewObtenerDetalleSolicitud(DetalleSolicitudData data) {
        DetalleSolicitudDTO detalleSolicitud = data.getDetalleSolicitud();
        tipoSolicitud = detalleSolicitud.getTipoSolicitud();

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

        //LISTA DE JEFECITOS
        mListadoJefecitos = new ArrayList<>();
        mListadoJefecitos.addAll(data.getDetalleSolicitud().getListaAutorizacion());
        for(JefeDTO jefe : mListadoJefecitos)
            {
                if(jefe.getNombreJefe().equals(SharedPreferencesUtil.getInstance().getNombreSP()))
                    {
                        mBtnEnviar.setVisibility(jefe.getAutorizacion() ? View.GONE : View.VISIBLE);
                        break;
                    }
            }

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
        mTemaEspecificoAdapter.setTipoSolicitud(tipoSolicitud);
        mTemaEspecificoAdapter.addTemasEspecificos(mListTemas.get(0).getListaTemasEspecificos());
        mTemaEspecificoAdapter.notifyDataSetChanged();

        //RANGO DE FECHAS***************************************************************************
        mTvFechaInicio.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaInicio()));
        mTvFechaFin.setText(CelulaEntrenamiento.obtenerFechaSolicitud(detalleSolicitud.getFechaFin()));

        mNsvLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
    }


    public void onViewMostrarLoader() {
        FragmentManager fm = getSupportFragmentManager();
        try
        {
            if(mLoader != null)
            {
                mLoader.show(fm,"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }

    }

    @Override
    public void onViewOcultarLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
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

    /**
     * <p>Respuesta de la petición cuando se autorizó la solicitud.</p>
     */
    @Override
    public void onViewAutorizacionConcedida() {
        Toast.makeText(this, "Se ha autorizado la solicitud.", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * <p>Respuesta de la petición cuando se agregó a un participante.</p>
     */
    @Override
    public void onViewParticipanteAgregado() {
        mParticipantesAdapter.clear();
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
    }

    /**
     * <p>Respuesta de la petición cuando se eliminó a un participante.</p>
     */
    @Override
    public void onViewParticipanteEliminado() {
        if (idEmpleadoEliminado==SharedPreferencesUtil.getInstance().getIdEmpleado()){
            finish();
        } else {
            mListParticipantes.remove(posicion);
            mListEditarParticipantes.clear();
            mParticipantesAdapter.addParticipantes(mListParticipantes);
            mParticipantesAdapter.notifyDataSetChanged();
        }
    }
}

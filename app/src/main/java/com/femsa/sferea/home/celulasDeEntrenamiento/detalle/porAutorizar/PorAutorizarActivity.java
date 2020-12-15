package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
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
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.JefeDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ParticipanteDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.TemaEspecificoDTO;
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
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.AutorizacionAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.ParticipantesAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.TemaEspecificoAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.AgregarParticipante.EditarParticipantesPorAutorizarActivity;

import java.util.ArrayList;

public class PorAutorizarActivity extends AppCompatActivity implements ParticipantesAdapter.ParticipantesListener,
        DetalleSolicitudView, EditarParticipantesView {

    //Vistas de la Activity.
    private Toolbar mToolbar;
    private Loader mLoader;
    private NestedScrollView mNsvLayout;

    //Referencias a los archivos include para obtener las vistas que se encuentran dentro.
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

    //Adaptadores para los RecyclerView.
    private ParticipantesAdapter mParticipantesAdapter;
    private TemaEspecificoAdapter mTemaEspecificoAdapter;
    private AutorizacionAdapter mAutorizacionAdapter;

    //ArrayLists que contiene los elementos que se visualizarán en los RecyclerView.
    private ArrayList<Object> mListAutorizacion;
    private ArrayList<ParticipanteDTO> mListParticipantes = new ArrayList<>(), mListaEliminados;
    private ArrayList<InformacionDeInduccionDTO> mListTemas;
    private ArrayList<JefeDTO> mListadoJefecitos;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    private int idSolicitud;
    private DetalleSolicitudPresenter mPresenter;

    private int idEmpleadoEliminado; //Variable para saber si un empleado se eliminó a si mismo

    //Presenter para editar participantes
    private EditarParticipantesPresenter mPresenterEditarParticipantes;

    //Lista de participantes que se van a eliminar/agregar.
    private ArrayList<Integer> mListEditarParticipantes;

    private String tipoSolicitud;

    //Posición de un participante en la lista para eliminarlo.
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        setContentView(R.layout.activity_por_autorizar_solicitante);
        bindViews();
        initializeToolbar();
        initializeAdapters();
        initializePresenter();
    }

    private void bindViews(){
        //Obteniendo las referencias de las vistas dentro del Activity.
        mToolbar = findViewById(R.id.activity_por_autorizar_solicitante_toolbar);
        mLoader = Loader.newInstance();
        mNsvLayout = findViewById(R.id.nsv_activity_por_autorizar_solicitante);

        //Obteniendo las referencias de los include.
        mIlDatosSolicitud = findViewById(R.id.layout_activity_por_autorizar_solicitante_datos_solicitud);
        mIlDatosSolicitante = findViewById(R.id.layout_activity_por_autorizar_solicitante_datos_solicitante);
        mIlParticipantes = findViewById(R.id.layout_activity_por_autorizar_solicitante_participantes);
        mIlInformacionInduccion = findViewById(R.id.layout_activity_por_autorizar_solicitante_informacion_induccion);
        mIlRangoFechas = findViewById(R.id.layout_activity_por_autorizar_solicitante_rango_fechas);
        mIlAutorizacion = findViewById(R.id.layout_activity_por_autorizar_solicitante_autorizacion);

        //Obteniendo las referencias de las vistas que se encuentran dentro de los include.
        mTvFechaSolicitud = mIlDatosSolicitud.findViewById(R.id.tv_layout_fecha_numero_solicitud_fecha);
        mTvNumeroSolicitud = mIlDatosSolicitud.findViewById(R.id.tv_layout_fecha_numero_solicitud_numero);

        mTvNumeroEmpleado = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_numero_empleado);
        mTvNombreSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_nombre_solicitante);
        mTvPosicionSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_posicion_solicitante);
        mTvPaisSolicitante = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_pais_solicitante);
        mTvCorreoElectronico = mIlDatosSolicitante.findViewById(R.id.tv_layout_datos_solicitante_correo_eletronico);

        mIvEditarParticipantes = mIlParticipantes.findViewById(R.id.iv_editar_participantes);
            mIvEditarParticipantes.setOnClickListener(v->{onAgregarParticipante();});
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

    private void onAgregarParticipante() {
        Intent sendTo = new Intent(this, EditarParticipantesPorAutorizarActivity.class);
        sendTo.putExtra("PorAutorizar", mListParticipantes);
        sendTo.putExtra("jefes", mListadoJefecitos);
        sendTo.putExtra("tipoSolicitud", tipoSolicitud);
        sendTo.putExtra("idSolicitud", idSolicitud);
        startActivityForResult(sendTo, 1);
    }

    /**
     * Método que trae de vuelta un FacilitadorDTO el cual es asignado al listado de Facilitadores y asignado en el RecyclerView
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

    /**
     * <p>Método que inicializa los adaptadores para los RecyclerView de la actividad.</p>
     */
    private void initializeAdapters(){
        mListTemas = new ArrayList<>();
        mListAutorizacion = new ArrayList<>();
        mListEditarParticipantes = new ArrayList<>();

        //mParticipantesAdapter = new ParticipantesAdapter(this, false, true);
        mListadoPaises = new ArrayList<>();
        mParticipantesAdapter = new ParticipantesAdapter(this, false, true, mListadoPaises);
        mParticipantesAdapter.setListener(this);
        mRvParticipantes.setAdapter(mParticipantesAdapter);
        mRvParticipantes.setLayoutManager(new LinearLayoutManager(this));

        mTemaEspecificoAdapter = new TemaEspecificoAdapter(this);
        mRvTemasEspecificos.setAdapter(mTemaEspecificoAdapter);
        mRvTemasEspecificos.setLayoutManager(new LinearLayoutManager(this));

        mAutorizacionAdapter = new AutorizacionAdapter(this, mListAutorizacion, mListadoPaises);
        mRvAutorizacion.setAdapter(mAutorizacionAdapter);
        mRvAutorizacion.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initializePresenter(){
        mPresenterEditarParticipantes = new EditarParticipantesPresenter(this, new EditarParticipantesInteractor());

        mPresenter = new DetalleSolicitudPresenter(this, new DetalleSolicitudInteractor());
        mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * <p>No se implementa.</p>
     */
    @Override
    public void noAutorizarParticipante(ParticipanteDTO participante) { }

    /**
     * <p>No se implementa.</p>
     */
    @Override
    public void autorizarParticipante(ParticipanteDTO participante) { }

    /**
     * <p>Método que llama al presentador para eliminar a un participante.</p>
     */
    @Override
    public void eliminarParticipante(ParticipanteDTO participante, int posicion) {
        if(mListParticipantes.size() > 1)
            {
                this.posicion = posicion;
                idEmpleadoEliminado = participante.getIdEmpleado();
                mListEditarParticipantes.add(participante.getIdEmpleado());
                mPresenterEditarParticipantes.iniciarProcesoEliminarParticipantes(String.valueOf(idSolicitud),
                mListEditarParticipantes, false);
            }
        else
            {
                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_facilitadores));
            }
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

        //LISTA DE AUTORIZACIONES*******************************************************************

        //Agregando la lista de autorización de los jefes.
        mListAutorizacion.addAll(detalleSolicitud.getListaAutorizacion());

        //Agregando la lista de autorización de los facilitadores.
        for (int i=0; i<mListTemas.size(); i++){
            ArrayList<TemaEspecificoDTO> listaTemasEspecificos = mListTemas.get(i).getListaTemasEspecificos();
            for (int j=0; j<listaTemasEspecificos.size(); j++){
                TemaEspecificoDTO temaEspecificoDTO = listaTemasEspecificos.get(j);
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


    @Override
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
        mListadoPaises = data.getmPaises().getListadoPaises();
        mParticipantesAdapter.setPaisesListado(mListadoPaises);
        mAutorizacionAdapter.setPaisesListado(mListadoPaises);
    }

    /**
     * <p>Respuesta de la petición cuando se agregó a un participante.</p>
     */
    @Override
    public void onViewParticipanteAgregado() {
        mParticipantesAdapter.clear();
        mAutorizacionAdapter.clear();
        mTemaEspecificoAdapter.clear();
        mPresenter.iniciarProcesoObtenerDetalleSolicitud(idSolicitud);
        /*mListEditarParticipantes.clear();
        mParticipantesAdapter.clear();
        mParticipantesAdapter.addParticipantes(mListParticipantes);
        mParticipantesAdapter.notifyDataSetChanged();*/
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

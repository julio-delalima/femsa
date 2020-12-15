package com.femsa.sferea.home.celulasDeEntrenamiento.listado;

import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.SolicitudDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.ActivityDialog;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias.CelulasEntrenamientoVaciaFragment;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.PorAutorizarActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto.PorAutorizarJefeActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.PorProgramarActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.PorProgramarFacilitadorActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.programado.ProgramadoActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.listado.viewPager.CelulasDeEntrenamientoSlideAdapter;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;

import java.util.ArrayList;

public class CelulasDeEntrenamientoFragment extends Fragment implements CelulaDeEntrenamientoAdapter.OnItemClickListener,
        View.OnClickListener, ListadoSolicitudesView, CelulaDeEntrenamientoAdapter.OnCelulaListadoClick, SwipeRefreshLayout.OnRefreshListener,
        DialogFragmentManager.OnDialogManagerButtonActions{

    private static final String ID_SOLICITUD = "idSolicitud";

    private ArrayList<SolicitudDTO> mList;

    private View mView;

    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    private RecyclerView mRvListado;
    private ViewPager mViewPager;
    private FloatingActionButton mFabAgregarCelula;
    private Loader mLoader;

    //Layout en el que se encuentran todas las vistas. Esta referencia se ocupa para ocultarlas. En
    //caso de que sí haya una lista de solicitudes se hace visible.
    private CoordinatorLayout mColLayout;

    private CelulaDeEntrenamientoAdapter mAdapter;

    private CelulasDeEntrenamientoSlideAdapter mSliderAdapter;

    private ListadoSolicitudesPresenter mPresenter;

    private SwipeRefreshLayout swipeContainer;

    //Número de páginas que se mostrarán en el slider.
    private static final int NUM_PAGES = 2;

    private int mCelulaEliminarID = -1;

    private int idSolicitudPush = -1;

    //Listeners para configurar la apariencia de HomeActivity.
    private OnCelulasInit mListener;
    private CelulasEntrenamientoVaciaFragment.OnCelulasInit mListenerVacio;

    /**
     * <p>Método para que agrega dos listeners </p>
     * @param listener Listener para este Fragment.
     * @param listenervacio Listener para {@link CelulasEntrenamientoVaciaFragment}.
     */
    public void setListener(OnCelulasInit listener, CelulasEntrenamientoVaciaFragment.OnCelulasInit listenervacio) {
        mListener = listener;
        mListenerVacio = listenervacio;
    }

    @Override
    public void onEliminarcelula(int idCelula) {
        mCelulaEliminarID = idCelula;
        DialogFragmentManager f = DialogFragmentManager.newInstance(false, false, false,
                getResources().getString(R.string.delete), getResources().getString(R.string.eliminar_celula),
                getResources().getString(R.string.si), getResources().getString(R.string.no), false);
        f.setListener(this);
        f.show(getFragmentManager(), "eliminar");
    }

    @Override
    public void onRefresh() {
        idSolicitudPush = -1;
        onTraerListadoSolicitudes();
    }

    @Override
    public void OnDialogAceptarClick() {
        mPresenter.onInteractorEliminarSolicitud(mCelulaEliminarID);
    }

    @Override
    public void OnDialogCancelarClick() {

    }

    /**
     * <p>Interface que define el método para configurar el aspecto de {@link HomeActivity} al cargar el fragment.</p>
     */
    public interface OnCelulasInit {
        void SetCelulasInMainView();
    }

    public CelulasDeEntrenamientoFragment() { }

    public static CelulasDeEntrenamientoFragment newInstance(){
        Bundle args = new Bundle();
        CelulasDeEntrenamientoFragment fragment = new CelulasDeEntrenamientoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setPushNotification(int idSolicitud)
        {
            idSolicitudPush = idSolicitud;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_celulas_de_entrenamiento, container, false);
        bindViews();
        bindListeners();
        initializeAdapter();
        initializeViewPagerAdapter();
        mListener.SetCelulasInMainView();
        initializePresenter();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        onTraerListadoSolicitudes();
    }

    private void abrirdetalleCelulaPush()
        {
            SolicitudDTO tempSolicitud = revisPushNotification();
            if (tempSolicitud != null)
                {
                    onClickDetalleCelulaEntrenamiento(tempSolicitud);
                }
            idSolicitudPush = -1;
        }

    private SolicitudDTO revisPushNotification()
        {
            for(SolicitudDTO dto: mList)
            {
                if(dto.getIdSolicitud() == idSolicitudPush)
                    {
                        return dto;
                    }
            }
            return null;
        }

    private void bindViews(){
        mLoader = Loader.newInstance();
        mRvListado = mView.findViewById(R.id.rv_celulas_entrenamiento_fragment);
        mViewPager = mView.findViewById(R.id.vp_celulas_entrenamiento_fragment);
        mFabAgregarCelula = mView.findViewById(R.id.fab_fragment_celulas_de_entrenamiento_agregar_celula);
        mColLayout = mView.findViewById(R.id.col_fragment_celulas_de_entrenamiento);
        swipeContainer = mView.findViewById(R.id.swipe_celulas);
            swipeContainer.setColorSchemeColors(
                    getResources().getColor(R.color.femsa_red_a),
                    getResources().getColor(R.color.femsa_red_b),
                    getResources().getColor(R.color.femsa_red_d));
            swipeContainer.setOnRefreshListener(this);
    }

    private void bindListeners(){
        mFabAgregarCelula.setOnClickListener(this);
    }

    /**
     * <p>Método que inicializa el adaptador del LayoutManager del RecyclerView.</p>
     */
    private void initializeAdapter(){
        mList = new ArrayList<>();
        mListadoPaises = new ArrayList<>();
        mAdapter = new CelulaDeEntrenamientoAdapter(getContext(), mList, mListadoPaises);
        mAdapter.setListener(this);
        mAdapter.setOnItemClickListener(this);
        mRvListado.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvListado.setAdapter(mAdapter);
    }

    /**
     * <p>Método que inicializa el ViewPager del adaptador.</p>
     */
    private void initializeViewPagerAdapter(){
        mSliderAdapter = new CelulasDeEntrenamientoSlideAdapter(getChildFragmentManager(), NUM_PAGES);
        mViewPager.setAdapter(mSliderAdapter);
    }

    private void initializePresenter(){
        mPresenter = new ListadoSolicitudesPresenter(this, new ListadoSolicitudesInteractor());
    }

    /**
     * <p>Método que es invocado cuando se presiona el FloatingActionButton para crear una nueva
     * solicitud. Abre un DialogFragment para llevar a cabo esta acción.</p>
     * @param v Referencia del FloatingActionButton.
     */
    private void crearSolicitud(View v){
        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), v, "transition");
        int relativeX = (int) (v.getX() + v.getWidth()/2);
        int relativeY = (int) (v.getY() + v.getHeight()/2);

        Intent intent = new Intent(getContext(), ActivityDialog.class);
        intent.putExtra(ActivityDialog.EXTRA_CIRCULAR_REVELARX, relativeX);
        intent.putExtra(ActivityDialog.EXTRA_CIRCULAR_REVELARY, relativeY);
        ActivityCompat.startActivity(getContext(), intent, option.toBundle());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_fragment_celulas_de_entrenamiento_agregar_celula) {
            crearSolicitud(view);
        }
    }

    /**
     * <p>Método que le asigna un listener a un elemento que se muestra en la lista de células de
     * entrenamiento para poder obtener el detalle de esta.</p>
     * @param solicitud CélulaDeEntrenamiento a la que se selecciona.
     */
    @Override
    public void onClickDetalleCelulaEntrenamiento(SolicitudDTO solicitud) {
        String estatus = solicitud.getStatusSolicitud();
        String rol = solicitud.getRolSolicitud();
        int idSolicitud = solicitud.getIdSolicitud();
        Intent intent;

        if (estatus.equals(CelulaEntrenamiento.POR_AUTORIZAR)){ //POR AUTORIZAR
            if (rol.equals(CelulaEntrenamiento.AUTORIZADOR)){
                intent = new Intent(getContext(), PorAutorizarJefeActivity.class);
            }
            else{
                intent = new Intent(getContext(), PorAutorizarActivity.class);
            }
            intent.putExtra(ID_SOLICITUD, idSolicitud);
        }
        else if (estatus.equals(CelulaEntrenamiento.POR_PROGRAMAR)) { //POR PROGRAMAR
            if (rol.equals(CelulaEntrenamiento.FACILITADOR)){
                intent = new Intent(getContext(), PorProgramarFacilitadorActivity.class);
            }
            else {
                intent = new Intent(getContext(), PorProgramarActivity.class);
            }
            intent.putExtra(ID_SOLICITUD, idSolicitud);
        }
        else { //PROGRAMADO
            intent = new Intent(getContext(), ProgramadoActivity.class);
            intent.putExtra(ID_SOLICITUD, idSolicitud);
        }

        startActivityForResult(intent, 100);
    }

    /**
     * <p>Método que se ejecuta cuando se termina cualquier Activity sobre el detalle de una
     * solicitud. La funcionalidad de este método fue reemplazada por el método onResumen() de este
     * Fragment.</p>
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (resultCode== Activity.RESULT_OK){
            if (requestCode==100){
                mAdapter.clear();
                mRvListado.setVisibility(View.GONE);
                mPresenter.iniciarProcesoObtenerListadoSolicitudes();
            }
        }*/
    }

    /**
     * <p>Respuesta cuando existen solicitudes. En este caso se hacen visibles las vistas del
     * layout.</p>
     * @param data Información obtenida de la petición.
     */
    @Override
    public void onViewObtenerListadoSolicitudes(ListaSolicitudesData data) {
        swipeContainer.setRefreshing(false);
        mList.clear();
        mList.addAll(data.getListadoSolicitudes());
        mAdapter.notifyDataSetChanged();
        mColLayout.setVisibility(View.VISIBLE);
        mRvListado.setVisibility(View.VISIBLE);
        abrirdetalleCelulaPush();
    }

    /**
     * <p>Respuesta cuando no existen solicitudes. En este caso se muestra a la pantalla de
     * solicitudes vacías.</p>
     */
    @Override
    public void onViewSinSolicitudes() {
        swipeContainer.setRefreshing(false);
        CelulasEntrenamientoVaciaFragment fragment = new CelulasEntrenamientoVaciaFragment();
        fragment.setListener(mListenerVacio);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, fragment, "celulasEntrenamientoVacias")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onViewMostrarMensaje(int tipoMensaje) {
        swipeContainer.setRefreshing(false);
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
    }

    @Override
    public void onViewMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        swipeContainer.setRefreshing(false);
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    @Override
    public void onViewMostrarLoader() {
        try{
            if(mLoader != null && !mLoader.isAdded()){
                FragmentManager fragmentManager = getChildFragmentManager();
                mLoader.show(fragmentManager, "Loader");
            }
        }
        catch (Exception ignored){

        }
    }

    @Override
    public void onViewOcultarLoader() {
        if (mLoader!=null && mLoader.isAdded()){
            mLoader.dismiss();
        }
    }

    /**
     * <p>Cuando el token del usuario expire se mandará a llamar este método.</p>
     */
    @Override
    public void onViewSinAutorizacion() {
        swipeContainer.setRefreshing(false);
        GlobalActions globalActions = new GlobalActions(getActivity());
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewCelulaEliminada() {
        onTraerListadoSolicitudes();
    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {
        mListadoPaises.clear();
        mListadoPaises = data.getmPaises().getListadoPaises();
        mAdapter.setPaisAdapter(mListadoPaises);
    }

    private void onTraerListadoSolicitudes()
        {
            mPresenter.iniciarProcesoObtenerListadoSolicitudes();
            mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

    /**
     * <p>Método que se ejecuta cuando se destruye el Fragment.</p>
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}

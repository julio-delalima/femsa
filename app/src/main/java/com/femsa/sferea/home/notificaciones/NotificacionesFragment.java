package com.femsa.sferea.home.notificaciones;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.DTO.User.Notificaciones.NotificacionesData;
import com.femsa.requestmanager.Response.NotificacionesResponse.NotificacionesResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.notificaciones.detalle.DetalleNotificacionActivity;
import com.femsa.sferea.home.notificaciones.listado.NotificacionesAdapter;

import java.util.ArrayList;

public class NotificacionesFragment extends Fragment implements NotificacionesAdapter.OnNotificacionListener,
        NotificacionesView, SwipeRefreshLayout.OnRefreshListener {

    private View mView;
    private RecyclerView mNotificacionesRecycler;
    private ArrayList<NotificacionesData> mData;
    private OnNotificacionesInterface mListener;
    private NotificacionesAdapter mAdapterNotificaciones;
    private LinearLayoutManager mManager;
    private Loader mLoader;
    private NotificacionesPresenter mPresenter;
    private ArrayList<NotificacionesData> mNotificaciones;
    private ConstraintLayout mRootView;
    private int mIndiceLayout = -1;
    private SwipeRefreshLayout mSwipe;

    public static final String DETALLE_NOTIFICACION_DTO = "detalleNotif";


    ConstraintLayout mSinNotificacionescontainer;

    @Override
    public void onRefresh() {
        mPresenter.onInteractorTraerNotificaciones();
    }

    public interface OnNotificacionesInterface
    {
        /**
         * Método para inicializar el aspecto visual al cargar el fragment de notificaciones.
         */
        void setNotificacionesInMainView();
    }

    public void setListener(OnNotificacionesInterface listener)
    {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        bindViews();
        initData();
        return mView;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link #() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        callData();
    }

    /**
     * Método llamado al inicio de la ejecución del fragment para cargar todos los elementos visuales.
     * */
    private void bindViews()
    {
        if(mListener != null)
            {
                mListener.setNotificacionesInMainView();
            }

        mData = new ArrayList<>();
        mAdapterNotificaciones = new NotificacionesAdapter(mData, this);

        mManager = new LinearLayoutManager(getContext());
            mManager.setOrientation(LinearLayoutManager.VERTICAL);

            mNotificacionesRecycler = mView.findViewById(R.id.notificaciones_fragment_buzon_rv);
            mNotificacionesRecycler.setLayoutManager(mManager);
            mNotificacionesRecycler.setAdapter(mAdapterNotificaciones);

        mLoader = Loader.newInstance();

        mSinNotificacionescontainer = mView.findViewById(R.id.sin_notificaciones_container);

        mRootView = mView.findViewById(R.id.notificaciones_root_view_cl);

        mSwipe = mView.findViewById(R.id.swipe_refresh_notificaciones);
            mSwipe.setOnRefreshListener(this);
            mSwipe.setColorSchemeColors(
                    getResources().getColor(R.color.femsa_red_a),
                    getResources().getColor(R.color.femsa_red_b),
                    getResources().getColor(R.color.femsa_red_a)
            );

    }

    /**
     * <p>Método para inicializar los listado de datos y el presentador.</p>
     * */
    private void initData()
    {
        mNotificaciones = new ArrayList<>();
        mPresenter = new NotificacionesPresenter(this, new NotificacionesInteractor());
    }

    /**
     * <p>Método que utiliza el presneter para llamar al Web Service y traer el listado de notificaciones.</p>
     * */
    private void callData(){
        mPresenter.onInteractorTraerNotificaciones();
    }

    /**
     * Método para cambiar de vista en caso de que no haya notificaciones
     * */
    private void OnNoHayNotificaciones()
    {
        mSwipe.setRefreshing(false);
        mNotificacionesRecycler.setVisibility(View.GONE);
        mSinNotificacionescontainer.setVisibility(View.VISIBLE);
    }

    /**
     * Mandado a llamar cuando la sesión caduca.
     */
    @Override
    public void OnViewNoAuth() {
        mSwipe.setRefreshing(false);
        GlobalActions g = new GlobalActions(null);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * Cuando se eliminó una notificación exitosamente se manda a llamar este método.
     */
    @Override
    public void OnViewNotificacionEliminada() {
        mAdapterNotificaciones.deleteItem(mIndiceLayout);
    }

    /**
     * Cuando una notificación se ha marcado como importante exitosamente.
     */
    @Override
    public void OnViewNotificacionImportante() {
        mAdapterNotificaciones.updateItem(mIndiceLayout);
    }

    /**
     * Muestra en pantalla el listado de notificaciones provenientes del web Service.
     * @param data listado de notificaciones
     */
    @Override
    public void OnViewMostrarNotificaciones(NotificacionesResponseData data) {
        mSwipe.setRefreshing(false);
        mNotificacionesRecycler.setVisibility(View.VISIBLE);
        mData.clear();
        mData.addAll(data.getNotificaciones().getTodasLasNotificaciones());
        mAdapterNotificaciones.notifyDataSetChanged();
        if(mData.size() < 1)
            {
                OnNoHayNotificaciones();
            }

    }

    /**
     * Cuando no se tiene ninguna notificación.
     */
    @Override
    public void OnViewSinNotificaciones() {
        mSwipe.setRefreshing(false);
        OnNoHayNotificaciones();
    }

    /**
     * Utilizado para mostrar el Loader en la pantalla
     */
    @Override
    public void OnViewShowLoader() {
        FragmentManager fm = getChildFragmentManager();
        mLoader.show(fm, "Loader");
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void OnViewHideLoader() {
        try
        {
            if(mLoader != null)
            {
                mLoader.dismiss();
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    /**
     * Método para mostrar mensaje al usuario.
     * @param tipoMensaje el numero que identifica a cada tipo de mensaje.
     */
    @Override
    public void OnViewMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
        if(tipoMensaje == StringManager.EXPIRED_TOKEN){
            GlobalActions g = new GlobalActions(getActivity());
            g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }
    }

    /**
     * Método para mostrar el mensaje de error inesperado y el código de respuesta.
     * @param tipoMensaje el numero que identifica a cada tipo de mensaje.
     * @param codigoError El numero del codigo de respuesta recibido.
     */
    @Override
    public void OnViewMostrarMensajeInesperado(int tipoMensaje, int codigoError) {
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoError);
    }


    /***
     * Método definido en NotificacionesAdapter para cuando se haga click en cada elemento del buzón de notificaciones
     * @param data el DTO de la notificacion
     */
    @Override
    public void OnNotificacionPressed(NotificacionesData data) {
        mPresenter.onInteractorMarcarVista(data.getIdNotificacion());
        Intent detalleNotificacion = new Intent(getContext(), DetalleNotificacionActivity.class);
        detalleNotificacion.putExtra(DETALLE_NOTIFICACION_DTO, data);
        startActivity(detalleNotificacion);
    }

    /**
     * Acción a ejecutar cuando se marca una notificación como importante.
     * @param idNotificacion el id de la notificacion
     */
    @Override
    public void OnNotificacionImportanteClick(int idNotificacion, int indice) {
        mIndiceLayout = indice;
        mPresenter.onInteractorMarcarImportante(idNotificacion);
    }

    /**
     * Se ejecuta cuando se va a eliminar una notificación
     *
     * @param idNotificacion id de la notificación a eliminar.
     */
    @Override
    public void OnEliminarNotificacion(int idNotificacion, int indice) {
        mIndiceLayout = indice;
        mPresenter.onInteractorEliminarNotificacion(idNotificacion);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}

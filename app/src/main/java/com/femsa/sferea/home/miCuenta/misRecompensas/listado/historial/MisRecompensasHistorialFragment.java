package com.femsa.sferea.home.miCuenta.misRecompensas.listado.historial;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;
import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.RecompensaDTO;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.misRecompensas.detalle.DetalleRecompensaActivity;
import com.femsa.sferea.home.miCuenta.misRecompensas.listado.MisRecompensasAdapter;

import java.util.ArrayList;

public class MisRecompensasHistorialFragment extends Fragment implements MisRecompensasAdapter.OnClickRecompensaListener,
        ObtenerHistorialRecompensasView {

    private View mView;
    private RecyclerView mRvHistorial;
    private CardView mCvHistorial;
    private Loader mLoader;

    private MisRecompensasAdapter mAdapter;

    private ArrayList<RecompensaDTO> mList = new ArrayList<>();

    private ObtenerHistorialRecompensasPresenter mPresenter;

    private OnHistorialRcompensaListado mListener;

    private boolean mYaHayDatos = false;

    public interface OnHistorialRcompensaListado
    {
        void onEnviaHistorial(ArrayList<RecompensaDTO> listado, boolean yaData);
    }

    public void setLstener(OnHistorialRcompensaListado listener, boolean datos, ArrayList<RecompensaDTO> list)
    {
        mListener = listener;
        mYaHayDatos = datos;
        mList = new ArrayList<>();
        if(list != null)
        {
            mList.addAll(list);
        }
    }

    public MisRecompensasHistorialFragment() {}

    public static MisRecompensasHistorialFragment newInstance() {
        Bundle args = new Bundle();
        MisRecompensasHistorialFragment fragment = new MisRecompensasHistorialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mis_recompensas_historial, container, false);
        bindViews();
        if(!mYaHayDatos)
        {
            initializePresenter();
        }
        else
        {
            configuraAdapter();
        }
        return mView;
    }

    private void bindViews(){
        mRvHistorial = mView.findViewById(R.id.rv_fragment_mis_recompensas_historial);
        mCvHistorial = mView.findViewById(R.id.cv_fragment_mis_recompensas_historial);
        mLoader = Loader.newInstance();
    }

    private void initializePresenter(){
        mPresenter = new ObtenerHistorialRecompensasPresenter(this, new ObtenerHistorialRecompensasInteractor());
        mPresenter.iniciarProcesoObtenerHistorialRecompensas();
    }

    /**
     * <p>Método que define la acción que se debe llevar a cabo cuando se presione un elemento del
     * RecyclerView. En este caso la acción es que se iniciará {@link DetalleRecompensaActivity}
     * para mostrar más información acerca de la recompensa seleccionada.</p>
     */
    @Override
    public void onClickRecompensa(RecompensaDTO recompensa) {
        Intent intent = new Intent(getContext(), DetalleRecompensaActivity.class);
        intent.putExtra("idRecompensa", recompensa.getIdRecompensa());
        intent.putExtra("historial", true);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null)
            {
                mPresenter.onDestroy();
            }
        super.onDestroy();
    }

    @Override
    public void onViewObtenerHistorialRecompensas(ObtenerListadoHistorialData data) {
        if(data.getmListHistorial().size() > 0)
            {
                mList.addAll(data.getmListHistorial());
                configuraAdapter();
                if(!mYaHayDatos)
                {
                    mListener.onEnviaHistorial(mList, true);
                }
            }
        else
            {
                mCvHistorial.setVisibility(View.VISIBLE);
                mRvHistorial.setVisibility(View.GONE);
            }
    }

    @Override
    public void onViewSinRecompensas() {
        mCvHistorial.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getChildFragmentManager(),mensaje);
    }

    private void configuraAdapter()
    {

        mAdapter = new MisRecompensasAdapter(mList, this);
        mRvHistorial.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvHistorial.setAdapter(mAdapter);
        mRvHistorial.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getChildFragmentManager();
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
        GlobalActions globalActions = new GlobalActions(getActivity());
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }
}

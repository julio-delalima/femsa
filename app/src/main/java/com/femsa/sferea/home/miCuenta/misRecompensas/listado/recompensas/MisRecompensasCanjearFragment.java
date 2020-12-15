package com.femsa.sferea.home.miCuenta.misRecompensas.listado.recompensas;

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

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoRecompensasData;
import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.RecompensaDTO;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.misRecompensas.detalle.DetalleRecompensaActivity;
import com.femsa.sferea.home.miCuenta.misRecompensas.listado.MisRecompensasAdapter;

import java.util.ArrayList;

public class MisRecompensasCanjearFragment extends Fragment implements MisRecompensasAdapter.OnClickRecompensaListener,
        ObtenerListadoRecompensasView {

    private View mView;
    private RecyclerView mRvCanjear;
    private CardView mCvCanjear;
    private Loader mLoader;

    private MisRecompensasAdapter mAdapter;

    private ArrayList<RecompensaDTO> mList = new ArrayList<>();

    private ObtenerListadoRecompensasPresenter mPresenter;

    private OnRcompensaListado mListener;

    private boolean mYaHayDatos = false;

    public MisRecompensasCanjearFragment() {}

    public interface OnRcompensaListado
        {
            void onEnviaListado(ArrayList<RecompensaDTO> listado, boolean yaData);
        }

    public void setLstener(OnRcompensaListado listener, boolean datos, ArrayList<RecompensaDTO> list)
        {
            mListener = listener;
            mYaHayDatos = datos;
            mList = new ArrayList<>();
            if(list != null)
                {
                    mList.addAll(list);
                }
        }

    public static MisRecompensasCanjearFragment newInstance() {
        Bundle args = new Bundle();
        MisRecompensasCanjearFragment fragment = new MisRecompensasCanjearFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mis_recompensas_canjear, container, false);
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
        mRvCanjear = mView.findViewById(R.id.rv_fragment_mis_recompensas_canjear);
        mCvCanjear = mView.findViewById(R.id.cv_fragment_mis_recompensas_canjear);
        mLoader = Loader.newInstance();
    }

    private void initializePresenter(){
        mPresenter = new ObtenerListadoRecompensasPresenter(this, new ObtenerListadoRecompensasInteractor());
        mPresenter.iniciarProcesoObtenerListadoRecompensas();
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
        startActivity(intent);
    }

    @Override
    public void onViewObtenerListadoRecompensas(ObtenerListadoRecompensasData data) {
        if(data.getListadoRecompensas().size() > 0) {
            mList.addAll(data.getListadoRecompensas());
            configuraAdapter();
            if(!mYaHayDatos)
                {
                    mListener.onEnviaListado(mList, true);
                }
        }
        else
            {
                mRvCanjear.setVisibility(View.GONE);
                mCvCanjear.setVisibility(View.VISIBLE);
            }
    }

    private void configuraAdapter()
        {
            mAdapter = new MisRecompensasAdapter(mList, this);
            mRvCanjear.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRvCanjear.setAdapter(mAdapter);
            mRvCanjear.setVisibility(View.VISIBLE);
        }

    @Override
    public void onViewSinRecompensas() {
        mCvCanjear.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getChildFragmentManager(),mensaje);
    }


    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getChildFragmentManager();
        mLoader.show(fragmentManager, "Loader");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null)
            {
                mPresenter.onDestroy();
            }
    }
}

package com.femsa.sferea.home.miCuenta.miActividad.logros;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.LogroDTO;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class MiActividadLogrosFragment extends Fragment {

    //Referencia que infla los elementos del layout
    private View mView;

    //CardViews contenedores de la información del historial
    private CardView mCvTitulo;
    private CardView mCvContenedor;

    private ArrayList<LogroDTO> mLogrosArray = new ArrayList<>();
    private RecyclerView mRvLogros;
    private LogrosAdapter mLogrosAdapter;
    private LinearLayoutManager mLogrosManager;
    private Loader mLoader;

    public MiActividadLogrosFragment() { }

    public static MiActividadLogrosFragment newInstance() {
        Bundle args = new Bundle();
        MiActividadLogrosFragment fragment = new MiActividadLogrosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mi_actividad_logros, container, false);
        bindViews();
        initializeRecyclerView();
        return mView;
    }

    private void bindViews(){
        mRvLogros = mView.findViewById(R.id.rv_fragment_mi_actividad_logros_mis_logros);
        mLoader = Loader.newInstance();

        mCvTitulo = mView.findViewById(R.id.cv_logros_titulo);
        mCvContenedor = mView.findViewById(R.id.cv_logros_contenedor);
        if(mLogrosArray.size() > 1)
            {
                mCvTitulo.setVisibility(View.VISIBLE);
                mCvContenedor.setVisibility(View.VISIBLE);
            }
    }

    /**
     * <p>Método que inicializa el adaptador y el manager para el RecyclerView donde se colocarán los logros.</p>
     */
    private void initializeRecyclerView(){
        mLogrosAdapter = new LogrosAdapter(getContext(), mLogrosArray);
        mLogrosManager = new LinearLayoutManager(getContext());
        mRvLogros.setAdapter(mLogrosAdapter);
        mRvLogros.setLayoutManager(mLogrosManager);
    }


    public void onViewSetLogrosData(MiActividadResponseData data) {
        mLogrosArray.addAll(data.getMiActividad().getListadoLogros());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

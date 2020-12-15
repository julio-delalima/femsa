package com.femsa.sferea.home.miCuenta.miActividad.programas;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingData;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.home.miCuenta.miActividad.MiActividadPresenter;

import java.util.ArrayList;

public class MiActividadProgramasFragment extends Fragment {

    //Referencia que infla los elementos del layout
    private View mView;

    //Elementos correspondientes a la lista de programas activos
    private CardView mCvActivosTitulo;
    private CardView mCvActivosContenedor;
    private ConstraintLayout mClSinProgramasActivos;
    private ConstraintLayout mClSinProgramasHistorial;
    private RecyclerView mRvActivos;
    private LinearLayoutManager mActivosManager;
    private ProgramasAdapter mActivosAdapter;
    private ArrayList<ProgramRankingData> mProgramasActivos;

    //Elementos correspondientes al historial de programas
    private CardView mCvHistorialTitulo;
    private CardView mCvHistorialContenedor;
    private RecyclerView mRvHistorial;
    private LinearLayoutManager mHistorialManager;
    private ProgramasAdapter mHistorialAdapter;
    private ArrayList<ProgramRankingData> mHistorialProgramas;

    public MiActividadResponseData mProgramasData;

    //Presentador
    private MiActividadPresenter mPresenter;

    //Loader
    private Loader mLoader;

    //Constructor por defecto
    public MiActividadProgramasFragment() { }

    public static MiActividadProgramasFragment newInstance() {
        Bundle args = new Bundle();
        MiActividadProgramasFragment fragment = new MiActividadProgramasFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mi_actividad_programas, container, false);
        bindViews();
        initializeRecyclerView();
        if(mProgramasData != null)
            {
                onViewMiActividadData(mProgramasData);
            }
        return mView;
    }

    private void bindViews() {
        mRvActivos = mView.findViewById(R.id.rv_fragment_mi_actividad_programas_activos);
        mCvActivosTitulo = mView.findViewById(R.id.cv_activos_titulo);
        mCvActivosContenedor = mView.findViewById(R.id.cv_activos_contenido);
        mClSinProgramasActivos = mView.findViewById(R.id.cl_sin_programas_activos);
        mClSinProgramasHistorial = mView.findViewById(R.id.cl_sin_programas_historial);
        mRvHistorial = mView.findViewById(R.id.rv_fragment_mi_actividad_programas_historial);
        mCvHistorialTitulo = mView.findViewById(R.id.cv_historial_titulo);
        mCvHistorialContenedor = mView.findViewById(R.id.cv_historial_contenido);

        mLoader = Loader.newInstance();
    }

    /**
     * <p>Método que inicializa los adaptadores y los manager para los RecyclerView donde se muestran
     * los programas activos y e historial-</p>
     */
    private void initializeRecyclerView() {
        mProgramasActivos = new ArrayList<>();
        mHistorialProgramas = new ArrayList<>();

        mActivosManager = new LinearLayoutManager(getContext());
        mActivosAdapter = new ProgramasAdapter(getContext(), mProgramasActivos);
        mRvActivos.setAdapter(mActivosAdapter);
        mRvActivos.setLayoutManager(mActivosManager);

        mHistorialManager = new LinearLayoutManager(getContext());
        mHistorialAdapter = new ProgramasAdapter(getContext(), mHistorialProgramas);
        mRvHistorial.setAdapter(mHistorialAdapter);
        mRvHistorial.setLayoutManager(mHistorialManager);
    }

   public void setProgramasData(MiActividadResponseData data)
    {
        mProgramasData = data;
    }

    private void onViewMiActividadData(MiActividadResponseData data) {
        //Tamaño de los arreglos que contiene la información sobre programas activos e historial
        int programasActivosSize = data.getMiActividad().getActivePrograms().size();
        int programasHistorialSize = data.getMiActividad().getHistoryPrograms().size();

        if (programasActivosSize>0 && programasHistorialSize>0){ //Si existen elementos que mostrar en programas activos e historial

            mCvActivosTitulo.setVisibility(View.VISIBLE);
            mCvHistorialTitulo.setVisibility(View.VISIBLE);

            mCvActivosContenedor.setVisibility(View.VISIBLE);
            mCvHistorialContenedor.setVisibility(View.VISIBLE);

            mRvActivos.setVisibility(View.VISIBLE);
            mRvHistorial.setVisibility(View.VISIBLE);

            mClSinProgramasActivos.setVisibility(View.GONE);
            mClSinProgramasHistorial.setVisibility(View.GONE);

            mProgramasActivos.addAll(data.getMiActividad().getActivePrograms());
            mActivosAdapter.notifyDataSetChanged();

            mHistorialProgramas.addAll(data.getMiActividad().getHistoryPrograms());
            mHistorialAdapter.notifyDataSetChanged();
        }
        else if(programasActivosSize>0 && programasHistorialSize<1){ //Si existen elementos que mostrar en programas activos pero no en historial


            mCvActivosTitulo.setVisibility(View.VISIBLE);
            mCvHistorialTitulo.setVisibility(View.VISIBLE);

            mCvActivosContenedor.setVisibility(View.VISIBLE);
            mCvHistorialContenedor.setVisibility(View.VISIBLE);

            mRvActivos.setVisibility(View.VISIBLE);
            mRvHistorial.setVisibility(View.GONE);

            mClSinProgramasActivos.setVisibility(View.GONE);
            mClSinProgramasHistorial.setVisibility(View.VISIBLE);


            mProgramasActivos.addAll(data.getMiActividad().getActivePrograms());
            mActivosAdapter.notifyDataSetChanged();

            //ConstraintSet se utilza para colocar el CardView de programas activos en la parte inferior de forma que se tenga un margin_bottom
            /*ConstraintSet constraintSet = new ConstraintSet();
            ConstraintLayout constraintLayout = mView.findViewById(R.id.cl_fragment_mi_actividad_programas); //constraintLayout es la referencia del layout padre
            constraintSet.clone(constraintLayout);
            constraintSet.connect(mCvActivosContenedor.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 32);
            constraintSet.applyTo(constraintLayout);*/

        }
        else if (programasActivosSize<1 && programasHistorialSize>0){ //Si existen elementos que mostrar en historial pero no en programas activos

            mCvActivosTitulo.setVisibility(View.VISIBLE);
            mCvHistorialTitulo.setVisibility(View.VISIBLE);

            mCvActivosContenedor.setVisibility(View.VISIBLE);
            mCvHistorialContenedor.setVisibility(View.VISIBLE);

            mRvActivos.setVisibility(View.GONE);
            mRvHistorial.setVisibility(View.VISIBLE);

            mClSinProgramasActivos.setVisibility(View.VISIBLE);
            mClSinProgramasHistorial.setVisibility(View.GONE);

            mHistorialProgramas.addAll(data.getMiActividad().getHistoryPrograms());
            mHistorialAdapter.notifyDataSetChanged();

        }
        else{ //Si no existen elementos en programas activos ni en historial

            mCvActivosTitulo.setVisibility(View.VISIBLE);
            mCvHistorialTitulo.setVisibility(View.VISIBLE);

            mCvActivosContenedor.setVisibility(View.VISIBLE);
            mCvHistorialContenedor.setVisibility(View.VISIBLE);

            mRvActivos.setVisibility(View.GONE);
            mRvHistorial.setVisibility(View.GONE);

            mClSinProgramasActivos.setVisibility(View.VISIBLE);
            mClSinProgramasHistorial.setVisibility(View.VISIBLE);

            //ConstraintSet se utilza para colocar el CardView de programas activos en la parte inferior de forma que se tenga un margin_bottom
            //ConstraintSet constraintSet = new ConstraintSet();
            //ConstraintLayout constraintLayout = mView.findViewById(R.id.cl_fragment_mi_actividad_programas); //constraintLayout es la referencia del layout padre
           // constraintSet.clone(constraintLayout);
            //constraintSet.connect(mCvActivosContenedor.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 32);
            //constraintSet.applyTo(constraintLayout);
        }
    }


}

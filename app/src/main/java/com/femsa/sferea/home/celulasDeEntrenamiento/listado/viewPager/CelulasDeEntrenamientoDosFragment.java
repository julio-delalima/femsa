package com.femsa.sferea.home.celulasDeEntrenamiento.listado.viewPager;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.sferea.R;

/**
 * <p>Fragment que corresponde a la segunda vista que se ve en el ViewPager de Células de
 * Entrenamiento.</p>
 */
public class CelulasDeEntrenamientoDosFragment extends Fragment {

    public CelulasDeEntrenamientoDosFragment() {}

    public static CelulasDeEntrenamientoDosFragment newInstance(){
        Bundle args = new Bundle();
        CelulasDeEntrenamientoDosFragment fragment = new CelulasDeEntrenamientoDosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_celulas_de_entrenamiento_dos, container, false);
    }

}

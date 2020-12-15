package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias.viewPager;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.sferea.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CelulasEntrenamientoVaciaUnoFragment extends Fragment {


    public CelulasEntrenamientoVaciaUnoFragment() {
        // Required empty public constructor
    }

    public static CelulasEntrenamientoVaciaUnoFragment newInstance(){
        Bundle args = new Bundle();
        CelulasEntrenamientoVaciaUnoFragment fragment = new CelulasEntrenamientoVaciaUnoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_celulas_entrenamiento_vacia_uno, container, false);
    }

}

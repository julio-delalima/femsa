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
public class CelulasEntrenamientoVaciaDosFragment extends Fragment {


    public CelulasEntrenamientoVaciaDosFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        CelulasEntrenamientoVaciaDosFragment fragment = new CelulasEntrenamientoVaciaDosFragment();
        fragment.setArguments(args);
        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_celulas_entrenamiento_vacia_dos, container, false);
    }

}

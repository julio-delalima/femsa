package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias.viewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CelulasEntrenamientoVaciaAdapter extends FragmentStatePagerAdapter {

    private int numeroPaginas; //Número de páginas que tendrá el adaptador del slider

    public CelulasEntrenamientoVaciaAdapter(FragmentManager fm, int contador) {
        super(fm);
        numeroPaginas = contador;
    }


    @Override
    public Fragment getItem(int i) {
        if (i == 1) {
            return CelulasEntrenamientoVaciaDosFragment.newInstance();
        }
        return CelulasEntrenamientoVaciaUnoFragment.newInstance();
    }

    @Override
    public int getCount() {
        return numeroPaginas;
    }
}

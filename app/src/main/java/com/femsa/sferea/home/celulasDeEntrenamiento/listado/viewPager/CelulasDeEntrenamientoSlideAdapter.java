package com.femsa.sferea.home.celulasDeEntrenamiento.listado.viewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * <p>Clase que será el adaptador para el ViewPager que se encuentra en la pantalla principal
 * de Células de Entrenamiento.</p>
 */
public class CelulasDeEntrenamientoSlideAdapter extends FragmentStatePagerAdapter {

    private int numeroPaginas; //Número de páginas que tendrá el adaptador del slider

    public CelulasDeEntrenamientoSlideAdapter(FragmentManager fm, int contador) {
        super(fm);
        numeroPaginas = contador;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return CelulasDeEntrenamientoUnoFragment.newInstance();
            case 1:
                return CelulasDeEntrenamientoDosFragment.newInstance();
            default:
                return CelulasDeEntrenamientoUnoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return numeroPaginas;
    }
}

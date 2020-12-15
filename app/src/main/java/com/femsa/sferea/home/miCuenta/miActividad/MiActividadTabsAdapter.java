package com.femsa.sferea.home.miCuenta.miActividad;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.femsa.sferea.home.miCuenta.miActividad.logros.MiActividadLogrosFragment;
import com.femsa.sferea.home.miCuenta.miActividad.programas.MiActividadProgramasFragment;

public class MiActividadTabsAdapter extends FragmentStatePagerAdapter {

    private int mNumberOfTabs; //Número de tabs que tendrá el TabLayout

    public MiActividadTabsAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        mNumberOfTabs = numberOfTabs;
    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MiActividadProgramasFragment programasFragment = MiActividadProgramasFragment.newInstance();
                return programasFragment;

            case 1:
                MiActividadLogrosFragment logrosFragment = MiActividadLogrosFragment.newInstance();
                return logrosFragment;
        }
        return null;
    }
}

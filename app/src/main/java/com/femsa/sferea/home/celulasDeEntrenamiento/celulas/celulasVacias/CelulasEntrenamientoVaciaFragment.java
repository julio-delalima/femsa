package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.femsa.sferea.R;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.ActivityDialog;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias.viewPager.CelulasEntrenamientoVaciaAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CelulasEntrenamientoVaciaFragment extends Fragment {

    // elementos de la vista
    private View mView; //Referencia que infla los elementos del layout
    private ImageView mIvPortada;
    private TextView mTvTitulo;
    private TextView mTvDescripcion;
    private ViewPager mViewPager;
    private CelulasEntrenamientoVaciaAdapter mSliderAdapter; //Adaptador del slider
    private FloatingActionButton mButton;
    private static final int NUM_PAGES = 2; //Número de páginas que se mostrarán en el slider
    private Toolbar toolbar;
    private OnCelulasInit mListener;

    public interface OnCelulasInit
    {
        /**
         * Método para configurar el aspecto de HomeActivity al cargar el fragment.
         * */
        void SetCelulasInMainView();
    }

    public void setListener(OnCelulasInit listener)
    {
        mListener = listener;
    }

    public CelulasEntrenamientoVaciaFragment() {
        // Required empty public constructor
    }

    public static CelulasEntrenamientoVaciaFragment newInstance(){
        Bundle args = new Bundle();
        CelulasEntrenamientoVaciaFragment fragment = new CelulasEntrenamientoVaciaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_celulas_entrenamiento_vacia, container, false);
        bindViews();
        bindListeners();
        initializeViewPagerAdapter();
        mListener.SetCelulasInMainView();
        return mView;
    }



    private void bindViews() {
        mIvPortada = mView.findViewById(R.id.iv_celulas_entrenamiento_fragment_portada);
        mTvTitulo = mView.findViewById(R.id.tv_celulas_entrenamiento_fragment_titulo);
        mTvDescripcion = mView.findViewById(R.id.tv_celulas_entrenamiento_fragment_descripcion);
        mViewPager = mView.findViewById(R.id.vp_celulas_entrenamiento_fragment);
        mButton = mView.findViewById(R.id.floatingActionButton);
    }

    private void bindListeners() {
        mButton.setOnClickListener(v -> {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), v, "transition");
            int relativeX = (int) (v.getX() + v.getWidth()/2);
            int relativeY = (int) (v.getY() + v.getHeight()/2);

            Intent intent = new Intent(getContext(), ActivityDialog.class);
            intent.putExtra(ActivityDialog.EXTRA_CIRCULAR_REVELARX, relativeX);
            intent.putExtra(ActivityDialog.EXTRA_CIRCULAR_REVELARY, relativeY);
            ActivityCompat.startActivity(getContext(), intent, option.toBundle());
        });
    }



    private void initializeViewPagerAdapter() {
        mSliderAdapter = new CelulasEntrenamientoVaciaAdapter(getChildFragmentManager(), NUM_PAGES);
        mViewPager.setAdapter(mSliderAdapter);
    }

}

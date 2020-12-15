package com.femsa.sferea.home.legal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;


public class FragmentLegal extends Fragment {

    private View mView;

    private AppBarLayout mLayout;

    private OnLegalFragmentCreated mListener;

    public FragmentLegal() { }

    public interface OnLegalFragmentCreated
        {
            void setLegalInMainView(AppBarLayout layour);
        }

    public void setListener(OnLegalFragmentCreated listener)
        {
            mListener = listener;
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_legal_2, container, false);
        bindViews();
        if(mListener != null)
            {
                mListener.setLegalInMainView(mLayout);
            }
        return mView;
    }


    private void bindViews() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        TabLayout tabLayout = mView.findViewById(R.id.tabs);
        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = mView.findViewById(R.id.container);
                viewPager.setAdapter(sectionsPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        mLayout = mView.findViewById(R.id.appbar);
    }
    public static class PlaceholderFragment extends Fragment implements LegalView {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private LegalPresenter mPresenter;

        private TextView currentCountry;

        WebView legalContainer;

        Loader loader;

        public PlaceholderFragment() { }

        public static PlaceholderFragment newInstance(int sectionNumber)
            {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_legal, container, false);
            ImageView nextCountry, prevCountry;
            LinearLayout countryContainer;
            legalContainer = rootView.findViewById(R.id.legal_text_container);

            mPresenter = new LegalPresenter(this, new LegalInteractor());

            loader = Loader.newInstance();

            assert getArguments() != null;
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (section)
            {
                case 1: //Términos y Condiciones
                    countryContainer = rootView.findViewById(R.id.legal_country_chooser);
                        countryContainer.setVisibility(View.GONE);
                    getTerms();

                    break;
                case 2: //Aviso de privacidad
                    nextCountry = rootView.findViewById(R.id.legal_next_contry);
                    prevCountry = rootView.findViewById(R.id.legal_previous_country);
                    currentCountry = rootView.findViewById(R.id.legal_country);

                    nextCountry.setOnClickListener(v -> {
                        //Toast.makeText(getContext(), "Siguiente pais", Toast.LENGTH_SHORT).show();
                    });

                    prevCountry.setOnClickListener(v -> {
                        //Toast.makeText(getContext(), "País anterior", Toast.LENGTH_SHORT).show();
                    });
                    getAdvice();

                    break;
            }

            return rootView;
        }

        @Override
        public void onShowTermsAndConditions(TermsAndConditionsResponseData data) {
            String text = GlobalActions.webViewConfigureText( (data.getmTerms().getTerms()).replace("dir=\"ltr\"", "") );
            legalContainer.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        }

        @Override
        public void onShowPrivacyAdvice(PrivacyAdviceResponseData data) {
            currentCountry.setText(data.getAdvice().getCountry());
            String text = GlobalActions.webViewConfigureText(data.getAdvice().getContent());
            legalContainer.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        }

        @Override
        public void onShowLoader() {
            FragmentManager fm = getChildFragmentManager();
            loader.show(fm, "Loader");
        }

        /**
         * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
         * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
         * */
        @Override
        public void onHideLoader() {
            try
            {
                if(loader != null && loader.isAdded())
                {
                    loader.dismiss();
                }
            }
            catch (IllegalStateException ignored)
            {
                // There's no way to avoid getting this if saveInstanceState has already been called.
            }
        }

        @Override
        public void onBackToLogin() {
            GlobalActions g = new GlobalActions(getActivity());
            g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

        /**
         * Se llama cuando se necesita mostrar un mensaje como error 500
         *
         * @param tipoMensaje
         */
        @Override
        public void onMostrarMensage(int tipoMensaje) {

        }

        /**
         * Se llama cuando se necesita mostrar un mensaje como error 500
         *
         * @param tipoMensaje
         * @param codigoRespuesta
         */
        @Override
        public void onMostrarMensageInesperado(int tipoMensaje, int codigoRespuesta) {

        }

        public void getTerms()
        {
            mPresenter.OnInteractorGetTerms(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

        public void getAdvice()
        {
            mPresenter.OnInteractorGetPrivacyAdvice(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

        @Override
        public void onDestroyView() {
            mPresenter.onDestroy();
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            mPresenter.onDestroy();
            super.onDestroy();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return LegalActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

}

package com.femsa.sferea.home.legal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.registro.RegistryPart2;

import java.util.ArrayList;
import java.util.Locale;

public class LegalActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // add back arrow to toolbar
        if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tabs);
            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LegalView{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private LegalPresenter mPresenter;

        private ArrayList<PrivacyAdviceResponseData> mListadoTerminos;

        private int idiomaIndex = 0;

        private int paisIndex = 1;

        private TextView currentCountry;

        WebView legalContainer;

        Loader loader;

        private boolean showAcceptButton = false;

        private Button mAcceptButton;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_legal, container, false);
            /** Creating components */
            ImageView nextCountry, prevCountry;
            ConstraintLayout countryContainer;
            legalContainer = rootView.findViewById(R.id.legal_text_container);

            mPresenter = new LegalPresenter(this, new LegalInteractor());
            mListadoTerminos = new ArrayList<>();

            showAcceptButton = ((Activity) getContext()).getIntent().getBooleanExtra("Accept", false);
            if(showAcceptButton)
                {
                    mAcceptButton = rootView.findViewById(R.id.legal_accept_button);
                    mAcceptButton.setVisibility(View.VISIBLE);
                        mAcceptButton.setOnClickListener(v->{
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result","aceptado");
                            ((Activity) getContext()).setResult(RegistryPart2.RESULT_OK, returnIntent);
                            ((Activity) getContext()).finish();
                        });
                }

            loader = Loader.newInstance();

            int section = getArguments().getInt(ARG_SECTION_NUMBER);
                switch (section)
                {
                    case 1: //Términos y Condiciones
                        countryContainer = rootView.findViewById(R.id.legal_country_chooser);
                        countryContainer.setVisibility(View.GONE);
                        //
                        getTerms();

                    break;
                    case 2: //Aviso de privacidad
                        nextCountry = rootView.findViewById(R.id.legal_next_contry);
                        prevCountry = rootView.findViewById(R.id.legal_previous_country);
                        currentCountry = rootView.findViewById(R.id.legal_country);

                        nextCountry.setOnClickListener(v -> {
                            String text;
                            if(paisIndex + 1 >= mListadoTerminos.size()){
                                text = GlobalActions.webViewConfigureText(mListadoTerminos.get(0).getAdvice().getContent());
                                paisIndex = 0;
                            } else{
                                text = GlobalActions.webViewConfigureText(mListadoTerminos.get(paisIndex + 1).getAdvice().getContent());
                                paisIndex+=1;
                            }
                            currentCountry.setText(mListadoTerminos.get(paisIndex).getAdvice().getCountry());
                            legalContainer.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
                        });

                        prevCountry.setOnClickListener(v -> {
                            String text;
                            if(paisIndex - 1 < 0){
                                text = GlobalActions.webViewConfigureText(mListadoTerminos.get(mListadoTerminos.size() - 1).getAdvice().getContent());
                                paisIndex = mListadoTerminos.size() - 1;
                            } else{
                                text = GlobalActions.webViewConfigureText(mListadoTerminos.get(paisIndex - 1).getAdvice().getContent());
                                paisIndex-=1;
                            }
                            currentCountry.setText(mListadoTerminos.get(paisIndex).getAdvice().getCountry());
                            legalContainer.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
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
            if(idiomaIndex == 0){
                getAdvicePt();
                idiomaIndex++;
                currentCountry.setText(data.getAdvice().getCountry());
                String text = GlobalActions.webViewConfigureText(data.getAdvice().getContent());
                legalContainer.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
            } else if(idiomaIndex == 1){
                getAdviceUS();
                idiomaIndex++;
            }
            mListadoTerminos.add(data);
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
         * @param tipoMensaje el tipo de mensaje a mostrar
         */
        @Override
        public void onMostrarMensage(int tipoMensaje) {
            DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
        }

        /**
         * Se llama cuando se necesita mostrar un mensaje como error 500
         *
         * @param tipoMensaje
         * @param codigoRespuesta
         */
        @Override
        public void onMostrarMensageInesperado(int tipoMensaje, int codigoRespuesta) {
            DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
        }

        public void getTerms()
        {
            mPresenter.OnInteractorGetTerms(Locale.getDefault().getLanguage());
        }

        public void getAdvice()
        {
            mPresenter.OnInteractorGetPrivacyAdvice(Locale.getDefault().getLanguage());
        }

        public void getAdvicePt()
        {
            mPresenter.OnInteractorGetPrivacyAdvice("pt");
        }

        public void getAdviceUS()
        {
            mPresenter.OnInteractorGetPrivacyAdvice("en");
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

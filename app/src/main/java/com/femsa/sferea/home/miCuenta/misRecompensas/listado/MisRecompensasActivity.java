package com.femsa.sferea.home.miCuenta.misRecompensas.listado;

import android.os.Bundle;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.sferea.home.miCuenta.miRanking.RankingInteractor;
import com.femsa.sferea.home.miCuenta.miRanking.RankingPresenter;
import com.femsa.sferea.home.miCuenta.miRanking.RankingView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.RecompensaDTO;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.miCuenta.misRecompensas.listado.historial.MisRecompensasHistorialFragment;
import com.femsa.sferea.home.miCuenta.misRecompensas.listado.recompensas.MisRecompensasCanjearFragment;

import java.util.ArrayList;

import static com.femsa.sferea.Utilities.SharedPreferencesUtil.CORCHOLATAS_KEY;

public class MisRecompensasActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MisRecompensasCanjearFragment.OnRcompensaListado,
        MisRecompensasHistorialFragment.OnHistorialRcompensaListado, RankingView {

    private Toolbar mToolbar;
    private TextView mTvCorcholatas;
    private TabLayout mTabLayout;
    private ArrayList<RecompensaDTO> mRecompensas, mHistorialListado;
    private boolean mDatos = false, mHistorial = false;
    private FrameLayout mFlFragments; //Contenedor para colocar Fragments.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_recompensas);
        bindViews();
        bindListeners();
        initializeToolbar();
        setCanjearTab();
    }

    private void bindViews(){
        mToolbar = findViewById(R.id.toolbar_activity_mis_recompensas);
        mTvCorcholatas = findViewById(R.id.tv_activity_mis_recompensas_tus_puntos);
        mTabLayout = findViewById(R.id.tl_activity_mis_recompensas);
        mFlFragments = findViewById(R.id.fl_activity_mis_recompensas_fragments);
        RankingPresenter rp = new RankingPresenter(this, new RankingInteractor());
        rp.OnInteractorCallRanking();
    }

    private void bindListeners(){
        mTabLayout.addOnTabSelectedListener(this);
    }

    /**
     * <p>Método que configura la Toolbar.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método para colocar el tab "Canjear" por defecto.</p>
     */
    private void setCanjearTab(){
        MisRecompensasCanjearFragment fragment = MisRecompensasCanjearFragment.newInstance();
        fragment.setLstener(this, mDatos, mRecompensas);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_activity_mis_recompensas_fragments, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * <p>Listener para realizar una acción cuando se selecciona un tab. Dependiendo del que se
     * seleccione se dibujará en el FrameLayout el fragment correspondiente a canjear o historial.</p>
     * @param tab Tab que se seleccionó.
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment fragment = null;
        switch (tab.getPosition()){
            case 0:
                fragment = MisRecompensasCanjearFragment.newInstance();
                ((MisRecompensasCanjearFragment) fragment).setLstener(this, mDatos, mRecompensas);
                break;
            case 1:
                fragment = MisRecompensasHistorialFragment.newInstance();
                ((MisRecompensasHistorialFragment) fragment).setLstener(this, mHistorial, mHistorialListado);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_activity_mis_recompensas_fragments, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    /**
     * <p>Listener para realizar una acción cuando se deselecciona un Tab. No se implementa.</p>
     * @param tab Tab que se deselecciono.
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    /**
     * <p>Listener para realizar una acción cuando se vuelve a seleccionar un Tab. No se implementa.</p>
     * @param tab Tab que se volvió a seleccionar.
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) { }

    @Override
    public void onEnviaListado(ArrayList<RecompensaDTO> listado, boolean yaData) {
        mDatos = yaData;
        mRecompensas = listado;
    }

    @Override
    public void onEnviaHistorial(ArrayList<RecompensaDTO> listado, boolean yaData) {
        mHistorial = yaData;
        mHistorialListado = listado;
    }

    @Override
    public void onShowLoader() {

    }

    @Override
    public void onHideLoader() {

    }

    @Override
    public void onRankingSuccess(RankingResponseData data) {
        SharedPreferencesUtil.getEditor().putInt(CORCHOLATAS_KEY, data.getRanking().getCorcholatas());
        SharedPreferencesUtil.getEditor().apply();
        mTvCorcholatas.setText(String.valueOf(SharedPreferencesUtil.getInstance().getCorcholatas()));
    }

    @Override
    public void onProgramRankingSuccess(ProgramRankingResponseData data) {

    }

    @Override
    public void onShowMessage(int messageType) {

    }

    @Override
    public void onShowMessage(int messageType, int codigo) {

    }

    @Override
    public void onRankingTierListSuccess(RankingTierlistResponseData data) {

    }

    @Override
    public void onRankingLikesuccess() {

    }

    @Override
    public void onRankingNoAuth() {

    }

    @Override
    public void onRankingNoInternet() {

    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {

    }
}

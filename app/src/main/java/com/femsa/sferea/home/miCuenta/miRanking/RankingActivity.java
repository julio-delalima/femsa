package com.femsa.sferea.home.miCuenta.miRanking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.miRanking.detalle.DetalleRankingActivity;
import com.femsa.sferea.home.miCuenta.miRanking.listado.RankingAdapter;

import java.util.ArrayList;


public class RankingActivity extends AppCompatActivity implements RankingAdapter.OnItemClickListener, RankingView {

    private RecyclerView mRecycler;

    private ImageView mProfilePicture, mCountryFlag;

    private String mFullName, mFullProfile;

    private TextView mName, mCountryname, mEarnedPoint;

    private Button mActive, mFinished;

    private final String  PROFILE_PIC = "profilePicture", FULL_NAME = "FullName";

    private TabLayout mTabs;

    private Loader mLoader;

    private RankingPresenter mPresenter;

    private ArrayList<ProgramRankingData> mActivePrograms, mHistoryPrograms;

    private ConstraintLayout mMainView, mSinHistorialContenedor, mSinProgramasContenedor;

    RankingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ranking);
        bindComponents();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }

    private void setData()
    {
        GlideApp.with(getApplicationContext() ).load(mFullProfile).error(R.mipmap.ic_circled_user).into(mProfilePicture);
        mName.setText(SharedPreferencesUtil.getInstance().getNombreSP());
    }

    private void bindComponents()
    {
        mMainView = findViewById(R.id.ranking_main_view);
            mMainView.setVisibility(View.INVISIBLE);

        mActivePrograms = new ArrayList<>();

        mHistoryPrograms = new ArrayList<>();

        mSinHistorialContenedor = findViewById(R.id.cl_sin_programas_historial);

        mSinProgramasContenedor = findViewById(R.id.cl_sin_programas_activos);

        Toolbar toolbar = findViewById(R.id.my_ranking_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // add back arrow to toolbar
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mEarnedPoint = findViewById(R.id.my_ranking_earned_points);

        mActive = findViewById(R.id.my_ranking_active_programs);

        mFinished = findViewById(R.id.my_ranking_finished_programs);

        mName = findViewById(R.id.my_ranking_name);

        mCountryFlag = findViewById(R.id.my_ranking_country_flag);

        mCountryname = findViewById(R.id.my_ranking_country_name);

        mProfilePicture = findViewById(R.id.my_ranking_profile_picture);

        mRecycler = findViewById(R.id.ranking_recycler);


        mTabs = findViewById(R.id.ranking_tabbar);
            mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
            {
                @Override
                public void onTabSelected(TabLayout.Tab tab) { changeTabData(tab.getPosition()); }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });

        mLoader = Loader.newInstance();

        mPresenter = new RankingPresenter(this, new RankingInteractor());
            mPresenter.OnInteractorCallRanking();
            mPresenter.OnInteractorCallProgramRanking();

    }

    private void changeTabData(int index)
    {
        mSinHistorialContenedor.setVisibility(View.GONE);
        mSinProgramasContenedor.setVisibility(View.GONE);
        adapter =
            (index == 0) ?
                    new RankingAdapter(mActivePrograms, this)
                    :
                    new RankingAdapter(mHistoryPrograms, this);
        if(index == 0 && mActivePrograms.size() == 0)
            {
                mSinProgramasContenedor.setVisibility(View.VISIBLE);
            }
        else if(index == 1 && mHistoryPrograms.size() == 0)
            {
                mSinHistorialContenedor.setVisibility(View.VISIBLE);
            }
        else
            {
                mSinHistorialContenedor.setVisibility(View.GONE);
                mSinProgramasContenedor.setVisibility(View.GONE);
            }
        adapter.setOnItemClickListener(this);
        mRecycler.setAdapter(adapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * <p>Método que permite agregar un listener a cada elemento inidividual que aparece en la
     * sección "ACTIVOS" o "HISTORIAL" de RankingActivity.</p>
     */
    @Override
    public void onClickVerDetalleRanking(int idProgram, String nameProgram) {
        Intent intent = new Intent(this, DetalleRankingActivity.class);
        intent.putExtra("idProgram", idProgram);
        intent.putExtra("nameProgram", nameProgram);
        startActivity(intent);
    }

    private void setImage()
    {
        String fullPath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();

        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(getApplicationContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mProfilePicture);
        String fullPath2 = RequestManager.IMAGEN_circular_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getApplicationContext()).load(fullPath2).error(R.drawable.sin_imagen).into(mCountryFlag);
        mCountryname.setText(SharedPreferencesUtil.getInstance().getNombrePais());
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void onHideLoader() {
        try
        {
            if(mLoader != null)
            {
                mLoader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void onShowLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if(!mLoader.isAdded())
            {
                mLoader.show(fm, "Loader");
            }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRankingSuccess(RankingResponseData data) {
        mActive.setText(data.getRanking().getIncompletePrograms() + "");
        mFinished.setText(data.getRanking().getCompletedPrograms() + "");
        mEarnedPoint.setText(data.getRanking().getCorcholatas() + "");
        mMainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgramRankingSuccess(ProgramRankingResponseData data) {
        mActivePrograms.addAll(data.getProgramsRanking().getActivePrograms());
        mHistoryPrograms.addAll(data.getProgramsRanking().getHistoryPrograms());
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new RankingAdapter(mActivePrograms, this);
        adapter.setOnItemClickListener(this);
        mRecycler.setLayoutManager(LayoutManager);
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        changeTabData(0);
    }

    @Override
    public void onShowMessage(int messageType) {
        DialogManager.displaySnack(getSupportFragmentManager(), messageType);
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
    /**Cuando el token del usuario expire se mandará a llamar este método*/
    @Override
    public void onRankingNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * Método que se ejecuta cuando no hay conexión a internet, cerrando la actividad de mi ranking
     * */
    @Override
    public void onRankingNoInternet() {
        new CountDownTimer(2000, 1000)
        {
            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {

    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}

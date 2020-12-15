package com.femsa.sferea.home.miCuenta.miRanking.detalle;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.Ranking.RankingTierlistData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.miRanking.RankingInteractor;
import com.femsa.sferea.home.miCuenta.miRanking.RankingPresenter;
import com.femsa.sferea.home.miCuenta.miRanking.RankingView;
import com.femsa.sferea.home.miCuenta.miRanking.listado.DetalleRankingAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalleRankingActivity extends AppCompatActivity implements RankingView, DetalleRankingAdapter.OnItemClickListener{

    //Identificadores que permiten distinguir si un elemento es ACTIVO o HISTORIAL
    private static final int ACTIVOS = 1;
    private static final int HISTORIAL = 2;

    private Toolbar mToolbar;

    private CircleImageView mCivFotoPerfil;

    private TextView mTvTitulo;
    private TextView mTvClasificacion;
    private TextView mTvNombre;
    private ImageView mIvBanderaPais;
    private TextView mTvNombrePais;
    private TextView mTvNumeroCorcholatas;
    private TextView mTvNumeroLikes;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;
    private RecyclerView mRvTop;

    private int mIDProgram;

    private DetalleRankingAdapter mAdapter;

    private LinearLayoutManager mManager;

    private Loader mLoader;

    private ArrayList<RankingTierlistData> mData;

    private RankingPresenter mPresenter;

    private String mNameProgram = "-";

    private ConstraintLayout mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ranking);
        mIDProgram = getIntent().getIntExtra("idProgram", 0);
        mNameProgram = getIntent().getStringExtra("nameProgram");
        bindViews();
        initializeToolbar();
        initializeAdapter();
    }

    private void bindViews(){
        mToolbar = findViewById(R.id.activity_detalle_ranking_toolbar);
        mTvTitulo = findViewById(R.id.tv_activity_detalle_ranking_titulo);
            mTvTitulo.setText(mNameProgram);
        mCivFotoPerfil = findViewById(R.id.civ_activity_detalle_ranking_foto_perfil);
        mTvClasificacion = findViewById(R.id.tv_activity_detalle_ranking_clasificacion);
        mTvNombre = findViewById(R.id.tv_activity_detalle_ranking_nombre);
        mIvBanderaPais = findViewById(R.id.iv_activity_detalle_ranking_flag);
        mTvNombrePais = findViewById(R.id.tv_activity_detalle_ranking_pais);
        mTvNumeroCorcholatas = findViewById(R.id.tv_activity_detalle_ranking_numero_corcholatas);
        mTvNumeroLikes = findViewById(R.id.tv_activity_detalle_ranking_numero_likes);
        mRvTop = findViewById(R.id.rv_activity_detalle_ranking_top_10);
        mLoader = Loader.newInstance();
        mPresenter = new RankingPresenter(this, new RankingInteractor());
        mMainView = findViewById(R.id.ranking_tierlist_cl);
            mMainView.setVisibility(View.INVISIBLE);

        String fullPath = RequestManager.IMAGEN_circular_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mIvBanderaPais);
        mTvNombrePais.setText(SharedPreferencesUtil.getInstance().getNombrePais());
    }

    /**
     * <p>Método que permite configurar la toolbar de la Activity.</p>
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
     * <p>Método que inicializa el adaptador que muestra el top 10.</p>
     */
    private void initializeAdapter(){
        mData = new ArrayList<>();
        mListadoPaises = new ArrayList<>();
        mAdapter = new DetalleRankingAdapter(this, mData, mListadoPaises);
        mAdapter.setOnItemClickListener(this, mIDProgram);
        mManager = new LinearLayoutManager(this);
        mRvTop.setAdapter(mAdapter);
        mRvTop.setLayoutManager(mManager);
        mPresenter.OnInteractorCallRankingList(mIDProgram);
        mPresenter.onInteractorTraerPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onHideLoader() {
        mLoader.dismiss();
    }

    @Override
    public void onShowLoader() {
        FragmentManager fm = getSupportFragmentManager();
        mLoader.show(fm, "Loader");
    }


    @Override
    public void onRankingSuccess(RankingResponseData data) {

    }

    @Override
    public void onProgramRankingSuccess(ProgramRankingResponseData data) {

    }

    @Override
    public void onShowMessage(int messageType) {
        DialogManager.displaySnack(getSupportFragmentManager(), messageType);
    }

    @Override
    public void onShowMessage(int messageType, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), messageType, codigo);
    }

    @Override
    public void onRankingTierListSuccess(RankingTierlistResponseData data) {
        mData.clear();
        mData.addAll(data.getProgramsRanking().getTierlist());
        mAdapter.notifyDataSetChanged();
        mTvNombre.setText(SharedPreferencesUtil.getInstance().getNombreSP());
        String fullPath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();

        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(getApplicationContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mCivFotoPerfil);
        for(int i = 0; i < mData.size(); i++)
            {
                if(mData.get(i).getName().equals(SharedPreferencesUtil.getInstance().getNombreSP()))
                    {
                        mTvNumeroCorcholatas.setText(mData.get(i).getTotalBonus() + "");
                        mTvClasificacion.setText(mData.get(i).getPlacement() + "");
                        mTvNumeroLikes.setText(mData.get(i).getTotalLikes() + "");
                        break;
                    }
            }
        mMainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRankingLikesuccess() {
        mPresenter.OnInteractorCallRankingList(mIDProgram);
    }

    /**
     * Cuando el token del usuario expire se mandará a llamar este método
     */
    @Override
    public void onRankingNoAuth() {

    }

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
        mListadoPaises.addAll(data.getmPaises().getListadoPaises());
        mAdapter.setListadoPaises(mListadoPaises);
    }

    @Override
    public void onClickCallLike(int idProgram, int idEmployee) {
        mPresenter.OnInteractorCallLike(idProgram, idEmployee);
        mData.clear();
    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

}
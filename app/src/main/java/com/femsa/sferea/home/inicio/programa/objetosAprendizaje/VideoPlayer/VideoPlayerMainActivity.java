package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.FullScreenMediaController;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.TITULO_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.IS_OFFLINE_FILE_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.OFFLINE_FILE_KEY;

public class VideoPlayerMainActivity extends AppCompatActivity implements FullScreenMediaController.OnButtonMediaClick, VideoPlayerDetalleFragment.setDatatoMain{

    private ConstraintLayout mRootView;

    private int currentTime = 0;

    private int mIDObjetoAprendizaje;

    private String mRutaVideo;

    public static final String ID_OBJECT_KEY = "idObject";

    private String mNombrePrograma;

    private FullScreenVideoFragment fullvideoFragment;

    private VideoPlayerDetalleFragment videoFragment;

    //Banderas para el offline
    private boolean mIsOfflineFile = false;

    private ObjetoAprendizajeEntity mObjetoEntity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_container);
        collectData();
        bindViews();
    }

    private void collectData()
    {
        mIsOfflineFile = getIntent().getBooleanExtra(IS_OFFLINE_FILE_KEY, false);

        mObjetoEntity = (ObjetoAprendizajeEntity) getIntent().getSerializableExtra(OFFLINE_FILE_KEY);

        mIDObjetoAprendizaje = getIntent().getIntExtra(ID_OBJECT_KEY, 0);
    }

    private void bindViews()
    {
        mRootView = findViewById(R.id.activity_video_player_main_view);
        mNombrePrograma = getIntent().getStringExtra(TITULO_PROGRAMA_KEY);
        initDetalleFragment();
    }


    private void initDetalleFragment()
    {
        videoFragment = new VideoPlayerDetalleFragment();
        videoFragment.setNombrePrograma(mNombrePrograma);
        videoFragment.initData(currentTime, mIDObjetoAprendizaje, this, this);
        if(mObjetoEntity != null){
            videoFragment.setObjetoOffline(mObjetoEntity);
        }
        getSupportFragmentManager().beginTransaction()
                .detach(videoFragment)
                .attach(videoFragment)
                .replace(R.id.activity_video_player_main_view, videoFragment, "videoFragment")
                .commit();
    }

    private void initFullScreenFragment(String rutaVideo)
    {
        fullvideoFragment = new FullScreenVideoFragment();
        fullvideoFragment.initData(currentTime, rutaVideo, this);
        getSupportFragmentManager().beginTransaction()
                .detach(fullvideoFragment)
                .attach(fullvideoFragment)
                .replace(R.id.activity_video_player_main_view, fullvideoFragment, "fullvideoFragment")
                .commit();
    }

    @Override
    public void OnButtonMediaClick(int timestamp, String rutaVideo, boolean isPantallaCompleta) {
        currentTime = timestamp;
        if(isPantallaCompleta)
        {
            initDetalleFragment();
        }
        else
        {
            initFullScreenFragment(rutaVideo);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setRutaVideo(String ruta) {
        mRutaVideo = ruta;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            currentTime = videoFragment.getVideoTime();
            initFullScreenFragment(mRutaVideo);
        }
        else
            {
                currentTime = fullvideoFragment.getVideoTime();
                initDetalleFragment();
            }
    }
}

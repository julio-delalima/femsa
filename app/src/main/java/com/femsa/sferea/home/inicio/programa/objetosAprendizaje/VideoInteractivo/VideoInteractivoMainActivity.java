package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.FullScreenMediaController;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer.FullScreenVideoFragment;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer.VideoPlayerDetalleFragment;

public class VideoInteractivoMainActivity extends AppCompatActivity implements FullScreenMediaController.OnButtonMediaClick, VideoPlayerDetalleFragment.setDatatoMain, VideoInteractivoSuperDetalle.OnVideoInteractivoData{

    private ConstraintLayout mRootView;

    private int currentTime = 0;

    private int mIDObjetoAprendizaje;

    private String mRutaVideo;

    public static final String ID_OBJECT_KEY = "idObject";

    public int mVideoIndex = 0, mTotalPreguntas = 0, mTotalCorrectas = 0;

    boolean mRecienAbierto = true;

    private FullScreenVideoFragment fullvideoFragment;

    private VideoInteractivoSuperDetalle videoFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_container);
        collectData();
        bindViews();
    }

    private void collectData()
    {
        mIDObjetoAprendizaje = getIntent().getIntExtra(ID_OBJECT_KEY, 0);
    }

    private void bindViews()
    {
        mRootView = findViewById(R.id.activity_video_player_main_view);
        initDetalleFragment();
    }


    private void initDetalleFragment()
    {
        videoFragment = new VideoInteractivoSuperDetalle();
        videoFragment.setListener(this);
        videoFragment.retrieveData(mVideoIndex, mTotalPreguntas, mTotalCorrectas, mRecienAbierto);
        videoFragment.initData(currentTime, mIDObjetoAprendizaje, this, this);
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

    @Override
    public void mVideoData(int mVideoIndex, int totalPreguntas, int totalCorrectas, boolean recienAbierto) {
        this.mVideoIndex = mVideoIndex;
        mTotalPreguntas = totalPreguntas;
        mTotalCorrectas = totalCorrectas;
        mRecienAbierto = recienAbierto;
    }
}

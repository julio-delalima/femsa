package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.FullScreenMediaController;

public class FullScreenVideoFragment extends Fragment {

        private VideoView videoView;
        private MediaController mediaController;
        private String mVideoPath = "";
        private int mVideoTimestamp;
        private View mView;
        private ProgressBar mLoader;
        private FullScreenMediaController.OnButtonMediaClick mTempListener;


        public void initData(int currentTime, String rutaVideo, FullScreenMediaController.OnButtonMediaClick listener)
        {
            mVideoTimestamp = currentTime;
            mTempListener = listener;
            mVideoPath = rutaVideo;
        }

        public FullScreenVideoFragment newInstance() {
            Bundle args = new Bundle();
            FullScreenVideoFragment fragment = new FullScreenVideoFragment();
            fragment.setArguments(args);
            return fragment;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fullscreen_videoview, container, false);
        videoView = mView.findViewById(R.id.videoView);
        mLoader = mView.findViewById(R.id.fullscreen_loader_carga);
        addVideo();
        return mView;
    }

    private void addVideo()
    {
        Uri videoUri = Uri.parse(mVideoPath);

        videoView.setVideoURI(videoUri);

        setVideoMedia();

        videoView.start();
        videoView.seekTo(mVideoTimestamp);
    }

    private void setVideoMedia()
    {
        mediaController = new FullScreenMediaController(getContext());
        ((FullScreenMediaController) mediaController).setmVideo(videoView);
        mediaController.setAnchorView(videoView);
        ((FullScreenMediaController) mediaController).setRutaVideo(mVideoPath);
        ((FullScreenMediaController) mediaController).isPantallaCompleta(true);
        ((FullScreenMediaController) mediaController).setListener(mTempListener);
        videoView.setMediaController(mediaController);
        /**
         * Método de VideoPlayer que desaparecerá el Loader cuando el video inicie la reproducción.
         * */
        videoView.setOnPreparedListener(mp -> {
            if (mLoader != null){
                mLoader.setVisibility(View.GONE);
                videoView.start();
            }
        });
    }

    public int getVideoTime()
    {
        return ((FullScreenMediaController) mediaController).getVideoTime();
    }

    @Override
    public void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }
}

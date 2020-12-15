package com.femsa.sferea.Utilities;

import android.content.Context;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.femsa.sferea.R;

public class FullScreenMediaController extends MediaController {

    private ImageButton fullScreen;
    private String mRutaVideo = "";
    private VideoView mVideo;
    private Boolean mPantallaCompleta = false;
    private OnButtonMediaClick mListener;

    public interface OnButtonMediaClick
    {
        void OnButtonMediaClick(int timeStamp, String rutaVideo, boolean isPantallaCompleta);
    }

    public void setListener(OnButtonMediaClick listener)
    {
        mListener = listener;
    }

    public FullScreenMediaController(Context context) {
        super(context);
    }

    public void setRutaVideo(String ruta)
    {
        mRutaVideo = ruta;
    }

    public void isPantallaCompleta(boolean estado)
    {
        mPantallaCompleta = estado;
    }

    @Override
    public void setAnchorView(View view) {

        super.setAnchorView(view);

        //image button for full screen to be added to media controller
        fullScreen = new ImageButton (super.getContext());
        if(mPantallaCompleta) //si está en modo pantalla completa
            {
                fullScreen.setImageDrawable(getResources().getDrawable(R.mipmap.ic_shrink_screen));
            }
        else
            {
                fullScreen.setImageDrawable(getResources().getDrawable(R.mipmap.ic_full_screen));
            }
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.rightMargin = 40;
        fullScreen.setBackground(null);
        fullScreen.setScaleX(0.7f);
        fullScreen.setScaleY(0.7f);
        addView(fullScreen, params);

        fullScreen.setOnClickListener(v -> {
            if (Settings.System.getInt(AppTalentoRHApplication.getApplication().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 1) //1 == enabled
                {
                    Toast.makeText(getContext(), "Por favor activa la rotación automática en tu dispositivo", Toast.LENGTH_SHORT).show();
                }
            else
                {
                    mListener.OnButtonMediaClick(mVideo.getCurrentPosition(), mRutaVideo, mPantallaCompleta);
                }

        });

    }

    public void setmVideo(VideoView video)
    {
        mVideo = video;
    }

    public int getVideoTime()
    {
        return mVideo.getCurrentPosition();
    }



}
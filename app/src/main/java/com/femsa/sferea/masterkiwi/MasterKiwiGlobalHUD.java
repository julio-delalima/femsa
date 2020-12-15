package com.femsa.sferea.masterkiwi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.femsa.sferea.R;

/**
 * <p>Clase que permite dibujar arriba de la pantalla de SDL.</p>
 */
public class MasterKiwiGlobalHUD extends View implements MasterKiwiListener
{
    /**
     * <p>Boton para activar y desactivar el audio del juego</p>
     */
    ImageButton audioButton;
    ImageView backgroundHelper;
    /**
     * <p>Constructor base</p>
     *
     * @param context Contexto en el cuál se crearán los componentes a dibujar sobre los juegos.
     */
    public MasterKiwiGlobalHUD(Context context)
        {
        super(context);
        audioButton = new ImageButton(context);
        audioButton.setBackgroundColor(Color.TRANSPARENT);
        audioButton.setImageResource(R.drawable.mkaudio);
        audioButton.setVisibility(GONE);
        audioButton.setOnClickListener(new View.OnClickListener()
            {
            public void onClick(View view)
                {
                MasterKiwiWrapper.GetInstance().changeAudioState();
                if(MasterKiwiWrapper.GetInstance().isAudioActivated())
                    {
                    audioButton.setImageResource(R.drawable.mkaudio);
                    }
                else
                    {
                    audioButton.setImageResource(R.drawable.mksinaudio);
                    }
                }
            });
        backgroundHelper = new ImageView(context);
//        File image = new File(MasterKiwiWrapper.GetInstance().loadingBackgroundPath);
       /* if(image.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (bitmap != null)
            {
                backgroundHelper.setImageBitmap(bitmap);
            }
        }*/
        MasterKiwiWrapper.GetInstance().registerForEvents(this);
        }

    /**
     * <p>Función que manda a llamar la creación del los elementos.</p>
     */
    public void createHUD()
        {
        ViewGroup parent = ((ViewGroup) this.getParent());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayMetrics.widthPixels, displayMetrics.widthPixels);
        audioButton.setLayoutParams(params);
        audioButton.setScaleX(0.13f);
        audioButton.setScaleY(0.13f);
        if(MasterKiwiWrapper.GetInstance().gameType == 1307)
            {
            audioButton.setRotation(90);
            audioButton.setTranslationX(displayMetrics.widthPixels * 0.39f);
            audioButton.setTranslationY((displayMetrics.heightPixels * 0.92f) - (displayMetrics.widthPixels * 0.5f));
            }
        else
            {
            audioButton.setTranslationX(displayMetrics.widthPixels * 0.4f);
            audioButton.setTranslationY(-(displayMetrics.widthPixels * 0.42f));
            }
        backgroundHelper.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels,displayMetrics.heightPixels));
        parent.addView(backgroundHelper);
        parent.addView(audioButton);
        }
    /**
     * <p>Metodo que se manda a llamar cada que se dibujan los elementos.</p>
     *
     * @param canvas Canvas controlado por el sistema.
     */
    @Override
    protected void onDraw(Canvas canvas)
        {
        if(MasterKiwiWrapper.GetInstance().isAudioActivated())
            {
            audioButton.setImageResource(R.drawable.mkaudio);
            }
        else
            {
            audioButton.setImageResource(R.drawable.mksinaudio);
            }
        }

    @Override
    public void onLogin(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onLogout(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onNetError(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onInitiatingCompresure(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onInitiatingTextures(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onInitiatingAsstesList(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onInitiatingDownload(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onDownloadFinished(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onAssetsDownload(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onSatrtingGame(Object sender, MasterKiwiArgs eventArgs)
    {
        MasterKiwiWrapper.GetInstance().unResgisterForEvents(this);
        MasterKiwiWrapper.GetInstance().currentActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                backgroundHelper.setVisibility(GONE);
            }
        });
    }
    @Override
    public void onGameOver(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onExitGame(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onNoneUserLogged(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void onNoGameData(Object sender, MasterKiwiArgs eventArgs) {}
    @Override
    public void jsonSQData(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void mandarDatos(Object sender, MasterKiwiArgs eventArgs) {

    }
}
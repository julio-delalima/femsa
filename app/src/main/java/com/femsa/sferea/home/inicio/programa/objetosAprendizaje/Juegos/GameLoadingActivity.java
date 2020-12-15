package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AnimationManager;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.UtilsMK.dialog.NoInternetDialog;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.DataGusanosEscaleras;
import com.femsa.sferea.masterkiwi.MasterKiwiArgs;
import com.femsa.sferea.masterkiwi.MasterKiwiListener;
import com.femsa.sferea.masterkiwi.MasterKiwiWrapper;

import java.io.File;
import java.util.Locale;

import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.IS_MULTIPLAYER_GAME;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.MULTIPLAYER_GAME_DATA;

public class GameLoadingActivity extends AppCompatActivity implements MasterKiwiListener,DialogInterface.OnDismissListener
{
    public static final String RECEIVING_GAME_ID = "selectedGameID";
    public static final String RECEIVING_GAME_TYPE = "selectedGameType";
    public static final String RECEIVING_GAME_NAME = "selectedGameName";
    public static final String RECEIVING_GAME_IMAGE = "selectedGameImage";
    public static final String SQUIRREL_JSON = "SQJson";
    public static final String RECEIVING_GAME_PATH = "gamePath";

    protected boolean isWorking;
    protected MasterKiwiWrapper wrapper;
    protected AnimationManager animManager;

    protected int loadingGameID;
    protected int loadingGameType;
    protected String gameName;
    protected String gameImagePath;
    protected String mGamePath;

    private int toDownload;
    private ImageView backgroundImage;
    private TextView loadingProgress;
    private ImageView loadingImage;
    private AppCompatActivity mActivity;

    private String jsonToSquirrel;

    private boolean isMultiplayer = false;

    private DataGusanosEscaleras mDataJuego;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        onInitMasterKiwi();
        getData();
        //MasterKiwiInitialization();
        //ViewInitialization();
        MasterKiwiInitialization();
    }

    private void getData(){
        isMultiplayer = getIntent().getBooleanExtra(IS_MULTIPLAYER_GAME, false);
        if(isMultiplayer){
            mDataJuego = (DataGusanosEscaleras) getIntent().getSerializableExtra(MULTIPLAYER_GAME_DATA);
        }

    }

    private void onInitMasterKiwi(){
        loadingGameID = getIntent().getIntExtra(RECEIVING_GAME_ID,0);
        loadingGameType = getIntent().getIntExtra(RECEIVING_GAME_TYPE,0);
        gameName = getIntent().getStringExtra(RECEIVING_GAME_NAME);
        mGamePath = getIntent().getStringExtra(RECEIVING_GAME_PATH);
        LogManager.d("MasterKiwisito", loadingGameID + " " + loadingGameType + " " + mGamePath);
        setContentView(R.layout.game_loading_activity);
        getResourcesFromXML();
        gameImagePath = getIntent().getStringExtra(RECEIVING_GAME_IMAGE);
        //setBackground();
    }

    protected void ViewInitialization()
    {
        loadingGameID = getIntent().getIntExtra(RECEIVING_GAME_ID,0);
        loadingGameType = getIntent().getIntExtra(RECEIVING_GAME_TYPE,0);
        gameName = getIntent().getStringExtra(RECEIVING_GAME_NAME);
        gameImagePath = getIntent().getStringExtra(RECEIVING_GAME_IMAGE);
        LogManager.d("MasterKiwisito", "Imagen " + gameImagePath);
        jsonToSquirrel = getIntent().getStringExtra(SQUIRREL_JSON);
        animManager = new AnimationManager();
        setContentView(R.layout.game_loading_activity);
        getResourcesFromXML();
        //adjustSizes();
       // positionateObjects();
       // setBackground();
        ((AnimationDrawable) loadingImage.getDrawable()).start();

    }

    protected void getResourcesFromXML()
    {
        loadingProgress = findViewById(R.id.game_loading_text);
        loadingImage = findViewById(R.id.game_loading_animation);
        backgroundImage = findViewById(R.id.background);
    }

    protected void adjustSizes()
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        loadingProgress.setLayoutParams(new ConstraintLayout.LayoutParams(displayMetrics.widthPixels,(int)(displayMetrics.heightPixels * 0.15f)));
        loadingImage.setLayoutParams(new ConstraintLayout.LayoutParams((int)(displayMetrics.widthPixels * 0.5f),(int)(displayMetrics.heightPixels * 0.2f)));
    }

    protected void positionateObjects()
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        animManager.getNewTranslateAnimForView(loadingProgress,0,displayMetrics.heightPixels * 0.79f,0,0);
        animManager.getNewTranslateAnimForView(loadingImage,displayMetrics.widthPixels * 0.25f,displayMetrics.heightPixels * 0.7f,0,0);
    }

    protected void setBackground()
    {
        File image = new File(gameImagePath);
        if(image.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (bitmap != null)
            {
                backgroundImage.setImageBitmap(bitmap);
            } 
        }
    }

    protected void MasterKiwiInitialization()
    {
        mActivity = this;
        isWorking = false;
        loadingProgress = findViewById(R.id.game_loading_text);
        wrapper = MasterKiwiWrapper.GetInstance();
        wrapper.setActivity(this);
        wrapper.registerForEvents(this);
        if(isMultiplayer){
            wrapper.setDataMultijugador(mDataJuego);
        }
        LogManager.d("MasterKiwisito", "A punto del startGame()");
        startGame();
        //startearJuego();
    }

    private void startearJuego(){
        runOnUiThread(() -> {
            if(!isWorking)
            {
                LogManager.d("MasterKiwisito", "start");

                        wrapper.setGameType(loadingGameType,gameName);
                        wrapper.loadingBackgroundPath = gameImagePath;
                        wrapper.isAppOnBackground = false;
                        wrapper.startGame(loadingGameID, false);
                        isWorking = true;

            }
        });
    }


    protected void startGame()
    {
        LogManager.d("MasterKiwisito", "startGame()");
        runOnUiThread(() -> {
            LogManager.d("MasterKiwisito", isWorking + "");
            if(!isWorking)
            {
                wrapper.setGameType(loadingGameType,gameName);
                wrapper.loadingBackgroundPath = gameImagePath;
               /* if(WebUtil.isNetworkAvailable(getApplicationContext()))
                {
                    LogManager.d("MasterKiwisito", "Web Util");
                    wrapper.askDataDate(loadingGameID);
                    wrapper.jsonSQData(jsonToSquirrel);
                    isWorking = true;
                }
                else*/
                    if(wrapper.hasGameData(loadingGameID))
                {
                    LogManager.d("MasterKiwisito", "hay game data");
                    wrapper.isAppOnBackground = false;
                    wrapper.startGame(loadingGameID, false);
                    isWorking = true;
                }
                else if(new File(mGamePath+"/interface/data/"+loadingGameID+"_m_a.txt").exists()){
                        LogManager.d("MasterKiwisito", "hay game datita en " + mGamePath);
                        wrapper.isAppOnBackground = false;
                        wrapper.startGame(loadingGameID, false);
                        isWorking = true;
                    }
                else
                {
                    NoInternetDialog dialog = NoInternetDialog.newInstance("", "Por favor, verifica tu conexión a internet");
                    dialog.setOnDismissListener(GameLoadingActivity.this);
                    dialog.setCancelable(false);
                    getSupportFragmentManager().beginTransaction().add(dialog, "VoteDialog").commitAllowingStateLoss();
                }
            }
        });
    }



    @Override
    public void onResume()
    {
        super.onResume();
        if(wrapper != null)
            wrapper.isAppOnBackground = false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(wrapper != null)
            wrapper.isAppOnBackground = true;
    }

    @Override
    public void onBackPressed()
    {
        try{
            super.onBackPressed();
            if(wrapper != null)
            {
                wrapper.networkManager.cancelDownloads();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("backJuego", 1);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

        }
        catch (Exception ignored){}
        finish();
    }

    @Override
    public void onLogin(Object sender, final MasterKiwiArgs eventArgs)
    {
        startGame();
    }

    @Override
    public void onLogout(Object sender, MasterKiwiArgs eventArgs)
    {}

    @Override
    public void onNetError(Object sender, MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            mActivity.runOnUiThread(() -> {
                //Toast.makeText(mActivity, "Error en la red", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        isWorking = false;
    }

    @Override
    public void onInitiatingCompresure(Object sender, MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            mActivity.runOnUiThread(() -> loadingProgress.setText("Esperando al servidor"));
        }
    }

    @Override
    public void onInitiatingTextures(Object sender, MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            mActivity.runOnUiThread(() -> loadingProgress.setText("Creando texturas"));
        }
    }

    @Override
    public void onInitiatingAsstesList(Object sender, MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            mActivity.runOnUiThread(() -> loadingProgress.setText("Inicializando lista de recursos"));
        }
    }

    @Override
    public void onInitiatingDownload(Object sender, MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            toDownload = eventArgs.assetsToDownload;
            mActivity.runOnUiThread(() -> loadingProgress.setText("Iniciando descarga"));
        }
    }

    @Override
    public void onDownloadFinished(Object sender, MasterKiwiArgs eventArgs)
    {
        isWorking = false;
    }

    @Override
    public void onAssetsDownload(Object sender, final MasterKiwiArgs eventArgs)
    {
        if(mActivity != null && !mActivity.isFinishing())
        {
            mActivity.runOnUiThread(() -> {
                float toDisplay = 100 - (eventArgs.remainigAssets * (toDownload * 0.01f));
                loadingProgress.setText(String.format(Locale.getDefault(),
                        "Descargando: %,.0f%%", toDisplay));
            });
        }
    }

    @Override
    public void onSatrtingGame(Object sender, MasterKiwiArgs eventArgs)
    {
        finish();
    }

    @Override
    public void onGameOver(Object sender, MasterKiwiArgs eventArgs)
    {
        isWorking = false;
        finish();
    }

    @Override
    public void onExitGame(Object sender, MasterKiwiArgs eventArgs)
    {
        isWorking = false;
        finish();
    }

    @Override
    public void onNoneUserLogged(Object sender, MasterKiwiArgs eventArgs)
    {
        finish();
    }

    @Override
    public void onNoGameData(Object sender, MasterKiwiArgs eventArgs)
    {
        finish();
    }

    @Override
    public void onDestroy()
    {
        if(wrapper != null)
            wrapper.unResgisterForEvents(this);
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        finish();
    }

    @Override
    public void jsonSQData(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void mandarDatos(Object sender, MasterKiwiArgs eventArgs) {

    }
}


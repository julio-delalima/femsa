package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.GIFPlayer;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.ConnectionState;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargaArchivoAsync;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargasParams;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.InsertObjetoAsyncTask;
import com.femsa.sferea.home.descargas.base.DBManager;

import java.io.File;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.TITULO_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.IS_OFFLINE_FILE_KEY;


public class FullGIFActivity extends AppCompatActivity implements DescargaArchivoAsync.OnStatusDescarga, InsertObjetoAsyncTask.OnBDStatus{

    private ImageView mGIFContainer;

    private ImageView mDownloadButton;

    private String mGIFPath = "";

    private boolean mDescargable;

    private ProgressBar mLoader;

    private DBManager.ObjetoAprendizaje mBD;
    private String mGIFFilename, mImageName, mIMAGEPath, mRutaImagen, mRutaArchivo, mNombrePrograma;
    private Loader loader;
    private ObjectDetailResponseData mDataDetalle;
    private boolean mIsOffline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_full_viewer);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mGIFPath = extras.getString(RequestManager.ARCHIVO_URL_KEY);
            mDescargable = extras.getBoolean(GIFActivity.DESCARGA_HABILITADA);
            mDataDetalle = (ObjectDetailResponseData) extras.getSerializable("detalle");
            mNombrePrograma = extras.getString(TITULO_PROGRAMA_KEY);
            mIsOffline = extras.getBoolean(IS_OFFLINE_FILE_KEY);
            if(mDataDetalle != null){
                mGIFFilename = mDataDetalle.getCategories().getFilename();
                mIMAGEPath = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + mDataDetalle.getCategories().getmImageObject();
                mImageName = mDataDetalle.getCategories().getmImageObject();
            }
            bindComponents();
        }
    }

    private void bindComponents()
    {
        mGIFContainer = findViewById(R.id.full_gif_container);
        mDownloadButton = findViewById(R.id.gif_download_button);
            mDownloadButton.setOnClickListener(v -> descargarObjeto());
            mDownloadButton.setVisibility((mDescargable) ? View.VISIBLE : View.INVISIBLE);
        loader = Loader.newInstance();
        Toolbar toolbar = findViewById(R.id.gif_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mLoader = findViewById(R.id.gif_full_loader);
        mBD = new DBManager.ObjetoAprendizaje(this);
        if(mIsOffline) {
            addGIFOffline();
        }
        else
            {
                addGIF();
            }

    }

    private void addGIF()
    {
        Uri uri = Uri.parse(mGIFPath);//"android.resource://" + getApplication().getPackageName() + "/" + R.raw.escaner);
        GlideApp.with(getApplicationContext()).asGif().listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                mLoader.setVisibility(View.GONE);
                return false;
            }
        }).load(uri).error(R.drawable.sin_imagen).into(mGIFContainer);
    }

    private void addGIFOffline()
    {
        File uri = new File(mGIFPath);
        GlideApp.with(getApplicationContext()).asGif().listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                mLoader.setVisibility(View.GONE);
                return false;
            }
        }).load(uri).error(R.drawable.sin_imagen).into(mGIFContainer);
    }

    private void descargarObjeto(){
        if(ConnectionState.getTipoConexion(this) != ConnectionState.NO_NETWORK)
        {
            DescargaArchivoAsync dlw = new DescargaArchivoAsync();
            dlw.setListener(this);
            dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName, mIMAGEPath));
        }
        else{
            DialogManager.displaySnack(getSupportFragmentManager(), StringManager.NO_INTERNET);
        }
    }

    @Override
    public void onDownloadBegin() {
        showLoader();
    }

    @Override
    public void onDownloadEnd(String uri, int tipo) {
        if(tipo == DescargaArchivoAsync.IMAGEN)
        {
            mRutaImagen = uri;
            DescargaArchivoAsync filedlw = new DescargaArchivoAsync();
            filedlw.setListener(this);
            filedlw.execute(new DescargasParams(DescargaArchivoAsync.ARCHIVO, mGIFFilename, mGIFPath));
        }
        else{
            mRutaArchivo = uri;
            InsertObjetoAsyncTask baseDBAsync = new InsertObjetoAsyncTask();
            baseDBAsync.setListener(this);
            baseDBAsync.setData(mDataDetalle);
            baseDBAsync.setRutas(mRutaArchivo, mRutaImagen, mNombrePrograma);
            baseDBAsync.execute(mBD);
        }
    }

    @Override
    public void onDownloadError(String error) {
        hideLoader();
        DialogManager.displaySnack(getSupportFragmentManager(), error);
    }

    @Override
    public void OnInicioQuery() {
    }

    @Override
    public void OnFinQuery() {
        hideLoader();
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.descarga_exitosa));
    }

    @Override
    public void OnErrorQuery(String error) {
        DialogManager.displaySnack(getSupportFragmentManager(), error);
    }

    private void showLoader(){
        try{
            FragmentManager fm = getSupportFragmentManager();
            if(!loader.isAdded())
            {
                loader.show(fm, "Loader");
            }
        }
        catch (Exception ignored){}
    }

    private void hideLoader(){
        try
        {
            if(loader != null && loader.isAdded())
            {
                loader.dismiss();
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

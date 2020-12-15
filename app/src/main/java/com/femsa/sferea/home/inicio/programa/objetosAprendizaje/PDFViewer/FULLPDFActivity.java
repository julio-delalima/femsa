package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.PDFViewer;

import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.ConnectionState;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargaArchivoAsync;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargasParams;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.InsertObjetoAsyncTask;
import com.femsa.sferea.home.descargas.base.DBManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.GIFPlayer.GIFActivity;
import com.github.barteksc.pdfviewer.PDFView;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.TITULO_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.IS_OFFLINE_FILE_KEY;

public class FULLPDFActivity extends AppCompatActivity implements DescargaArchivoAsync.OnStatusDescarga, InsertObjetoAsyncTask.OnBDStatus {

    private PDFView mFullPDFView;
    private String mPDFPath = "";
    private Boolean isDownloadable = false;
    private ImageView mDownloadButton;
    private ProgressBar mLoader;
    private DBManager.ObjetoAprendizaje mBD;
    private String mPDFFilename, mImageName, mFullPath, mRutaImagen, mRutaArchivo, mNombrePrograma;
    private Loader loader;
    private ObjectDetailResponseData mDataDetalle;

    private boolean mIsOffline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdffull_viewer);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mPDFPath = extras.getString(RequestManager.ARCHIVO_URL_KEY);
            isDownloadable = extras.getBoolean(GIFActivity.DESCARGA_HABILITADA);
            mIsOffline = extras.getBoolean(IS_OFFLINE_FILE_KEY);
            mDataDetalle = (ObjectDetailResponseData) extras.getSerializable("detalle");
            mNombrePrograma = extras.getString(TITULO_PROGRAMA_KEY);
            if(mDataDetalle != null){
                mPDFFilename = mDataDetalle.getCategories().getFilename();
                mFullPath = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + mDataDetalle.getCategories().getmImageObject();
                mImageName = mDataDetalle.getCategories().getmImageObject();
            }
            bindComponents();
        }

    }

    private void bindComponents()
    {
        mLoader = findViewById(R.id.pdf_fullview_loader);
        mFullPDFView = findViewById(R.id.pdf_view);
        mDownloadButton = findViewById(R.id.pdf_full_download_button);
            mDownloadButton.setOnClickListener(v->descargarObjeto());
            mDownloadButton.setVisibility((isDownloadable) ? View.VISIBLE : View.GONE);
        loader = Loader.newInstance();
        Toolbar toolbar = findViewById(R.id.pdf_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mBD = new DBManager.ObjetoAprendizaje(this);
        if(mIsOffline){
            mFullPDFView.fromFile(new File(mPDFPath)).pageSnap(true).autoSpacing(true)
                    .onLoad(nbPages -> mLoader.setVisibility(View.GONE)).pageFling(true).load();
        }
        else{
            addPDF();
        }
    }

    private void addPDF()
    {
        new RetrivePDFStream().execute(mPDFPath);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void descargarObjeto(){
        if(ConnectionState.getTipoConexion(this) != ConnectionState.NO_NETWORK)
        {
            DescargaArchivoAsync dlw = new DescargaArchivoAsync();
            dlw.setListener(this);
            dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mFullPath));
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
            filedlw.execute(new DescargasParams(DescargaArchivoAsync.ARCHIVO, mPDFFilename,mPDFPath));
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

    public class RetrivePDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                if (urlConnection instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsConn = (HttpsURLConnection) urlConnection;
                    httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                    httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                }
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            mFullPDFView.fromStream(inputStream).pageSnap(true).autoSpacing(true)
                    .onLoad(nbPages -> mLoader.setVisibility(View.GONE)).pageFling(true).load();
        }
    }
}

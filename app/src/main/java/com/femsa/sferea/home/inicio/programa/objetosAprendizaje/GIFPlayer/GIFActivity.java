package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.GIFPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.ConnectionState;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargaArchivoAsync;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.DescargasParams;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.InsertObjetoAsyncTask;
import com.femsa.sferea.home.descargas.base.DBManager;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.LikeDialog;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;

import java.io.File;
import java.io.InputStream;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.TITULO_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.IS_OFFLINE_FILE_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.OFFLINE_FILE_KEY;

public class GIFActivity extends AppCompatActivity implements LearningObjectView, DescargaArchivoAsync.OnStatusDescarga, InsertObjetoAsyncTask.OnBDStatus {

    private ImageView mGIFContainer, mDownloadButton;

    private WebView mDescription;

    private Loader loader;

    private static final String ID_OBJECT_KEY = "idObject";

    private int mIDObject;

    private LearningObjectsPresenter mPresenter;

    private TextView mTitle, mEstimatedTime, mCorcholatas;

    private ImageView mLike;

    private boolean mIsLiked = false, isDownload = false;

    private String mGIFFullPath, mGIFFilename, mImageName, mFullPath, mRutaImagenbd;

    private ScrollView mGIFRootView;

    private ImageView mMaskasReadImg;

    private Button MaskAsReadButton;

    //Para la BD
    private String mRutaArchivo, mRutaImagen, mNombrePrograma;

    private DBManager.ObjetoAprendizaje mBD;

    private ObjectDetailResponseData mDataDetalle;

    public static final String DESCARGA_HABILITADA = "descargable";

    //Banderas para el offline
    private boolean mIsOfflineFile = false;

    private ObjetoAprendizajeEntity mObjetoEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_view_player);
        bindData();
        bindViews();
    }


    private void bindData()
    {
        mIsOfflineFile = getIntent().getBooleanExtra(IS_OFFLINE_FILE_KEY, false);

        mObjetoEntity = (ObjetoAprendizajeEntity) getIntent().getSerializableExtra(OFFLINE_FILE_KEY);

        mIDObject = getIntent().getIntExtra(ID_OBJECT_KEY, 0);

        mPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());

        loader = Loader.newInstance();

        mNombrePrograma = getIntent().getStringExtra(TITULO_PROGRAMA_KEY);

        mBD = new DBManager.ObjetoAprendizaje(this);
    }

    private void bindViews(){

        mGIFContainer = findViewById(R.id.gif_container);
            mGIFContainer.setScaleType(ImageView.ScaleType.FIT_XY);

        ConstraintLayout allContainer = findViewById(R.id.gif_constraint);

        Toolbar toolbar = findViewById(R.id.pdf_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ImageView beginButton = findViewById(R.id.pdf_begin_button);
        beginButton.setOnClickListener(v -> {
            onBeginPressed();
        });

        mDescription = findViewById(R.id.gif_description);

        mDownloadButton = findViewById(R.id.gif_download_button);
            mDownloadButton.setOnClickListener(v -> descargarObjeto());

        mTitle = findViewById(R.id.gif_title_tv);

        mEstimatedTime = findViewById(R.id.gif_stimated_time_tv);

        mCorcholatas = findViewById(R.id.pdf_corcholata_counter);

        mLike = findViewById(R.id.gif_like_iv);
            mLike.setOnClickListener(v -> OnLikePressed());

        mMaskasReadImg = findViewById(R.id.mark_as_read_image_objeto);
        MaskAsReadButton = findViewById(R.id.mark_as_read_objeto_button);
            MaskAsReadButton.setOnClickListener(v-> mPresenter.OnInteractorCallBonus(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario()));

        mGIFRootView = findViewById(R.id.gif_root_view);
            mGIFRootView.setVisibility(View.INVISIBLE);
    }

    private void configuraOffline(){
        mGIFRootView.setVisibility(View.VISIBLE);
        mTitle.setText(mObjetoEntity.getNombreObjeto());
        String minutosEstimados;
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + mObjetoEntity.getTiempoEstimado();
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(String.valueOf(mObjetoEntity.getCorcholatas()));
        if(mObjetoEntity.isLike())
        {
            mLike.setImageDrawable(getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(mObjetoEntity.getDescripcion());
        mDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        GlideApp.with(this)
                .load(new File(mObjetoEntity.getRutaImagen()))
                .centerCrop().placeholder(R.drawable.sin_imagen)
                .error(R.drawable.sin_imagen)
                .into(mGIFContainer);

        mDownloadButton.setVisibility(View.GONE);
        mMaskasReadImg.setVisibility(View.INVISIBLE);
        MaskAsReadButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        if(!mIsOfflineFile){
            mLike.animate().scaleX(0.2f);
            mLike.animate().scaleY(0.2f);
            mPresenter.OnInteractorLike(mIDObject);
        }
    }

    private void onBeginPressed(){
        if(mIsOfflineFile){
            Intent sendTo = new Intent(this, FullGIFActivity.class);
            sendTo.putExtra(RequestManager.ARCHIVO_URL_KEY, mObjetoEntity.getRutaArchivo());
            sendTo.putExtra(GIFActivity.DESCARGA_HABILITADA, false);
            sendTo.putExtra(IS_OFFLINE_FILE_KEY, true);
            startActivity(sendTo);
        }
        else{
            mPresenter.OnInteractorMarkAsRead(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario());
            Intent semdTo = new Intent(getApplicationContext(), FullGIFActivity.class);
            semdTo.putExtra(RequestManager.ARCHIVO_URL_KEY, mFullPath);
            semdTo.putExtra(DESCARGA_HABILITADA,isDownload);
            semdTo.putExtra("detalle", mDataDetalle);
            semdTo.putExtra(TITULO_PROGRAMA_KEY, mNombrePrograma);
            startActivity(semdTo);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void likeSuccess() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLike.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLike.animate().scaleY(0.7f);
        mLike.animate().scaleX(0.7f);
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTitle.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }

    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {

        mTitle.setText(data.getCategories().getmObjectName());
        String minutosEstimados;
        /**
         * Funcion para extraer el tiempo deseado de un String de la forma hh:mm:ss
         * @param  time el String de entrada en formato hh:mm:ss
         * @param returnType el valor de retorno que queremos, 0 la hora, 1 minutos y 2 los segundos
         * */
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + data.getCategories().getmEstimatedTime();//minutosEstimados = getResources().getString(R.string.estimated_time) + " " + GlobalActions.getTimeFromstring(data.getCategories().getmEstimatedTime(), 1) + " " + getResources().getString(R.string.minutos_min);
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(data.getCategories().getmBonus() + "");
        if(data.getCategories().getmLike() == 1)
        {
            mLike.setImageDrawable(getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(data.getCategories().getmDetailContent());
        mDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        mGIFFullPath =  RequestManager.ARCHIVO_PROGRAMA_URL + data.getCategories().getFilename();
        mGIFFilename = data.getCategories().getFilename();
        //Picasso.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mPDFView);
        mImageName = data.getCategories().getmImageObject();
        mFullPath = RequestManager.ARCHIVO_PROGRAMA_URL + data.getCategories().getFilename();
        mRutaImagenbd = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject();

        //Picasso.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mGIFContainer);

        GlideApp.with(this)
                .asDrawable()
                .load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject())
                .centerCrop().placeholder(R.drawable.sin_imagen)
                .into(mGIFContainer);

        mGIFRootView.setVisibility(View.VISIBLE);
        isDownload = data.getCategories().getDescargas();
        mDownloadButton.setVisibility(isDownload ? View.VISIBLE : View.GONE);


        //isStatusSeriado te dice si ya viste el objeto de aprendizaje
        boolean mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());

        mDataDetalle = data;
    }


    @Override
    public void showLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if(!loader.isAdded())
        {
            loader.show(fm, "Loader");
        }
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void hideLoader() {
        try
        {
            if(loader != null && loader.isAdded())
            {
                loader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void muestraMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    @Override
    public void OnMarkAsReadExitoso() {

    }


    @Override
    public void OnBonusSuccess() {
        mMaskasReadImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_checkmark));
        MaskAsReadButton.setText(getResources().getString(R.string.completado));
        MaskAsReadButton.setTextColor(getResources().getColor(R.color.femsa_red_d));
        MaskAsReadButton.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsOfflineFile){
            configuraOffline();
        }
        else{
            if(mIDObject > 0) {
                mPresenter.OnInteractorCallObjectDetail(mIDObject);
            }
        }
    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void OnNoInternet() {
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
    public void OnJuegoListo(InputStream zip, int buffer) {

    }


    private void descargarObjeto(){
        if(ConnectionState.getTipoConexion(this) != ConnectionState.NO_NETWORK)
            {
                DescargaArchivoAsync dlw = new DescargaArchivoAsync();
                dlw.setListener(this);
                dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mRutaImagenbd));
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
            filedlw.execute(new DescargasParams(DescargaArchivoAsync.ARCHIVO, mGIFFilename, mGIFFullPath));
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
}

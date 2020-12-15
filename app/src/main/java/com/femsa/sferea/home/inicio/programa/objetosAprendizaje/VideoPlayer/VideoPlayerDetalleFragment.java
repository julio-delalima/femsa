package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.ConnectionState;
import com.femsa.sferea.Utilities.CustomVideoView;
import com.femsa.sferea.Utilities.FullScreenMediaController;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
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
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo.Truster;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class VideoPlayerDetalleFragment extends Fragment implements LearningObjectView,  DescargaArchivoAsync.OnStatusDescarga, InsertObjetoAsyncTask.OnBDStatus {

    private CustomVideoView videoView;

    private Loader loader;

    private MediaController mMediaController;

    private ConstraintLayout mScroll;

    private ImageView mDownloadButton, mBotonPlay;

    private WebView mDescrption;

    private int mIDObject;

    private TextView mTitle, mEstimatedTime, mCorcholatas;

    private ImageView mLike, mVideoPreview;

    private boolean mIsLiked = false;

    private LearningObjectsPresenter mPresenter;

    private NestedScrollView mVideoRootView;

    private String mFullPath = RequestManager.ARCHIVO_PROGRAMA_URL;

    private View mView;

    private int mTimeStamp = 0;

    private FullScreenMediaController.OnButtonMediaClick mTempListener;

    private  setDatatoMain mListener;

    private ProgressBar mVideoLoader;

    private ImageView mMaskasReadImg;

    private Button MaskAsReadButton;

    private String mVideoPath, mVideoName, mImageName, mImagePath;
    //Para la BD
    private String mRutaArchivo, mRutaImagen, mNombrePrograma;

    private DBManager.ObjetoAprendizaje mBD;

    private ObjectDetailResponseData mDataDetalle;

    private ObjetoAprendizajeEntity mObjetoOffline;

    public interface setDatatoMain
    {
        void setRutaVideo(String ruta);
    }

    public void setObjetoOffline(ObjetoAprendizajeEntity objetoOffline) {
        mObjetoOffline = objetoOffline;
    }

    public VideoPlayerDetalleFragment newInstance() {
        Bundle args = new Bundle();
        VideoPlayerDetalleFragment fragment = new VideoPlayerDetalleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initData(int currentTime, int IDObjetoAprendizaje, FullScreenMediaController.OnButtonMediaClick listener, setDatatoMain listenerData)
    {
        mTimeStamp = currentTime;
        mIDObject = IDObjetoAprendizaje;
        mTempListener = listener;
        mListener = listenerData;
    }

    public void setNombrePrograma(String nombre){
        mNombrePrograma = nombre;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_video_player, container, false);
        bindComponents();
        return  mView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindComponents()
    {
        mVideoLoader = mView.findViewById(R.id.video_carga_loader);

        mVideoRootView = mView.findViewById(R.id.video_scroll);
            mVideoRootView.setVisibility(View.INVISIBLE);

        mPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());

        loader = Loader.newInstance();

        mDownloadButton = mView.findViewById(R.id.video_download_button);
            mDownloadButton.setOnClickListener(v -> descargarObjeto());

        mScroll = mView.findViewById(R.id.scrollable_video);
            mScroll.setOnTouchListener((v, event) -> {
                if(mMediaController != null && mMediaController.isShowing()) {
                    mMediaController.hide();
                }
                return false;
            });

        videoView = mView.findViewById(R.id.video_view);

        mVideoPreview = mView.findViewById(R.id.video_image_preview);

        Toolbar toolbar =  mView.findViewById(R.id.pdf_toolbar);
            if (null != ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
            {
                ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
                Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
                Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            toolbar.setTitle("");
            toolbar.setSubtitle("");

        mBotonPlay = mView.findViewById(R.id.video_play_button);
            mBotonPlay.setOnClickListener(v -> {
                mPresenter.OnInteractorMarkAsRead(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario());
                mVideoLoader.setVisibility(View.VISIBLE);
               setVideoMedia();
            });

        mDescrption = mView.findViewById(R.id.video_player_description);
            mDescrption.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mTitle = mView.findViewById(R.id.video_title_tv);

        mEstimatedTime = mView.findViewById(R.id.video_estimated_time_tv);

        mCorcholatas = mView.findViewById(R.id.video_corcholata_counter);

        mLike = mView.findViewById(R.id.video_like_iv);
            mLike.setOnClickListener(v -> OnLikePressed());

        mMaskasReadImg = mView.findViewById(R.id.mark_as_read_image_objeto);
        MaskAsReadButton = mView.findViewById(R.id.mark_as_read_objeto_button);
            MaskAsReadButton.setOnClickListener(v-> mPresenter.OnInteractorCallBonus(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario()));

        mBD = new DBManager.ObjetoAprendizaje(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mObjetoOffline != null){
            configurarOffline();
        }
        else{
            mPresenter.OnInteractorCallObjectDetail(mIDObject);
        }

    }

    /**
     * Método que se encarga de cargar el video dentro del reproductor, además de que verifica si se viene desde la sección de kof o desde
     * la pantalla completa, la cual le envia el tiempo timestamp que fue el último instante de reproducción del video antes de girar la pantalla
     * */
    private void addVideo() {
        try {
                URL videoUri;
                videoUri = new URL(mFullPath);
                Truster.trustFemsa(mFullPath, getContext());
                videoView.setVideoURI(Uri.parse(videoUri.toString()));
                if(mTimeStamp > 0)
                    {
                        setVideoMedia();
                        videoView.seekTo(mTimeStamp);
                    }
                else
                {
                    videoView.seekTo(1);
                }
            }
        catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    /**
     * Método que agrega al reproductor los elementos de MediaController personalizados, incluye el botón de pantalla completa
     * */
    private void setVideoMedia()
    {
        mMediaController = new FullScreenMediaController(getActivity());
            ((FullScreenMediaController) mMediaController).setmVideo(videoView);
            mMediaController.setAnchorView(videoView);
            ((FullScreenMediaController) mMediaController).setRutaVideo(mFullPath);
            ((FullScreenMediaController) mMediaController).isPantallaCompleta(false);
            ((FullScreenMediaController) mMediaController).setListener(mTempListener);

        videoView.setMediaController(mMediaController);
        //Método de VideoPlayer que desaparecerá el Loader cuando el video inicie la reproducción.
        videoView.setOnPreparedListener(mp -> {
            if (mVideoLoader != null){
                mVideoLoader.setVisibility(View.GONE);
                videoView.start();
            }
        });

        //Método que detecta cuando se hace click en el play o pausa del VideoMediaController
        videoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                if (mVideoLoader != null){
                    mVideoLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPause() {
                if (mVideoLoader != null){
                    mVideoLoader.setVisibility(View.GONE);
                }
            }
        });

        mBotonPlay.setVisibility(View.GONE);
        mVideoPreview.setVisibility(View.GONE);
    }


    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        mLike.animate().scaleX(0.2f);
        mLike.animate().scaleY(0.2f);
        mPresenter.OnInteractorLike(mIDObject);
    }


   /* @Override
    public boolean onSupportNavigateUp() {
        getActivity().onBackPressed();
        /*Intent sendTo = new Intent(this, DetalleProgramaActivity.class);
        startActivity(sendTo);
        finish();*/
       // return true;
    //}*/

    public int getVideoTime()
    {
        return ((FullScreenMediaController) mMediaController).getVideoTime();
    }


    @Override
    public void likeSuccess() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLike.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLike.animate().scaleY(0.7f);
        mLike.animate().scaleX(0.7f);
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTitle.getText().toString());
        FragmentManager fm = getChildFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {
        mTitle.setText(data.getCategories().getmObjectName());
        String minutosEstimados;
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + data.getCategories().getmEstimatedTime();//GlobalActions.getTimeFromstring(data.getCategories().getmEstimatedTime(), 1)  + " " + getResources().getString(R.string.minutos_min);;
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(data.getCategories().getmBonus() + "");
        if(data.getCategories().getmLike() == 1)
        {
            mLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(data.getCategories().getmDetailContent());
        mDescrption.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        mFullPath += data.getCategories().getFilename();
        mListener.setRutaVideo(mFullPath);
        mVideoRootView.setVisibility(View.VISIBLE);

        //Picasso.with(getContext()).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mVideoPreview);

        GlideApp.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).
                centerCrop().placeholder(R.drawable.sin_imagen)
                .into(mVideoPreview);

        mDownloadButton.setVisibility((data.getCategories().getDescargas()) ? View.VISIBLE : View.GONE);
        addVideo();

        //isStatusSeriado te dice si ya viste el objeto de aprendizaje
        boolean mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());

        mVideoPath =  RequestManager.ARCHIVO_PROGRAMA_URL + data.getCategories().getFilename();
        mVideoName = data.getCategories().getFilename();
        //Picasso.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mPDFView);
        mImageName = data.getCategories().getmImageObject();
        mFullPath = RequestManager.ARCHIVO_PROGRAMA_URL + data.getCategories().getFilename();
        mImagePath = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject();

        mDataDetalle = data;
    }


    /**
     * <p>Método que coloca los datos dentro de la vista tras obtenerlos de la base de datos.</p>
     * */
    private void configurarOffline(){
        mTitle.setText(mObjetoOffline.getNombreObjeto());
        String minutosEstimados;
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + mObjetoOffline.getTiempoEstimado();
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(mObjetoOffline.getCorcholatas());
        if(mObjetoOffline.isLike())
        {
            mLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(mObjetoOffline.getDescripcion());
        mDescrption.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        mFullPath = mObjetoOffline.getRutaArchivo();
        mListener.setRutaVideo(mFullPath);
        mVideoRootView.setVisibility(View.VISIBLE);

        //Picasso.with(getContext()).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mVideoPreview);

        GlideApp.with(this).load( mObjetoOffline.getRutaImagen()).
                centerCrop().placeholder(R.drawable.sin_imagen)
                .into(mVideoPreview);

            mDownloadButton.setVisibility(View.GONE);
        mMaskasReadImg.setVisibility(View.INVISIBLE);
        MaskAsReadButton.setVisibility(View.INVISIBLE);
        addVideo();
    }

    @Override
    public void showLoader() {
        try{
            if(loader != null && !loader.isAdded()){
                FragmentManager fm = getChildFragmentManager();
                loader.show(fm, "Loader");
            }
        }
        catch (Exception ignored){}

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
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
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
    public void onDestroy() {
        mPresenter.onDestroy();
        videoView.stopPlayback();
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
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        }.start();
    }

    @Override
    public void OnJuegoListo(InputStream zip, int buffer) {

    }


    private void descargarObjeto(){
        if(ConnectionState.getTipoConexion(Objects.requireNonNull(getContext())) != ConnectionState.NO_NETWORK)
        {
            DescargaArchivoAsync dlw = new DescargaArchivoAsync();
            dlw.setListener(this);
            dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mImagePath));
        }
        else{
            DialogManager.displaySnack(getChildFragmentManager(), StringManager.NO_INTERNET);
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
            filedlw.execute(new DescargasParams(DescargaArchivoAsync.ARCHIVO, mVideoName, mVideoPath));
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
        DialogManager.displaySnack(getChildFragmentManager(), error);
    }

    @Override
    public void OnInicioQuery() {
    }

    @Override
    public void OnFinQuery() {
        hideLoader();
        DialogManager.displaySnack(getChildFragmentManager(), getResources().getString(R.string.descarga_exitosa));
    }

    @Override
    public void OnErrorQuery(String error) {
        DialogManager.displaySnack(getChildFragmentManager(), error);
    }
}

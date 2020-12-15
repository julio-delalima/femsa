package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VideoInteractivoResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.CustomVideoView;
import com.femsa.sferea.Utilities.FullScreenMediaController;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer.VideoPlayerDetalleFragment;

import java.io.InputStream;
import java.util.Objects;

public class VideoInteractivoSuperDetalle extends Fragment implements VideoInteractivoView, LearningObjectView, PreguntasFragment.OnVideoInteractivo {
    private CustomVideoView mVideo;
    private ImageView mDownloadButton;
    private Toolbar mtoolbar;
    private ConstraintLayout mContenedor;
    private Loader mLoader;
    private VideoInteractivoPresenter mPresenter;
    private MediaController mMediaController;
    private ImageView mBtnPlay;
    private View mView;
    private int mTotalVideos = 0;
    private int mVideoIndex = 0;
    private int mTotalCorrectas = 0;
    private VideoInteractivoResponseData mVideosData;
    //objeto
    public final static String OBJETOKEY="objeto_key";

    public static final String ID_ELEMENTO_KEY = "id_video_interactivo";

    public static final String TOTAL_PREGUNTAS_VIDEO = "totalVideoInteractivo";

    public static final String TOTAL_RESPUESTAS_CORRECTAS = "totalCorrectasVideoInteractivo";

    private ImageView mVideoPreview;

    private String mFullPath;

    private int mIDObject;

    private int mTimeStamp = 0;

    private FullScreenMediaController.OnButtonMediaClick mTempListener;

    private VideoPlayerDetalleFragment.setDatatoMain mListener;

    private LearningObjectsPresenter mObjetosPresenter;

    private ProgressBar mVideoLoader;

    public int totalPreguntasVideoInteractivo = 0;

    private OnVideoInteractivoData mListenerMain;

    private boolean mVideoRecienAbierto = true;

    public interface OnVideoInteractivoData
        {
            void mVideoData(int mVideoIndex, int totalPreguntas, int totalCorrectas, boolean recienAbierto);
        }

    public void setListener(OnVideoInteractivoData listener)
        {
            mListenerMain = listener;
        }

    public void retrieveData(int mVideoIndex, int totalPreguntas, int totalCorrectas, boolean recienAbierto)
        {
            this.mVideoIndex = mVideoIndex;
            totalPreguntasVideoInteractivo = totalPreguntas;
            mTotalCorrectas = totalCorrectas;
            mVideoRecienAbierto = recienAbierto;
        }

    public void initData(int currentTime, int IDObjetoAprendizaje, FullScreenMediaController.OnButtonMediaClick listener, VideoPlayerDetalleFragment.setDatatoMain listenerData)
    {
        mTimeStamp = currentTime;
        mIDObject = IDObjetoAprendizaje;
        mTempListener = listener;
        mListener = listenerData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_interactive_video, container, false);
        setData();
        bindViews();

        if(mVideoRecienAbierto)
            {
                Bundle bundle = new Bundle();
                FragmentManager fregmentManager = getChildFragmentManager();
                FragmentTransaction transaction = fregmentManager.beginTransaction();
                DetalleVideoFragment dVF = new DetalleVideoFragment();
                bundle.putInt(OBJETOKEY, mIDObject);
                dVF.setArguments(bundle);
                transaction.replace(R.id.contenedor_preguntas, dVF);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        else
            {
                addPreguntasFragmentVacio();
            }
        return  mView;
    }



    private void setData() {
        mLoader = Loader.newInstance();
        mObjetosPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());
            mObjetosPresenter.OnInteractorCallObjectDetail(mIDObject);
    }

    //metodo para ver el video
    private void setVideoViewMedia() {
        mMediaController = new FullScreenMediaController(getActivity());
        ((FullScreenMediaController) mMediaController).setmVideo(mVideo);
        mMediaController.setAnchorView(mVideo);
        ((FullScreenMediaController) mMediaController).setRutaVideo(mFullPath);
        ((FullScreenMediaController) mMediaController).isPantallaCompleta(false);
        ((FullScreenMediaController) mMediaController).setListener(mTempListener);
        mVideo.setMediaController(mMediaController);
        /**
         * Método de VideoPlayer que desaparecerá el Loader cuando el video inicie la reproducción.
         * */
        mVideo.setOnPreparedListener(mp -> {
            if (mVideoLoader != null){
                mVideoLoader.setVisibility(View.GONE);
                mVideo.start();
            }
        });

        /**
         * Método que detecta cuando se hace click en el play o pausa del VideoMediaController
         * */
        mVideo.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
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

        mBtnPlay.setVisibility(View.GONE);
        mVideoPreview.setVisibility(View.GONE);
    }

    private void addPreguntasFragmentVacio()
        {
            PreguntasFragment preguntas = new PreguntasFragment();
            preguntas.setListener(this);
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.contenedor_preguntas, preguntas);
            transaction.commit();
        }



    private void addVideo(int i)
    {
        //VideoPlayerDetalleFragment.trustAll(getChildFragmentManager());
        Truster.trustFemsa(mFullPath, getContext());
        Uri videoUri = Uri.parse(mFullPath);//"android.resource://"+getPackageName()+"/"+R.raw.minikof);
        mVideo.setVideoURI(videoUri);
        mVideo.requestFocus();
        if(mTimeStamp > 0)
        {
            setVideoViewMedia();
            mVideo.seekTo(mTimeStamp);
        }
        else
        {
            mVideo.seekTo(1);
        }

        mVideo.setOnPreparedListener(
                v -> {
                    if(!mVideoRecienAbierto)
                        {
                           addPreguntasFragmentVacio();
                           mVideo.start();
                        }
                });

        /**
         * Método de VideoPlayer para detectar el fin del video
         * */
        mVideo.setOnCompletionListener(mp -> {
            Bundle bundle = new Bundle();
            PreguntasFragment preguntas = new PreguntasFragment();
            preguntas.setListener(this);
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            bundle.putInt(OBJETOKEY, mIDObject);
            bundle.putInt(ID_ELEMENTO_KEY, i);
            bundle.putInt(TOTAL_PREGUNTAS_VIDEO, totalPreguntasVideoInteractivo);
            bundle.putInt(PreguntasFragment.TOTAL_VIDEOS_KEY, mTotalVideos);
            bundle.putInt(TOTAL_RESPUESTAS_CORRECTAS, mTotalCorrectas);
            preguntas.setArguments(bundle);
            transaction.replace(R.id.contenedor_preguntas,preguntas);
            transaction.commit();
        });


    }

    public int getVideoTime()
    {
        return ((FullScreenMediaController) mMediaController).getVideoTime();
    }

    private void bindViews() {
        mVideoLoader = mView.findViewById(R.id.video_interactivo_loader);
        mVideo = mView.findViewById(R.id.video_interactive_view);
        mtoolbar = mView.findViewById(R.id.interactive_video_toolbar);
        mBtnPlay = mView.findViewById(R.id.video_play_button_interactive);
            mBtnPlay.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mVideoLoader.setVisibility(View.VISIBLE);
                    mVideoRecienAbierto = false;
                    setVideoViewMedia();
                }
            });
        if (null != ((AppCompatActivity) getActivity()).getSupportActionBar())
        {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mtoolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mtoolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            mtoolbar.setTitle("");
            mtoolbar.setSubtitle("");

        mContenedor = mView.findViewById(R.id.contenedor_preguntas);
        mLoader = Loader.newInstance();
        mPresenter = new VideoInteractivoPresenter(this, new VideoInteractivoInteractor());
            mPresenter.OnInteractorCallVideoInteractivo(mIDObject);

        mVideoPreview = mView.findViewById(R.id.video_interactivo_image_preview);

        mDownloadButton = mView.findViewById(R.id.video_download_button);
        mDownloadButton.setOnClickListener(v -> {

        });
    }


    @Override
    public void videoInteractivoSuccess(VideoInteractivoResponseData data) {
        mTotalVideos = data.getVideo().getContenidos().size();
        mVideosData = data;
        videoIndexController();
    }

    private void videoIndexController()
    {
        if(mVideoIndex < mTotalVideos)
        {
            mFullPath = RequestManager.ARCHIVO_PROGRAMA_URL + mVideosData.getVideo().getContenidos().get(mVideoIndex).getVideoRuta();
            mListener.setRutaVideo(mFullPath);
            try
                {
                    addVideo(mVideoIndex);
                }
            catch (Exception ex)
                {
                    DialogManager.displaySnack(getChildFragmentManager(),  getResources().getString(R.string.video_error));
                    Objects.requireNonNull(getActivity()).finish();
                }
        }
    }

    @Override
    public void likeSuccess() {

    }

    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {
            //Picasso.with(getContext()).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mVideoPreview);
        GlideApp.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).
                centerCrop().placeholder(R.drawable.sin_imagen)
                .into(mVideoPreview);
                if(data.getCategories().getDescargas())
                {
                    mDownloadButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    mDownloadButton.setVisibility(View.GONE);
                }
    }

    @Override
    public void showLoader() {
    }

    @Override
    public void hideLoader() {
    }

    @Override
    public void muestraMensaje(int tipoMensaje) {
       // SnackbarManager.displaySnack(, tipoMensaje);
    }

    @Override
    public void muestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

    }

    @Override
    public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

    }

    @Override
    public void OnMarkAsReadExitoso() {

    }

    @Override
    public void OnBonusSuccess() {

    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        mObjetosPresenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void OnUnVideoCompleto() {
        mTimeStamp = 0;
        mVideoIndex++;
        videoIndexController();
        mListenerMain.mVideoData(mVideoIndex, totalPreguntasVideoInteractivo, mTotalCorrectas, mVideoRecienAbierto);
    }

    @Override
    public void OnAgregaTotalPreguntas(int masPreguntas, int masCorrectas) {
        totalPreguntasVideoInteractivo = masPreguntas;
        mTotalCorrectas = masCorrectas;
        mListenerMain.mVideoData(mVideoIndex, totalPreguntasVideoInteractivo, mTotalCorrectas, mVideoRecienAbierto);
    }

    @Override
    public void OnNoInternet() {
    }

    @Override
    public void OnJuegoListo(InputStream zip, int buffer) {

    }
}

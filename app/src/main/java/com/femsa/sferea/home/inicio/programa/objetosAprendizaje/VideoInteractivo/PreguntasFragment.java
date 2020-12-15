package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;


import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo.Respuesta;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VideoInteractivoResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreguntasFragment extends Fragment implements VideoInteractivoView, RespuestasInteractiveAdapter.OnRespuestasListener, PruebaTerminadaDialog.OnPruebaTerminadaClick {

    //inicializa elemetos de la vista
    private TextView mPreguta;

    private ProgressBar mTimer;

    private RecyclerView mrecyclerView;

    private ArrayList<Respuesta> mProgramasActivos;

    private View mView;

    private Loader mLoader;

    private  int detalleID;

    private VideoInteractivoPresenter mPresenter;

    private RespuestasInteractiveAdapter mRespuestaAdapter;

    private ArrayList<Respuesta> mAllRespuestas;

    private VideoInteractivoResponseData mData;

    public final static String OBJETOKEY="objeto_key";

    public static final String TOTAL_VIDEOS_KEY = "totalVideos";

    private int videoIndex = 0, mTotalVideos, mTotalPreguntas = 0;

    PruebaTerminadaDialog pruebaTerminada;

    private OnVideoInteractivo mListenerVideos;

    private ConstraintLayout lastChecked;

    private CardView mLastOptionSelected;

    private int mTotalRespuestasCorrectas = 0;

    CountDownTimer mCountDownTimer;

    private PreguntasFragment instanciaPreguntas;

    // Bandera que hace que el contador deje de avanzar cuando ya se respondió una pregunta.
    private boolean mPreguntasContestada = false;

    private boolean mRespuestaEnviada = false;

    public interface OnVideoInteractivo
        {
            void OnUnVideoCompleto();

            void OnAgregaTotalPreguntas(int masPreguntas, int masCorrectas);
        }

    public void setListener(OnVideoInteractivo listener)
    {
        mListenerVideos = listener;
    }

    @Override
    public void selecionarChecked(int position, boolean correcto) {
        if(correcto && !mRespuestaEnviada)
            {
                mTotalRespuestasCorrectas+=1;
                mRespuestaEnviada = true;
            }
        lastChecked = (ConstraintLayout) mrecyclerView.getLayoutManager().findViewByPosition(position);
        mLastOptionSelected = (CardView) lastChecked.getChildAt(0);
            if(mLastOptionSelected != null)
            {
                mLastOptionSelected.setCardBackgroundColor(getResources().getColor((correcto) ? R.color.femsa_green_a : R.color.femsa_red_d));
                mPreguntasContestada = true;
            }
        new CountDownTimer(1000,1000) {
            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                mTimer.setProgress(100);
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                mCountDownTimer.onFinish();
                mTimer.setProgress(100);
            }
        }.start();
    }

    @Override
    public void OnPruebaTerminadaAceptar() {
        if(getActivity() != null)
            {
                getActivity().finish();
            }
    }


    public interface OnClickRespuesta{
        void onClick(int i);
    }




    public PreguntasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_preguntas, container, false);
        mLoader = Loader.newInstance();
        if (getArguments() != null){
            detalleID = getArguments().getInt(OBJETOKEY , 0);
            mTotalVideos = getArguments().getInt(TOTAL_VIDEOS_KEY, 0);
            videoIndex = getArguments().getInt(VideoInteractivoSuperDetalle.ID_ELEMENTO_KEY, 0);
            mTotalPreguntas = getArguments().getInt(VideoInteractivoSuperDetalle.TOTAL_PREGUNTAS_VIDEO, 0);
            mTotalRespuestasCorrectas = getArguments().getInt(VideoInteractivoSuperDetalle.TOTAL_RESPUESTAS_CORRECTAS, 0);
        }
        bindViews();
        mPresenter = new VideoInteractivoPresenter(this, new VideoInteractivoInteractor());
            mPresenter.OnInteractorCallVideoInteractivo(detalleID);
        mAllRespuestas = new ArrayList<>();
        mRespuestaAdapter = new RespuestasInteractiveAdapter(mAllRespuestas, this);
            mRespuestaAdapter.setmListenner(this);
        mrecyclerView = mView.findViewById(R.id.contenedor_respuestas);
            mrecyclerView.setAdapter(mRespuestaAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mrecyclerView.setLayoutManager(layoutManager);

        return mView;
    }

    private void bindViews() {
        mPreguta = mView.findViewById(R.id.tv_pregunta);
        mTimer = mView.findViewById(R.id.timer);
        instanciaPreguntas = this;
    }

    @Override
    public void videoInteractivoSuccess(VideoInteractivoResponseData data) {
        mData = data;
        if(mData != null && mData.getVideo().getContenidos().size() > 0) {
            int totalPreguntas = data.getVideo().getContenidos().get(videoIndex).getPreguntas().size();
            mTotalPreguntas += totalPreguntas;
            final int[] numPreguntas = {0};
            updateData(0);
            int segundos = mData.getVideo().getSegundos();
            mCountDownTimer = new CountDownTimer(1000 * segundos, 1000) {
                int i = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    mTimer.setProgress((mPreguntasContestada) ? 100 : (i * 100 / (1000 * segundos / 1000)));
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    i++;
                    mTimer.setProgress(100);
                    numPreguntas[0]++;
                    mAllRespuestas.clear();

                    if (numPreguntas[0] >= totalPreguntas) {
                        mListenerVideos.OnAgregaTotalPreguntas(mTotalPreguntas, mTotalRespuestasCorrectas);
                        if (videoIndex == (mTotalVideos - 1)) {
                            try {
                                numPreguntas[0] = 0;
                                FragmentManager frm = getChildFragmentManager();
                                boolean aproboVideoInteractivo = aproboExamen(mTotalPreguntas, mTotalRespuestasCorrectas);
                                if (aproboVideoInteractivo) {
                                    //mPresenter.OnInteractorMarkAsRead(detalleID, SharedPreferencesUtil.getInstance().getTokenUsuario());
                                    mPresenter.OnInteractorCallBonus(detalleID, SharedPreferencesUtil.getInstance().getTokenUsuario());
                                }
                                pruebaTerminada = PruebaTerminadaDialog.newInstance(aproboVideoInteractivo);
                                pruebaTerminada.setListener(instanciaPreguntas);
                                if (pruebaTerminada.isAdded()) {
                                    pruebaTerminada.dismiss();
                                }
                                pruebaTerminada.show(frm, "PruebaTerminadaDialog");
                            } catch (Exception ex) {
                                LogManager.d("Fragment", "No pasa nada");
                            }
                        } else {
                            clearPreguntas();
                            numPreguntas[0] = 0;
                            mListenerVideos.OnUnVideoCompleto();
                            this.cancel();
                        }
                    } else {
                        updateData(numPreguntas[0]);
                        mTimer.setProgress(0);
                        i = 0;
                        this.start();
                    }

                }
            }.start();
        }
        }


        void updateData(int num_preguntas)
            {
                    mPreguta.setText(mData.getVideo().getContenidos().get(videoIndex).getPreguntas().get(num_preguntas).getPreguntaCompleta());
                    mAllRespuestas.addAll(mData.getVideo().getContenidos().get(videoIndex).getPreguntas().get(num_preguntas).getPosiblesRespuestas());
                    mRespuestaAdapter.notifyDataSetChanged();
                    mTimer.setProgress(0);
                    mRespuestaEnviada = false;
                    mPreguntasContestada = false;
            }

        void clearPreguntas()
            {
                mPreguta.setText(getResources().getString(R.string.siguiente_video));
                mAllRespuestas.clear();
                mRespuestaAdapter.notifyDataSetChanged();
                mTimer.setProgress(0);
                mPreguntasContestada = false;
            }


    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void muestraMensaje(int tipoMensaje) {

    }

    @Override
    public void muestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

    }

    @Override
    public void OnNoInternet() {

    }

    /**
     * Método para verificar que el usuario haya aprobado el examen del video interactivo.
     * @return true si contestó bien todas las preguntas.
     *         false si tuvo algún error.
     * */
    private boolean aproboExamen(int totalPreguntas, int totalCorrectas)
        {
            return ((float)(totalCorrectas * 100) / (float)totalPreguntas) >= 80;
        }

}

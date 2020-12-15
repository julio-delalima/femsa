package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.LikeDialog;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleVideoFragment extends Fragment implements LearningObjectView {


    /**
     * Elementos que inicializan la vista
     */

    private TextView mTextViewTitle;
    private View mView;
    private int detalleID;
    private LearningObjectsPresenter mPresenter;
    private Loader loader;
    private ImageView mLike, mVideoPreview;
    private boolean mIsLiked = false;
    private TextView mEstimatedTime, mCorcholatas;
    private WebView mDescription;
    private ConstraintLayout mRootView;
    private ImageView mMaskasReadImg;

    private Button MaskAsReadButton;


    public DetalleVideoFragment() {
        // Required empty public constructor
    }

    public final static String OBJETOKEY="objeto_key";

    public static DetalleVideoFragment newInstance(){
        DetalleVideoFragment fragment = new DetalleVideoFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_detalle_video, container, false);
        loader = Loader.newInstance();

        mRootView = mView.findViewById(R.id.video_detalle_interactivo_cl);
            mRootView.setVisibility(View.INVISIBLE);

        bindViews();
        loader = Loader.newInstance();
        if (getArguments() != null){
            detalleID = getArguments().getInt(OBJETOKEY , 0);
        }
        mPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());
            mPresenter.OnInteractorCallObjectDetail(detalleID);


        mTextViewTitle = mView.findViewById(R.id.interactive_title);

        mDescription = mView.findViewById(R.id.descripcion);

        mEstimatedTime = mView.findViewById(R.id.tiempoestimado);

        mCorcholatas = mView.findViewById(R.id.numCorcholatas);

        mLike = mView.findViewById(R.id.video_interactive_like_iv);
            mLike.setOnClickListener(v -> OnLikePressed());

        mMaskasReadImg = mView.findViewById(R.id.mark_as_read_image_objeto);
        MaskAsReadButton = mView.findViewById(R.id.mark_as_read_objeto_button);
            MaskAsReadButton.setOnClickListener(v-> mPresenter.OnInteractorCallBonus(detalleID, SharedPreferencesUtil.getInstance().getTokenUsuario()));

        return mView;
    }

    private void bindViews() {

    }

    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        mLike.animate().scaleX(0.2f);
        mLike.animate().scaleY(0.2f);
        mPresenter.OnInteractorLike(detalleID);
    }



    @Override
    public void likeSuccess() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLike.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLike.animate().scaleY(0.7f);
        mLike.animate().scaleX(0.7f);
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTextViewTitle.getText().toString());
        FragmentManager fm = getChildFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {
        mTextViewTitle.setText(data.getCategories().getmObjectName());
        String minutosEstimados;
        /**
         * Funcion para extraer el tiempo deseado de un String de la forma hh:mm:ss
         * @param  time el String de entrada en formato hh:mm:ss
         * @param returnType el valor de retorno que queremos, 0 la hora, 1 minutos y 2 los segundos
         * */
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + data.getCategories().getmEstimatedTime();//minutosEstimados = getResources().getString(R.string.estimated_time) + " " + GlobalActions.getTimeFromstring(data.getCategories().getmEstimatedTime(), 1)  + " " + getResources().getString(R.string.minutos_min);;
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(data.getCategories().getmBonus() + "");
        if(data.getCategories().getmLike() == 1)
        {
            mLike.setImageDrawable(getActivity().getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(data.getCategories().getmDetailContent());
        mDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        //isStatusSeriado te dice si ya viste el objeto de aprendizaje
        boolean mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());
        mRootView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoader() {
        FragmentManager fm = getChildFragmentManager();
        loader.show(fm, "Loader");
    }


    @Override
    public void hideLoader() {
        loader.dismiss();
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
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }.start();

    }

    @Override
    public void OnJuegoListo(InputStream zip, int buffer) {

    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}

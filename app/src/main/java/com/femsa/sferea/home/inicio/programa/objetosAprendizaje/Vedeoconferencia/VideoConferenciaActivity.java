package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Vedeoconferencia;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoconferenciaResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.LikeDialog;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;


import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoConferenciaActivity extends AppCompatActivity implements VedeoconferenciaView, View.OnClickListener, LearningObjectView {

    private Toolbar myToolbar;

    private int mIDObjeto;

    private TextView mVedeoHorarioTV;

    private WebView mDescription;

    private Loader mLoader;

    private ImageButton mbtnInicioVedeo;

    private ImageView mPreviewImage;

    private TextView mEstimatedTime, mCorcholatas, mTituloObjeto;

    private ImageView mLike;

    private boolean mIsLiked = false;

    private ConstraintLayout mRootView;

    String tipoObjeto = "";

    private ImageView mMaskasReadImg;

    private Button MaskAsReadButton;

    private String URI;

    private LearningObjectsPresenter mPresenter;

    private Date mFechaInicio;

    private Date mFechaFin;
    private CardView mContenedorFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_conferencia);
        bindData();
        bindviews();
        bindListeners();
        initializeToolbar();
        initializePresenter();
    }


    private void validarantes() {
        int resta = mFechaInicio.getMinutes()-5;
        mFechaInicio.setMinutes(resta);
        Date mFechaToDay = new Date();
        if (mFechaToDay.before(mFechaInicio) || mFechaToDay.after(mFechaFin)){
            mbtnInicioVedeo.setVisibility(View.GONE);
            //mContenedorFecha.setVisibility(View.GONE);
        }
    }


    private boolean mMostrarBotonInicio(String dia, String horainicio, String horaFin){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date strDate;
        try {
            strDate = sdf.parse(dia);
            if (System.currentTimeMillis() > strDate.getTime()) {
                return false;
            }
        }
        catch (ParseException e) {
            return false;
        }
        return false;
    }

    private void bindListeners() {
        mbtnInicioVedeo.setOnClickListener(this);
    }

    private void bindData()
        {
            mIDObjeto = getIntent().getIntExtra("idObject",0);
        }

    private void bindviews() {

        myToolbar = findViewById(R.id.objeto_videoconferencia_toolbar);

        mVedeoHorarioTV = findViewById(R.id.horario_vedeo_conferencia_tv);

        mbtnInicioVedeo = findViewById(R.id.btnVideoConferencia);

        mMaskasReadImg = findViewById(R.id.mark_as_read_image_objeto);

        MaskAsReadButton = findViewById(R.id.mark_as_read_objeto_button);

        mTituloObjeto = findViewById(R.id.objeto_aprendizaje_titulo_tv);

        mDescription = findViewById(R.id.objeto_aprendizaje_description);

        mRootView = findViewById(R.id.root_view);
            mRootView.setVisibility(View.INVISIBLE);

        mPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());
            mPresenter.OnInteractorCallObjectDetail(mIDObjeto);

        mLike = findViewById(R.id.objeto_aprendizaje_like_iv);
        mLike.setOnClickListener(v -> OnLikePressed());

        mPreviewImage = findViewById(R.id.objeto_aprendizaje_preview);

        mLoader = Loader.newInstance();

        mEstimatedTime = findViewById(R.id.objeto_aprendizaje_estimado);

        mCorcholatas = findViewById(R.id.objeto_aprendizaje_corcholata_counter);

        mContenedorFecha = findViewById(R.id.Contenedor_fechainicio);
    }



    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        mLike.animate().scaleX(0.2f);
        mLike.animate().scaleY(0.2f);
        mPresenter.OnInteractorLike(mIDObjeto);
    }

    @Override
    public void likeSuccess() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLike.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLike.animate().scaleY(0.7f);
        mLike.animate().scaleX(0.7f);
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + tipoObjeto);
        FragmentManager fm = getSupportFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }

    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {
        mTituloObjeto.setText(data.getCategories().getmObjectName());

        String minutosEstimados;
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + data.getCategories().getmEstimatedTime();
        mEstimatedTime.setText(minutosEstimados);
        mCorcholatas.setText(String.valueOf(data.getCategories().getmBonus()));
        if(data.getCategories().getmLike() == 1)
        {
            mLike.setImageDrawable(getDrawable(R.mipmap.ic_full_heart));
            mIsLiked = true;
        }
        String text = GlobalActions.webViewConfigureText(data.getCategories().getmDetailContent());
        mDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        GlideApp.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).
                centerCrop().placeholder(R.drawable.sin_imagen).error(R.drawable.sin_imagen)
                .into(mPreviewImage);

        //isStatusSeriado te dice si ya viste el objeto de aprendizaje
        boolean mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());
    }

    @Override
    public void showLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.show(getSupportFragmentManager(),"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void hideLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
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


    /**
     * <p>Método que configura la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(myToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * Método para inicializar el Presentador de la VedeoConferencia e iniciar las peticiones.
     * */
    private void initializePresenter()
        {
            VedeoconferenciaPresenter presenter = new VedeoconferenciaPresenter(this, new VedeoconferenciaInteractor());
            presenter.OnInteractorTraeVedeoconferencia(mIDObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

        /**
     * Determine whether the Skype for Android client is installed on this device.
     */
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try
            {
                myPackageMgr.getPackageInfo("com.microsoft.office.lync15", PackageManager.GET_ACTIVITIES);
            }
        catch (PackageManager.NameNotFoundException e)
            {
                return (false);
            }
        return (true);
    }

    /**
     * Install the Skype client through the market: URI scheme.
     */
    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.microsoft.office.lync15");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Método implementado esde VedeoConferenciaView que se ejecuta cuando se quiere mostrar el loader.
     * */
    @Override
    public void OnVedeoconferenciaMuestraLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.show(getSupportFragmentManager(),"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    /**
     * Método implementado esde VedeoConferenciaView que se ejecuta cuando se quiere quitar el loader.
     * */
    @Override
    public void OnVedeoconferenciaOcultaLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    /**
     * Método implementado de VedeoConferenciaView que se encarga de mostrar los datos obtenidos desde el Web Service y
     * pintarlos en su elemento visual correspondiente.
     * @param data Información de la Vedeoconferencia traída desde el Web Service.
     * */
    @Override
    public void OnVedeoconferenciaAcomodaData(VedeoconferenciaResponseData data) {
        SimpleDateFormat formatoActual = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SimpleDateFormat nuevoFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
        //mVedeoHorarioTV.setText(data.getVedeo().getFecha());
        if (data.getMensaje()!= null && data.getMensaje().equals("No tiene fechas disponibles")) {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.snfechas_definidas));
            mVedeoHorarioTV.setVisibility(View.GONE);
            mbtnInicioVedeo.setVisibility(View.GONE);
        }else {
            if (data.getVedeo() != null)
            {
                mVedeoHorarioTV.setText(getString(R.string.inicio_videoConferencia, StringManager.parseFecha(data.getVedeo().getFecha(), formatoActual, nuevoFormato), data.getVedeo().getHoranicio()));
                SimpleDateFormat formatoActual2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                SimpleDateFormat nuevoFormato2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
                String fechaconHora = data.getVedeo().getFecha() + " " + data.getVedeo().getHoranicio();
                mFechaInicio = StringManager.parseFecha2(fechaconHora, formatoActual2, nuevoFormato2);
                String fechaconHora2 = data.getVedeo().getFecha() + " " + data.getVedeo().getHorafinal();
                mFechaFin = StringManager.parseFecha2(fechaconHora2, formatoActual2, nuevoFormato);
            }

            URI = data.getVedeo().getURL();
            if (data.getVedeo().getActivo() == 3) {//ya paso
                mbtnInicioVedeo.setVisibility(View.GONE);
                mVedeoHorarioTV.setVisibility(View.GONE);
            } else if (data.getVedeo().getActivo() == 2) {//esta activa
                mbtnInicioVedeo.setVisibility(View.VISIBLE);
                mVedeoHorarioTV.setVisibility(View.GONE);
            } else if (data.getVedeo().getActivo() == 1) {//todavia no empieza
                mbtnInicioVedeo.setVisibility(View.GONE);
                mVedeoHorarioTV.setVisibility(View.VISIBLE);
            }
        }
        mRootView.setVisibility(View.VISIBLE);
    }

    /**
     * Método implementado de VedeoConferenciaView que se ejecuta cuando se va a mostrar un mensaje de error.
     * @param mensaje el tipo de mensaje a mostrar.
     * */
    @Override
    public void OnVedeoconferenciaMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    /**
     * Método implementado de VedeoConferenciaView que se ejecuta cuando se va a mostrar un mensaje de error inesperado
     * junto al código de respuesta de serbidor asociado a ese error.
     * @param mensaje el tipo de mensaje a mostrar.
     * @param codigo el código de respuesta del servidor.
     * */
    @Override
    public void OnVedeoconferenciaMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje,codigo);
    }

    private void abrirSkypeBusiness(String url)
        {
            if (!isSkypeClientInstalled(this)) {
                goToMarket(this);
                return;
                
            }
            String uriString = url;//+"&video=true";
            Uri uri = Uri.parse(uriString);
            Intent callIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(callIntent);
        }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVideoConferencia) {
            mPresenter.OnInteractorMarkAsRead(mIDObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
            abrirSkypeBusiness(URI);
        }
        if(v.getId() == R.id.mark_as_read_objeto_button){
            mPresenter.OnInteractorCallBonus(mIDObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
        }
    }
}

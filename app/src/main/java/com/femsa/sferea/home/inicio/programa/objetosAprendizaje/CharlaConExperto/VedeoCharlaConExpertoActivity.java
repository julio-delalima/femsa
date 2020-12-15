package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto.Dias;
import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto.IntervaloHorasSensuales;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoCharlaConExpertoResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.LikeDialog;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VedeoCharlaConExpertoActivity extends AppCompatActivity implements AdapterCharlaExperto.OnHorarioSrensualSelected, View.OnClickListener, DiasrangoSpinnerDialog.OnDiasSelected,
    VedeoCharlaConExpertoView, DialogFragmentManager.OnDialogManagerButtonActions {

    private Toolbar myToolbar;
    private Button mEditTextDate;
    private AdapterCharlaExperto mAdapter;
    private RecyclerView mRecycler;
    private CardView mcardviewHorarios;
    private Button mbtnAceptar;
    private VedeoCharlaConExpertoPresenter mPresenter;
    private int mIdObjeto;
    private IntervaloHorasSensuales mHoraElegidaData = null;

    private TextView mVedeoHorarioTV;
    //Vistas para el detalle
    private TextView mEstimatedTime, mCorcholatas, mTituloObjeto;

    private ImageView mMaskasReadImg, mCalendarioImg;

    private ImageButton mBtnIniciarCharla;

    private String URL_SKYPE = "";

    private Button MaskAsReadButton;

    private CardView mContenedorFecha;

    private WebView mDescription;

    private ImageView mDownloadButton, mPreviewImage;

    private boolean mIsLiked = false;

    private ImageView mLike;

    //Vistas para cuando ya se contestó
    private TextView mTituloGracias, mDescripcionContestada;

    private View mLinearojaContestada;

    private int mHoraElegida = 0;

    public static final String ID_OBJECT_KEY = "idObject";
    private LearningObjectsPresenter mPresenterObjeto;
    private Loader mLoader;
    private ArrayList<IntervaloHorasSensuales>  mHorasPorDia;
    private VedeoCharlaConExpertoResponseData mData;
    private ArrayList<String> mListadoDias;

    private ConstraintLayout mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charla_experto);
        bindviews();
        initializeToolbar();
        initAdapter();
        bindListeners();
    }

    private void bindListeners() {
        mEditTextDate.setOnClickListener(this);
        mbtnAceptar.setOnClickListener(this);
    }

    private void initAdapter() {
        mHorasPorDia = new ArrayList<>();
        mListadoDias = new ArrayList<>();
        mAdapter = new AdapterCharlaExperto(mHorasPorDia, this);
        mAdapter.setmListener(this);
        mRecycler.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mcardviewHorarios.setVisibility(View.GONE);

        mbtnAceptar.setVisibility(View.INVISIBLE);
        mPresenter = new VedeoCharlaConExpertoPresenter(this, new VedeoCharlaConExpertoInteractor());
            if(mIdObjeto > 0)
                {
                    mPresenter.OnInteractorTraeVedeoCharlaConExperto(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
                }
        mPresenterObjeto = new LearningObjectsPresenter(new LearningObjectView() {
            @Override
            public void likeSuccess() {
                likeSuccess2();
            }

            @Override
            public void getObjectDetailSuccess(ObjectDetailResponseData data) {
                getObjectDetailSuccess2(data);
            }

            @Override
            public void showLoader() {
            }

            @Override
            public void hideLoader() {
            }

            @Override
            public void muestraMensaje(int tipoMensaje) {
                OnVedeoCharlaConExpertoMuestraMensaje2(tipoMensaje);
            }

            @Override
            public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {
                OnVedeoCharlaConExpertoMuestraMensaje2(tipoMensaje, codigoRespuesta);
            }

            @Override
            public void OnMarkAsReadExitoso() {

            }

            @Override
            public void OnBonusSuccess() {
                OnBonusSuccess2();
            }

            @Override
            public void OnNoInternet() {

            }

            @Override
            public void OnJuegoListo(InputStream zip, int buffer) {

            }
        }, new LearningObjectInteractor());

        mPresenterObjeto.OnInteractorCallObjectDetail(mIdObjeto);
    }

    /**
     * Método que manda a llamar Skype for Bussiness cuando llega el momento d ela VedeoCharla con
     * VedeoExperto y verifica que la URL de invitación de Skype no esté vacia.
     * */
    private void iniciarCharla(){
        if(!URL_SKYPE.isEmpty()){
            abrirSkypeBusiness(URL_SKYPE);
            mPresenterObjeto.OnInteractorMarkAsRead(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
        }
    }

    private void bindviews() {
        mBtnIniciarCharla = findViewById(R.id.btnVideoConferencia);
            mBtnIniciarCharla.setOnClickListener(v->iniciarCharla());
            mBtnIniciarCharla.setVisibility(View.GONE);

        mRootView = findViewById(R.id.root_view);
            mRootView.setVisibility(View.INVISIBLE);

        mVedeoHorarioTV = findViewById(R.id.horario_vedeo_conferencia_tv);
            mVedeoHorarioTV.setVisibility(View.INVISIBLE);
        findViewById(R.id.Contenedor_fechainicio).setVisibility(View.INVISIBLE);


        myToolbar = findViewById(R.id.objeto_charlaExperto_toolbar);
        mEditTextDate = findViewById(R.id.registry_usernumber);
        mRecycler = findViewById(R.id.rcvHoras);
        mcardviewHorarios = findViewById(R.id.cv_horarios);
        mbtnAceptar = findViewById(R.id.btnAceptar);
        mIdObjeto = getIntent().getIntExtra("idObject", 0);
        mLoader = Loader.newInstance();
        mTituloObjeto = findViewById(R.id.objeto_aprendizaje_titulo_tv);
        mMaskasReadImg = findViewById(R.id.mark_as_read_image_objeto);
        mCalendarioImg = findViewById(R.id.imageView14);
        MaskAsReadButton = findViewById(R.id.mark_as_read_objeto_button);
            MaskAsReadButton.setOnClickListener(v-> mPresenterObjeto.OnInteractorCallBonus(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario()));

        mDownloadButton = findViewById(R.id.objeto_aprendizaje_download_button);
        mDownloadButton.setOnClickListener(v -> {

        });

        mDescription = findViewById(R.id.objeto_aprendizaje_description);

        mEstimatedTime = findViewById(R.id.objeto_aprendizaje_estimado);

        mCorcholatas = findViewById(R.id.objeto_aprendizaje_corcholata_counter);

        mLike = findViewById(R.id.objeto_aprendizaje_like_iv);
        mLike.setOnClickListener(v -> OnLikePressed());

        mPreviewImage = findViewById(R.id.objeto_aprendizaje_preview);

        mTituloGracias = findViewById(R.id.contestada_titulo);
            mTituloGracias.setVisibility(View.GONE);

        mLinearojaContestada = findViewById(R.id.contesda_linea_roja);
            mLinearojaContestada.setVisibility(View.GONE);

        mDescripcionContestada = findViewById(R.id.contestada_descrpcion);
            mDescripcionContestada.setVisibility(View.GONE);
    }


    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        mLike.animate().scaleX(0.2f);
        mLike.animate().scaleY(0.2f);
        mPresenterObjeto.OnInteractorLike(mIdObjeto);
    }


    public void likeSuccess2() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLike.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLike.animate().scaleY(0.7f);
        mLike.animate().scaleX(0.7f);
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTituloObjeto.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }



    /**
     * <p>Método que configura la Toolbar de la Activity.</p>
     */
    private void initializeToolbar() {
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registry_usernumber:
                DiasrangoSpinnerDialog horas = DiasrangoSpinnerDialog.newInstance();
                horas.setListener(VedeoCharlaConExpertoActivity.this);
                horas.setDias(mListadoDias);
                horas.show(getSupportFragmentManager(), "horas");
                break;
            case R.id.btnAceptar:
                insertarCharla(mIdObjeto, mHoraElegida);
                break;

        }

    }


    /**
     *Método implementado de AdapterCharlaExperto que trae el dia seleccionado en el Spinner de fechas.
     * @param dias el dia seleccionado en formato yyyy-MM-dd
     * @param parseada el prámetro dias pero en formato dd/MM/yyyy
     * */
    @Override
    public void onDiasSelected(String dias, String parseada) {
        mEditTextDate.setText(parseada);
        mcardviewHorarios.setVisibility(View.VISIBLE);
        mbtnAceptar.setVisibility(View.VISIBLE);
        cargarHorariosPordia(dias);
        mHoraElegida = 0;
        mHoraElegidaData = null;
    }

    /**
     * Método que carga en el RecyclerView el listado de horas a elegir de acuerdo a qué día se seleccione en el
     * spinner de fechas.
     * @param diaElegido el dia elegido.
     * */
    private void cargarHorariosPordia(String diaElegido){
        for(int i = 0; i < mData.getVedeo().getListadoDiasDisponiblesParaUnaVedeoCharlaConExperto().size(); i++) {
            if(diaElegido.equals(mData.getVedeo().getListadoDiasDisponiblesParaUnaVedeoCharlaConExperto().get(i).getFechita())){
                mHorasPorDia.clear();
                mHorasPorDia.addAll(mData.getVedeo().getListadoDiasDisponiblesParaUnaVedeoCharlaConExperto().get(i).getListadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto());
                mAdapter.updateHoras(mHorasPorDia);
            }
        }
    }

    @Override
    public void OnVedeoCharlaConExpertoCargaLoader() {
        if (!mLoader.isAdded())
        {
            FragmentManager fm = getSupportFragmentManager();
            try {
                mLoader.show(fm, "Loader");
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void OnVedeoCharlaConExpertoOcultaLoader() {
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
     * Método que manda a llamar el intent de Skype for Business.
     * @param url la url de la invitación del Skype.
     * */
    private void abrirSkypeBusiness(String url)
    {
        if (!isSkypeClientInstalled(this)) {
            goToMarket(this);
            return;

        }
        Uri uri = Uri.parse(url);
        Intent callIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(callIntent);
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
    public void OnVedeoCharlaConExpertoCargaDatos(VedeoCharlaConExpertoResponseData data) {
        ArrayList<Dias> dias;
        SimpleDateFormat formatoActual = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SimpleDateFormat nuevoFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);

        if(data.getVedeo().getMensaje() != null && data.getVedeo().getMensaje().equals("No tiene fechas disponibles"))
            {
                DialogManager.displaySnack(getSupportFragmentManager(),getResources().getString(R.string.sinfechas_definidas_charla));
                findViewById(R.id.texto_fecha_solicitada_tv).setVisibility(View.INVISIBLE);
                findViewById(R.id.red_line_fecha).setVisibility(View.INVISIBLE);
                mBtnIniciarCharla.setVisibility(View.GONE);
                findViewById(R.id.Contenedor_fechainicio).setVisibility(View.INVISIBLE);
                ocultarSelectorFecha();
            }
        else
            {
                if(data.getVedeo().isContestada())
                {
                    findViewById(R.id.Contenedor_fechainicio).setVisibility(View.VISIBLE);
                    mVedeoHorarioTV.setVisibility(View.VISIBLE);
                    mVedeoHorarioTV.setText(getString(R.string.inicio_videoConferencia, StringManager.parseFecha(data.getVedeo().getApartado().getDiaApartado(), formatoActual, nuevoFormato), data.getVedeo().getApartado().getHoraInicio()));

                    vedeoCharlaReservada(
                            data.getVedeo().getApartado().getDiaApartado(),
                            data.getVedeo().getApartado().getHoraInicio(),
                            data.getVedeo().getApartado().getHoraFinal());

                    LogManager.d("Asesor", data.getVedeo().getApartado().getStatus() + "");
                    if(data.getVedeo().getApartado().getStatus() == 3){//ya paso
                        mBtnIniciarCharla.setVisibility(View.GONE);
                        mVedeoHorarioTV.setVisibility(View.GONE);
                        findViewById(R.id.Contenedor_fechainicio).setVisibility(View.GONE);
                    }
                    else if(data.getVedeo().getApartado().getStatus() == 2){//esta activa
                        mBtnIniciarCharla.setVisibility(View.VISIBLE);
                        mVedeoHorarioTV.setVisibility(View.GONE);
                        findViewById(R.id.Contenedor_fechainicio).setVisibility(View.GONE);
                    }
                    else if(data.getVedeo().getApartado().getStatus() == 1){//todavia no empieza
                        mBtnIniciarCharla.setVisibility(View.GONE);
                        mVedeoHorarioTV.setVisibility(View.VISIBLE);
                        findViewById(R.id.Contenedor_fechainicio).setVisibility(View.VISIBLE);
                    }
                    ocultarGracias();
                    URL_SKYPE = data.getVedeo().getApartado().getURLSesion();
                }
                else
                {
                    dias = data.getVedeo().getListadoDiasDisponiblesParaUnaVedeoCharlaConExperto();
                    if(dias.size() > 0){
                        mData = data;
                        for(int i = 0; i < dias.size(); i++)
                        {
                            mListadoDias.add(dias.get(i).getFechita());
                        }
                    }
                    else
                    {
                        ocultarSelectorFecha();
                        findViewById(R.id.texto_fecha_solicitada_tv).setVisibility(View.INVISIBLE);
                        findViewById(R.id.red_line_fecha).setVisibility(View.INVISIBLE);
                        mBtnIniciarCharla.setVisibility(View.INVISIBLE);
                        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.sin_horarios_vedeocharla));
                        //SnackbarManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.sin_fechas_disponibles));
                    }
                }

            }
        mRootView.setVisibility(View.VISIBLE);
    }

    private void ocultarSelectorFecha(){
        mEditTextDate.setVisibility(View.GONE);
        mCalendarioImg.setVisibility(View.GONE);
    }

    @Override
    public void OnVedeoCharlaConExpertoMuestraMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    public void OnVedeoCharlaConExpertoMuestraMensaje2(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
    }

    public void OnVedeoCharlaConExpertoMuestraMensaje2(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    @Override
    public void OnVedeoCharlaConExpertoMuestraMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
    }

    @Override
    public void OnVedeoCharlaConExpertoInsertada() {
        vedeoCharlaReservada("", "", "");
        //mPresenterObjeto.OnInteractorMarkAsRead(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void OnVedeoCharlaConExpertoInapartable() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.fecha_inapartable), false, this);
    }

    @Override
    public void onSeleccionarIntervalo(IntervaloHorasSensuales data) {
        mHoraElegida = data.getIDHora();
        mHoraElegidaData = data;
    }

    private void insertarCharla(int idObjeto, int idHoras){
        if(idHoras > 0) {
            mPresenter.OnInteractorInsertarVedeoCharla(idObjeto, idHoras,SharedPreferencesUtil.getInstance().getTokenUsuario());
        }
        else{
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_opciones_horario));
        }
    }

    private void reconfigurarFechaInapartada(){
        finish();
        startActivity(getIntent());
    }

    public void getObjectDetailSuccess2(ObjectDetailResponseData data) {
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

        boolean isDownloadable = data.getCategories().getDescargas();
        mDownloadButton.setVisibility(isDownloadable ? View.VISIBLE : View.GONE);


        //isStatusSeriado te dice si ya viste el objeto de aprendizaje
        boolean mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());
    }

    private void vedeoCharlaReservada(String dia, String fechaInicio, String fechafin){
        String fechaInicioParsed = StringManager.parseFecha(dia, "yyyy-MM-dd", "dd/MMM/yyyy");
        ocultarElementos();
        mTituloGracias.setVisibility(View.VISIBLE);
        mDescripcionContestada.setVisibility(View.VISIBLE);
        mLinearojaContestada.setVisibility(View.VISIBLE);
        if(mHoraElegidaData != null)
            {
                mDescripcionContestada.setText(getResources().getString(R.string.charla_reservada, mEditTextDate.getText().toString(), mHoraElegidaData.getHoraInicio(), mHoraElegidaData.getHoraFinal()));
            }
        else
            {
                mDescripcionContestada.setText(getResources().getString(R.string.charla_reservada, fechaInicioParsed, fechaInicio, fechafin));
            }
    }

    private void ocultarElementos(){
        mCalendarioImg.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
        findViewById(R.id.texto_fecha_solicitada_tv).setVisibility(View.GONE);
        mEditTextDate.setVisibility(View.GONE);
        mbtnAceptar.setVisibility(View.GONE);
        findViewById(R.id.container_horas_chidas).setVisibility(View.GONE);
    }

    private void ocultarGracias(){
        mTituloGracias.setVisibility(View.GONE);
        mDescripcionContestada.setVisibility(View.GONE);
        mLinearojaContestada.setVisibility(View.GONE);
    }

    public void OnBonusSuccess2() {
        mMaskasReadImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_checkmark));
        MaskAsReadButton.setText(getResources().getString(R.string.completado));
        MaskAsReadButton.setTextColor(getResources().getColor(R.color.femsa_red_d));
        MaskAsReadButton.setEnabled(false);
    }

    private boolean mMostrarBotonInicio(String dia, String horainicio, String horaFin){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date hoy, diaVedeo;
        Date hoyHora, horaInicioVedeo, horafinVedeo;
        try{
            hoy = sdf.parse(sdf.format(new Date()));
            diaVedeo = sdf.parse(dia);
            if(hoy.equals(diaVedeo)) //mismo dia :o
                {
                    hoyHora = sdfHora.parse(sdfHora.format(new Date()));
                    horaInicioVedeo = sdfHora.parse(horainicio);
                    long t = horaInicioVedeo.getTime();
                    Date horaInicioMenos5Mins= new Date(t - (5 * 60000));
                    horafinVedeo = sdfHora.parse(horaFin);
                    if(hoyHora.after(horaInicioMenos5Mins) && hoyHora.before(horafinVedeo)){
                        return true;
                    }
                }
        }
        catch(Exception ignored){
        }
        return false;
    }

    @Override
    public void OnDialogAceptarClick() {
        reconfigurarFechaInapartada();
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}
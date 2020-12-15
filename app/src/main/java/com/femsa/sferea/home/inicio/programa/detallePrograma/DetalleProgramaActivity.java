package com.femsa.sferea.home.inicio.programa.detallePrograma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.ObjectData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectResponseData;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.Login.LoginActivity;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.DialogInscripcion;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.LikeDialog;
import com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments.SinContenidoDialog;
import com.femsa.sferea.home.inicio.programa.detallePrograma.listado.TemarioAdapter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto.VedeoCharlaConExpertoActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Ebook.EbookActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Enlace.EnlaceActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.GIFPlayer.GIFActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.PDFViewer.PDFActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Vedeoconferencia.VideoConferenciaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo.VideoInteractivoMainActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer.VideoPlayerMainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.CHECKLIST;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.COMIC;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.CONFERENCIA;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_PDF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_WORD;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EBOOK;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.ENCOESTA;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.ENLACE;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EVALUACION;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EXCEL;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.GIF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.POWER_POINT;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO_ASESORIA_CON_EXPERTO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO_INTERACTIVO;

public class DetalleProgramaActivity extends AppCompatActivity implements KOFSectionView, TemarioAdapter.OnHomeListener, SinContenidoDialog.OnSinContenidoKOF, DialogInscripcion.OnDialogDetalleInscripcion, SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<String> mTitles;

    private ArrayList<ObjectData> mData;

    private RecyclerView mRecyclerObjetosAprendizaje;

    private WebView mDescription;

    private Button mInscripcionButton;

    private SwipeRefreshLayout mSwipe;

    private Loader loader;

    private KOFSectionPresenter mPresenter;

    private TextView mTitleTv, mDateTv, mEstimatedTimeTV, mCorcholataCount, mTemarioTituloTv;

    private ImageView mHeaderImage, mMedalIcon, mArrowsIcon, mMuscledIcon, mHandsIcon, mHumanIcon, mLikeButton, mShareButton;

    private int mProgramID = -1;

    private boolean mIsLiked = false;

    private static final String ID_OBJECT_KEY = "idObject";

    public static final String ID_PROGRAMA_KEY = "idPrograma";

    public static final String TITULO_PROGRAMA_KEY = "tituloPrograma";

    private TemarioAdapter mAdapter;

    private String mTituloSeccion = "KOF";

    private ConstraintLayout mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kofsection);
        bindComponents();
        initRecyclers();
    }


    @Override
    public void onRefresh() {
        mPresenter.OnInteractorCallLearningObject(mProgramID, SharedPreferencesUtil.getInstance().getTokenUsuario());
        mPresenter.OnInteractorCallProgramDetail(mProgramID, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    private void bindComponents()
    { 
        mMainView = findViewById(R.id.kof_main_view);
            mMainView.setVisibility(View.INVISIBLE);


        mSwipe = findViewById(R.id.swipe_refresh_notificaciones);
        mSwipe.setOnRefreshListener(this);
        mSwipe.setEnabled(false);
        mSwipe.setColorSchemeColors(
                getResources().getColor(R.color.femsa_red_a),
                getResources().getColor(R.color.femsa_red_b),
                getResources().getColor(R.color.femsa_red_a)
        );

        Toolbar toolbar = findViewById(R.id.kof_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle("");
            toolbar.setSubtitle("");
            if (getSupportActionBar() != null)
                {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
        mDescription = findViewById(R.id.kofsection_description);

        loader = Loader.newInstance();

        mPresenter = new KOFSectionPresenter(this, new KOFSectionInteractor());

        mTitleTv = findViewById(R.id.kofsection_title_tv);

        mDateTv = findViewById(R.id.kofsection_date_tv);

        mEstimatedTimeTV = findViewById(R.id.kofsection_time_tv);

        mCorcholataCount = findViewById(R.id.kofsection_corcholata_tv);

        mHeaderImage = findViewById(R.id.kofsection_imageview);

        mMedalIcon = findViewById(R.id.kofsection_medal);

        mArrowsIcon = findViewById(R.id.kofsection_arrows);

        mMuscledIcon = findViewById(R.id.kofsections_muscles);

        mHandsIcon = findViewById(R.id.kofsections_hands);

        mHumanIcon = findViewById(R.id.kofsections_human);

        mLikeButton = findViewById(R.id.kofsection_heart);
            mLikeButton.setOnClickListener(v-> OnLikePressed());

        mShareButton = findViewById(R.id.kfsection_share_btn);
            mShareButton.setOnClickListener(v -> onSharePressed());

        mInscripcionButton = findViewById(R.id.kof_section_inscripcion_boton);
            mInscripcionButton.setOnClickListener(v-> onInscripcion());

        mTemarioTituloTv = findViewById(R.id.kofsection_temario);
    }

    /**
     * Método que muestra el DialogFragment para que se pueda realizar la inscripción de un usuario a un programa de aprendizaje.
     * */
    private void onInscripcion()
    {
        FragmentManager fm = getSupportFragmentManager();
        DialogInscripcion dialog = DialogInscripcion.newInstance(mProgramID, mTituloSeccion);
        dialog.setListener(this);
        dialog.show(fm, "Inscripcion");
    }

    /**
     * Método que inicializa el RecyclerView que contendrá a todos los objetos de aprendizaje e inicializa los Arraylist de Objetos
     * con información vacía, además de que se obtiene el ID del programa para poder traer el detalle y los objetos asociados al mismo.
     * */
    private void initRecyclers() {
        mTitles = new ArrayList<>();
        mData = new ArrayList<>();
        mRecyclerObjetosAprendizaje = findViewById(R.id.kofsection_recycler);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new TemarioAdapter(mTitles, mData, this);
        mAdapter.setOnHomeListener(this);
        mRecyclerObjetosAprendizaje.setLayoutManager(LayoutManager);
        mRecyclerObjetosAprendizaje.setAdapter(mAdapter);
         //Esto es para el deep Link
        Intent intent = getIntent();
        Uri data = intent.getData();
        String idProgramaDeepLink = null;
        if (data != null){
             idProgramaDeepLink = data.getQueryParameter("dataProgram");
            if(SharedPreferencesUtil.getInstance().getTokenUsuario() == null || SharedPreferencesUtil.getInstance().getTokenUsuario().equals(""))
                {
                    Intent aLogin = new Intent(this, LoginActivity.class);
                    startActivity(aLogin);
                    finish();
                }
        }
        mProgramID = (idProgramaDeepLink != null) ? Integer.parseInt(idProgramaDeepLink) : getIntent().getIntExtra("id", 0);
        mPresenter.OnInteractorCallProgramDetail(mProgramID, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * Función que se ejecuta cuando el usuario presiona el ícono de corazón para dar like
     * */
    private void OnLikePressed()
    {
        mLikeButton.animate().scaleX(0.2f);
        mLikeButton.animate().scaleY(0.2f);
        mPresenter.OnInteractorLike(mProgramID, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(isTaskRoot())
            {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        else
            {
                onBackPressed();
            }

        return true;
    }

    /**
     * Interfaz implementada de el Adaptador de Objetos de aprendizaje, con la que se manipula qué vista de objeto de aprendizaje se
     * abrirá de acuerdo al tipo que sea éste.
     * @param data la información de todos los objetos de aprendizaje asociados a dicho programa.
     * */
    @Override
    public void onResponseListener(ObjectData data) {
        Intent sendTo;
        int tipoObjeto = Integer.parseInt(data.getType());
        switch(tipoObjeto)
            {
                case GIF:
                        sendTo = new Intent(getApplicationContext(), GIFActivity.class);
                        break;
                case DOC_PDF:
                        sendTo = new Intent(getApplicationContext(), PDFActivity.class);
                        break;
                case EBOOK:
                        sendTo = new Intent(getApplicationContext(), EbookActivity.class);
                        break;
                case VEDEO:
                        sendTo = new Intent(getApplicationContext(), VideoPlayerMainActivity.class);
                        break;
                case VEDEO_INTERACTIVO:
                        sendTo = new Intent(getApplicationContext(), VideoInteractivoMainActivity.class);
                        break;
                case DOC_WORD:
                case POWER_POINT:
                case EXCEL:
                case CHECKLIST:
                case ENCOESTA:
                    sendTo = new Intent(this, ObjetoAprendizajeActivity.class);
                    break;
                case EVALUACION:
                case JUEGO_MULTIJUGADOR:
                case JUEGO:
                case ENLACE:
                    sendTo = new Intent(this, ObjetoAprendizajeActivity.class);
                        sendTo.putExtra("programa", mProgramID);
                    break;
                case CONFERENCIA:
                        sendTo = new Intent(this, VideoConferenciaActivity.class);
                    break;
                case VEDEO_ASESORIA_CON_EXPERTO:
                        sendTo = new Intent(this, VedeoCharlaConExpertoActivity.class);
                    break;
                case COMIC:
                        sendTo = new Intent(this, null);
                    break;
                default:
                   /* sendTo = new Intent(this, VideoConferenciaActivity.class);
                    sendTo.putExtra(ID_OBJECT_KEY, data.getIdObject());
                    sendTo.putExtra(ObjetoAprendizajeActivity.TIPO_OBJETO_KEY, tempType);
                    startActivity(sendTo);*/
                    sendTo = null;
                   // Toast.makeText(this, "En desarrollo", Toast.LENGTH_SHORT).show();
            }
        if(sendTo!= null)
            {
                sendTo.putExtra(ID_OBJECT_KEY, data.getIdObject());
                sendTo.putExtra(ObjetoAprendizajeActivity.TIPO_OBJETO_KEY, tipoObjeto);
                sendTo.putExtra(TITULO_PROGRAMA_KEY, mTituloSeccion);
                startActivity(sendTo);
            }
    }

    /**
     * Método que se ejecuta cuando se obtienen todos los datos del detalle de programa, colocandolos en su respectivo elemento
     * y llamando traer todos los objetos de aprendizaje asociados al programa
     * @param data toda la información del detalle de programa proveniente del Web Service.
     * */
    @SuppressLint("SetTextI18n")
    private void setDataDetallePrograma(ProgramDetailResponseData data)
    {
        mSwipe.setRefreshing(false);
        //final long ESTIMATED_TIME = data.getPrograms().getFechaInicio();
        final long ESTIMATED_TIME = data.getPrograms().getFechaFin() ;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(ESTIMATED_TIME);
        String date = DateFormat.format("dd/MMM/yyyy", cal).toString();

       /* long millisecond = Long.parseLong(ESTIMATED_TIME);
       // long milisecond_estimated_time = Long.parseLong(DATE_END);

        // or you already have long value of date, use this instead of milliseconds variable.
        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MMM/yyyy");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String vigenciaUTC;
        try
        {
            Date parsed = sourceFormat.parse(new Date(millisecond).toString());
            TimeZone tz = TimeZone.getTimeZone("GMT-5:00");
            sourceFormat.setTimeZone(tz);

            String result = sourceFormat.format(parsed);
            vigenciaUTC = result;
        }
        catch (ParseException ex)
        {
            vigenciaUTC = DateFormat.format("dd/MMM/yyyy", new Date(millisecond)).toString();
        }
*/
       // String estimatedTimeh = DateFormat.format("hh", new Date(milisecond_estimated_time)).toString();

        mTitleTv.setText(data.getPrograms().getTituloImagen());

        mTituloSeccion = data.getPrograms().getTituloImagen();

        mDateTv.setText(getResources().getString(R.string.vigencia) + " " +
                (data.getPrograms().isPermanente()
                    ? getResources().getString(R.string.permanente)
                    : date));

        String estimatedTimeh =
                data.getPrograms().getTiempoEstimado();//GlobalActions.getTimeFromstring(data.getPrograms().getTiempoEstimado() + ":00", 0);
        mEstimatedTimeTV.setText(getResources().getString(R.string.estimated_time_data, estimatedTimeh));

        mCorcholataCount.setText(data.getPrograms().getmCorcholatas() + "");
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + data.getPrograms().getImagenPrograma();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(this)
                .load(glideUrl).apply(new RequestOptions().override(400, 750))
                .placeholder(R.drawable.sin_imagen)
                .error(R.drawable.sin_imagen)
                .into(mHeaderImage);
//        Picasso.with(this).load(fullPath).error(R.drawable.sin_imagen).resize(480, 720).into(mHeaderImage);

        if(data.getPrograms().isExcelnciaOperativa())
            {
                mMedalIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_excelencia_v2_red));
            }
        if(data.getPrograms().isDecisores())
            {
                mArrowsIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_decisores_v2_red));
            }
        if(data.getPrograms().isMentalidad())
            {
                mMuscledIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mundo_mamadisimo_v2_red));
            }
        if(data.getPrograms().isGente())
            {
                mHandsIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_primero_v2_red));
            }
        if(data.getPrograms().isFoco())
            {
                mHumanIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_foco_v2_red));
            }
        if(data.getPrograms().getLike() == 1 )
            {
                mLikeButton.setImageDrawable(getDrawable(R.mipmap.ic_full_heart));
                mIsLiked = true;
            }

        String text = GlobalActions.webViewConfigureText(data.getPrograms().getContenido());
        mDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        mMainView.setVisibility(View.VISIBLE);

        mTemarioTituloTv.setVisibility((data.getPrograms().isInscrito()) ? View.VISIBLE : View.GONE);

        mInscripcionButton.setVisibility((data.getPrograms().isInscrito()) ? View.GONE : View.VISIBLE);

        mRecyclerObjetosAprendizaje.setVisibility((data.getPrograms().isInscrito()) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.OnInteractorCallLearningObject(mProgramID, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /***
     * Función que se ejecuta cuando se presiona el botón de compartir
     */
    private void onSharePressed(){
        Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("text/plain");
            String tituloContenido = mTituloSeccion;
            String contenidoACompartir = RequestManager.API_URL + "apifemsa/homeCards?dataProgram="+mProgramID;
            compartir.putExtra(Intent.EXTRA_SUBJECT, tituloContenido);
            compartir.putExtra(Intent.EXTRA_TEXT, contenidoACompartir);
            startActivity(Intent.createChooser(compartir, getResources().getString(R.string.compartir)));
    }

    @Override
    public void getProgramDetailSuccess(ProgramDetailResponseData data) {
        setDataDetallePrograma(data);
    }

    @Override
    public void likeSuccess() {
        int tempDrawable = (mIsLiked) ? R.mipmap.ic_heart : R.mipmap.ic_full_heart;
        int tempString = (mIsLiked) ? R.string.youdislike : R.string.youlike;
        mLikeButton.setImageDrawable(getResources().getDrawable(tempDrawable));
        mLikeButton.animate().scaleY(1f);
        mLikeButton.animate().scaleX(1f);
        //Toast.makeText(this, getString(tempString) +  " " + mTitleTv.getText().toString(), Toast.LENGTH_SHORT).show();
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTitleTv.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }

    @Override
    public void showLoader() {
        try
        {
            if(loader != null && !loader.isAdded())
            {
                loader.show(getSupportFragmentManager(),"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
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
    public void getObjectsSuccess(ObjectResponseData data) {
        mSwipe.setRefreshing(false);
        if(data.getCategories().getAllObjects().size() < 1)
            {
                FragmentManager fm = getSupportFragmentManager();
                SinContenidoDialog scd = SinContenidoDialog.newInstance();
                scd.setListener(this);
                scd.show(fm, "SinContenido501");
            }
        else
            {
                mData.clear();
                mTitles.clear();
                mData.addAll(data.getCategories().getAllObjects());
                mTitles.addAll(data.getCategories().getAllTitles());
                mAdapter.notifyDataSetChanged();
            }
    }

    /**
     * Muestra un SnackBar con el mensaje de error.
     * @param tipoMensaje
     */
    @Override
    public void onMostrarMensaje(int tipoMensaje) {
        mSwipe.setRefreshing(false);
        if(tipoMensaje == DialogManager.NO_CONTENT || tipoMensaje == DialogManager.EMPTY)
            {
                FragmentManager fm = getSupportFragmentManager();
                SinContenidoDialog scd = SinContenidoDialog.newInstance();
                scd.setListener(this);
                scd.show(fm, "SinContenido501");
            }
        else
            {
                DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
            }
    }

    /**
     * Muestra un SnackBar con el mensaje de error, cuando éste sea unesperado
     * @param tipoMensaje
     * @param codigoError
     */
    @Override
    public void onMostrarMensajeInesperado(int tipoMensaje, int codigoError) {
        mSwipe.setRefreshing(false);
        if(codigoError == 501)
            {
                FragmentManager fm = getSupportFragmentManager();
                SinContenidoDialog scd = SinContenidoDialog.newInstance();
                scd.setListener(this);
                scd.show(fm, "SinContenido501");
            }
        else
        {
            DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoError);
        }
    }

    /**
     * Método a llamar cuando se realize una correcta inscrpción al programa.
     */
    @Override
    public void onInscripcionExitosa() {

    }

    @Override
    public void onNoAuth() {
        mSwipe.setRefreshing(false);
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onNoInternet() {
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
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void OnSinContenidoCloseDialog() {
        this.finish();
    }

    @Override
    public void OnInscripcionDialogExitosa() {
        mInscripcionButton.setVisibility(View.GONE);
        mTemarioTituloTv.setVisibility(View.VISIBLE);
        mRecyclerObjetosAprendizaje.setVisibility(View.VISIBLE);
    }


    public static class ObjetosAprendizaje{
        public static final int DOC_PDF = 1;
        public static final int EXCEL = 2;
        public static final int EBOOK = 3;
        public static final int GIF = 4;
        public static final int VEDEO = 5;
        public static final int VEDEO_INTERACTIVO= 6;
        public static final int DOC_WORD = 7;
        public static final int POWER_POINT = 8;
        public static final int ENCOESTA = 9;
        public static final int EVALUACION = 10;
        public static final int CHECKLIST = 11;
        public static final int COMIC = 12 ;
        public static final int CONFERENCIA = 13;
        public static final int ENLACE = 14;
        public static final int JUEGO = 15;
        public static final int VEDEO_ASESORIA_CON_EXPERTO = 16;
        public static final int JUEGO_MULTIJUGADOR = 17;
    }
}

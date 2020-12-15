package com.femsa.sferea.home.inicio.programa.objetosAprendizaje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.ConnectionState;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.DownloadManager;
import com.femsa.sferea.Utilities.DownloadManagerURL;
import com.femsa.sferea.Utilities.FileManager;
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
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist.ChecklistActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta.DetalleEncuestaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion.EvaluacionActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingDialogFragment;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.JuegosInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.JuegosPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.JuegosView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.DataGusanosEscaleras;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.MultiJugadorView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.MultijugadorInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.MultijugadorPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.RetarOponenteActivity;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static com.femsa.sferea.Utilities.FirebaseMessagingPush.RETADOR_ID_KEY;
import static com.femsa.sferea.Utilities.FirebaseMessagingPush.RETADOR_NOMBRE_KEY;
import static com.femsa.sferea.Utilities.StringManager.EXCEL_EXTENSION;
import static com.femsa.sferea.Utilities.StringManager.EXCEL_TYPE;
import static com.femsa.sferea.Utilities.StringManager.POWERPOINT_EXTENSION;
import static com.femsa.sferea.Utilities.StringManager.POWERPOINT_TYPE;
import static com.femsa.sferea.Utilities.StringManager.WORD_EXTENSION;
import static com.femsa.sferea.Utilities.StringManager.WORD_TYPE;
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
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.TITULO_PROGRAMA_KEY;

public class ObjetoAprendizajeActivity extends AppCompatActivity
        implements LearningObjectView, DownloadManagerURL.ONDownloadStatus,
        DescargaArchivoAsync.OnStatusDescarga,
        InsertObjetoAsyncTask.OnBDStatus, FileManager.onDescargaJuego,
        JuegosView, MultiJugadorView, DialogFragmentManager.OnDialogManagerButtonActions {

        private ImageView mDownloadButton;

        private ImageView mPreviewImage;

        private WebView mDescription;

        private Loader loader;

        private String mNombreJuego;

        private boolean mIsPushMultijugador = false;

        private TextView mTitleToolbar, mEstimatedTime, mCorcholatas, mTituloObjeto;

        private ImageView mLike;

        private ImageView mMaskasReadImg;

        private Button MaskAsReadButton;

        private ConstraintLayout mContenedorInformacion;

        private TextView mLabelInformacion;

        private GameLoadingDialogFragment fragment;

        private ConstraintLayout mRootView;

        private JuegosPresenter mJuegosPresenter;

        private int mVersion;

        private int mIDEmpleadoRetado = -1;

        //Banderas para el offline

        public static final String ID_OBJECT_KEY = "idObject";

        public static final String ID_PROGRAMA_KEY = "programa";

        public static final String IS_OFFLINE_FILE_KEY = "isOffline";

        public static final String OFFLINE_FILE_KEY = "OFFLINEfILE";

        private boolean mIsOfflineFile = false;

        private ObjetoAprendizajeEntity mObjetoEntity;

        private boolean mDescargarOffline = false;

        //Variables para el multijugador
        private int mIdPartida = 0, mUltimoTiro = 0, mIdRetador = 0;

        private CardView mBotonIniciarMultiPlayer;

        private TextView mTextoMultijugador;

        private DataGusanosEscaleras mDataGE;

        private boolean mYaJugueGusanos = false;

        //Lo normal
        private int mIDObject;

        private LearningObjectsPresenter mPresenter;

        private MultijugadorPresenter mMultijugadorPresenter;

        private FragmentManager fm;

        private DownloadManager dlw;

        private boolean mIsLiked = false;

        private boolean mMCagarFragment = false;

        private ImageView beginButton;

        int tipoObjeto = 0;

        private int mIdPrograma;

        private boolean mBackDescargable = false;

        public static final String TIPO_OBJETO_KEY = "tipoObjetoAprendizaje";

        private String mURL;

        private boolean mCompletado = false;

        private ObjectDetailResponseData mDataDetalle;

        //Para la BD
        private String mRutaArchivo, mRutaImagen;

        private String mRutaOffline;

        private String lastErrorDownload = "";

        private String mRutaMSOffline;

        private DBManager.ObjetoAprendizaje mBD;

        private String mNombrePrograma = "";

        private String mArchivoPath, mArchivoFilename, mImageName, mImagePath;

        private String mURLEnlace = "";


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_objetos_aprendizaje);
            bindData();
            bindviews();
            objectoChooser();
            initData();
            bindOnline(getIntent());
        }

        /**
         * <p>Método que configura la vista para el Juego Multijugador cuando se obtienen datos desde la Push
         * y se valida si se muestra o no el mensaje de aceptar reto.</p>
         * */
        private void bindOnline(Intent intent){
            //Datos para multijugador
            mIsPushMultijugador = intent.getStringExtra(RETADOR_NOMBRE_KEY) != null;
            if(mIsPushMultijugador){
                mIdRetador = intent.getIntExtra(RETADOR_ID_KEY, 0);
                String tempNombre = intent.getStringExtra(RETADOR_NOMBRE_KEY);
                LogManager.d("PUSHES","tempNombre: "  + tempNombre + " ID:" +mIDObject);
                // Cuando ya fuiste retado, el nombre llega como "0"
                // Si apenas estás siendo retado y no haz aceptado la invitación llega el nombre completo
                if(!tempNombre.equals("0") && tipoObjeto == JUEGO_MULTIJUGADOR && mIdPartida == 0){
                    abreMensajeReto(tempNombre);
                }
                bindData();
            }
        }

        /**
         * Método para habilitar o no el mensaje de visor para los tipos d eojeto de Microsoft
         * */
        private void objectoChooser()
            {
                mContenedorInformacion.setVisibility(View.VISIBLE);
                switch (tipoObjeto) {
                    case DOC_WORD:
                        mLabelInformacion.setText(getResources().getString(R.string.texto_informacion_microsoft, getResources().getString(R.string.documento_word)));
                        break;
                    case POWER_POINT:
                    case EXCEL:
                        mLabelInformacion.setText(getResources().getString(R.string.texto_informacion_microsoft, getnombreMS(tipoObjeto, this)));
                        break;
                    default:
                        mContenedorInformacion.setVisibility(View.GONE);
                }
            }

        /**
         * Función queregresa el nombre completo en formato String del objeto de aprendizaje.
         * @param tipo el ID del Objeto de aprendizaje.
         * @return el nombre en String del objeto.
         * */
        public String getnombreMS(int tipo, Context mContext){
            switch(tipo){
                case DOC_PDF:
                    return  mContext.getResources().getString(R.string.pdf);
                case EBOOK:
                    return  mContext.getResources().getString(R.string.ebook);
                case VEDEO:
                    return  mContext.getResources().getString(R.string.video);
                case VEDEO_INTERACTIVO:
                    return  mContext.getResources().getString(R.string.videoInteractivo);
                case GIF:
                    return  mContext.getResources().getString(R.string.gif);
                case CHECKLIST:
                    return  mContext.getResources().getString(R.string.checklist);
                case EVALUACION:
                    return  mContext.getResources().getString(R.string.evaluacion);
                case ENCOESTA:
                    return  mContext.getResources().getString(R.string.encuesta_toolbar_title);
                case COMIC:
                    return  mContext.getResources().getString(R.string.comic);
                case CONFERENCIA:
                    return  mContext.getResources().getString(R.string.video_conferencia);
                case VEDEO_ASESORIA_CON_EXPERTO:
                    return  mContext.getResources().getString(R.string.charla_con_experto);
                case ENLACE:
                    return  mContext.getResources().getString(R.string.enlace);
                case JUEGO:
                    return  mContext.getResources().getString(R.string.juegos);
                case DOC_WORD:
                    return mContext.getResources().getString(R.string.word);
                case EXCEL:
                    return mContext.getResources().getString(R.string.excek);
                case POWER_POINT:
                    return mContext.getResources().getString(R.string.powerpoint);
                case JUEGO_MULTIJUGADOR:
                    return mContext.getResources().getString(R.string.juego_multijugador);
                default:
                    return mContext.getResources().getString(R.string.indeterminado);
            }
        }

        /**
         * <p>Método que inicializa los datos de la vista ya sea si vienen desde la BD en modo offline
         * o si se llama el web service para traer datos.</p>
         * */
        private void initData(){
            if(mIsOfflineFile){
                configuraOffline();
            }
            else{
                if(mIDObject > 0) {
                    mPresenter.OnInteractorCallObjectDetail(mIDObject);
                }
            }
        }

        /**
         * Función que retorna el ID del ícono a usar para los objetos de aprendizaje.
         * @param tipo el ID del objeto de aprendizaje.
         * @return el ID del recurso gráfico a usar.
         * */
        private int chooseIcon(int tipo)
            {
                switch (tipo) {
                    case DOC_WORD:
                        return R.drawable.ic_word;
                    case EXCEL:
                        return R.drawable.ic_excel;
                    case POWER_POINT:
                        return R.drawable.ic_powerpoint;
                    case CHECKLIST:
                    case EVALUACION:
                    case ENCOESTA:
                        return R.drawable.ic_encuesta;
                    case JUEGO:
                        return R.drawable.ic_juegos;
                    case VEDEO_ASESORIA_CON_EXPERTO:
                        return R.drawable.ic_charla_experto;
                    case CONFERENCIA:
                        return R.drawable.ic_vedeo_conferencia;
                    case JUEGO_MULTIJUGADOR:
                        mBotonIniciarMultiPlayer.setVisibility(View.VISIBLE);
                        beginButton.setVisibility(View.GONE);
                        return R.drawable.ic_add_celula;
                    default:
                        return R.drawable.ic_pdf;
                }
            }

        /**
         * <p>Método que obtiene los datos escenciales de la vista a través del intent, pudiendo venir de una actividad anterior,
         * una push, la base de datos, etc.</p>
         * */
        private void bindData()
        {
            LogManager.d("PUSHES", "bindData()");

            //Datos que se necesitan siempre
            mIdPrograma = getIntent().getIntExtra(ID_PROGRAMA_KEY, 0);

            mIDObject = getIntent().getIntExtra(ID_OBJECT_KEY, 0);

            tipoObjeto = getIntent().getIntExtra(TIPO_OBJETO_KEY, 0);

            LogManager.d("PUSHES", mIdPrograma + " - " + mIDObject + " - " + tipoObjeto);

            //Datos necesarios cuando es offline

            mNombrePrograma = getIntent().getStringExtra(TITULO_PROGRAMA_KEY);

            mIsOfflineFile = getIntent().getBooleanExtra(IS_OFFLINE_FILE_KEY, false);

            mObjetoEntity = (ObjetoAprendizajeEntity) getIntent().getSerializableExtra(OFFLINE_FILE_KEY);

            mPresenter = new LearningObjectsPresenter(this, new LearningObjectInteractor());

            mMultijugadorPresenter = new MultijugadorPresenter(this, new MultijugadorInteractor());

            mJuegosPresenter = new JuegosPresenter(this, new JuegosInteractor());

        }

        /**
         * <p>Método que obtiene y configura cada elemento a través de los ID.</p>
         * */
        private void bindviews(){
            mTituloObjeto = findViewById(R.id.objeto_aprendizaje_titulo_tv);

            loader = Loader.newInstance();

            fm = getSupportFragmentManager();

            mBotonIniciarMultiPlayer = findViewById(R.id.boton_multiplayer_cv);
                mBotonIniciarMultiPlayer.setOnClickListener(v-> onMultiPlayerClick());

            mMaskasReadImg = findViewById(R.id.mark_as_read_image_objeto);

            MaskAsReadButton = findViewById(R.id.mark_as_read_objeto_button);
                MaskAsReadButton.setOnClickListener(v-> mPresenter.OnInteractorCallBonus(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario()));


            mDownloadButton = findViewById(R.id.objeto_aprendizaje_download_button);
                mDownloadButton.setOnClickListener(v -> descargarObjeto());

            mRootView = findViewById(R.id.objeto_aprendizaje_root_view);
                mRootView.setVisibility(View.INVISIBLE);

            Toolbar toolbar = findViewById(R.id.objeto_videoconferencia_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            toolbar.setTitle("");
            toolbar.setSubtitle("");
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            beginButton = findViewById(R.id.objeto_aprendizaje_begin_button);
                beginButton.setOnClickListener(v -> onBeginPressed());
                beginButton.setImageDrawable(getDrawable(chooseIcon(tipoObjeto)));

            mDescription = findViewById(R.id.objeto_aprendizaje_description);

            mTitleToolbar = findViewById(R.id.objeto_aprendizaje_titulo_toolbar);
                mTitleToolbar.setText(getnombreMS(tipoObjeto, this));

            mEstimatedTime = findViewById(R.id.objeto_aprendizaje_estimado);

            mCorcholatas = findViewById(R.id.objeto_aprendizaje_corcholata_counter);

            mLike = findViewById(R.id.objeto_aprendizaje_like_iv);
                mLike.setOnClickListener(v -> OnLikePressed());

            mContenedorInformacion = findViewById(R.id.informacion_objeto);
            mLabelInformacion = findViewById(R.id.informacion_objeto_label);

            mPreviewImage = findViewById(R.id.objeto_aprendizaje_preview);

            mBD = new DBManager.ObjetoAprendizaje(this);

            mTextoMultijugador =  findViewById(R.id.estatus_multiplayer_tv);
        }

        /**
         * <p>Método que se ejecuta al hacer click sobre el botón de Start de Gusanos y Escaleras.
         * Dependerá del sttaus de la partida si el juego inicia o si el botón se inhabilita.
         * Desaparecerá cuando no se trate de un juego de Gusanos y Escaleras para 2 jugadores.
         * Si el ultimo tiro = mi ID significa que yo tiré por última vez y es turno del otro.
         * Si son diferentes es mi turno.</p>
         * */
        private void onMultiPlayerClick(){
            if(mIdPartida > 0 &&  mUltimoTiro != SharedPreferencesUtil.getInstance().getIdEmpleado()){
                mIDEmpleadoRetado = 0;
                descargaJuego();
                mTextoMultijugador.setText(getResources().getString(R.string.esperando_a_tu_rival));
                mBotonIniciarMultiPlayer.setEnabled(false);
                //finish();
            }
            else{
                Intent retarIntent = new Intent(this, RetarOponenteActivity.class);
                retarIntent.putExtra("idprograma", mIdPrograma);
                startActivityForResult(retarIntent, 1);
            }
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

        /**
         * Método que se ejecuta cuando se hace click sobre el botón de iniciar del objeto de aprendizaje, donde se llama al activity correspondiente a cada uno
         * y se marcan como vistos a excepción de la encuesta, checklist y evaluación, que se marcarán una vez terminen de responderlos.
         * */
        private void onBeginPressed()
        {
            if(mIsOfflineFile){
                DownloadManagerURL mDlw = new DownloadManagerURL(null, 0, null, getApplicationContext(), false);
                switch (tipoObjeto){
                    case JUEGO:
                        onJuegoYaDescargado();
                        break;
                    case DOC_WORD:
                        mDlw.abrirMicrosoft(mObjetoEntity.getRutaArchivo(), WORD_TYPE);
                        break;
                    case EXCEL:
                        mDlw.abrirMicrosoft(mObjetoEntity.getRutaArchivo(), EXCEL_TYPE);
                        break;
                    case POWER_POINT:
                        mDlw.abrirMicrosoft(mObjetoEntity.getRutaArchivo(), POWERPOINT_TYPE);
                        break;
                }
            }
            else
            {
                if(ConnectionState.getTipoConexion(AppTalentoRHApplication.getApplication().getApplicationContext()) != ConnectionState.NO_NETWORK)
                    {
                        mMCagarFragment = true;
                        if(tipoObjeto != EVALUACION && tipoObjeto != ENCOESTA && tipoObjeto != CHECKLIST && mIDObject > 0 && mIdPrograma >= 0 && !mCompletado)
                        {
                            mPresenter.OnInteractorMarkAsRead(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario());
                        }
                        Intent sendTo = null;
                        switch(tipoObjeto)
                        {
                            case CHECKLIST:
                                sendTo = new Intent(this, ChecklistActivity.class);
                                break;
                            case EVALUACION:
                                sendTo = new Intent(this, EvaluacionActivity.class);
                                sendTo.putExtra(ID_PROGRAMA_KEY, mIdPrograma);
                                break;
                            case ENCOESTA:
                                sendTo = new Intent(this, DetalleEncuestaActivity.class);
                                sendTo.putExtra(ID_PROGRAMA_KEY, mIdPrograma);
                                sendTo.putExtra(ID_OBJECT_KEY, mIDObject);
                                startActivityForResult(sendTo, 1);
                                break;
                            case ENLACE:
                                abrirEnlace(mURLEnlace);
                                break;
                            case JUEGO:
                                descargaJuego();
                                break;
                            case DOC_WORD:
                                onObjetoMS(WORD_TYPE);
                                break;
                            case EXCEL:
                                onObjetoMS(EXCEL_TYPE);
                                break;
                            case POWER_POINT:
                                onObjetoMS(POWERPOINT_TYPE);
                                break;
                        }
                        if(sendTo != null && tipoObjeto != ENCOESTA)
                        {
                            sendTo.putExtra(ID_OBJECT_KEY, mIDObject);
                            startActivity(sendTo);
                        }
                }
                else {
                    DialogManager.displaySnack(getSupportFragmentManager(), StringManager.NO_INTERNET);
                }
            }
        }

        /**
         * <p>Método que descarga el juego, comprobando primero si ya está descargado (para no tener que descargar de nuevo)
         * y verificando que sea la misma versión, en caso de ser una nueva versión se descargará y se sobreescribirá
         * con la nueva información.</p>
         * */
        private void descargaJuego(){
            if(FileManager.directoryExists(mIDObject) && FileManager.isCurrentVersion(mIDObject, mVersion))
            {
                onJuegoYaDescargado();
            }
            else{
                mPresenter.OnInteractorCallJuego(mIDObject);
                mBackDescargable = true;
            }
        }

        /**
         * <p>Método que recibe un tipo de Objeto de Microsoft (Word, Excel o Powerpoint)
         * y verifica si está instalada la aplicación correspondiente al objeto,
         * en caso de estar instalada la app, descarga el archivo y lo abre con la misma,
         * en caso contrario abre la PlayStore para descargar la app.</p>
         * @param objeto identificador del Objeto de Microsoft.
         * */
        private void onObjetoMS(String objeto){
            if(isMicrosoftClientInstalled(this, objeto)){
                DescargaObjetosMicrosoft(mURL, objeto);
            }
            else {
                goToMarket(this, objeto);
            }
        }

        /**
         * <p>Función que retorna la extensión asociada al objeto de Microsoft.</p>
         * @param objeto Tipo de objeto/archivo del que queremos la extensión.
         * @return  extensión del objeto, sea .pptx, .xlsx, docx, etc.
         * */
        private String getExtension(String objeto){
            switch (objeto)
                {
                    case WORD_TYPE:
                        return WORD_EXTENSION;
                    case EXCEL_TYPE:
                        return EXCEL_EXTENSION;
                    case POWERPOINT_TYPE:
                        return POWERPOINT_EXTENSION;
                    default:
                        return "";
                }
        }

        /**
         * <p>Método que permite la descarga del archivo de Microsoft el cual existe en el servidor</p>
         * @param url URL del servidor en donde se aloja el archivo a descargar.
         * @param objeto Tipo de archivo de MS que se va a descargar.
         * */
        public void DescargaObjetosMicrosoft(String url, String objeto){
            DownloadManagerURL dwlmM = new DownloadManagerURL(getExtension(objeto), mIDObject, objeto, this, mDescargarOffline);
            dwlmM.setListener(this);
            dwlmM.execute(url);
            dwlmM.onPostExecute("1");
        }


    /**
     * <p>Método que manda a iniciar un juego una vez que ha terminado la descarga o ya se encontraba en la memoria del dispositivo.
     * Además llama la petición de actualizar sesion para evitar que ésta expire mientras el usuario juega.
     * Valida si el mIDEmpleadoRetado es mayor a 0 para decidir si inicia un juego clásico o un juego Multijugador</p>
     * */
    private void onJuegoYaDescargado(){
        if(mIDEmpleadoRetado >= 0)
            {
                FileManager.iniciarJuegoYaDescargadoMultiJugador(mIDObject, mDataGE);
                mYaJugueGusanos = true;
                LogManager.d("Gusanos", mDataGE.getIdEmpleadoRetado() + " vs " + mDataGE.getIdPartida());
            }
        else
            {
                FileManager.iniciarJuegoYaDescargado(mIDObject);
            }
        mJuegosPresenter.onCallActualizaSesion(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }



    /**
     * <p>Funciín booleana que valida si ya se encuentra instalada la aplicación de Microsoft en el dispositivo</p>
     * @param myContext contexto de la aplicación.
     * @param nombre tipo del objeto de Microsoft.
     * @return true si se encuentra instalada, false en caso contrario.
     * */
    public boolean isMicrosoftClientInstalled(Context myContext, String nombre) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try
        {
            myPackageMgr.getPackageInfo("com.microsoft.office." + nombre, PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return (false);
        }
        return (true);
    }

    /**
     * Método que configura la vista cuando se está cargando un objeto descargado en modo Offline.
     * */
    private void configuraOffline(){
        mRootView.setVisibility(View.VISIBLE);
        mNombreJuego = mObjetoEntity.getNombreObjeto().trim().replace(" ", "").toLowerCase();
        mTitleToolbar.setText(getnombreMS(tipoObjeto, this));
        mTituloObjeto.setText(mObjetoEntity.getNombreObjeto());
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

        GlideApp.with(this).load(mObjetoEntity.getRutaImagen()).
                centerCrop().placeholder(R.drawable.sin_imagen).error(R.drawable.sin_imagen)
                .into(mPreviewImage);

        mDownloadButton.setVisibility(View.GONE);

        mMaskasReadImg.setVisibility(View.INVISIBLE);
        MaskAsReadButton.setVisibility(View.INVISIBLE);
        mDataGE = new DataGusanosEscaleras(0,0,0,0,0, -1, 0, 0);
    }

    /**
     * Install the Skype client through the market: URI scheme.
     */
    public void goToMarket(Context myContext, String cadena) {
        Uri marketUri = Uri.parse("market://details?id=com.microsoft.office." + cadena);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
    }

    /**
     * <p>Método que llama al navegador para abrir la URL especificada.</p>
     * @param url URL que se desea abrir.
     * */
    private void abrirEnlace(String url){
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * <p>Método que muestra el DialogManager para aceptar o rechazar un reto de gusanos y escaleras.</p>
     * */
    private void abreMensajeReto(String nombreRetador){
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.te_ha_retado, nombreRetador), getResources().getString(R.string.reto_titulo), false, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                if(data.getStringExtra("encoesta") != null && data.getStringExtra("encoesta").equals("encoesta"))
                    {
                        mPresenter.OnInteractorMarkAsRead(mIDObject, SharedPreferencesUtil.getInstance().getTokenUsuario());
                    }
                else if(data.getIntExtra("juego", -1) == JUEGO_MULTIJUGADOR)
                    {
                        mIDEmpleadoRetado = data.getIntExtra("idEmpleado", 0);
                        mDataGE.setIdEmpleadoRetado(mIDEmpleadoRetado);
                        mMultijugadorPresenter.onCallEnviarReto(SharedPreferencesUtil.getInstance().getTokenUsuario(), mIDEmpleadoRetado, mIDObject, mIdPrograma);
                        mTextoMultijugador.setText(getResources().getString(R.string.esperando_a_tu_rival));
                        mBotonIniciarMultiPlayer.setEnabled(false);
                        //descargaJuego();
                    }
                else{}
            }
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
        LikeDialog likeDialogo = LikeDialog.newInstance(getString(tempString) +  " " + mTituloObjeto.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        likeDialogo.show(fm, "LikeDialog");
        mIsLiked = !mIsLiked;
    }

    @Override
    public void getObjectDetailSuccess(ObjectDetailResponseData data) {

        mVersion = data.getCategories().getVersion();

        mNombreJuego = data.getCategories().getmObjectName().trim().replace(" ", "").toLowerCase();

        mTitleToolbar.setText(getnombreMS(tipoObjeto, this));
        mTituloObjeto.setText(data.getCategories().getmObjectName());
        String minutosEstimados;
        minutosEstimados = getResources().getString(R.string.estimated_time) + " " + data.getCategories().getmEstimatedTime();
        mEstimatedTime.setText(minutosEstimados);
        mURL =  RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getFilename();
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
        mCompletado = data.getCategories().ismStatusBonus() && data.getCategories().isStatusSeriado();
        mMaskasReadImg.setImageDrawable((mCompletado) ? getResources().getDrawable(R.drawable.ic_red_checkmark) : getResources().getDrawable(R.drawable.ic_gray_checkmark));
        MaskAsReadButton.setText((mCompletado) ? getResources().getString(R.string.completado) : getResources().getString(R.string.marcar_completado));
        MaskAsReadButton.setTextColor((mCompletado) ? getResources().getColor(R.color.femsa_red_d) : getResources().getColor(R.color.femsa_gray_b));
        MaskAsReadButton.setEnabled(data.getCategories().isStatusSeriado());
        mRootView.setVisibility(View.VISIBLE);

        mArchivoPath =  RequestManager.ARCHIVO_PROGRAMA_URL + data.getCategories().getFilename();
        mArchivoFilename = data.getCategories().getFilename();
        //Picasso.with(this).load(RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject()).error(R.drawable.sin_imagen).into(mPDFView);

        mImagePath = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + data.getCategories().getmImageObject();
        mImageName = data.getCategories().getmImageObject();

        mDataDetalle = data;

        //mDataGE = new DataGusanosEscaleras(1,5,8,1,1, mIDEmpleadoRetado, data.getCategories().getGEIDPartida());
        mDataGE = new DataGusanosEscaleras(
                data.getCategories().getGEUsado(),
                data.getCategories().getGEPosJ1(),
                data.getCategories().getGEPosJ2(),
                data.getCategories().getGEMapa(),
                data.getCategories().getGETurno(),// ? 2 : 1, //1 mueve el verde, 2 mueve el rojo
                data.getCategories().isRetador() ? 1 : 0,
                data.getCategories().getGEIDPartida(),
                mIdPrograma
                );

        if(!data.getCategories().isRetador()){
            mDataGE.setPosJ1(data.getCategories().getGEPosJ2());
            mDataGE.setPosJ2(data.getCategories().getGEPosJ1());
        }

        //Si el ultimo tiro = mi ID significa que yo tiré por última vez y es turno del otro.
        //Si son diferentes es mi turno.
        mIdPartida = data.getCategories().getGEIDPartida();
        mUltimoTiro = data.getCategories().getUltimoTiro();
        if(data.getCategories().getGEIDPartida() == 0){
            mTextoMultijugador.setText(getResources().getString(R.string.retar_label));
        }
        else if(data.getCategories().getUltimoTiro() > 0 && data.getCategories().getUltimoTiro() != SharedPreferencesUtil.getInstance().getIdEmpleado()){
            mTextoMultijugador.setText(getResources().getString(R.string.es_tu_turno));
        }
        else{
            mTextoMultijugador.setText(getResources().getString(R.string.esperando_a_tu_rival));
            mBotonIniciarMultiPlayer.setEnabled(false);
        }

        //código para validar el multijugador desde el detalle
        String tempNombreRetador = data.getCategories().getNombreRetador();
        if(tempNombreRetador != null && !tempNombreRetador.equals("null")
            && !tempNombreRetador.equals("0") && tipoObjeto == JUEGO_MULTIJUGADOR && mIdPartida == 0){
            abreMensajeReto(tempNombreRetador);
        }

        mIdRetador = data.getCategories().getIdRetador();
        mURLEnlace = data.getCategories().getmURLEnlace();
    }

    @Override
    public void showLoader() {
        if(tipoObjeto == JUEGO && mMCagarFragment){
            try{
                fragment = GameLoadingDialogFragment.newInstance();
                loader.setCancelable(true);
                fragment.show(getSupportFragmentManager(), "Loading");
            }
            catch (Exception ignored){            }
        }
        else{
            showGenericLoader();
        }
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void hideLoader() {
            hideGenericLoader();
    }

    @Override
    public void muestraMensaje(int tipoMensaje) {
        cierraFragmentLoader();
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {
        cierraFragmentLoader();
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
       // Toast.makeText(this, "Descargando...", Toast.LENGTH_SHORT).show();
        if(mBackDescargable){
                dlw = new DownloadManager();
                dlw.execute(new JuegosTaskParams(buffer, mIDObject, zip, mNombreJuego, mDescargarOffline,this, this, mVersion, mDataGE));
                mDescargarOffline = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMCagarFragment = false;
        cierraFragmentLoader();
        hideGenericLoader();
    }

    /**
     * <p>Método que busca entre todos los Fragments existentes/activos una instancia del DialogManager de "Cargando Juego..."
     * para cerrarlo.</p>
     * */
    private void cierraFragmentLoader(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment  instanceof GameLoadingDialogFragment){
                ((GameLoadingDialogFragment) fragment).dismiss();
            }

        }
    }

    /**
     * <p>Método para abrir el loader de manera genérica</p>
     * */
    private void showGenericLoader(){
        loader = Loader.newInstance();
        try
        {
            if(!loader.isAdded())
            {
                loader.dismiss();
                loader.show(fm,"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    /**
     * <p>Método para ocultar el loader.</p>
     * */
    private void hideGenericLoader(){
        try
        {
            Fragment prev = getSupportFragmentManager().findFragmentByTag("loader");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }
        }
        catch (Exception ex)
        {
            LogManager.d("Loaderss", ex.getMessage());
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void onDownloadURLBegin() {
        showLoader();
    }

    @Override
    public void onDownloadURLEnd() {
        hideLoader();
    }

    @Override
    public void onDownloadBegin() {
        showLoader();
    }

    @Override
    public void onDownloadEnd(String uri, int tipo) {
            if(tipo == DescargaArchivoAsync.IMAGEN){
                mRutaImagen = uri;
            }
           if(tipo == DescargaArchivoAsync.IMAGEN && tipoObjeto != JUEGO && tipoObjeto !=  DOC_WORD && tipoObjeto != EXCEL && tipoObjeto != POWER_POINT){
               mRutaImagen = uri;
            DescargaArchivoAsync filedlw = new DescargaArchivoAsync();
            filedlw.setListener(this);
            filedlw.execute(
                    new DescargasParams(
                            DescargaArchivoAsync.ARCHIVO,
                            mArchivoFilename,
                            mArchivoPath)
            );
        }
        else{
            mRutaArchivo =
                    (tipoObjeto == JUEGO || tipoObjeto == EXCEL || tipoObjeto ==  DOC_WORD || tipoObjeto == POWER_POINT)
                            ? mRutaOffline :
                            uri;
            InsertObjetoAsyncTask baseDBAsync = new InsertObjetoAsyncTask();
            baseDBAsync.setListener(this);
            baseDBAsync.setData(mDataDetalle);
            baseDBAsync.setRutas(mRutaArchivo, mRutaImagen, mNombrePrograma);
            baseDBAsync.execute(mBD);
        }
        hideLoader();
    }

    @Override
    public void onDownloadError(String error) {
        hideLoader();
        if(!error.equals(lastErrorDownload)){
            DialogManager.displaySnack(getSupportFragmentManager(), error);
        }
        lastErrorDownload = error;
    }

    @Override
    public void onDownloadURLError(String error) {
        hideLoader();
        DialogManager.displaySnack(getSupportFragmentManager(), error);
    }

    @Override
    public void onDownloadURLEndForOffline(String ruta) {
        mDescargarOffline = false;
        mRutaOffline = ruta;
        DescargaArchivoAsync dlw = new DescargaArchivoAsync();
        dlw.setListener(this);
        dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mImagePath));
    }

    /**
     * <p>Método que inicia la descarga de un objeto cuando se hace click sobre el botón de Descargar Objeto,
     * validando primero la conectividad del dispositivo para inicializar la descarga,</p>
     * */
    private void descargarObjeto(){
        if(ConnectionState.getTipoConexion(this) != ConnectionState.NO_NETWORK){
            if(tipoObjeto == JUEGO){
                mPresenter.OnInteractorCallJuego(mIDObject);
                mBackDescargable = true;
                mDescargarOffline = true;
            }
            else if(tipoObjeto == DOC_WORD || tipoObjeto == EXCEL || tipoObjeto == POWER_POINT){
                mDescargarOffline = true;
                switch (tipoObjeto){
                    case DOC_WORD:
                        onObjetoMS(WORD_TYPE);
                        break;
                    case EXCEL:
                        onObjetoMS(EXCEL_TYPE);
                        break;
                    case POWER_POINT:
                        onObjetoMS(POWERPOINT_TYPE);
                }
            }
            else{
                DescargaArchivoAsync dlw = new DescargaArchivoAsync();
                dlw.setListener(this);
                dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mImagePath));
            }
        }
        else{
            DialogManager.displaySnack(getSupportFragmentManager(), StringManager.NO_INTERNET);
        }
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

    @Override
    public void onJuegoDescargadoOffline(String rutajuego) {
        mRutaOffline = rutajuego;
        DescargaArchivoAsync dlw = new DescargaArchivoAsync();
        dlw.setListener(this);
        dlw.execute(new DescargasParams(DescargaArchivoAsync.IMAGEN, mImageName,mImagePath));
    }

    @Override
    public void onSesionActualizada() {
        LogManager.d("Juegos", "ObjetoAprendizajeActivity.java: Sesion actualizada");
    }

    @Override
    public void onMuestraMensaje(int tipo) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipo);
    }

    @Override
    public void onMuestraMensaje(int tipo, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipo, codigo);
    }

    @Override
    public void onCargaListadoRetadores(RetadorResponseData data) {
        //
    }

    @Override
    public void onSinRetadores() {
//
    }

    @Override
    public void onNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onMuestraLoader() {
        if(tipoObjeto == JUEGO && mMCagarFragment){
            try{
                fragment = GameLoadingDialogFragment.newInstance();
                loader.setCancelable(true);
                getSupportFragmentManager().executePendingTransactions();
                fragment.show(getSupportFragmentManager(), "Loader2");
            }
            catch (Exception ignored){

            }

        }
        else{
            showGenericLoader();
        }
    }

    @Override
    public void onQuitaLoader() {
        hideGenericLoader();
    }

    @Override
    public void OnRetoEnviado() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.reto_enviado));
        mTextoMultijugador.setText(getResources().getString(R.string.esperando_a_tu_rival));
        new CountDownTimer(3000, 1000){
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
    public void OnRetoAceptado() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.reto_aceptado));
        mTextoMultijugador.setText(getResources().getString(R.string.esperando_a_tu_rival));
        new CountDownTimer(3000, 1000){
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
    public void OnDialogAceptarClick() {
        mMultijugadorPresenter.onCallAceptarReto(mIdRetador, mIDObject);
    }

    @Override
    public void OnDialogCancelarClick() {
        //Toast.makeText(this, "Rechazaste reto", Toast.LENGTH_SHORT).show();
    }


    /**
     * Clase estática que contiene los parámetros para niciar un juego de MasterKiwi
     * */
    public static class JuegosTaskParams {
        int buffer;
        int idObjeto;
        InputStream zip;
        String nombreJuego;
        Context mContext;
        private boolean mOffLine;
        private int version;
        private FileManager.onDescargaJuego mListenerJuego;
        private DataGusanosEscaleras mDataGE;

        JuegosTaskParams(int buffer, int idObjeto, InputStream zip,
                         String nombreJuego, boolean offline, Context context,
                         FileManager.onDescargaJuego listener, int version, DataGusanosEscaleras data) {
            this.buffer = buffer;
            this.idObjeto = idObjeto;
            this.zip = zip;
            this.nombreJuego = nombreJuego;
            this.version = version;
            mOffLine = offline;
            mContext = context;
            mListenerJuego = listener;
            mDataGE = data;
        }

        public DataGusanosEscaleras getDataGE() {
            return mDataGE;
        }

        public int getIdObjeto() {
            return idObjeto;
        }

        public int getBuffer() {
            return buffer;
        }

        public InputStream getZip() {
            return zip;
        }

        public Context getmContext() {
            return mContext;
        }

        public boolean isOffLine() {
            return mOffLine;
        }

        public FileManager.onDescargaJuego getListenerJuego() {
            return mListenerJuego;
        }

        public int getVersion() {
            return version;
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            if(mBackDescargable) {
                mBackDescargable = false;
                try{
                    dlw.cancel(true);
                }
                catch (Exception ignored){}
            }
    }

    /**
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bindOnline(intent);
        //Toast.makeText(this, "ola", Toast.LENGTH_SHORT).show();
    }
}



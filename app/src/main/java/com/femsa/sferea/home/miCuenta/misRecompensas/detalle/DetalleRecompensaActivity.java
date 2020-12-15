package com.femsa.sferea.home.miCuenta.misRecompensas.detalle;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaDTO;
import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.miCuenta.misRecompensas.dialog.MisRecompensasDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetalleRecompensaActivity extends AppCompatActivity implements View.OnClickListener,
        MisRecompensasDialogFragment.RecompensasListener, DetalleRecompensaView, DialogFragmentManager.OnDialogManagerButtonActions {

    private NestedScrollView mNsvLayout;
    private Toolbar mToolbar;
    private Loader mLoader;
    private RoundedImageView mRivImagen;
    private TextView mTvNombre;
    private TextView mTvPuntos;
    private TextView mTvVigencia;
    private TextView mTvDescripcion;
    private TextView mTvTerminosCondiciones;
    private Button mBtnSolicitar;
    private boolean mIsHistorial = false;
    private int idRecompensa;
    private int costoDeLaRecompensa = 0;

    private DetalleRecompensaPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la recompensa.
        idRecompensa = getIntent().getIntExtra("idRecompensa", 0);
        mIsHistorial = getIntent().getBooleanExtra("historial", false);
        setContentView(R.layout.activity_detalle_recompensa);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializePresenter();
    }

    private void bindViews(){
        mNsvLayout = findViewById(R.id.nsv_activity_detalle_recompensa);
        mToolbar = findViewById(R.id.toolbar_activity_detalle_recompensa);
        mLoader = Loader.newInstance();
        mRivImagen = findViewById(R.id.riv_activity_detalle_recompensa);
        mTvNombre = findViewById(R.id.tv_activity_detalle_recompensa_nombre);
        mTvPuntos = findViewById(R.id.tv_activity_detalle_recompensa_puntos);
        mTvVigencia = findViewById(R.id.tv_activity_detalle_recompensa_vigencia);
        mTvDescripcion = findViewById(R.id.tv_activity_detalle_recompensa_descripcion);
        mTvTerminosCondiciones = findViewById(R.id.tv_activity_detalle_recompensa_terminos_condiciones);
        mBtnSolicitar = findViewById(R.id.btn_activity_detalle_recompensa);
            mBtnSolicitar.setVisibility(mIsHistorial ? View.GONE : View.VISIBLE);
    }

    private void bindListeners(){
        mBtnSolicitar.setOnClickListener(this);
    }

    /**
     * <p>Método que crea el DialogFragment que se muestra al querer canjear una recompensa.</p>
     */
    private void initializeDialogFragment(){
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(false, false, true, AppTalentoRHApplication.getApplication().getString(R.string.fragment_mis_recompensas_dialog_success_title),
                AppTalentoRHApplication.getApplication().getString(R.string.fragment_mis_recompensas_dialog_success_descripcion), AppTalentoRHApplication.getApplication().getString(R.string.ok), "", false);
        fragment.setListener(this);
        fragment.show(getSupportFragmentManager(), "Error");
    }

    /**
     * <p>Método que crea el Presenter para iniciar la petición de obtener el detalle de una
     * recompensa.</p>
     */
    private void initializePresenter(){
        mPresenter = new DetalleRecompensaPresenter(this, new DetalleRecompensaInteractor());
        mPresenter.iniciarProcesoObtenerDetalleRecompensa(idRecompensa);
    }

    /**
     * <p>Método que configura la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_activity_detalle_recompensa){
            if(costoDeLaRecompensa <= SharedPreferencesUtil.getInstance().getCorcholatas())
                {
                    mPresenter.iniciarProcesoCanjearRecompensa(idRecompensa);
                }
            else
                {
                    DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.corcholatas_insuficientes));
                }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClickAceptar() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment!=null){
            MisRecompensasDialogFragment dialogFragment = (MisRecompensasDialogFragment) fragment;
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onViewObtenerDetalleRecompensa(DetalleRecompensaData data) {
        DetalleRecompensaDTO detalleRecompensa = data.getDetalle();

        //mRivImagen
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL+"/"+ SharedPreferencesUtil.getInstance().getTokenUsuario()+"/"+data.getDetalle().getNombreImagenRecompensa();
        GlideApp.with(AppTalentoRHApplication.getApplication())
                .load(fullPath)
                .error(R.drawable.sin_imagen)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(mRivImagen);


        SimpleDateFormat formatoActual = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SimpleDateFormat nuevoFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);


        mTvVigencia.setText(getResources().getString(R.string.activity_detalle_recompensa_vigencia,
                (detalleRecompensa.getVigencia().equals("null")) ? getResources().getString(R.string.permanente) :StringManager.parseFecha(detalleRecompensa.getVigencia(),formatoActual,nuevoFormato)));
        mTvNombre.setText(detalleRecompensa.getTituloRecompensa());
        mTvPuntos.setText(String.valueOf(detalleRecompensa.getValorRecompensa()));
        mTvDescripcion.setText(detalleRecompensa.getDescripcion());
        mTvTerminosCondiciones.setText(detalleRecompensa.getTerminos());
        costoDeLaRecompensa = detalleRecompensa.getValorRecompensa();
        mNsvLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewRecompensaObtenida() {
        initializeDialogFragment();
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(),mensaje);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(),mensaje, codigo);
    }


    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mLoader.show(fragmentManager, "loader");
    }

    @Override
    public void onViewOcultarLoader() {
        if (mLoader!=null && mLoader.isAdded()){
            mLoader.dismiss();
        }
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewRecompensaIncanjeable() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.incanjeable));
    }

    @Override
    public void onViewRecompensaAgotada() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.recompensa_agotada));
    }

    @Override
    public void onViewRecompensaInalcanzable() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.corcholatas_insuficientes));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void OnDialogAceptarClick() {
        finish();
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}

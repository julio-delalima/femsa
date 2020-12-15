package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador.ObtenerAreaYFacilitadorActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.CelulasEntrenamientoDialogFragment;

import java.util.ArrayList;

public class NoResponsableActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher,
        CelulasEntrenamientoDialogFragment.RechazarDialogListener, ActualizarFacilitadorView {

    private Loader mLoader;
    private Toolbar mToolbar;
    private EditText mEtNombreFacilitador;
    private CheckBox mCbNoConozcoResponsable;
    private Button mBtnEnviar;

    private FacilitadorDTO mFacilitador; //Objeto para obtener el nombre del facilitador suplente.
    private String nombreFacilitador = "";
    private boolean conozcoFacilitador = false;
    private int idSolicitud, idReceptor;
    private ArrayList<ParticipanteDTO> participantes;
    private ActualizarFacilitadorPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        idReceptor = getIntent().getIntExtra("receptor", 0);
        participantes = (ArrayList<ParticipanteDTO>) getIntent().getSerializableExtra("participante");
        setContentView(R.layout.activity_no_responsable);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializePresenter();
    }

    private void bindViews(){
        mLoader = Loader.newInstance();
        mToolbar = findViewById(R.id.toolbar_activity_no_responsable);
        mEtNombreFacilitador = findViewById(R.id.et_activity_no_responsable);
        mCbNoConozcoResponsable = findViewById(R.id.cb_activity_no_responsable);
        mBtnEnviar = findViewById(R.id.btn_activity_no_responsable);
    }

    private void bindListeners() {
        //mEtNombreFacilitador.addTextChangedListener(this);
        mEtNombreFacilitador.setKeyListener(null); //Para evitar que se pueda ingresar texto en el EditText.
        mEtNombreFacilitador.setOnClickListener(this);
        mCbNoConozcoResponsable.setOnCheckedChangeListener(this);
        mBtnEnviar.setOnClickListener(this);
    }

    /**
     * <p>Método que inicializa la Toolbar.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método que crea el Presenter para realizar la petición.</p>
     */
    private void initializePresenter(){
        mPresenter = new ActualizarFacilitadorPresenter(this, new ActualizarFacilitadorInteractor());
    }

    /**
     * <p>Método que valida que la información ingresada sea la correcta, antes de cerrar la
     * Activity.</p>
     */
    private void validarDatos(String nombreFacilitador, boolean conozcoFacilitador){
        //Caso cuando se marca el CheckBox y se ingresa texto en el EditText.
        if(conozcoFacilitador && nombreFacilitador.length()>0){
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.max_campos));
        }
        //Caso cuando no se marca el CheckBox y no se ingresa texto en el EditText.
        else if (!conozcoFacilitador && nombreFacilitador.length()<=0){
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.min_campos));
        }
        else{
            mPresenter.iniciarProcesoNoResponsable(nombreFacilitador, String.valueOf(idSolicitud), String.valueOf(SharedPreferencesUtil.getInstance().getIdEmpleado()), conozcoFacilitador);
        }

    }

    /**
     * <p>Método que crea el DialogFragment.</p>
     */
    private void initializeDialogFragment(){
        CelulasEntrenamientoDialogFragment dialogFragment = CelulasEntrenamientoDialogFragment.newInstance(getString(R.string.fragment_no_disponible_dialog_success));
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "NoResponsable");
    }

    /**
     * Método que cierra el Softkeyboard si se hace click afuera de él.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * <p>Listener para escuchar cuando el botón "Enviar" sea presionado.</p>
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_activity_no_responsable) {
            validarDatos(nombreFacilitador, conozcoFacilitador);
        } else if (view.getId() == R.id.et_activity_no_responsable){
            Intent intent = new Intent(this, ObtenerAreaYFacilitadorActivity.class);
            CelulaDTO cel = new CelulaDTO();
            cel.setPaisReceptorId(idReceptor);
            cel.setListadoParticipantes(participantes);
            intent.putExtra("celula", cel);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==Activity.RESULT_OK){
            if (requestCode==100){
                mFacilitador = (FacilitadorDTO) data.getSerializableExtra("Facilitador");
                mEtNombreFacilitador.setText(mFacilitador.getNombreFacilitador());
                nombreFacilitador = mFacilitador.getNombreFacilitador();
            }
        }
    }

    /**
     * <p>Listener para escuchar cuando se presione el CheckBox.</p>
     * @param compoundButton Vista que es presionada.
     * @param b Valor boleano para saber si el CheckBox fue presionado. True: CheckBox es seleccionado.
     *          False: CheckBox no está seleccionado o es deseleccionado.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        conozcoFacilitador = b;
    }

    /**
     * <P>Listener para escuchar cuando se ingrese un texto en el EditText del nombre del
     * facilitador.</P>
     * @param editable String del EditText.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        nombreFacilitador = editable.toString();
    }

    //ESTE MÉTODO ES PARTE DE LA INTERFAZ TextWatcher. NO SE IMPLEMENTA.

    /**
     *<p>Método que forma parte de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     *<p>Método que forma parte de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     * <p>Método que se ejecuta cuando se presiona el botón "Aceptar" del DialogFragment. Después de
     * esto se cierra la Activity.</p>
     */
    @Override
    public void onFinishRechazar() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onViewFacilitadorActualizado() {
        initializeDialogFragment();
    }

    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mLoader.show(fragmentManager, "Loader");
    }

    @Override
    public void onViewOcultarLoader() {
        if (mLoader!=null && mLoader.isAdded()){
            mLoader.dismiss();
        }
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(mensaje));
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }
}

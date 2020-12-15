package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;

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
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.CelulasEntrenamientoDialogFragment;

public class NoDisponibleActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        TextWatcher, CelulasEntrenamientoDialogFragment.RechazarDialogListener, ActualizarFacilitadorView {

    private Loader mLoader;
    private Toolbar mToolbar;
    private EditText mEtCorreo;
    private CheckBox mCbPersonaSuplente;
    private Button mBtnAceptar;

    private FacilitadorDTO mFacilitador; //Objeto para obtener el correo del facilitador suplente.
    private String correoFacilitador = "";
    private boolean personaSuplente = false;
    private int idSolicitud;
    private ActualizarFacilitadorPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        setContentView(R.layout.activity_no_disponible);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializePresenter();
    }

    private void bindViews(){
        mLoader = Loader.newInstance();
        mToolbar = findViewById(R.id.toolbar_activity_no_disponible);
        mEtCorreo = findViewById(R.id.et_activity_no_disponible);
        mCbPersonaSuplente = findViewById(R.id.cb_activity_no_disponible);
        mBtnAceptar = findViewById(R.id.btn_activity_no_disponible);
    }

    private void bindListeners(){
        mEtCorreo.addTextChangedListener(this);
        //mEtCorreo.setKeyListener(null); //Para evitar que se pueda ingresar texto en el EditText.
        mEtCorreo.setOnClickListener(this);
        mCbPersonaSuplente.setOnCheckedChangeListener(this);
        mBtnAceptar.setOnClickListener(this);
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
     * <p>Método que llama al método del Presenter para actualizar al facilitador.</p>
     */
    private void initializePresenter(){
        mPresenter = new ActualizarFacilitadorPresenter(this, new ActualizarFacilitadorInteractor());
    }

    /**
     * <p>Método que valida que la información ingresada sea la correcta, antes de cerrar la
     * Activity.</p>
     * @param correoFacilitador Cadena de texto que se ingresa en el EditText.
     * @param personaSuplente Parámetro que permite conocer si se seleccionó el CheckBox.
     */
    private void validarDatos(String correoFacilitador, boolean personaSuplente){
        //Caso cuando se marca el CheckBox y se ingresa texto en el EditText.
        if (personaSuplente && correoFacilitador.length()>0){
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.max_campos));
        }
        //Caso cuando no se marca el CheckBox y no se ingresa texto en el EditText.
        else if (!personaSuplente && correoFacilitador.length()<=0){
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.min_campos));
        }
        else if (correoFacilitador.contains("@")){
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.invalid_mail));
        }
        else
            {
                mPresenter.iniciarProcesoNoDisponible(correoFacilitador + "@kof.com.mx", String.valueOf(idSolicitud) ,String.valueOf(SharedPreferencesUtil.getInstance().getIdEmpleado()), personaSuplente);
            }
    }

    /**
     * <p>Método que crea y configura el DialogFragment.</p>
     */
    private void initializeDialogFragment(){
        CelulasEntrenamientoDialogFragment dialogFragment = CelulasEntrenamientoDialogFragment.newInstance(getString(R.string.fragment_no_disponible_dialog_success));
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "NoDisponible");
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
     * <p>Método que forma parte de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     * <p>Método que forma parte de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     * <p>Listener para escuchar cuando se ingrese un texto en el EditText del correo.</p>
     * @param editable String del EditText.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        correoFacilitador = editable.toString();
    }

    /**
     * <p>Listener para escuchar cuando el botón "Enviar" sea presionado.</p>
     * @param view Referencia al botón.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_activity_no_disponible){
            validarDatos(correoFacilitador, personaSuplente);
        } /*else if (view.getId() == R.id.et_activity_no_disponible){
            Intent intent = new Intent(this, ObtenerAreaYFacilitadorActivity.class);
            startActivityForResult(intent, 100);
        }*/
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==Activity.RESULT_OK){
            if (requestCode==100){
                mFacilitador = (FacilitadorDTO) data.getSerializableExtra("Facilitador");
                correoFacilitador = mFacilitador.getCorreo();
                //Separando la extensión del correo para colocarla en el TextView.
                String[] correo = mFacilitador.getCorreo().split("@");
                mEtCorreo.setText(correo[0]);
            }
        }
    }*/

    /**
     * <p>Listener para escuchar cuando se presione el CheckBox.</p>
     * @param compoundButton Vista que es presionada.
     * @param b Valor booleano para saber si el CheckBox fue presionado. True: CheckBox es presionado.
     *          False: CheckBox no está seleccionad o es deseleccionado.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        personaSuplente = b;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * <p>Método que se ejecuta cuando se presiona el botón "Aceptar" del DialogFragment. Cuando
     * esto sucede se cierra la Activity.</p>
     */
    @Override
    public void onFinishRechazar() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
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
        mensaje = mensaje == R.string.no_se_encotnro_mail ? R.string.facilitador_no_encontrado: mensaje;
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(mensaje));
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}

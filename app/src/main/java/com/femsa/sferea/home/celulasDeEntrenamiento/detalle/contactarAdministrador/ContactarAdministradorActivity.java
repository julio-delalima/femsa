package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto.MensajeEnviadoDialogFragment;

public class ContactarAdministradorActivity extends AppCompatActivity implements View.OnClickListener,
        MensajeEnviadoDialogFragment.MensajeEnviadoDialogListener, TextWatcher, ContactarAdministradorView {

    private Loader mLoader;
    private Toolbar mToolbar;
    private TextView mTvDestinatario;
    private TextView mTvDestinatarioCargo;
    private TextView mTvRemitente;
    private TextView mTvCorreoElectronico;
    private EditText mEtMensaje;
    private Button mBtnEnviar;
    private int idSolicitud;
    private ContactarAdministradorPresenter mPresenter;

    //Bandera que cambia de estado cuando la longitud del mensaje es mayor a 0.
    private boolean mensajeNoVacio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactar_administrador);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializePresenter();
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
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

    private void bindViews(){
        mLoader = Loader.newInstance();
        mToolbar = findViewById(R.id.toolbar_activity_contactar_administrador);
        mTvDestinatario = findViewById(R.id.tv_activity_contactar_administrador_para);
        mTvDestinatarioCargo = findViewById(R.id.tv_activity_contactar_administrador_para_puesto);
        mTvRemitente = findViewById(R.id.tv_activity_contactar_administrador_de);
        mTvCorreoElectronico = findViewById(R.id.tv_activity_contactar_administrador_correo_electronico);
        mEtMensaje = findViewById(R.id.et_activity_contactar_administrador_mensaje);
        mBtnEnviar = findViewById(R.id.btn_activity_contactar_administrador_enviar);

        //Colocando la información del remitente.
        mTvRemitente.setText(SharedPreferencesUtil.getInstance().getNombreSP());
        mTvCorreoElectronico.setText(SharedPreferencesUtil.getInstance().getCorreo());
        mTvDestinatarioCargo.setText(SharedPreferencesUtil.getInstance().getNombreAdmin());
    }

    private void bindListeners(){
        mBtnEnviar.setOnClickListener(this);
        mEtMensaje.addTextChangedListener(this);
    }

    /**
     * <p>Método que crea el DialogFragment para notificar que el mensaje fue enviado
     * correctamente.</p>
     */
    private void initializeDialogFragment(){
        MensajeEnviadoDialogFragment mensaje = MensajeEnviadoDialogFragment.newInstance();
        mensaje.setCancelable(false);
        mensaje.show(getSupportFragmentManager(), "mensajeEnviado");
    }

    private void initializePresenter(){
        mPresenter = new ContactarAdministradorPresenter(this, new ContactarAdministradorInteractor());
    }

    /**
     * <p>Método que permite configurar la Toolbar.</p>
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
     * <p>Método que valida que la longitud del mensaje sea mayor que cero. Si lo es, entonces se
     * muestra un mensaje exitoso, de lo contrario, se muestra un Toast con un mensaje de error.</p>
     */
    private void validarDatos(){
        if (mensajeNoVacio){
            mPresenter.iniciarProcesoContactarAdministrador(mEtMensaje.getText().toString(), idSolicitud, 1);
        }
        else{
            Toast.makeText(this, "Por favor, ingresa tu mensaje.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_activity_contactar_administrador_enviar) {
            validarDatos();
        }
    }

    @Override
    public void onFinishMensajeEnviado() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     *<p>Método de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     * <p>Listener para conocer la longitud del texto que se ingresa en el apartado del mensaje.</p>
     * @param charSequence Cadena de texto.
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        mensajeNoVacio= charSequence.length() > 0;
    }

    /**
     *<p>Método de la interfaz TextWatcher. No se implementa.</p>
     */
    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onViewMensajeEnviado() {
        initializeDialogFragment();
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(mensaje));
    }

    @Override
    public void onViewMostrarMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
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
}

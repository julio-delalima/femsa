package com.femsa.sferea.home.miCuenta.configuracion;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.Configuracion.IdiomaDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Configuracion.IdiomaResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracionActivity extends AppCompatActivity implements ConfiguracionView {

    private SwitchButton mNotificacionesSB, mDescargaSB;
    private TextView mNombreTV, mCorreoTV;
    private CircleImageView mFotoPerfilCIV;
    private Spinner mPaisSpinner;
    private ArrayList<Idiomas> mIdiomasListado;
    private ConfiguracionPresenter mConfiguracionPresenter;
    private Loader mLoader;
    private Toolbar mToolbar;
    private boolean primerIntento = true;
    String lang = "";
    int idIdioma = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        bindviews();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }

    @Override
    public void onDestroy() {
        mConfiguracionPresenter.OnDestroyPresenter();
        super.onDestroy();
    }

    /**
     * Método para asignar las variables con su respectivo elemento de la vista
     * */
    private void bindviews()
    {
        mLoader = Loader.newInstance();

        mConfiguracionPresenter = new ConfiguracionPresenter(this, new ConfiguracionInteractor());
            mConfiguracionPresenter.OnInteractorTraeIdiomas(SharedPreferencesUtil.getInstance().getIdioma());

        mNotificacionesSB = findViewById(R.id.switch_notificaciones);
            mNotificacionesSB.setOnCheckedChangeListener((view, isChecked) -> {
               // SnackbarManager.displaySnack(getSupportFragmentManager(), StringManager.MENSAJES_CHIDOS, "Próximamente");
                if(isChecked){
                    setTokenFirebase();
                }
                else{
                    mConfiguracionPresenter.OnInteractorCambiarNotificaciones(SharedPreferencesUtil.getInstance().getTokenUsuario(), false);
                }
            });
            mNotificacionesSB.setChecked(SharedPreferencesUtil.getInstance().isNotificaciones());

        mDescargaSB = findViewById(R.id.switch_wifi);
            mDescargaSB.setOnCheckedChangeListener((view, isChecked) -> {
                //SnackbarManager.displaySnack(getSupportFragmentManager(), SnackbarManager.SIN_WS);
                mConfiguracionPresenter.OnInteractorCambiarDescarga(SharedPreferencesUtil.getInstance().getTokenUsuario(), isChecked);
            });
            mDescargaSB.setChecked(SharedPreferencesUtil.getInstance().isContenidoWifi());


        mNombreTV = findViewById(R.id.configuracion_mi_nombre_tv);

        mCorreoTV = findViewById(R.id.configuracion_mi_correo_tv);

        mFotoPerfilCIV = findViewById(R.id.configuracion_foto_perfil_civ);

        mPaisSpinner = findViewById(R.id.configuracion_spinner_idioma_selector);

        mToolbar = findViewById(R.id.configuracion_toolbar);

        initializeToolbar();
    }


    /**
     * <p>Método que permite configurar la toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método que realiza la conexión con Firebase para obtener un token el cual se va a
     * asociar al dispositivo y se va a guardar en el servidor mediante un Web Service.</p>
     * */
    private void setTokenFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        LogManager.d("FIREBASE", "getInstanceId failed" + task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    LogManager.d("FIREBASE", token);
                    if (!token.equals("")) {
                        mConfiguracionPresenter.OnInteractorSetTokenFirebase(token, "A");
                    }
                });
    }


    /**
     * Método para cargar datos en cada elemento visual
     * */
    private void setData()
        {
            mNombreTV.setText(SharedPreferencesUtil.getInstance().getNombreSP());

            mCorreoTV.setText(SharedPreferencesUtil.getInstance().getCorreo());

            mIdiomasListado = new ArrayList<>();
        }


    /**
     * <p>Método que configura el spinner para el listado de idiomas</p>
     * @param data listado de idiomas provenientes del Web Service.
     * */
    private void configurarSpinnerIdiomas(IdiomaResponseData data)
        {

            //mIdiomasListado.add(new Idiomas(getResources().getString(R.string.idioma), 0));
            for(int i = 0; i < data.getIdiomaDTO().getListadoIdiomas().size(); i++)
                {
                    IdiomaDTO idiomaActual = data.getIdiomaDTO().getListadoIdiomas().get(i);
                    if(idiomaActual.getIDIdioma() == SharedPreferencesUtil.getInstance().getIdioma())
                        {
                            mIdiomasListado.add(0, new Idiomas(idiomaActual.getNombreIdioma(), idiomaActual.getIDIdioma(), "imagen", idiomaActual.getISO()));
                        }
                    else
                        {
                            mIdiomasListado.add(new Idiomas(idiomaActual.getNombreIdioma(), idiomaActual.getIDIdioma(), "imagen", idiomaActual.getISO()));
                        }
                }

            //mIdiomasListado.add(new Idiomas("Chino Tradicional", 4, "imagen", "zh"));
            //mIdiomasListado.add(new Idiomas("Hebreo", 4, "imagen", "he"));

            ConfiguracionSpinneradapter adapter = new ConfiguracionSpinneradapter(this, R.layout.item_element_spinner, mIdiomasListado);

            mPaisSpinner.setAdapter(adapter);
            mPaisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    lang = mIdiomasListado.get(pos).getISO();
                    if(!primerIntento)
                    {
                        mConfiguracionPresenter.OnInteractorCambiarIdioma(mIdiomasListado.get(pos).getIdIdioma(), SharedPreferencesUtil.getInstance().getTokenUsuario());
                        idIdioma = mIdiomasListado.get(pos).getIdIdioma();
                    }
                    primerIntento = false;
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    /**
     * Método para poner la foto de perfil.
     * */
    private void setImage()
    {
        String fullpath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();
        GlideUrl glideUrl = new GlideUrl(fullpath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(this).load(glideUrl).error(R.mipmap.ic_circled_user).into(mFotoPerfilCIV);
    }

    @Override
    public void OnConfiguracionViewNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
        onHideLoader();
    }

    @Override
    public void OnConfiguracionViewMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
        onHideLoader();
    }

    @Override
    public void OnConfiguracionViewMostrarMensaje(int tipoMensaje, int codigoError) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoError);
        onHideLoader();
    }

    @Override
    public void OnConfiguracionViewIdiomaCambiado() {
        GlobalActions g = new GlobalActions(ConfiguracionActivity.this);
        if(lang != null && !primerIntento)
        {
            g.setLocale(lang, idIdioma);
        }

    }

    @Override
    public void OnConfiguracionViewNotificacionesCambiadas() {
        SharedPreferencesUtil.getEditor().putBoolean(
                SharedPreferencesUtil.NOTIFICATIONES_KEY,
                !SharedPreferencesUtil.getInstance().isNotificaciones()).apply();
    }

    @Override
    public void OnConfiguracionViewDescargaCambiada() {
        SharedPreferencesUtil.getEditor().putBoolean(
                SharedPreferencesUtil.DESCARGA__CONTENIDO_KEY,
                !SharedPreferencesUtil.getInstance().isContenidoWifi()).apply();
    }

    @Override
    public void OnConfiguracionViewShowLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if (!mLoader.isAdded()) {
            mLoader.show(fm, "Loader");
        }
    }

    private void onHideLoader(){
        try
        {
            mLoader.dismiss();
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnConfiguracionViewHideLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnConfiguracionViewCargarIdiomas(IdiomaResponseData data) {
        configurarSpinnerIdiomas(data);
    }

    @Override
    public void setTokenDefinido() {
        SharedPreferencesUtil.getEditor().putBoolean(
                SharedPreferencesUtil.NOTIFICATIONES_KEY,
                true).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

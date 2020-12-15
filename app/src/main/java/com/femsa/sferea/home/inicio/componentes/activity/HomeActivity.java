package com.femsa.sferea.home.inicio.componentes.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Login.LoginActivity;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.ClaseMiguel;
import com.femsa.sferea.Utilities.FirebaseMessagingPush;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.celulasVacias.CelulasEntrenamientoVaciaFragment;
import com.femsa.sferea.home.celulasDeEntrenamiento.listado.CelulasDeEntrenamientoFragment;
import com.femsa.sferea.home.descargas.DescargasFragment;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.CloseAppDialog;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.CloseSessionDialog;
import com.femsa.sferea.home.inicio.componentes.fragment.FragmentHome;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.femsa.sferea.home.legal.FragmentLegal;
import com.femsa.sferea.home.miCuenta.MiCuentaFragment;
import com.femsa.sferea.home.notificaciones.NotificacionesFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Objects;

import static com.femsa.sferea.Utilities.FirebaseMessagingPush.CELULAS_CLICK_ACTION_KEY;
import static com.femsa.sferea.Utilities.FirebaseMessagingPush.GUSANOS_CLICK_ACTION_KEY;
import static com.femsa.sferea.Utilities.FirebaseMessagingPush.RETADOR_NOMBRE_KEY;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_OBJECT_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.TIPO_OBJETO_KEY;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentHome.OnHomeHideToolbar, HomeView,
        MiCuentaFragment.OnMyAccountListener, FragmentLegal.OnLegalFragmentCreated, NotificacionesFragment.OnNotificacionesInterface, CelulasEntrenamientoVaciaFragment.OnCelulasInit,
        CelulasDeEntrenamientoFragment.OnCelulasInit, ProviderInstaller.ProviderInstallListener, DescargasFragment.OnDescargasInterface {

    private FrameLayout frame;

    private ImageView profilePicture, mBanderaPaisLateral;

    private TextView nameProfile, countryProfile, mToolbarTitleTV;

    private String mFullName;

    private DrawerLayout drawer;

    private Toolbar toolbar;

    private RecyclerView mRecyclerHeader;

    private ConstraintLayout mBrowserContainer;

    public boolean mRecyclerHidden = false;

    private Loader loader;

    private HomePresenter mPresenter;

    private int idCelulaPushNotification = -1;

    private boolean isNewPassword;

    /**
     * Bandera utilizada para que cuando no se tengan fragments en la pila, al hacer back en el botón físico,
     * en vez de llamar el dialog de cerrar aplicación, mejor te regrese a home.
     */
    private boolean wasBrowserCalled = false;

    private boolean irACelulas = false;

    private ConstraintLayout mFilterContainer;

    /**
     * Bandetas utilizadas para controlar la instalación y actualización del provider de seguridad.
     * */
    private static final int ERROR_DIALOG_REQUEST_CODE = 1;

    private boolean retryProviderInstall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        int idioma;
        idioma = Integer.parseInt(SharedPreferencesUtil.getInstance().getIdioma() + "");
        GlobalActions g = new GlobalActions(this);
        g.setLocaleHome(idioma == 1 ? "es" : (idioma == 2 ? "en" : "pt"));

        idCelulaPushNotification = getIntent().getIntExtra(FirebaseMessagingPush.ID_SOLICITUD, 0);
        irACelulas = getIntent().getBooleanExtra("Celulas", false);
        if (getIntent().getStringExtra("new") != null) {
            String x = getIntent().getStringExtra("new");
            assert x != null;
            if (x.equals("new")) {
                isNewPassword = true;
            }
        }

        initData();

        bindViews();

        initHomeFragment();

        createLateralOptions();

        pintarOpcionMenuLateral(0);

        setProfileData();

        setImage();

        seleccionarFragment();

        getTokenFirebase();
        //Update the security provider when the activity is created.
        ProviderInstaller.installIfNeededAsync(this, this);
    }

    /**
     * This method is only called if the provider is successfully updated
     * (or is already up-to-date).
     */
    @Override
    public void onProviderInstalled() {
        // Provider is up-to-date, app can make secure network calls.
    }

    /**
     * This method is called if updating fails; the error code indicates
     * whether the error is recoverable.
     */
    @Override
    public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        if (availability.isUserResolvableError(errorCode)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            availability.showErrorDialogFragment(
                    this,
                    errorCode,
                    ERROR_DIALOG_REQUEST_CODE,
                    dialog -> {
                        // The user chose not to take the recovery action
                        onProviderInstallerNotAvailable();
                    });
        } else {
            // Google Play services is not available.
            onProviderInstallerNotAvailable();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ERROR_DIALOG_REQUEST_CODE) {
            // Adding a fragment via GoogleApiAvailability.showErrorDialogFragment
            // before the instance state is restored throws an error. So instead,
            // set a flag here, which will cause the fragment to delay until
            // onPostResume.
            retryProviderInstall = true;
        }
    }

    /**
     * On resume, check to see if we flagged that we need to reinstall the
     * provider.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (retryProviderInstall) {
            // We can now safely retry installation.
            ProviderInstaller.installIfNeededAsync(this, this);
        }
        retryProviderInstall = false;
    }

    private void onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP communication to be vulnerable, and take
        // appropriate action.
    }

    /**<p>Método que se manda a llamar cuando se recbe una push notification (intent) mientras la app
     * está en segundo plano o cerrada</p>
     * @param intent intent de la PushNotification.*/
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogManager.d("PUSHES", getClass().getCanonicalName() + ": onNewIntent()");
        String idNotificacion;
        if(intent != null)
            {
                idCelulaPushNotification = (intent.getExtras() != null) ? intent.getExtras().getInt("idSolicitud") : 0;
                irACelulas = (intent.getExtras() != null);

                if (intent.hasExtra("click_action")) {
                    switch (Objects.requireNonNull(intent.getStringExtra("click_action")))
                    {
                        case CELULAS_CLICK_ACTION_KEY:
                            if (intent.getExtras() != null)
                            {
                                idNotificacion = Objects.requireNonNull(intent.getExtras().getString("idNotificacion"));
                                switch (idNotificacion)
                                {
                                    case "N001":
                                    case "N002":
                                    case "N014":
                                    case "N013":
                                    case "N012":
                                    case "N011":
                                    case "N010":
                                    case "N009":
                                    case "N008":
                                    case "N007":
                                    case "N006":
                                    case "N005":
                                    case "N004":
                                    case "N003":
                                        break;
                                    default:
                                        Toast.makeText(this, "No me hackees por favor C:", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                idCelulaPushNotification = Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idSolicitud")));
                            }
                            break;
                        case GUSANOS_CLICK_ACTION_KEY:
                            LogManager.d("PUSHES", getClass().getCanonicalName() + ":onNewIntent() <- " + intent.getExtras().getString("idObjeto"));
                            LogManager.d("PUSHES", getClass().getCanonicalName() + ":onNewIntent() <- " + intent.getExtras().getString("idPrograma"));
                            irAJuegoMultijugador(
                                    Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idPrograma"))),
                                    Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idObjeto"))),
                                    Objects.requireNonNull(intent.getExtras().getString(RETADOR_NOMBRE_KEY))
                            );
                            return;
                        default:
                        {
                            idCelulaPushNotification = Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idSolicitud")));
                        }
                        break;
                    }
                }
                try{
                    if(Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("idObjeto"))) > 0 &&
                            Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idPrograma"))) > 0)
                    {
                        irAJuegoMultijugador(
                                Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idPrograma"))),
                                Integer.parseInt(Objects.requireNonNull(intent.getExtras().getString("idObjeto"))),
                                Objects.requireNonNull(intent.getExtras().getString("nombre"))
                        );
                    }
                    else{
                        seleccionarFragment();
                    }
                }
                catch (Exception ignored){}
            }
    }

    @Override
    public void onBackPressed() {
        mBrowserContainer.setVisibility(View.GONE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() <= 1) //si no hay fragmentos activos cerramos la aplicación
        {
            if (wasBrowserCalled) {
                recallHome();
            } else {
                CloseAppDialog closeAppDialog = CloseAppDialog.newInstance();
                FragmentManager frm = getSupportFragmentManager();
                closeAppDialog.show(frm, "PasswordDialog");
            }
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity_navigation, menu);
        return true;
    }

    private void bindViews() {
        mBrowserContainer = findViewById(R.id.browser_container);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setNavigationIcon(R.drawable.ic_burger);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_arrow);
        }


        mToolbarTitleTV = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        frame = findViewById(R.id.containerView);

        View headerView = navigationView.getHeaderView(0);

        LinearLayout headerMainLayour = headerView.findViewById(R.id.header_main_layour);
        headerMainLayour.setOnClickListener(v -> onOptionSelected(-1));

        profilePicture = headerView.findViewById(R.id.header_profile_picture);

        nameProfile = headerView.findViewById(R.id.header_name);

        countryProfile = headerView.findViewById(R.id.header_country);
        mBanderaPaisLateral = headerView.findViewById(R.id.bandera_pais_menu_lateral);

        //optionContainer = findViewById(R.id.option_lateral_container);

        mRecyclerHeader = findViewById(R.id.homeActivity_recyclerViewHeader_categories);

        ImageView tresPuntitos = headerView.findViewById(R.id.triple_point_button);
        tresPuntitos.setOnClickListener(v -> onOptionSelected(-1));

        Button logOutButton = findViewById(R.id.log_out);
        logOutButton.setOnClickListener(v -> onCloseSession());

        mFilterContainer = findViewById(R.id.filter_container);
        mFilterContainer.setVisibility(View.GONE);
        AppBarLayout mAppBarHome = findViewById(R.id.appbarlayout_home_activity);
            if(android.os.Build.VERSION.SDK_INT > 21)
                {
                    mAppBarHome.setElevation(0);
                    mAppBarHome.setTranslationZ(0.1f);
                }
            else
                {
                    mAppBarHome.setElevation(0.1f);
                    getSupportActionBar().setElevation(0.0f);
                }
    }

    /**
     * Método que maneja los clock sobre el menú lateral, ejecutado una acción correspondiente a cada sección
     */
    private void onOptionSelected(int index) {
        switch (index) {
            case -1: //My account
                initMyAccountFragment();
                break;
            case 0: //Home
                initHomeFragment();
                break;
            case 1: //Training cells
                initCelulasEntrenamientoFragment();
                break;
            case 2: //Notifications
                initNotificacionesFragment();
                break;
            case 3://Descargas
                initDescargasFragment();
                break;
            case 4: //Legal
                initLegalFragment();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        idCelulaPushNotification = -1;
    }

    /**
     * Método a llamar cuando se desea cerrrar la sesión, despliega el Dialog de confirmar cierre de sesión.
     */
    private void onCloseSession() {
        drawer.closeDrawer(GravityCompat.START);
        CloseSessionDialog closeSession = CloseSessionDialog.newInstance();
        FragmentManager frm = getSupportFragmentManager();
        mPresenter.OnInteractorSetTokenFirebase("", "");
        closeSession.show(frm, "SessionDialog");
    }

    /**
     * Método que retorna true si el fragmento activo es FragmentHome,
     * nos permite verificar que el fragmento home esté en uso y realizar funciones únicamente
     * cuando ese fragment esté activo
     */
    public boolean isActiveFragmentHome() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isVisible() && fragment instanceof FragmentHome) {
                return true;
            }
        }
        return false;
    }

    /***
     * Método que crea los botones del menú lateral.
     */
    public void createLateralOptions() {
        findViewById(R.id.menu_lateral_home).setOnClickListener(v->onOptionSelected(0));
        findViewById(R.id.menu_lateral_celulas).setOnClickListener(v->onOptionSelected(1));
        findViewById(R.id.menu_lateral_notificaciones).setOnClickListener(v->onOptionSelected(2));
        findViewById(R.id.menu_lateral_descargas).setOnClickListener(v->onOptionSelected(3));
        findViewById(R.id.menu_lateral_legal).setOnClickListener(v->onOptionSelected(4));
        //int[] icons = {R.mipmap.ic_circled_home, R.mipmap.ic_circled_sheets, R.mipmap.ic_mail_circled,R.mipmap.ic_circled_download, R.mipmap.ic_circled_legal};
//        int[] icons = {R.mipmap.ic_circled_home, R.mipmap.ic_mail_circled,R.mipmap.ic_circled_download, R.mipmap.ic_circled_legal};
//        //int[] titles = {R.string.menu_home, R.string.menu_cells, R.string.menu_notif, R.string.descargas,R.string.menu_legal};
//        int[] titles = {R.string.menu_home, R.string.menu_notif, R.string.descargas,R.string.menu_legal};
//
//        for (int i = 0; i < NUM_BUTTONS; i++) {
//            options[i] = new Button(this);
//            options[i].setText(getResources().getText(titles[i]));
//            options[i].setAllCaps(false);
//            options[i].setId(i);
//            options[i].setCompoundDrawablesWithIntrinsicBounds(icons[i], 0, 0, 0);
//            options[i].setCompoundDrawablePadding(2);
//            options[i].setOnClickListener(v -> onOptionSelected(v.getId()));
//            optionContainer.addView(customizeButton(options[i]));
//        }
        ((Button)findViewById(R.id.log_out)).setText(getResources().getString(R.string.log_out));
    }

//    /***
//     * Método que le aplica estilo a los botones del menú lateral.
//     */
//    public Button customizeButton(Button btn) {
////        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        btn.setLayoutParams(params);
////        btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
////        btn.setCompoundDrawablePadding(10);
////        btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
////        return btn;
//    }

    /**
     * Método que pinta de color rojo la sección en la que te encuentres dek menú lateral
     */
    public void pintarOpcionMenuLateral(int indexSelected) {
        clearSelectedOptions();
        switch (indexSelected){
            case 0:
                findViewById(R.id.menu_lateral_home).setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
                ((Button)findViewById(R.id.menu_lateral_home)).setTextColor(Color.WHITE);
            break;
            case 1:
                findViewById(R.id.menu_lateral_celulas).setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
                ((Button)findViewById(R.id.menu_lateral_celulas)).setTextColor(Color.WHITE);
            break;
            case 2:
                findViewById(R.id.menu_lateral_notificaciones).setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
                ((Button)findViewById(R.id.menu_lateral_notificaciones)).setTextColor(Color.WHITE);
            break;
            case 3:
                findViewById(R.id.menu_lateral_descargas).setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
                ((Button)findViewById(R.id.menu_lateral_descargas)).setTextColor(Color.WHITE);
            break;
            case 4:
                findViewById(R.id.menu_lateral_legal).setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
                ((Button)findViewById(R.id.menu_lateral_legal)).setTextColor(Color.WHITE);
            break;
            default:
        }
    }

    /**
     * Método que limpia el color rojo de las secciones seleccionadas en el menú lateral
     * para poder pintar una nueva.
     */
    public void clearSelectedOptions() {
        findViewById(R.id.menu_lateral_home).setBackgroundColor(Color.WHITE);
        findViewById(R.id.menu_lateral_celulas).setBackgroundColor(Color.WHITE);
        findViewById(R.id.menu_lateral_notificaciones).setBackgroundColor(Color.WHITE);
        findViewById(R.id.menu_lateral_descargas).setBackgroundColor(Color.WHITE);
        findViewById(R.id.menu_lateral_legal).setBackgroundColor(Color.WHITE);

        ((Button)findViewById(R.id.menu_lateral_home)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((Button)findViewById(R.id.menu_lateral_celulas)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((Button)findViewById(R.id.menu_lateral_notificaciones)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((Button)findViewById(R.id.menu_lateral_descargas)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((Button)findViewById(R.id.menu_lateral_legal)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    /**
     * Método para esconder el recycler view o menú horizontal que está contenido en el toolbar
     */
    private void hideToolbarRecyclerView() {
        mRecyclerHeader.setVisibility(View.GONE);
        //toolbar.removeView(mRecyclerHeader);
        mRecyclerHidden = true;
    }

    /**
     * Método para mostrar el recycler view o menú horizontal que está contenido en el toolbar
     */
    private void showToolbarRecyclerView() {
        if (mRecyclerHidden) {
            /*toolbar.removeView(mToolbarTitleTV);
            toolbar.addView(mToolbarTitleTV);
            toolbar.addView(mRecyclerHeader);*/
            mRecyclerHeader.setVisibility(View.VISIBLE);
            mRecyclerHidden = false;
        }
    }

    /**
     * Método para quitar el fondo del toolbar, permitiendo el efecto de transparencia.
     */
    private void hideToolbarBackground() {
        toolbar.setBackground(getResources().getDrawable(R.drawable.transparent_toolbar));
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) frame.getLayoutParams();
        p.setMargins(0, -30, 0, 0);
        frame.setLayoutParams(p);
        mBrowserContainer.setBackground(null);
    }

    /**
     * Método que miestra el fondo de la toolbar.
     */
    private void showToolbarBackground() {
        toolbar.setBackground(getResources().getDrawable(R.color.femsa_red_b));
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) frame.getLayoutParams();
        p.setMargins(0, 20, 0, 0);
        frame.setLayoutParams(p);
    }

    /**
     * Método que coloca la información del usuario en el menú lateral.
     */
    private void setProfileData() {
        nameProfile.setText(mFullName);
        countryProfile.setText(SharedPreferencesUtil.getInstance().getNombrePais());
        String fullPath = RequestManager.IMAGEN_circular_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBanderaPaisLateral);
    }

    private void initData() {
        mPresenter = new HomePresenter(this, new HomeInteractor());

        loader = Loader.newInstance();

        mFullName = GlobalActions.cutFullName(SharedPreferencesUtil.getInstance().getNombreSP());

        String tempCredencial = SharedPreferencesUtil.getInstance().getCredencial();
        if ((tempCredencial == null || tempCredencial.isEmpty() || tempCredencial.length() < 8) && !isNewPassword) {
            GlobalActions g = new GlobalActions(this);
            g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

    }


    private void setImage() {
        String fullPath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();
        //PicassoTrustAll.getInstance(getApplicationContext()).load(fullPath).error(R.mipmap.ic_circled_user).into(profilePicture);
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(getApplicationContext())
                .load(glideUrl)
                .error(R.mipmap.ic_circled_user)
                .into(profilePicture);
    }

    /**
     * Callings to the fragments used in the lateral menu
     */

    private void initHomeFragment() {
        mRecyclerHeader.setVisibility(View.VISIBLE);
        mFilterContainer.setVisibility(View.GONE);
        FragmentHome homeFr = new FragmentHome();
        homeFr.setListener(this);
        if (isNewPassword) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("new", true);
            homeFr.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, homeFr, "FragmentHome")
                .addToBackStack(null)
                .commit();
        mToolbarTitleTV.setText("");
        hideToolbarBackground();
        showToolbarRecyclerView();
    }

    /**
     * Método para controlar el toolbar cada que se inicia un fragment que no se home, ya que
     * el buscador y filtrado solo deben aparecer en home
     */
    private void initNoHomeFragments() {
        showToolbar();
        showToolbarBackground();
        hideToolbarRecyclerView();
        clearSelectedOptions();
        mFilterContainer.setVisibility(View.GONE);
        drawer.closeDrawer(GravityCompat.START, false);
        mBrowserContainer.setVisibility(View.GONE);
        mToolbarTitleTV.setVisibility(View.VISIBLE);
    }

    /**
     * Método que llama al fragment de Legal
     */
    private void initLegalFragment() {
        initNoHomeFragments();
        FragmentLegal legalFr = new FragmentLegal();
        legalFr.setListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, legalFr, "Legal")
                .addToBackStack(null)
                .commit();

    }

    /**
     * Método que llama al fragment de Mi Cuenta
     */
    private void initMyAccountFragment() {
        initNoHomeFragments();
        Bundle bundle = new Bundle();
        bundle.putString("FullName", mFullName);
        MiCuentaFragment accountFragment = new MiCuentaFragment();
        accountFragment.setListener(this);
        accountFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, accountFragment, "Account")
                .addToBackStack(null)
                .commit();
    }


    /**
     * Método que llama al fragment de notificaciones
     */
    private void initNotificacionesFragment() {
        initNoHomeFragments();
        NotificacionesFragment notificacionesFragment = new NotificacionesFragment();
        notificacionesFragment.setListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, notificacionesFragment, "Notificaciones")
                .addToBackStack(null)
                .commit();
    }

    /***
     * Método que llama al fragment de Células de Entrenamiento.
     */
    private void initCelulasEntrenamientoFragment() {
        initNoHomeFragments();
        CelulasDeEntrenamientoFragment fragment = CelulasDeEntrenamientoFragment.newInstance();
        fragment.setPushNotification(idCelulaPushNotification);
        //Agregando dos listeners: uno para el fragment que tiene solcitides, y otro para el caso en que no hay.
        fragment.setListener(this, this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, fragment, "Células")
                .addToBackStack(null)
                .commit();
    }

    /***
     * Método que llama al fragment de Descargas (Modo Offline).
     */
    private void initDescargasFragment(){
        initNoHomeFragments();
        DescargasFragment descargaFragment = new DescargasFragment();
        descargaFragment.setListener(this);
        //Agregando dos listeners: uno para el fragment que tiene solcitides, y otro para el caso en que no hay.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, descargaFragment, "Descargas")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Functions Implementations
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /**
     * Método del ciclo de vida del fragment
     */
    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }


    /***
     * Método que se ejecuta cuando se tuvo un exitoso cierre de sesión.
     */
    @Override
    public void onLogOutSuccess() {
        SharedPreferencesUtil.getInstance().closeSession();
        Intent sendTo = new Intent(getApplicationContext(), LoginActivity.class);
        sendTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendTo);
        finish();
    }

    /***
     * Método que se manda a llamar cuando queremos mostrar el loader.
     */
    @Override
    public void onShowLoader() {
        if (!loader.isAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            try {
                loader.show(fm, "Loader");
            } catch (Exception ex) {
                LogManager.d("Ex", ex.getMessage());
            }
        }
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     */
    @Override
    public void onHideLoader() {
        try {
            if (loader != null && loader.isAdded()) {
                loader.dismiss();
            }
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void onShowMessage(int tipoMensaje) {
    }

    @Override
    public void onShowMessage(int tipoMensaje, int codigoRespuesta) {
    }

    /**
     * Método que se llama cuando vamos a ocultar el toolbar.
     */
    @Override
    public void hideToolbar() {
        if (isActiveFragmentHome()) {
            Animation fadeIn = new AlphaAnimation(1, 0);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(1000);
            toolbar.setAnimation(fadeIn);
            toolbar.startAnimation(fadeIn);
            toolbar.setVisibility(View.GONE);
        }
    }

    /**
     * Método que se llama cuando vamos a mostrar la toolbar.
     */
    @Override
    public void showToolbar() {
        if (toolbar.getVisibility() != View.VISIBLE) {
            toolbar.setVisibility(View.VISIBLE);
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(1000);
            toolbar.setAnimation(fadeIn);
            toolbar.startAnimation(fadeIn);
        }
    }

    /**
     * Método para inicializar el aspecto visual al cargar el fragment de home.
     */
    @Override
    public void setHomeInMainView() {
        mFilterContainer.setVisibility(View.GONE);
        mRecyclerHeader.setVisibility(View.VISIBLE);
        showToolbar();
        showToolbarRecyclerView();
        pintarOpcionMenuLateral(0);
        mToolbarTitleTV.setVisibility(View.GONE);
        hideToolbarBackground();
    }

    /**
     * Método para inicializar el aspecto visual al cargar el fragment de notificaciones.
     */
    @Override
    public void setNotificacionesInMainView() {
        initNoHomeFragments();
        pintarOpcionMenuLateral(2);
        mToolbarTitleTV.setText(R.string.drawer_title_notificaciones);
    }

    /**
     * Método para inicializar el aspecto visual al cargar el fragment de Mi Cuenta.
     */
    @Override
    public void setMyAccountMainView() {
        initNoHomeFragments();
        mToolbarTitleTV.setText(R.string.drawer_title_my_Account);
    }

    /**
     * Método para inicializar el aspecto visual al cargar el fragment de Legal
     */
    @Override
    public void setLegalInMainView(AppBarLayout layout) {
        initNoHomeFragments();
        pintarOpcionMenuLateral(4);
        mToolbarTitleTV.setText(R.string.legal);
        FrameLayout.MarginLayoutParams params = (FrameLayout.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, toolbar.getHeight() - 12, 0, 0);
        layout.setLayoutParams(params);

    }

    /**
     * Método para configurar el aspecto de HomeActivity al cargar el fragment.
     */
    @Override
    public void SetCelulasInMainView() {
        initNoHomeFragments();
        pintarOpcionMenuLateral(1);
        mToolbarTitleTV.setText(R.string.title_celulas);
    }


    /**
     * Método para inicializar el aspecto visual al cargar el fragment de descargas.
     */
    @Override
    public void setDescargasInMainView() {
        initNoHomeFragments();
        pintarOpcionMenuLateral(3);
        mToolbarTitleTV.setText(R.string.descargas);
    }

    /**
     * Método para verificar si el fragmento activo o en pantalla es una instancia de FragmentHome
     * Retorna true si es instancia de Fragment home, de lo contrario retorna false.
     */
    @Override
    public boolean isActiveFragmentIntanceOfFragmentHome() {
        return isActiveFragmentHome();
    }

    /**
     * Método que controla el aspecto de home al utilizar el buscador
     */
    @Override
    public void setBrowserView() {
        showToolbarBackground();
    }

    /**
     * Método utilizado cuando se limpia el buscador
     */
    @Override
    public void hideBrowserView() {
        //hideToolbarBackground();
    }

    /**
     * Cambia la bandera de la contraseña una vez que ha sido actualizada
     */
    @Override
    public void updatedPasswordChangeFlag() {
        isNewPassword = false;
    }

    /**
     * Método para volver  allamar el menú de home
     */
    @Override
    public void recallHome() {
        initHomeFragment();
    }

    /**
     * función utilizada para que cuando no se tengan fragments en la pila, al hacer back en el botón físico,
     * en vez de llamar el dialog de cerrar aplicación, mejor te regrese a home.
     */
    @Override
    public void browserWasCalled() {
        wasBrowserCalled = true;
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
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Método que se ejecuta cuando se destruye la aplicación, el cual igual destruye el presenatdor
     * para evitar problemas al cambiar al modo multi ventanas.
     */
    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Método que te envia a un fragment en específico, analizando el intent mediante la función Miguel.
     */
    private void seleccionarFragment() {
        if (ClaseMiguel.funcionMiguel(irACelulas)) {
            initCelulasEntrenamientoFragment();
        }
    }

    /**
     * <p>Método que te envía al juego de Gusanos y Escaleras a través de una push</p>
     * @param idObjeto ID del objeto asociado al juego.
     * @param idPrograma ID del programa al que pertenece el juego.
     * */
    private void irAJuegoMultijugador(int idPrograma, int idObjeto, String retador){
        LogManager.d("PUSHES", "irAjuego " + idPrograma + " - " + idObjeto + " - " + retador);
        Intent irAJuego =
                retador.equals("0")
                        ? new Intent(this, DetalleProgramaActivity.class)
                        : new Intent(this, ObjetoAprendizajeActivity.class);
        irAJuego.putExtra(ID_OBJECT_KEY, idObjeto);
        irAJuego.putExtra(ID_PROGRAMA_KEY, idPrograma);
        irAJuego.putExtra(TIPO_OBJETO_KEY, JUEGO_MULTIJUGADOR);
        irAJuego.putExtra(RETADOR_NOMBRE_KEY, retador);
        irAJuego.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(irAJuego);
    }

    /**
     * <p>Método que realiza la conexión con Firebase para obtener un token el cual se va a
     * asociar al dispositivo y se va a guardar en el servidor mediante un Web Service.
     * a la petición al web service se le manda 'A' porque se trata de un dispositivo Android.</p>
     * */
    private void getTokenFirebase() {
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
                    if(!token.equals(""))
                        {
                            mPresenter.OnInteractorSetTokenFirebase(token, "A");
                        }
                });

    }


}
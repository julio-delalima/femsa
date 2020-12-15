package com.femsa.sferea.home.miCuenta.miActividad;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.sferea.Utilities.GlideApp;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.miActividad.logros.MiActividadLogrosFragment;
import com.femsa.sferea.home.miCuenta.miActividad.programas.MiActividadProgramasFragment;

public class MiActividadActivity extends AppCompatActivity implements MiActividadView, TabLayout.OnTabSelectedListener {

    private Toolbar mToolbar;

    //Vistas de la sección donde se muestran mis puntos, nivel, programas activos y terminados
    private ImageView mIvMiNivelFoto;
    private TextView mTvMiNivel;
    private TextView mTvNumeroCorcholatas;
    private TextView mTvProgramasActivos;
    private TextView mTvProgramasTerminados;

    //Vistas del medidor "Mentalidad de dueño
    private View mIlMedidorUno;
    private ImageView mIvMedidorUno;
    private ImageView mIvMedidorUnoIcono;
    private TextView mTvMedidorUnoProgreso;
    private TextView mTvMedidorUnoLimite;

    //Vistas del medidor "Foco obsesivo en consumidor y cliente"
    private View mIlMedidorDos;
    private ImageView mIvMedidorDos;
    private TextView mTvMedidorDosProgreso;
    private TextView mTvMedidorDosLimite;

    //Vista del medidor "Excelencia operativa"
    private View mIlMedidorTres;
    private ImageView mIvMedidorTres;
    private TextView mTvMedidorTresProgreso;
    private TextView mTvMedidorTresLimite;

    //Vistas del medidor "Decisiones ágiles"
    private View mIlMedidorCuatro;
    private ImageView mIvMedidorCuatro;
    private TextView mTvMedidorCuatroProgreso;
    private TextView mTvMedidorCuatroLimite;

    //Vistas del medidor "Primero la gente"
    private View mIlMedidorCinco;
    private ImageView mIvMedidorCinco;
    private TextView mTvMedidorCincoProgreso;
    private TextView mTvMedidorCincoLimite;

    //Vistas de la sección donde se muestran los programas y logros
    private TabLayout mTlProgramasYLogros;
    private FrameLayout mFlContenedor; //FrameLayout donde se colocarán los fragments de Programas y Logros

    //Loader de la vista
    private Loader mLoader;

    //Presentadores
    private MiActividadPresenter mMiActividadPresenter;

    //Adaptadores
    private MiActividadTabsAdapter mTabsAdapter;

    //Referencias donde se almacenan las respuestas de las peticiones
    private MiActividadResponseData mMiActividadData;

    //El contenedor principal de la vista
    private NestedScrollView mNSVPrincipal;

    //Icono de Medidores
    private ImageView mExcelenciaIV, mDecisoresIV, mFocoIV, mGenteIV, mMundoIV;
    private static final int FOCO = 0, EXCEL  = 1, GENTE = 2, DECISOR = 3, MUNDO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_actividad);
        bindViews();
        bindListeners();
        initializeToolbar();
        initializePresenter();

    }

    private void bindViews(){

        mNSVPrincipal = findViewById(R.id.nsv_activity_mi_actividad);
        mNSVPrincipal.setVisibility(View.INVISIBLE);

        mToolbar = findViewById(R.id.toolbar_activity_mi_actividad);

        mIvMiNivelFoto = findViewById(R.id.iv_activity_mi_actividad_mi_nivel);
        mTvMiNivel = findViewById(R.id.tv_activity_mi_actividad_mi_nivel);
        mTvNumeroCorcholatas = findViewById(R.id.tv_activity_mi_actividad_mis_corcholatas);
        mTvProgramasActivos = findViewById(R.id.tv_activity_mi_actividad_programas_activos);
        mTvProgramasTerminados = findViewById(R.id.tv_activity_mi_actividad_programas_terminados);

        //Obteniendo las referencias del primer medidor.
        mIlMedidorUno = findViewById(R.id.layout_activity_mi_actividad_medidor_1);
        mIvMedidorUno = mIlMedidorUno.findViewById(R.id.iv_layout_medidor_imagen_medidor_uno);
        mTvMedidorUnoProgreso = mIlMedidorUno.findViewById(R.id.tv_layout_medidor_progreso_uno);
        mTvMedidorUnoLimite = mIlMedidorUno.findViewById(R.id.tv_layout_medidor_limite_uno);

        //Obteniendo las referencias del segundo medidor.
        mIlMedidorDos = findViewById(R.id.layout_activity_mi_actividad_medidor_2);
        mIvMedidorDos = mIlMedidorDos.findViewById(R.id.iv_layout_medidor_imagen_medidor_dos);
        mTvMedidorDosProgreso = mIlMedidorDos.findViewById(R.id.tv_layout_medidor_progreso_dos);
        mTvMedidorDosLimite = mIlMedidorDos.findViewById(R.id.tv_layout_medidor_limite_dos);

        //Obteniendo las referencias del tercer medidor.
        mIlMedidorTres = findViewById(R.id.layout_activity_mi_actividad_medidor_3);
        mIvMedidorTres = mIlMedidorTres.findViewById(R.id.iv_layout_medidor_imagen_medidor_tres);
        mTvMedidorTresProgreso = mIlMedidorTres.findViewById(R.id.tv_layout_medidor_progreso_tres);
        mTvMedidorTresLimite = mIlMedidorTres.findViewById(R.id.tv_layout_medidor_limite_tres);

        //Obteniendo las referencias del cuarto medidor.
        mIlMedidorCuatro = findViewById(R.id.layout_activity_mi_actividad_medidor_4);
        mIvMedidorCuatro = mIlMedidorCuatro.findViewById(R.id.iv_layout_medidor_imagen_medidor_cuatro);
        mTvMedidorCuatroProgreso = mIlMedidorCuatro.findViewById(R.id.tv_layout_medidor_progreso_cuatro);
        mTvMedidorCuatroLimite = mIlMedidorCuatro.findViewById(R.id.tv_layout_medidor_limite_cuatro);

        //Obteniendo las referencias del quinto medidor.
        mIlMedidorCinco = findViewById(R.id.layout_activity_mi_actividad_medidor_5);
        mIvMedidorCinco = mIlMedidorCinco.findViewById(R.id.iv_layout_medidor_imagen_medidor_cinco);
        mTvMedidorCincoProgreso = mIlMedidorCinco.findViewById(R.id.tv_layout_medidor_progreso_cinco);
        mTvMedidorCincoLimite = mIlMedidorCinco.findViewById(R.id.tv_layout_medidor_limite_cinco);

        mTlProgramasYLogros = findViewById(R.id.tl_activity_mi_actividad_programas_logros);
        mFlContenedor = findViewById(R.id.fl_activity_mi_actividad_contenedor);

        //Iconos medidores
        mFocoIV = findViewById(R.id.mi_actividad_odom_foco);
        mExcelenciaIV  = findViewById(R.id.mi_actividad_odom_excel);
        mDecisoresIV = findViewById(R.id.mi_actividad_odom_decisor);
        mGenteIV = findViewById(R.id.mi_actividad_odom_gente);
        mMundoIV = findViewById(R.id.mi_actividad_odom_mundo);

        mLoader = Loader.newInstance();
    }

    private void bindListeners(){
        mTlProgramasYLogros.addOnTabSelectedListener(this);
    }

    private void initializePresenter(){
        mMiActividadPresenter = new MiActividadPresenter(this, new MiActividadInteractor());
        mMiActividadPresenter.onInteractorCallMiActividad();
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

    private void initializeFragmentProgramas() {
        Fragment fragment = MiActividadProgramasFragment.newInstance();
        ((MiActividadProgramasFragment) fragment).setProgramasData(mMiActividadData);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_activity_mi_actividad_contenedor, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    /**
     * <p>Método que con base en el progreso y el límite obtenidos del Web Service se calcula qué valor colocar
     * en los medidores.</p>
     * @param imageView ImageView donde
     * @param progreso Progreso que lleva el usuario.
     * @param limite Progreso necesario para avanzar al próximo nivel.
     */
    private void asignarAGraficas(ImageView imageView, int tipomedidor, int progreso, int limite){
        int porcentaje = (limite != 0) ? (progreso*10)/limite : 0;
        switch (tipomedidor){
            case EXCEL:
                mExcelenciaIV.setImageDrawable(ContextCompat.getDrawable(this, porcentaje > 0 ? R.drawable.ic_excelencia_v2_red : R.drawable.ic_excelencia_v2));
            break;
            case FOCO:
                mFocoIV.setImageDrawable(ContextCompat.getDrawable(this, porcentaje > 0 ? R.drawable.ic_foco_v2_red : R.drawable.ic_foco_v2));
            break;
            case GENTE:
                mGenteIV.setImageDrawable(ContextCompat.getDrawable(this, porcentaje > 0 ? R.drawable.ic_primero_v2_red : R.drawable.ic_primero_v2));
            break;
            case MUNDO:
                mMundoIV.setImageDrawable(ContextCompat.getDrawable(this, porcentaje > 0 ? R.drawable.ic_mundo_mamadisimo_v2_red : R.drawable.ic_mundo_mamadisimo_v2));
            break;
            case DECISOR:
                mDecisoresIV.setImageDrawable(ContextCompat.getDrawable(this, porcentaje > 0 ? R.drawable.ic_decisores_v2_red : R.drawable.ic_decisores_v1));
            break;
        }

        switch (porcentaje){
            case 0:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_1));
                break;
            case 1:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_2));
                break;
            case 2:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_3));
                break;
            case 3:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_4));
                break;
            case 4:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_5));
                break;
            case 5:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_6));
                break;
            case 6:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_7));
                break;
            case 7:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_8));
                break;
            case 8:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_9));
                break;
            case 9:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_10));
                break;
            case 10:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_11));
                break;
            default:
                imageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_medidor_progreso_11));
                break;
        }
    }

    /**
     * <p>Método que, una vez realizadas las peticiones, asigna la información a los respectivos elementos en la interfaz.</p>
     */
    private void asignarDatos(){
        mTvMiNivel.setText(String.valueOf(mMiActividadData.getMiActividad().getNombreMiLogro()));
        //String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getTokenUsuario() + "/" + mMiActividadData.getMiActividad().getNombreImagenMiLogro();
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL  + "/" + mMiActividadData.getMiActividad().getNombreImagenMiLogro();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(this).load(glideUrl).error(R.drawable.ic_logros_estrella_gris).into(mIvMiNivelFoto);


        //Asignando los valores de la parte superior: corcholatas, programas activos y programas terminados.
        mTvNumeroCorcholatas.setText(String.valueOf(mMiActividadData.getMiActividad().getCorcholatas()));
        mTvProgramasActivos.setText(String.valueOf(mMiActividadData.getMiActividad().getIncompletePrograms()));
        mTvProgramasTerminados.setText(String.valueOf(mMiActividadData.getMiActividad().getCompletedPrograms()));

        //Asignando los textos del progreso y límite de los medidores
        mTvMedidorUnoProgreso.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorActual().getMentalidad()));
        mTvMedidorDosProgreso.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorActual().getFoco()));
        mTvMedidorTresProgreso.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorActual().getExcelencia()));
        mTvMedidorCuatroProgreso.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorActual().getDecisionesAgiles()));
        mTvMedidorCincoProgreso.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorActual().getGente()));

        mTvMedidorUnoLimite.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorMaximo().getMentalidad()));
        mTvMedidorDosLimite.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorMaximo().getFoco()));
        mTvMedidorTresLimite.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorMaximo().getExcelencia()));
        mTvMedidorCuatroLimite.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorMaximo().getDecisionesAgiles()));
        mTvMedidorCincoLimite.setText(String.valueOf(mMiActividadData.getMiActividad().getMarcadorMaximo().getGente()));

        asignarAGraficas(mIvMedidorUno, MUNDO, mMiActividadData.getMiActividad().getMarcadorActual().getMentalidad(), mMiActividadData.getMiActividad().getMarcadorMaximo().getMentalidad());
        asignarAGraficas(mIvMedidorDos, FOCO, mMiActividadData.getMiActividad().getMarcadorActual().getFoco(), mMiActividadData.getMiActividad().getMarcadorMaximo().getFoco());
        asignarAGraficas(mIvMedidorTres, EXCEL, mMiActividadData.getMiActividad().getMarcadorActual().getExcelencia(), mMiActividadData.getMiActividad().getMarcadorMaximo().getExcelencia());
        asignarAGraficas(mIvMedidorCuatro, DECISOR, mMiActividadData.getMiActividad().getMarcadorActual().getDecisionesAgiles(), mMiActividadData.getMiActividad().getMarcadorMaximo().getDecisionesAgiles());
        asignarAGraficas(mIvMedidorCinco, GENTE, mMiActividadData.getMiActividad().getMarcadorActual().getGente(), mMiActividadData.getMiActividad().getMarcadorMaximo().getGente());

        mNSVPrincipal.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewMostrarLogros(ObtenerAllLogrosData data) {

        /*//Almacenando los valores límite de los medidores.
        medidorUnoLimite = String.valueOf(data.getKofSiguienteNivel().getMentalidad());
        medidorDosLimite = String.valueOf(data.getKofSiguienteNivel().getFoco());
        medidorTresLimite = String.valueOf(data.getKofSiguienteNivel().getExcelencia());
        medidorCuatroLimite = String.valueOf(data.getKofSiguienteNivel().getDecisionesAgiles());
        medidorCincoLimite = String.valueOf(data.getKofSiguienteNivel().getGente());

        //Asignando los textos del límite de los medidores
        mTvMedidorUnoLimite.setText(medidorUnoLimite);
        mTvMedidorDosLimite.setText(medidorDosLimite);
        mTvMedidorTresLimite.setText(medidorTresLimite);
        mTvMedidorCuatroLimite.setText(medidorCuatroLimite);
        mTvMedidorCincoLimite.setText(medidorCincoLimite);*/
    }

    @Override
    public void onViewMostrarDatos(RankingResponseData data) {
        /*
        //Asignando los valores de la parte superior: corcholatas, programas activos y programas terminados.
        mTvNumeroCorcholatas.setText(String.valueOf(data.getRanking().getCorcholatas()));
        mTvProgramasActivos.setText(String.valueOf(data.getRanking().getIncompletePrograms()));
        mTvProgramasTerminados.setText(String.valueOf(data.getRanking().getCompletedPrograms()));
        */
    }

    @Override
    public void onViewMostrarMedidoresProgresoYProgramas(ProgramRankingResponseData data) {

        /*//Asignando los textos del progreso de los medidores.
        mTvMedidorUnoProgreso.setText(String.valueOf(data.getProgramsRanking().getMentalidad()));
        mTvMedidorDosProgreso.setText(String.valueOf(data.getProgramsRanking().getFoco()));
        mTvMedidorTresProgreso.setText(String.valueOf(data.getProgramsRanking().getExcelencia()));
        mTvMedidorCuatroProgreso.setText(String.valueOf(data.getProgramsRanking().getDesiciones()));
        mTvMedidorCincoProgreso.setText(String.valueOf(data.getProgramsRanking().getGente()));

        //Asignando los valores de progreso y límite en las imágenes de medidores.
        asignarAGraficas(mIvMedidorUno, data.getProgramsRanking().getMentalidad(), Integer.parseInt(medidorUnoLimite));
        asignarAGraficas(mIvMedidorDos, data.getProgramsRanking().getFoco(), Integer.parseInt(medidorDosLimite));
        asignarAGraficas(mIvMedidorTres, data.getProgramsRanking().getExcelencia(), Integer.parseInt(medidorCuatroLimite));
        asignarAGraficas(mIvMedidorCuatro, data.getProgramsRanking().getDesiciones(), Integer.parseInt(medidorCuatroLimite));
        asignarAGraficas(mIvMedidorCinco, data.getProgramsRanking().getGente(), Integer.parseInt(medidorCincoLimite));*/
    }

    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        mLoader.show(fragmentManager, "Loader");
    }


    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void onViewOcultarLoader() {
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
    public void onViewMostrarMensaje(int messageType) {
        DialogManager.displaySnack(getSupportFragmentManager(), messageType);
    }

    @Override
    public void onViewMiActividadData(MiActividadResponseData data) {
        mMiActividadData = data;
        asignarDatos();
        initializeFragmentProgramas();
    }

    /**
     * Cuando el token del usuario expire se mandará a llamar este método
     */
    @Override
    public void onViewNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewNoInternet() {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        mMiActividadPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * <p>Método que, con base en el tab que se seleccione, dibujará en el FrameLayout el fragmento
     * correspondiente a Programas o Historial.</p>
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment fragment = MiActividadProgramasFragment.newInstance();

        switch (tab.getPosition()){
            case 0:
                if(mMiActividadData != null)
                    {
                        fragment = MiActividadProgramasFragment.newInstance();
                        ((MiActividadProgramasFragment) fragment).setProgramasData(mMiActividadData);
                    }
                break;

            case 1:
                fragment = MiActividadLogrosFragment.newInstance();
                ((MiActividadLogrosFragment) fragment).onViewSetLogrosData(mMiActividadData);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_activity_mi_actividad_contenedor, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //No se implementan.
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //No se imṕlementan.
    }

}

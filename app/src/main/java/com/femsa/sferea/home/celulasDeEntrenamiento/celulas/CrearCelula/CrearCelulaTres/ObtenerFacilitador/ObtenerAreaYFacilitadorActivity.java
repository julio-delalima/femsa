package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData.FacilitadorData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;

import java.util.ArrayList;

public class ObtenerAreaYFacilitadorActivity extends AppCompatActivity implements FacilitadorView, AreasAdapter.OnAreaSeleccionada, FacilitadoresAdapter.OnFacilitadorSeleccionado{

    //elementos de la vista
    private EditText mTIETPalabra;
    private RecyclerView mRv_Areas;
    private AreasAdapter mAreasAdapter;
    private ArrayList<AreaDTO> mAreasArreglo;
    private FacilitadorPresenter mFacilitadorPresenter;
    private ArrayList<Integer> mAreasSeleccionadas;
    private Button mAceptarButton, mAceptarFacilitadorButton;
    private CardView mContainerFacilitadores, mContainerArea;
    private RecyclerView mFacilitadoresrecycler;
    private Loader mLoader;
    private FacilitadorDTO mFacilitadorElBueno;
    private Toolbar mToolbar;
    private CelulaDTO mCelulaDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_facilitador);
        bindViews();
        initadapter();
        bindListeners();
        OnTraerAreas();
        mCelulaDTO = (CelulaDTO) getIntent().getSerializableExtra("celula");
    }

    private void bindViews() {
        mToolbar = findViewById(R.id.toolbar_celulas);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mTIETPalabra = findViewById(R.id.editText_palabra_clave);
            mTIETPalabra.setText("");
            mTIETPalabra.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            /**
                             * Cuando ya le picaste al enter
                             * */
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            });
        mRv_Areas = findViewById(R.id.container_areas);
        mAceptarButton = findViewById(R.id.facilitador_activity_aceptar_button);
            mAceptarButton.setOnClickListener(
                    v->{
                        if(mAreasSeleccionadas.size() > 0)
                            {
                                mFacilitadorPresenter.OnInteractorTraeFacilitadores(mAreasSeleccionadas, mCelulaDTO.getPaisReceptorId(),SharedPreferencesUtil.getInstance().getTokenUsuario(), mTIETPalabra.getText().toString());
                            }
                        else
                            {
                                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minima_areas));
                            }
                    }
                );

        mContainerArea = findViewById(R.id.container_area_cardview);
        mContainerFacilitadores = findViewById(R.id.activity_areas_cardview_facilitadores);
        mFacilitadoresrecycler = findViewById(R.id.recycler_facilitadores);
        mAceptarFacilitadorButton = findViewById(R.id.areas_activity_aceptarFacilitador_button);
            mAceptarFacilitadorButton.setOnClickListener(
                    v->  onAceptarAreaYFacilitador()
            );

        mLoader = Loader.newInstance();
    }

    private void bindListeners() {

    }

    private void initadapter() {
        mFacilitadorPresenter = new FacilitadorPresenter(this, new FacilitadorInteractor());
        mAreasArreglo = new ArrayList<>();
        mAreasSeleccionadas = new ArrayList<>();
        mAreasAdapter = new AreasAdapter(mAreasArreglo);
        mAreasAdapter.setListener(this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv_Areas.setLayoutManager(LayoutManager);
        mRv_Areas.setAdapter(mAreasAdapter);
    }

    private void OnTraerAreas()
        {
            mFacilitadorPresenter.OnInteractorTraeAreas(SharedPreferencesUtil.getInstance().getTokenUsuario());
        }

    /**
     * Método que cierra el Softkeyboard si se hace click afuera de él.
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void onAceptarAreaYFacilitador()
        {
            if(mFacilitadorElBueno == null)
                {
                    DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.selecciona_facilitador));
                    onFacilitadorViewMuestraMensaje(StringManager.SELECCIONA_FACILITADOR);
                }
            else
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Facilitador", mFacilitadorElBueno);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
        }

    @Override
    public void onFacilitadorViewShowLoader() {
        if(mLoader != null && !mLoader.isAdded())
        {
            FragmentManager fm = getSupportFragmentManager();
            mLoader.show(fm, "Loader");
        }
    }

    @Override
    public void onFacilitadorViewHideLoader() {
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
     * Método implementado de FacilitadorView que se manda a llamar cuando ya se tiene el listado
     * de áreas funcionales traidas a través del WS.
     * @param data listado de áreas funcionales.
     * */
    @Override
    public void onFacilitadorViewMuestraAreas(AreaDTO data) {
        mAreasArreglo.clear();
        mAreasArreglo.addAll(data.getAreas());
        mAreasAdapter.notifyDataSetChanged();
    }

    /**
     * Método implementado de FacilitadorView que se manda a llamar cuando ya se tiene el listado
     * de facilitadores de acuerdo a las funcionales seleccionadas, traidos a través del WS.
     * @param facilitadores listado de facilitadores correspondientes a las áreas funcionales seleccionadas.
     * */
    @Override
    public void onFacilitadorViewMuestraFacilitadores(FacilitadorData facilitadores) {
        mContainerArea.setVisibility(View.GONE);
        mAceptarButton.setVisibility(View.GONE);
        ArrayList<FacilitadorDTO> listaFacilitadores = new ArrayList<>(facilitadores.getFacilitadores().getListadoFacilitadores());
        for(int i = 0;  i < facilitadores.getFacilitadores().getListadoFacilitadores().size(); i++)
            {
                if(mCelulaDTO == null)
                    {
                        break;
                    }
                else
                    {
                        for(int j = 0; j < mCelulaDTO.getListadoParticipantes().size(); j++)
                            {

                                    if (facilitadores.getFacilitadores().getListadoFacilitadores().get(i).getIdFacilitador() == mCelulaDTO.getListadoParticipantes().get(j).getIdParticipante()) {
                                        if (listaFacilitadores.size() > 0) {
                                        listaFacilitadores.remove(i);}
                                        else{
                                            onFacilitadorViewSinFacilitador();
                                        }
                                    }
                                    if (facilitadores.getFacilitadores().getListadoFacilitadores().get(i).getIdFacilitador() == SharedPreferencesUtil.getInstance().getIdEmpleado()) {
                                        if (listaFacilitadores.size() > 0) {
                                        listaFacilitadores.remove(i);}
                                        else{
                                            onFacilitadorViewSinFacilitador();
                                        }
                                    }

                            }
                    }
            }

        mContainerFacilitadores.setVisibility(View.VISIBLE);
        mAceptarFacilitadorButton.setVisibility(View.VISIBLE);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        FacilitadoresAdapter adapterFacil = new FacilitadoresAdapter(listaFacilitadores);
        adapterFacil.setListener(this);
        mFacilitadoresrecycler.setLayoutManager(LayoutManager);
        mFacilitadoresrecycler.setAdapter(adapterFacil);
    }

    @Override
    public void onFacilitadorViewMuestraMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void onFacilitadorViewMuestraMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    @Override
    public void onFacilitadorViewSinFacilitador() {
        DialogManager.displaySnack(getSupportFragmentManager(), DialogManager.SIN_FACILITADOR);
    }

    @Override
    public void onFacilitadorViewSesionInvalida() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * Método implementado de AreasAdapter que maneja el listado de areas seleccionadas cuando
     * se selecciona una en el listado de checks de areas.
     * */
    @Override
    public void onAreaSeleccionada(int idArea) {
        mAreasSeleccionadas.add(idArea);
    }

    /**
     * Método implementado de AreasAdapter que maneja el listado de areas seleccionadas cuando
     * se desselecciona una en el listado de checks de areas.
     * */
    @Override
    public void onAreaDeseleccionada(int idArea) {
        for(int i = 0; i < mAreasSeleccionadas.size(); i++)
            {
                if(mAreasSeleccionadas.get(i) == idArea)
                    {
                        mAreasSeleccionadas.remove(i);
                        break;
                    }
            }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método implementado desde FacilitadoresAdapter que agrega dentro del DTO la información
     * del Facilitador que se seleccionó.
     * @param facilitador el facilitador seleccionado en el RecyclerView
     * */
    @Override
    public void FacilitadorEscogido(FacilitadorDTO facilitador) {
        mFacilitadorElBueno = facilitador;
    }
}

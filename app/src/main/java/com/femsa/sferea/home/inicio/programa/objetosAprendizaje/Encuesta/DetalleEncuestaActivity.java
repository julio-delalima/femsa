package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EncuestaResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetalleEncuestaActivity extends AppCompatActivity implements  EncuestaView, EncuestaRespuestasAdapter.OnEncuestaRespuestaElegida, DialogFragmentManager.OnDialogManagerButtonActions {

    private ConstraintLayout mRootView;

    private RecyclerView mPreguntasRecycler;

    private Loader mLoader;

    private Button mFinalizarButton;

    private EncuestaPreguntasAdapter mAdapter;

    private int mIdObjeto = 0;

    private EncuestaPresenter mEncuestaPresenter;

    private ArrayList<String> mListadoIDPreguntas;
    private ArrayList<String> mListadoRespuestas;
    private int aprobado = 0, mIdPrograma = 0;
    private int mTotalPreguntas, mContestadas = 0;
    private Toolbar mToolbar;
    private LearningObjectsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_encuesta);
        bindComponents();
        initPresenter();
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

    private void bindComponents()
    {
        mIdPrograma = getIntent().getIntExtra("programa", 0);
        mRootView = findViewById(R.id.encuesta_root_view);
        mRootView.setVisibility(View.INVISIBLE);
        mPreguntasRecycler = findViewById(R.id.preguntas_encuesta_rv);
        mLoader = Loader.newInstance();
        mFinalizarButton = findViewById(R.id.finalizar_boton);
        mFinalizarButton.setOnClickListener(v-> onEncuestaTerminada());
        mIdObjeto = getIntent().getIntExtra(ObjetoAprendizajeActivity.ID_OBJECT_KEY, 0);
        mToolbar = findViewById(R.id.toolbar_activity_detalle_encuesta);
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

    private void initPresenter()
    {
        mEncuestaPresenter = new EncuestaPresenter(this, new EncuestaInteractor());
        mEncuestaPresenter.OnInteractorLlamarEncuesta(mIdObjeto);
        mPresenter = new LearningObjectsPresenter(new LearningObjectView() {
            @Override
            public void likeSuccess() {

            }

            @Override
            public void getObjectDetailSuccess(ObjectDetailResponseData data) {

            }

            @Override
            public void showLoader() {

            }

            @Override
            public void hideLoader() {

            }

            @Override
            public void muestraMensaje(int tipoMensaje) {

            }

            @Override
            public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

            }

            @Override
            public void OnMarkAsReadExitoso() {

            }

            @Override
            public void OnBonusSuccess() {

            }

            @Override
            public void OnNoInternet() {

            }

            @Override
            public void OnJuegoListo(InputStream zip, int buffer) {

            }
        }, new LearningObjectInteractor());
    }

    /**
     * Método para agregar todos los datos del WS dentro de los adapters de las preguntas para la encuesta.
     * */
    private void initPreguntas(EncuestaResponseData data)
    {
        mAdapter = new EncuestaPreguntasAdapter(this, data.getEncuesta().getEncuesta(), this, data.getEncuesta().isContes_Pinches_tada());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPreguntasRecycler.setAdapter(mAdapter);
        mPreguntasRecycler.setLayoutManager(manager);
        mFinalizarButton.setVisibility(data.getEncuesta().isContes_Pinches_tada() ? View.GONE : View.VISIBLE);
    }

    /**
     * Método para verificar que la encuesta haya sido completada y para enviar el resultado.
     * */
    private void onEncuestaTerminada()
    {
        ArrayList<String> mIDsPreguntasSinRepetir = new ArrayList<>();
        Set<String> set = new HashSet<>(mListadoIDPreguntas);
        mIDsPreguntasSinRepetir.clear();
        mIDsPreguntasSinRepetir.addAll(set);
        if(mListadoRespuestas.size() < 1)
        {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.total_respuestas_encoesta));
        }
        else if(mIDsPreguntasSinRepetir.size() < mTotalPreguntas)
        {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.total_respuestas_encoesta));
        }
        else
        {
            mEncuestaPresenter.OnInteractorAddEncuesta(mIdObjeto, mListadoIDPreguntas, mListadoRespuestas, 0);
        }
    }

    @Override
    public void OnViewMostrarLoader() {
        if (!mLoader.isAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            try {
                mLoader.show(fm, "Loader");
            } catch (Exception ex) {

            }
        }
    }

    /**
     * Método para guardar en el listado de mCheckRespuestas auqellas que ya fueron contestadas anteriormente.
     * */
    private void guardaRespuestasEncuesta(EncuestaResponseData data)
    {
        for(int i = 0; i < data.getEncuesta().getEncuesta().size(); i++)
        {
            for(int j = 0; j < data.getEncuesta().getEncuesta().get(i).getRespuestas().size(); j++)
            {
                if(data.getEncuesta().getEncuesta().get(i).getRespuestas().get(j).isStatus())
                {
                    mListadoRespuestas.add(String.valueOf(data.getEncuesta().getEncuesta().get(i).getRespuestas().get(j).getRespuestaTexto()));
                    mListadoIDPreguntas.add(String.valueOf(data.getEncuesta().getEncuesta().get(i).getIdPregunta()));
                }
            }
        }
//        Toast.makeText(this,  mTotalPreguntas + " total " +mCorrectas + " vs " + mIncorrectas + " mal", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnViewEsconderLoader() {
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
    public void OnViewMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    /**
     * Método implementado de EncuestaView que se manda a llamar cuando se terminó con éxito la petición
     * para traer los datos de la encuesta.
     * @param data la información traída por el WS.
     * */
    @Override
    public void OnViewPintaEncuesta(EncuestaResponseData data) {
        initPreguntas(data);
        mListadoIDPreguntas = new ArrayList<>(data.getEncuesta().getEncuesta().size());
        mListadoRespuestas = new ArrayList<>(data.getEncuesta().getEncuesta().size());
        mRootView.setVisibility(View.VISIBLE);
        mTotalPreguntas = data.getEncuesta().getEncuesta().size();
        guardaRespuestasEncuesta(data);
    }

    /**
     * Método implemenetado de CheckListView que se manda a llamar cuando se realizó con exíto la inserción de los datos de
     * la encuesta con el Web Service.
     * */
    @Override
    public void OnViewEncuestaAdded() {
        DialogFragmentManager f = DialogFragmentManager.newInstance(
                false, false, true, getResources().getString(R.string.gracias),
                getResources().getString(R.string.informacion_aceptada), getResources().getString(R.string.aceptar), "", false);
        f.setListener(this);
        f.show(getSupportFragmentManager(), "check");
        mPresenter.OnInteractorMarkAsRead(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
        //mPresenter.OnInteractorCallBonus(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    /**
     * Método implementado de EncuestaAdapter que administra la lista de respuestas, agregando o elimninando
     * elementos de acuerdo a si se seleccionan o deseleccionan elementos del Check.
     * */
    @Override
    public void onCheckElegida(String idPregunta, String respuesta, boolean add, boolean isCorrecta) {
        if(add)
        {
            mListadoRespuestas.add(respuesta);
            mListadoIDPreguntas.add(idPregunta);
        }
        else
        {
            mListadoRespuestas.remove(respuesta);
            mListadoIDPreguntas.remove(idPregunta);
        }
    }

    /**
     * Método implementado de EncuestaAdapter que administra la lista de respuestas, agregando o elimninando
     * elementos de acuerdo a si se seleccionan o deseleccionan elementos de los RadioButtons.
     * */
    @Override
    public void onRadioElegida(String idPregunta, String respuesta, boolean isCorrecta) {
        for(int i = 0; i < mListadoIDPreguntas.size(); i++)
        {
            if(mListadoIDPreguntas.get(i).equals(idPregunta))
            {
                mListadoIDPreguntas.remove(i);
                mListadoRespuestas.remove(i);
                break;
            }
        }
        mListadoRespuestas.add(respuesta);
        mListadoIDPreguntas.add(idPregunta);
    }

    /**
     * Método implementado de EncuestaAdapter que administra la lista de respuestas, agregando o elimninando
     * elementos de acuerdo a si se escribe sobre los EditText del item del adapter.
     * */
    @Override
    public void onPregntaAbiertaRespondida(String idPregunta, String respuesta, boolean add) {
        if(!respuesta.equals("")) //si la respuesta está vacía no se agrega.
            {
                if(add)
                {
                    mListadoIDPreguntas.add(idPregunta);
                    mListadoRespuestas.add(respuesta);
                }
                else
                {
                    mListadoIDPreguntas.remove(idPregunta);
                    mListadoRespuestas.remove(respuesta);
                }
            }
    }

    @Override
    public void selecionarChecked(int position) {
        mRootView = (ConstraintLayout) mPreguntasRecycler.getLayoutManager().findViewByPosition(position);
        //mLastOptionSelected = (CardView) lastChecked.getChildAt(0);
    }

    @Override
    public void OnDialogAceptarClick() {
        /*mEncuestaPresenter.OnInteractorAddEncuesta(mIdObjeto, mListadoIDPreguntas, mListadoRespuestas, aprobado);
        Intent irA = new Intent(this, DetalleProgramaActivity.class);
        irA.putExtra("id", mIdPrograma);
        irA.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(irA);*/
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        returnIntent.putExtra("encoesta", "encoesta");
        finish();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEncuestaPresenter.onDeestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void OnDialogCancelarClick() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

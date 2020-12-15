package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EvaluacionResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EvaluacionActivity extends AppCompatActivity implements  EvaluacionView, EvaluacionRespuestasAdapter.OnEvaluacionRespuestaElegida, DialogFragmentManager.OnDialogManagerButtonActions {

    private ConstraintLayout mRootView;

    private RecyclerView mPreguntasRecycler;

    private Loader mLoader;

    private Button mFinalizarButton;

    private EvaluacionPreguntasAdapter mAdapter;

    private int mIdObjeto = 0;

    private EvaluacionPresenter mEvaluacionPresenter;

    private ArrayList<String> mListadoIDPreguntas;
    private ArrayList<String> mListadoIDRespuestas;
    private int aprobado = 0, mIdPrograma = 0;
    private int mCorrectasRadio, mIncorrectasRadio, mTotalPreguntas, mTotalPreguntasCorrectas;

    private int mCorrectasCheck = 0, mIncorrectasCheck = 0;

    private Toolbar mToolbar;
    private LearningObjectsPresenter mPresenter;
    DialogFragmentManager f;
    int calificacionEvaluacion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);
        bindComponents();
        initPresenter();
    }

    private void bindComponents()
    {
        mIdPrograma = getIntent().getIntExtra("programa", 0);
        mRootView = findViewById(R.id.evaluacion_root_view);
            mRootView.setVisibility(View.INVISIBLE);
        mPreguntasRecycler = findViewById(R.id.evaluacion_preguntas_recycler);
        mLoader = Loader.newInstance();
        mFinalizarButton = findViewById(R.id.finalizar_boton);
            mFinalizarButton.setOnClickListener(v-> onEvaluacionTerminada());
        mIdObjeto = getIntent().getIntExtra(ObjetoAprendizajeActivity.ID_OBJECT_KEY, 0);
        mToolbar = findViewById(R.id.evaluacion_toolbar);
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
            mEvaluacionPresenter = new EvaluacionPresenter(this, new EvaluacionInteractor());
            mEvaluacionPresenter.OnInteractorLlamarEvaluacion(mIdObjeto);
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
                    markExitoso();
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

    private void markExitoso(){
        f = DialogFragmentManager.newInstance(
                false, false, true, getResources().getString(R.string.felicitaciones),
                getResources().getString(R.string.aprobo_evaluacion, String.valueOf(calificacionEvaluacion)), getResources().getString(R.string.aceptar), "", false);
        f.setListener(this);
        f.show(getSupportFragmentManager(), "check");
    }

    private void initPreguntas(EvaluacionResponseData data)
    {
        mAdapter = new EvaluacionPreguntasAdapter(this, data.getEvaluacion().getEvaluacion(), this, data.getEvaluacion().isContes_Pinches_tada());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPreguntasRecycler.setAdapter(mAdapter);
        mPreguntasRecycler.setLayoutManager(manager); 
        mFinalizarButton.setVisibility(data.getEvaluacion().isContes_Pinches_tada() ? View.GONE : View.VISIBLE);
    }

    /**
     * Método para verificar que la encuesta haya sido completada y para enviar el resultado.
     * */
    private void onEvaluacionTerminada()
    {
        ArrayList<String> mIDsPreguntasSinRepetir = new ArrayList<>();
        Set<String> set = new HashSet<>(mListadoIDPreguntas);
        mIDsPreguntasSinRepetir.clear();
        mIDsPreguntasSinRepetir.addAll(set);
        if(mListadoIDRespuestas.size() < 1)
            {
                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_opciones));
            }
        else if(mIDsPreguntasSinRepetir.size() < mTotalPreguntas)
        {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.total_respuestas_encoesta));
        }
        else
            {

                calificacionEvaluacion = Math.min(aproboExamen(), 100);
                if(calificacionEvaluacion >= 85)
                {
                    aprobado = 1;
                    mPresenter.OnInteractorMarkAsRead(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
                    //mPresenter.OnInteractorCallBonus(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
                }
                else
                {
                    f = DialogFragmentManager.newInstance(
                            false, false, true, getResources().getString(R.string.falto_poco),
                            getResources().getString(R.string.reprobo_evaluacion, String.valueOf(calificacionEvaluacion)), getResources().getString(R.string.aceptar), "", false);
                    f.setListener(this);
                    f.show(getSupportFragmentManager(), "check");
                }


            }
    }


    /**
     * Método para calificar la evaluación considerando una regla de tres en donde a las preguntas correctas
     * se le restan las respuestas incorrectas, a ese resultado se multiplica por 100 y eso se divide entre el total de preguntas.
     * */
    private int aproboExamen()
        {
            //int calificacion = Math.round((float)(mCorrectas - mIncorrectas) * 100 / (float)mTotalPreguntasCorrectas);
            int calificacion = Math.round((mCorrectasRadio + (Math.max(0, mCorrectasCheck - mIncorrectasCheck))) * 100f / (float)mTotalPreguntasCorrectas);
            return Math.max(calificacion, 0);
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
     * Método para guardar en el listado de mCheckRespuestas aquellas que ya fueron contestadas anteriormente.
     * */
    private void guardaRespuestasEvaluacion(EvaluacionResponseData data)
    {
        if(!data.getEvaluacion().isContes_Pinches_tada()){
            for(int i = 0; i < data.getEvaluacion().getEvaluacion().size(); i++)
            {
                for(int j = 0; j < data.getEvaluacion().getEvaluacion().get(i).getRespuestas().size(); j++)
                {
                    if(data.getEvaluacion().getEvaluacion().get(i).getRespuestas().get(j).isStatus())
                    {
                        mListadoIDRespuestas.add(String.valueOf(data.getEvaluacion().getEvaluacion().get(i).getRespuestas().get(j).getIdRespuesta()));
                        mListadoIDPreguntas.add(String.valueOf(data.getEvaluacion().getEvaluacion().get(i).getIdPregunta()));
                        if(data.getEvaluacion().getEvaluacion().get(i).getTipoPregunta().equals("radio")){
                            if(data.getEvaluacion().getEvaluacion().get(i).getRespuestas().get(j).isCorrecta())
                            {
                                mCorrectasRadio++;
                            }
                            else
                            {
                                mIncorrectasRadio++;
                            }
                        } else  if(data.getEvaluacion().getEvaluacion().get(i).getTipoPregunta().equals("check")){
                            if(data.getEvaluacion().getEvaluacion().get(i).getRespuestas().get(j).isCorrecta())
                            {
                                mCorrectasCheck++;
                            }
                            else
                            {
                                mIncorrectasCheck++;
                            }
                        }
                    }
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
     * Método de EvaluacionView que se utiliza cuando ya se tienen datos del Web Service, para poder colcoarlos
     * en su respectivo elemento visual.
     * */
    @Override
    public void OnViewPintaEvaluacion(EvaluacionResponseData data) {
        contarCorrectas(data);
        initPreguntas(data);
        mListadoIDPreguntas = new ArrayList<>();
        mListadoIDRespuestas = new ArrayList<>();
        mRootView.setVisibility(View.VISIBLE);
        guardaRespuestasEvaluacion(data);
    }

    /**
     * Método que se ejecuta al inicio de la actividad para contar y asignar las respuestas en caso de que ya
     * hayan sido contestadas anteriormente o ya vengan del WebService contestadas.
     * */
    private void contarCorrectas(EvaluacionResponseData data)
        {
            for(int i = 0; i < data.getEvaluacion().getEvaluacion().size(); i++)
                {
                    mTotalPreguntas++;
                    for(int j = 0; j < data.getEvaluacion().getEvaluacion().get(i).getRespuestas().size(); j++)
                        {
                            if(data.getEvaluacion().getEvaluacion().get(i).getRespuestas().get(j).isCorrecta())
                                {
                                    mTotalPreguntasCorrectas++;
                                }
                        }
                }
        }

    @Override
    public void OnViewEvaluacionAdded() {

    }

    /**
     * Método implementado de EvaluacionAdapter que se manda a llamar cuando s ehace click sobre alguno de los check
     * de una pregunta, en caso de ser correcto el check, se suma +1 al contador de checks correctos y en caso de ser
     * incorrecto se suma +1 al contador de preguntas incorrectas, y cuando ese check se deselecciona, el contador de
     * pregunta correcta o incorrecta se disminuye.
     * */
    @Override
    public void onCheckElegida(String idPregunta, String idRespuesta, boolean add, boolean isCorrecta) {
        if(add)
            {
                mListadoIDRespuestas.add(idRespuesta);
                mListadoIDPreguntas.add(idPregunta);
                if(isCorrecta)
                    {
                        //mCorrectasRadio++;
                        mCorrectasCheck++;
                    }
                else
                    {
                        //mIncorrectasRadio++;
                        mIncorrectasCheck++;
                    }
            }
        else
            {
                mListadoIDRespuestas.remove(idRespuesta);
                mListadoIDPreguntas.remove(idPregunta);
                if(isCorrecta)
                    {
                        //mCorrectasRadio--;
                        mCorrectasCheck--;
                    }
                else
                    {
                        //mIncorrectasRadio--;
                        mIncorrectasCheck--;
                    }
            }
    }

    /**
     * Método implementado de EvaluacionAdapter que se manda a llamar cuando se hace click sobre alguno de los radio buttons
     * de una pregunta, en caso de ser correcto, se suma +1 al contador de checks correctos y en caso de ser
     * incorrecto se suma +1 al contador de preguntas incorrectas, y cuando se cambia de radio, el contador de
     * pregunta correcta o incorrecta se disminuye.
     * */
    @Override
    public void onRadioElegida(String idPregunta, String idRespuesta, boolean isCorrecta) {
        for(int i = 0; i < mListadoIDPreguntas.size(); i++)
            {
                if(mListadoIDPreguntas.get(i).equals(idPregunta))
                    {
                        mListadoIDPreguntas.remove(i);
                        mListadoIDRespuestas.remove(i);
                        break;
                    }
            }
        mListadoIDRespuestas.add(idRespuesta);
        mListadoIDPreguntas.add(idPregunta);
        if(isCorrecta)
            {
                mCorrectasRadio++;
                mIncorrectasRadio--;
            }
        else
            {
                mCorrectasRadio--;
                mIncorrectasRadio++;
            }
    }

    /**
     * Método implementado de EvaluacionRespuestasAdapter que únicamente se utiliza para mantener los índices de los
     * radio buttons para evitar que se puedan seleccionar múltiples radios.
     * */
    @Override
    public void selecionarChecked(int position) {
        mRootView = (ConstraintLayout) mPreguntasRecycler.getLayoutManager().findViewByPosition(position);
        //mLastOptionSelected = (CardView) lastChecked.getChildAt(0);
    }

    @Override
    public void OnDialogAceptarClick() {
       // Toast.makeText(this, mListadoIDPreguntas.toString() + " " + mListadoIDRespuestas.toString(), Toast.LENGTH_SHORT).show();
        mEvaluacionPresenter.OnInteractorAddEvaluacion(mIdObjeto, mListadoIDPreguntas, mListadoIDRespuestas, aprobado, calificacionEvaluacion);
        Intent irA = new Intent(this, DetalleProgramaActivity.class);
        irA.putExtra("id", mIdPrograma);
        irA.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(irA);
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
